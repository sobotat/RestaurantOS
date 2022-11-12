package com.restaurantos.controllers;

import com.restaurantos.User;
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

        tv_Name.setText(user.firstName + " " + user.lastName);
        tv_Role.setText(user.userRole.name);
        tv_Email.setText(user.email);
    }

    @FXML
    void editClicked() {
        System.out.println("Edit Clicked");
    }

    @FXML
    void removeClicked() {
        System.out.println("Remove Clicked");
    }
}
