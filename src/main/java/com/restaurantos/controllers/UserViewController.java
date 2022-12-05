package com.restaurantos.controllers;

import com.restaurantos.Main;
import com.restaurantos_domain.User;
import com.restaurantos_db.UserGateway;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class UserViewController extends ViewController {

    public static UserViewController userViewController;

    public void setUp(){
        userViewController = this;

        tv_LeftTitle.setText("Users");
        tv_RightTitle.setText("");
        tv_Text.setText("");

        String css = " -fx-background-radius: 20 20 0 0;";
        hbox_Top.setStyle("-fx-background-color: colorRed;" + css);

        loadUserItems();
    }

    public void updateViewSize(double width, double height){
        vbox_Root.setPrefWidth(width);
        vbox_Root.setMaxWidth(width);
        vbox_Root.setMinWidth(width);
        vbox_Root.setPrefHeight(height);
        vbox_Root.setMaxHeight(height);
        vbox_Root.setMinHeight(height);
    }

    public void loadUserItems(){
        UserItemViewController.userItemViewControllers = new LinkedList<>();

        UserGateway userGateway = new UserGateway();
        LinkedList<User> users = userGateway.findAllUsers();
        users.sort((o1, o2) -> {
            if(o1.getUserRole().getName().equals("Manager"))
                return -1;
            if(o2.getUserRole().getName().equals("Manager"))
                return 1;
            if(o1.getUserRole().getName().equals("Service") && o2.getUserRole().getName().equals("Chef"))
                return -1;
            if(o1.getUserRole().getName().equals("Chef") && o2.getUserRole().getName().equals("Service")){
                return 1;
            }
            return 0;
        });

        Node[] nodes = new Node[users.size()];
        vbox_List.getChildren().clear();

        try {
            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("user-item.fxml"));
                nodes[i] = fxmlLoader.load();

                UserItemViewController userItemViewController = fxmlLoader.getController();
                userItemViewController.setItem(users.get(i));

                int h = i;
                nodes[i].setOnMouseEntered(event -> nodes[h].setStyle("-fx-background-color: colorDarkWhite"));
                nodes[i].setOnMouseExited(event -> nodes[h].setStyle("-fx-background-color: colorLightWhite"));

                UserItemViewController.userItemViewControllers.add(userItemViewController);
                vbox_List.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
