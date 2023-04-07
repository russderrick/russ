package org.example;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameMenu extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                //return new SimpleGameMenu();
                return new MyPauseMenu();
            }
        });
    }

    public static class MyPauseMenu extends FXGLMenu {

        private static final int SIZE = 150;

        private Animation<?> animation;

        public MyPauseMenu() {
            super(MenuType.GAME_MENU);

            getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
            getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);


            Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.WHITE, FontType.GAME, 24.0);
            textResume.setTranslateX(50);
            textResume.setTranslateY(100);
            textResume.setMouseTransparent(true);

            Text textExit = FXGL.getUIFactoryService().newText("EXIT", Color.WHITE, FontType.GAME, 24.0);
            textExit.setTranslateX(200);
            textExit.setTranslateY(100);
            textExit.setMouseTransparent(true);

            Text textOptions = FXGL.getUIFactoryService().newText("OPTIONS", Color.WHITE, FontType.GAME, 24.0);
            textOptions.setTranslateX(110);
            textOptions.setTranslateY(195);
            textOptions.setMouseTransparent(true);

            getContentRoot().getChildren().addAll( textResume, textExit, textOptions);

            getContentRoot().setScaleX(0);
            getContentRoot().setScaleY(0);

            animation = FXGL.animationBuilder()
                    .duration(Duration.seconds(0.66))
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .scale(getContentRoot())
                    .from(new Point2D(0, 0))
                    .to(new Point2D(1, 1))
                    .build();
        }

        @Override
        public void onCreate() {
            animation.setOnFinished(EmptyRunnable.INSTANCE);
            animation.stop();
            animation.start();
        }

        @Override
        protected void onUpdate(double tpf) {
            animation.onUpdate(tpf);
        }
    }

    public static class MyMainMenu extends FXGLMenu {
        public MyMainMenu(){
            super(MenuType.MAIN_MENU);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
