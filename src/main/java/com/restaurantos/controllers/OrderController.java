package com.restaurantos.controllers;

import com.restaurantos.Order;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class OrderController {

    @FXML
    VBox vbox_Background, vbox_Root;

    @FXML
    Text tv_OrderId, tv_Status, tv_Table;

    Order order;
    public static LinkedList<OrderController> orderControllers;

    public void setItem(Order order){
        this.order = order;
        updateItem();
    }

    public void updateItem(){
        tv_OrderId.setText("Order " + order.orderId);
        tv_Table.setText("Table" + order.tableId);
        tv_Status.setText(order.status);

        String css = " -fx-background-radius: 20;";
        switch (order.status){
            case "Ordered"      -> vbox_Background.setStyle("-fx-background-color: colorLightGray;" + css);
            case "Preparing"    -> vbox_Background.setStyle("-fx-background-color: colorBlue;" + css);
            case "Prepared"     -> vbox_Background.setStyle("-fx-background-color: colorOrange;" + css);
            case "Served"       -> vbox_Background.setStyle("-fx-background-color: colorGreen;" + css);
            case "Payed"        -> vbox_Background.setStyle("-fx-background-color: colorRed;" + css);
        }
    }
}
