package com.restaurantos.controllers;

import com.restaurantos.AppSecurity;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

public class LoginController {

    @FXML
    TextField tf_Email, tf_Password;
    @FXML
    VBox vbox_Login;

    @FXML
    public void loginClicked(){
        String email = tf_Email.getText();
        String password = tf_Password.getText();

        if(email.equals("") || password.equals("")){
            System.out.println("Fill Email and Password");
            badEmailOrPassword();
            return;
        }

        boolean status = AppSecurity.login(email, password);
        if(!status) {
            badEmailOrPassword();
        }

        tf_Email.setText("");
        tf_Password.setText("");
    }

    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER)
            loginClicked();
    }

    private void badEmailOrPassword(){
        String style = vbox_Login.getStyle();
        vbox_Login.setStyle(style + "-fx-background-color: colorRed;");

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                vbox_Login.setStyle(style);
            }
        }, 1000);
    }
}