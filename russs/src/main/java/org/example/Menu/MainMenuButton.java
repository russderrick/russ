package org.example.Menu;

import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;

public class MainMenuButton extends RadioButton {
    public MainMenuButton(String text, Runnable action){
        setText(text);
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                action.run();
            }
        });
        setOnMouseClicked(event -> {
            action.run();
        });
        setOnMouseEntered(e -> {
                    setSelected(true);
                }
        );
    }

}
