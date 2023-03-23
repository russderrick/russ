package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameController;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsUnitConverter;
import com.almasb.fxgl.physics.box2d.collision.shapes.Shape;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static org.example.EntityType.CHICKEN;
import static org.example.EntityType.IKUN;


public class Main extends GameApplication {
    private Entity newikun;
    private Entity newchicken;
    boolean requestNewGame = false;
    boolean gameover = false;
    private String tpff;
    int finalscore = 0;
    double timer = 2;



    @Override
    protected void initSettings(@NotNull GameSettings gameSettings) {
        gameSettings.setTitle("chicken fly");
        gameSettings.setWidth(1280);
        gameSettings.setHeight(720);

    } // basic gamesetting

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.WHITE);

        getGameWorld().addEntityFactory(new PlayerFactory());
        getGameWorld().spawn("ikun");


        getGameTimer().runAtInterval(() -> {
            if(finalscore <= 8){
                getGameWorld().spawn("chicken");
                getWorldProperties().increment("score", +1);
                timer = 0.99 * timer;
                finalscore ++;
            }
            else{
                getGameWorld().spawn("chicken");
                getGameWorld().spawn("hardchicken");
                getWorldProperties().increment("score", +1);
                timer = 0.9 * timer;
                finalscore ++;
            }
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

        getGameTimer().runAtInterval(() -> {


        }, Duration.seconds(2 - tpf * 2.5));

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

        super.initPhysics();
    }   // collision

    protected void initInput() {
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
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
    }     //user interaction

    public void requestNewGame() {
        requestNewGame = true;
    }
    private void showGameOver() {
        showMessage("你的分数是" + finalscore);
        showMessage("小黑子还要继续努力呦");
        gameover = true;
    }


    public static  void  main(String[] args){

        launch(args);
    }
}