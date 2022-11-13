package com.restaurantos;

import com.restaurantos.controllers.Controller;
import com.restaurantos.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    protected static Stage mainStage;
    protected static Scene mainScene, loginScene;

    public static boolean useDarkMode = false;
    protected static String lightMode_css, darkMode_css;

    @Override
    public void start(Stage stage) throws IOException {
        logger.log(Level.INFO, "App Started\n-------------------------------------------------------------------------------------------------------------------");
        Settings.loadSettings();

        lightMode_css = Objects.requireNonNull(this.getClass().getResource("light-mode.css")).toExternalForm();
        darkMode_css = Objects.requireNonNull(this.getClass().getResource("dark-mode.css")).toExternalForm();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        mainScene = new Scene(fxmlLoader.load(), Settings.winWidth, Settings.winHeight);

        FXMLLoader fxmlLoginLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        loginScene = new Scene(fxmlLoginLoader.load(), Settings.winWidth, Settings.winHeight);
        setDarkMode(useDarkMode);

        mainStage = stage;
        stage.setTitle("RestaurantOS");
        stage.setScene(loginScene);

        Image icon = new Image("/app_logo.png");
        if (icon.isError()) {
            logger.log(Level.ERROR, "Icon Load Failed");
            logger.log(Level.ERROR, icon.exceptionProperty().get().getMessage());
        }
        stage.getIcons().add(icon);

        stage.setResizable(Settings.winCanResize);
        stage.setMaximized(Settings.winIsMaximize);
        stage.show();

        Controller.controller = fxmlLoader.getController();
        LoginController.loginController = fxmlLoginLoader.getController();
        Controller.controller.initialize();
        Controller.controller.start();

        stage.setOnCloseRequest(e -> {
            logger.log(Level.INFO, "Shutting down");

            Settings.winIsMaximize = stage.isMaximized();
            Settings.winWidth = Math.round((mainScene.getWidth() * 1000)) / (double) 1000;
            Settings.winHeight = Math.round((mainScene.getHeight() * 1000)) / (double) 1000;
            Settings.saveSettings();

            System.exit(0);
        });
    }

    public static void setDarkMode(boolean useDarkMode){
        Main.useDarkMode = useDarkMode;
        String css = useDarkMode ? darkMode_css : lightMode_css;

        mainScene.getStylesheets().clear();
        mainScene.getStylesheets().add(css);

        loginScene.getStylesheets().clear();
        loginScene.getStylesheets().add(css);
    }

    public static void switchScene(Scene scene){
        if(mainStage == null || scene == null) {
            logger.log(Level.ERROR, "Stage or Scene is NULL");
            return;
        }
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}