package org.example.Menu;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.DB.JdbcUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameController;


public class MyMainMenu extends FXGLMenu {

    private Animation<?> animation;
    private boolean verify(String username, String password) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "select count(1) count from `user` where username = ? and password = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean exists(String username) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "select count(1) count from `user` where username = ?";
        List<String> params = new ArrayList<>();
        params.add(username);
        try {
            return jdbcUtils.count(sql, params) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean signUp(String username, String password) {
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();

        String sql = "insert into `user` (username,password) values (?,?)";
        List<String> params = new ArrayList<>();
        params.add(username);
        params.add(password);
        try {
            return jdbcUtils.insert(sql, params) == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public MyMainMenu(){
        super(MenuType.MAIN_MENU);


        Image backgroundimage = new Image("assets/textures/kunkun.jpg");
        ImageView backgroundimageview = new ImageView(backgroundimage);

        MainMenuButton start = new MainMenuButton("Start", this::fireNewGame);
        MainMenuButton exit = new MainMenuButton("Exit", () -> getGameController().exit());
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(start,exit);
        start.setSelected(true);
        VBox menubox = new VBox(
                5,
                start,
                exit
        );
        menubox.setAlignment(Pos.CENTER_LEFT);
        menubox.setLayoutX(240);
        menubox.setLayoutY(360);
        menubox.setVisible(false);

        VBox loginBox = new VBox(5);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setLayoutX(480);
        loginBox.setLayoutY(360);
        loginBox.setVisible(true);

        VBox signUpBox = new VBox(5);
        signUpBox.setAlignment(Pos.CENTER_LEFT);
        signUpBox.setLayoutX(280);
        signUpBox.setLayoutY(360);
        signUpBox.setVisible(true);

        TextField loginBoxUsername = new TextField();
        loginBoxUsername.setId("inputText");
        loginBoxUsername.setPromptText("username");

        TextField signUpUsername = new TextField();
        signUpUsername.setId("inputText");
        signUpUsername.setPromptText("username");

        TextField loginBoxPassword = new TextField();
        loginBoxPassword.setId("inputText");
        loginBoxPassword.setPromptText("password");

        TextField signUpPassword = new TextField();
        signUpPassword.setId("inputText");
        signUpPassword.setPromptText("password");

        Label loginBoxLabel = new Label();
        loginBoxLabel.setId("label");
        loginBoxLabel.setStyle("-fx-text-fill: red;");

        Label signUpBoxLabel = new Label();
        signUpBoxLabel.setId("label");
        signUpBoxLabel.setStyle("-fx-text-fill: red;");

        Button signUpBoxBtn = new Button();
        signUpBoxBtn.setId("loginBtn");
        signUpBoxBtn.setText("Sign up");
        signUpBoxBtn.setOnAction(event -> {
            if (exists(signUpUsername.getText())) {
                signUpBoxLabel.setText("小黑子已经存在啦");
            } else {
                if (signUp(signUpUsername.getText(), signUpPassword.getText())) {
                    signUpBox.setVisible(false);
                    menubox.setVisible(true);
                }

            }
        });



        Button loginBtn = new Button();
        loginBtn.setId("loginBtn");
        loginBtn.setText("Sign in");
        loginBtn.setOnAction(event -> {
            if (verify(loginBoxUsername.getText(), loginBoxPassword.getText())) {
                loginBox.setVisible(false);
                menubox.setVisible(true);
            } else {
                loginBoxLabel.setText("你怕不是真爱粉吧");
            }
        });

        loginBox.getChildren().addAll(
                loginBoxUsername,
                loginBoxPassword,
                loginBoxLabel,
                loginBtn);

        signUpBox.getChildren().addAll(
                signUpUsername,
                signUpPassword,
                signUpBoxLabel,
                signUpBoxBtn);

        menubox.toFront();
        signUpBox.toFront();
        loginBox.toFront();

        getContentRoot().getChildren().addAll( backgroundimageview, loginBox, signUpBox, menubox);



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



