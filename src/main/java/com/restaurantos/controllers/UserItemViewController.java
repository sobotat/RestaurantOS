package com.restaurantos.controllers;

import com.restaurantos.Main;
import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.User;
import com.restaurantos_db.UserGateway;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;

public class UserItemViewController {
    private static final Logger logger = LogManager.getLogger(UserItemViewController.class.getName());
    public static LinkedList<UserItemViewController> userItemViewControllers;

    @FXML
    Button btn_Edit, btn_Remove;
    @FXML
    Circle dot_State;
    @FXML
    HBox hbox_Root, hbox_btn;
    @FXML
    Text tv_Name, tv_Role, tv_Email;

    User user;

    public void setItem(User user) {
        this.user = user;

        tv_Name.setText(user.getFirstName() + " " + user.getLastName());
        tv_Role.setText(user.getUserRole().getName());
        tv_Email.setText(user.getEmail());
    }

    @FXML
    void editClicked() {
        try {
            // Loading FXML
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("user-dialog.fxml"));
            VBox parent = fxmlLoader.load();

            // Setting a Controller
            UserDialogController userDialogController = fxmlLoader.getController();

            // Creating Scene and Stage for Dialog
            Scene scene = new Scene(parent);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Main.useDarkMode ? Main.darkMode_css : Main.lightMode_css);
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setResizable(false);
            stage.setTitle("User Info");
            stage.setScene(scene);

            // Adding Stage to Controller
            userDialogController.initAsUpdate(stage, user);

            // Changing style of stage
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);

            // Loading App Icon
            javafx.scene.image.Image icon = new Image("/app_logo.png");
            if (icon.isError()) {
                logger.error("Icon Load Failed");
                logger.error(icon.exceptionProperty().get().getMessage());
            }
            stage.getIcons().add( icon);

            // Starting Dialog
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("User Dialog load failed");
        }
    }

    @FXML
    void removeClicked() {

        if(user.equals(AppSecurity.getSignInUser()))
            return;

        UserGateway userGateway = new UserGateway();
        userGateway.delete(user);

        UserViewController.userViewController.loadUserItems();
    }
}
