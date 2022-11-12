package com.restaurantos.controllers;

import com.restaurantos.Main;
import com.restaurantos.Order;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.LinkedList;

public class OrderViewController extends ViewController {

    public static OrderViewController orderViewController;
    Order order;

    public void setUp(Order order){
        this.order = order;
        orderViewController = this;

        tv_LeftTitle.setText("Order " + order.orderId);
        tv_RightTitle.setText("at Table " + order.tableId);

        String css = " -fx-background-radius: 20;";
        switch (order.status){
            case "Ordered"      -> hbox_Top.setStyle("-fx-background-color: colorLightGray;" + css);
            case "Preparing"    -> hbox_Top.setStyle("-fx-background-color: colorBlue;" + css);
            case "Prepared"     -> hbox_Top.setStyle("-fx-background-color: colorOrange;" + css);
            case "Served"       -> hbox_Top.setStyle("-fx-background-color: colorGreen;" + css);
            case "Payed"        -> hbox_Top.setStyle("-fx-background-color: colorRed;" + css);
        }

        loadOrderItems();
    }

    public void updateViewSize(double width, double height){
        vbox_Root.setPrefWidth(width);
        vbox_Root.setMaxWidth(width);
        vbox_Root.setMinWidth(width);
        vbox_Root.setPrefHeight(height);
        vbox_Root.setMaxHeight(height);
        vbox_Root.setMinHeight(height);
    }

    public void loadOrderItems(){
        OrderItemViewController.orderItemViewControllers = new LinkedList<>();
        Node[] nodes = new Node[order.orderItems.size()];

        try {
            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("orderitem-item.fxml"));
                nodes[i] = fxmlLoader.load();

                OrderItemViewController orderItemViewController = fxmlLoader.getController();
                orderItemViewController.setItem(order.orderItems.get(i));

                int h = i;
                nodes[i].setOnMouseEntered(event -> nodes[h].setStyle("-fx-background-color: colorDarkWhite"));
                nodes[i].setOnMouseExited(event -> nodes[h].setStyle("-fx-background-color: colorLightWhite"));

                OrderItemViewController.orderItemViewControllers.add(orderItemViewController);
                vbox_List.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCost(){
        tv_Text.setText("Cost: " + order.getCost());
    }
}
