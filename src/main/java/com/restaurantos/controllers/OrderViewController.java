package com.restaurantos.controllers;

import com.restaurantos.Main;
import com.restaurantos_domain.Order;
import com.restaurantos_domain.OrderItem;
import com.restaurantos_db.OrderItemGateway;
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

        tv_LeftTitle.setText("Order " + order.getOrderId());
        tv_RightTitle.setText("at Table " + order.getTable().getTableId());

        updateStatus();
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

    public void addOrderItem(OrderItem orderItem){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("orderitem-item.fxml"));
            Node node = fxmlLoader.load();

            OrderItemViewController orderItemViewController = fxmlLoader.getController();
            orderItemViewController.setItem(orderItem);

            node.setOnMouseEntered(event -> node.setStyle("-fx-background-color: colorDarkWhite"));
            node.setOnMouseExited(event -> node.setStyle("-fx-background-color: colorLightWhite"));

            OrderItemViewController.orderItemViewControllers.add(orderItemViewController);
            vbox_List.getChildren().add(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadOrderItems(){
        OrderItemViewController.orderItemViewControllers = new LinkedList<>();

        OrderItemGateway orderItemGateway = new OrderItemGateway();
        LinkedList<OrderItem> orderItems = orderItemGateway.findAllForOrder(order);

        Node[] nodes = new Node[orderItems.size()];
        vbox_List.getChildren().clear();
        try {
            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("orderitem-item.fxml"));
                nodes[i] = fxmlLoader.load();

                OrderItemViewController orderItemViewController = fxmlLoader.getController();
                orderItemViewController.setItem(orderItems.get(i));

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

    public void updateStatus() {
        String css = " -fx-background-radius: 20 20 0 0;";
        switch (order.getStatus()){
            case "Ordered", "Empty"  -> hbox_Top.setStyle("-fx-background-color: colorLightGray;" + css);
            case "Preparing"    -> hbox_Top.setStyle("-fx-background-color: colorBlue;" + css);
            case "Prepared"     -> hbox_Top.setStyle("-fx-background-color: colorOrange;" + css);
            case "Served"       -> hbox_Top.setStyle("-fx-background-color: colorGreen;" + css);
            case "Paid"         -> hbox_Top.setStyle("-fx-background-color: colorDarkGreen;" + css);
            case "Canceled" -> hbox_Top.setStyle("-fx-background-color: colorRed;" + css);
        }
    }
}
