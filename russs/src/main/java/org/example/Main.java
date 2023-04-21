package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.Component.IkunComponent;
import org.example.Menu.GameMenu;
import org.example.Menu.MyMainMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.example.EntityType.*;


public class Main extends GameApplication {
    private Entity newikun;
    private Entity newchicken;
    boolean requestNewGame = false;
    boolean gameover = false;
    private String tpff;
    int finalscore = 0;
    int basketball = 0;
    int bossblood = 25;
    double timer = 2;
    TimerAction timerAction;
    TimerAction finaltimerAction;

    @Override
    protected void initSettings(@NotNull GameSettings gameSettings) {
        gameSettings.setTitle("chicken fly");
        gameSettings.setWidth(1280);
        gameSettings.setHeight(720);
        gameSettings.setSceneFactory(new GameMenu.MySceneFactory());
        gameSettings.setMainMenuEnabled(true);


    } // basic gamesetting

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.WHITE);

        getGameWorld().addEntityFactory(new PlayerFactory());
        getGameWorld().spawn("ikun");
        timerAction = getGameTimer().runAtInterval(() -> {
            if(finalscore < 8){
                getGameWorld().spawn("chicken");
                getWorldProperties().increment("score", +1);
                timer = 0.99 * timer;
                finalscore ++;
            }
            else{
                getGameWorld().spawn("chicken");
                getGameWorld().spawn("hardchicken");
                getWorldProperties().increment("score", +2);
                timer = 0.9 * timer;
                finalscore += 2;
            }
            getGameWorld().spawn("basketball");
            }, Duration.seconds(timer));


        super.initGame();
    } //entity initial, gamescene initial

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);

    }

    @Override
    protected void initUI() {
        Text textscore = getUIFactoryService().newText("score", Color.BLACK, 50);
        textscore.setTranslateX(1000);
        textscore.setTranslateY(100);
        textscore.textProperty().bind(getWorldProperties().intProperty("score").asString());
        getGameScene().addUINode(textscore);

    }

    @Override
    protected void onUpdate(double tpf) {

        if (requestNewGame) {
            requestNewGame = false;
            finalscore = 0;
            getGameController().startNewGame();
        }

        if(gameover) {
            getGameTimer().runOnceAfter(() -> {
                getGameController().exit();
            } , Duration.millis(1));
        }
        if(finalscore > 12 && finalscore < 20){
            timerAction.expire();
            getGameWorld().spawn("bosschicken");
            finalscore = 100;
        }

    }

    @Override
    protected void initPhysics() {

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(IKUN,CHICKEN) {
            @Override
            protected void onCollisionBegin(Entity newikun, Entity newchicken) {
                showGameOver();
                super.onCollisionBegin(newikun, newchicken);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(IKUN,BASKETBALL) {
            @Override
            protected void onCollisionBegin(Entity newikun, Entity newbasketball) {
                newbasketball.removeFromWorld();
                basketball ++;
                super.onCollisionBegin(newikun, newbasketball);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(IKUN,BOSS) {
            @Override
            protected void onCollisionBegin(Entity newikun, Entity newbosschicken) {
                newikun.removeFromWorld();
                super.onCollisionBegin(newikun, newbosschicken);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(BULLET,BOSS) {
            @Override
            protected void onCollisionBegin(Entity newbullet, Entity newbosschicken) {
                double x = newbullet.getRightX();
                double y = newbullet.getBottomY();
                newbullet.removeFromWorld();
                getGameWorld().spawn("explosion")
                        .setPosition(x,y);
                bossblood --;
                if(bossblood == 0)
                    newbosschicken.removeFromWorld();
                super.onCollisionBegin(newbosschicken, newbullet);
            }
        });

        super.initPhysics();
    }   // collision

    protected void initInput() {
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                IkunComponent.jump();
            }
        }, KeyCode.W, VirtualButton.UP);
        getInput().addAction(new UserAction("forward") {
            @Override
            protected void onAction() {
                IkunComponent.forward();
            }
        }, KeyCode.D, VirtualButton.RIGHT);
        getInput().addAction(new UserAction("backward") {
            @Override
            protected void onAction() {
                IkunComponent.backward();
            }
        }, KeyCode.A, VirtualButton.LEFT);
        getInput().addAction(new UserAction("exit") {
            @Override
            protected void onActionBegin() {
                requestNewGame();
            }
        }, KeyCode.F1);
            getInput().addAction(new UserAction("fire") {
                @Override
                protected void onActionBegin() {
                    if(basketball > 0 && finalscore >12) {
                        getGameWorld().spawn("bullet");
                        basketball --;
                    }

                    super.onActionBegin();
                }
            }, MouseButton.PRIMARY);

    }     //user interaction

    public void requestNewGame() {
        requestNewGame = true;
    }
    private void showGameOver() {
        showMessage("你的分数是" + finalscore);
        showMessage("小黑子还要继续努力呦");
        finalscore = 0;
        getGameTimer().runOnceAfter(() ->{
            FXGL.getGameController().gotoMainMenu();
        }, Duration.seconds(0.5));

    }


    public static  void  main(String[] args){

        launch(args);
    }
}