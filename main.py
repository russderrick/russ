from __future__ import print_function
import argparse
import torch
import torch.utils.data.dataloader
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import numpy as np
import random
import matplotlib.pyplot as plt
import statistics
import threading
import multiprocessing as mp
from torchvision import datasets, transforms
from torch.optim.lr_scheduler import StepLR
from utils.config_utils import read_args, load_config, Dict2Object
from multiprocessing import cpu_count
from torch.utils.data import DataLoader


class Net(nn.Module):

    def __init__(self):
        super(Net, self).__init__()
        self.conv1 = nn.Conv2d(1, 32, 3, 1)
        self.conv2 = nn.Conv2d(32, 64, 3, 1)
        self.dropout1 = nn.Dropout(0.25)
        self.dropout2 = nn.Dropout(0.5)
        self.fc1 = nn.Linear(9216, 128)
        self.fc2 = nn.Linear(128, 10)

    def forward(self, x):
        x = self.conv1(x)
        x = F.relu(x)
        x = self.conv2(x)
        x = F.relu(x)
        x = F.max_pool2d(x, 2)
        x = self.dropout1(x)
        x = torch.flatten(x, 1)
        x = self.fc1(x)
        x = F.relu(x)
        x = self.dropout2(x)
        x = self.fc2(x)
        output = F.log_softmax(x, dim=1)
        return output


def train(args, model, device, train_loader, optimizer, epoch, param):
    """
    tain the model and return the training accuracy
    :param args: input arguments
    :param model: neural network model
    :param device: the device where model stored
    :param train_loader: data loader
    :param optimizer: optimizer
    :param epoch: current epoch
    :return:
    """
    model.train()
    loss_sum = 0
    accuracy = 0
    for batch_idx, (data, target) in enumerate(train_loader):
        data, target = data.to(device), target.to(device)
        optimizer.zero_grad()
        output = model(data)
        loss = F.nll_loss(output, target)
        loss.backward()
        optimizer.step()
        loss_sum += loss.item()
        index = 0
        for i in range(output.size(0)):
            index += (torch.argmax(output[i], dim=-1) == target[i]).type(torch.float).sum().item()
        accuracy += index
        print('train accuracy', str(index / train_loader.batch_size), 'train loss', str(loss.item() / train_loader.batch_size))

    training_acc, training_loss = accuracy / len(train_loader.dataset), loss_sum / len(train_loader.dataset)  # replace this line
    f = open("training.txt", "a")
    f.write("training accuracy" + str(param) + ":" + str(training_acc))
    ff = open("training.txt", "a")
    ff.write(" training loss :" + str(training_loss))
    return training_acc, training_loss


def test(model, device, test_loader, param):
    """
    test the model and return the tesing accuracy
    :param model: neural network model
    :param device: the device where model stored
    :param test_loader: data loader
    :return:
    """

    loss_criterion = nn.CrossEntropyLoss()
    model.eval()
    testing_loss = 0
    correct = 0
    with torch.no_grad():
        for data, target in test_loader:
            data, target = data.to(device), target.to(device)
            output = model(data)
            testing_loss += loss_criterion(output, target).item()
            pred = output.argmax(dim=1, keepdim=True)
            correct += pred.eq(target.view_as(pred)).sum().item()

            pass
    testing_loss /= len(test_loader.dataset)
    testing_acc = correct / len(test_loader.dataset)

    f = open("testing.txt", "a")
    f.write("testing accuracy" +str(param) + ":" + str(testing_acc))
    ff = open("testing.txt", "a")
    ff.write(" testing loss:" + str(testing_loss))

    return testing_acc, testing_loss


def plot(epoches, train_acc, train_loss, test_acc, test_loss, parameter):
    """
    plot the model peformance

    """
    """Fill your code"""
    plt.figure(figsize=(40, 24))
    plt.plot(epoches, train_acc, 'b', label='Training accuracy')
    plt.title('Training accuracy ' + str(parameter))
    plt.xlabel('Epoches')
    plt.ylabel('Accuracy')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, test_acc, 'b', label='Validation accuracy')
    plt.title('Validation accuracy ' + str(parameter))
    plt.xlabel('Epoches')
    plt.ylabel('Accuracy')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, train_loss, 'b', label='Training loss')
    plt.title('Training loss ' + str(parameter))
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, test_loss, 'b', label='Validation loss')
    plt.title('Validation loss ' + str(parameter))
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()

    plt.show()

    pass


def run(config, parameter, lock, queue1, queue2, queue3, queue4):
    lock.acquire()
    torch.manual_seed(parameter)

    use_cuda = not config.no_cuda and torch.cuda.is_available()
    use_mps = not config.no_mps and torch.backends.mps.is_available()

    if use_cuda:
        device = torch.device("cuda")
    elif use_mps:
        device = torch.device("mps")
    else:
        device = torch.device("cpu")

    train_kwargs = {'batch_size': config.batch_size, 'shuffle': True}
    test_kwargs = {'batch_size': config.test_batch_size, 'shuffle': True}
    if use_cuda:
        cuda_kwargs = {'num_workers': 1,
                       'pin_memory': True, }
        train_kwargs.update(cuda_kwargs)
        test_kwargs.update(cuda_kwargs)

    # download data
    transform = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize((0.1307,), (0.3081,))
    ])
    dataset1 = datasets.MNIST('./data', train=True, download=True, transform=transform)
    dataset2 = datasets.MNIST('./data', train=False, transform=transform)

    """add random seed to the DataLoader, pls modify this function"""


    def create_dataloader(dataset, batch_size):
        dataloader = DataLoader(dataset, batch_size=batch_size, shuffle=True)
        return dataloader

    batch_size = 64
    test_batch_size = 1000
    train_loader = create_dataloader(dataset1, batch_size)
    test_loader = create_dataloader(dataset2, test_batch_size)

    model = Net().to(device)
    optimizer = optim.Adadelta(model.parameters(), lr=config.lr)

    """record the performance"""
    epoches = []
    training_accuracies = []
    training_loss = []
    testing_accuracies = []
    testing_loss = []
    scheduler = StepLR(optimizer, step_size=1, gamma=config.gamma)
    for epoch in range(1, config.epochs + 1):
        train_acc, train_loss = train(config, model, device, train_loader, optimizer, epoch, parameter)
        training_accuracies.append(train_acc)
        training_loss.append(train_loss)
        """record training info, Fill your code"""
        test_acc, test_loss = test(model, device, test_loader, parameter)
        testing_accuracies.append(test_acc)
        testing_loss.append(test_loss)
        epoches.append(epoch)
        """record testing info, Fill your code"""
        scheduler.step()
        """update the records, Fill your code"""
    queue1.put(training_accuracies)
    queue2.put(training_loss)
    queue3.put(testing_accuracies)
    queue4.put(testing_loss)
    """plotting training performance with the records"""
    plot(epoches, training_accuracies, training_loss, testing_accuracies, testing_loss, parameter)
    """plotting testing performance with the records"""

    if config.save_model:
        torch.save(model.state_dict(), "mnist_cnn.pt")

    lock.release()


matrix_training_accuracies = []
matrix_training_loss = []
matrix_testing_accuracies = []
matrix_testing_loss = []


def plot_mean(meanacc, meanls, meantacc, meantls):
    """
    Read the recorded results.
    Plot the mean results after three runs.
    :return:
    """
    """fill your code"""
    epoches = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
    plt.figure(figsize=(40, 24))
    plt.plot(epoches, meanacc, 'b', label='Mean Training accuracy')
    plt.title('Mean Training accuracy ')
    plt.xlabel('Epoches')
    plt.ylabel('Accuracy')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, meantacc, 'b', label='Mean Validation accuracy')
    plt.title('Mean Validation accuracy ')
    plt.xlabel('Epoches')
    plt.ylabel('Accuracy')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, meanls, 'b', label='Mean Training loss')
    plt.title('Mean Training loss ' )
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()

    plt.figure(figsize=(40, 24))
    plt.plot(epoches, meantls, 'b', label='Mean Validation loss')
    plt.title('Mean Validation loss ')
    plt.xlabel('Epochs')
    plt.ylabel('Loss')
    plt.legend()

    plt.show()


lock = threading.Lock()

if __name__ == '__main__':
    arg = read_args()

    """toad training settings"""
    config = load_config(arg)
    queue1 = mp.Queue()
    queue2 = mp.Queue()
    queue3 = mp.Queue()
    queue4 = mp.Queue()
    """train model and record results"""
    t1 = threading.Thread(target=run, args=(config, config.seed1, lock, queue1, queue2, queue3, queue4, ))
    t2 = threading.Thread(target=run, args=(config, config.seed2, lock, queue1, queue2, queue3, queue4, ))
    t3 = threading.Thread(target=run, args=(config, config.seed3, lock, queue1, queue2, queue3, queue4, ))

    t1.start()
    t2.start()
    t3.start()

    t1.join()
    t2.join()
    t3.join()

    """plot the mean results"""
    mean_acc = []
    mean_ls = []
    mean_tacc = []
    mean_tls = []

    while not queue1.empty():
        matrix_training_accuracies.append(queue1.get())
    mean_acc = [sum(x)/len(x) for x in zip(*matrix_training_accuracies)]
    while not queue2.empty():
        matrix_training_loss.append(queue2.get())
    mean_ls = [sum(x)/len(x) for x in zip(*matrix_training_loss)]
    while not queue3.empty():
        matrix_testing_accuracies.append(queue3.get())
    mean_tacc = [sum(x)/len(x) for x in zip(*matrix_testing_accuracies)]
    while not queue4.empty():
        matrix_testing_loss.append(queue4.get())
    mean_tls = [sum(x)/len(x) for x in zip(*matrix_testing_loss)]
    plot_mean(mean_acc, mean_ls, mean_tacc, mean_tls)

