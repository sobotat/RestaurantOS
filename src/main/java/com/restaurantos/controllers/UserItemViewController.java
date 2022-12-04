package com.restaurantos.controllers;

import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.User;
import com.restaurantos_db.UserGateway;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class UserItemViewController {
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
        System.out.println("Edit Clicked");
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
