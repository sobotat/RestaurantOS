package com.restaurantos.controllers;

import com.restaurantos.AppSecurity;
import com.restaurantos.Main;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController {

    public static LoginController loginController = null;

    @FXML
    TextField tf_Email, tf_Password;
    @FXML
    VBox vbox_Login;
    @FXML
    ImageView iv_Logo;

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
        }else{
            tf_Email.setText("");
            tf_Password.setText("");
        }
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
                vbox_Login.setStyle(style + "-fx-background-color: colorLightGray;");
            }
        }, 1000);
    }

    public void initialize(){
        if(Main.useDarkMode){
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app_logo_white.png")));
            iv_Logo.setImage(logo);
        }else{
            Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app_logo.png")));
            iv_Logo.setImage(logo);
        }
    }
}