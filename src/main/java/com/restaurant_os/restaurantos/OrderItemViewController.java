package com.restaurant_os.restaurantos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class OrderItemViewController {

    @FXML
    Button btn_State, btn_Select, btn_Remove;
    @FXML
    Circle dot_State;
    @FXML
    HBox hbox_Root, hbox_btn;
    @FXML
    Text tv_ItemName, tv_Num, tv_Cost;

    OrderItem orderItem;
    public static LinkedList<OrderItemViewController> orderItemViewControllers;

    public void setItem(OrderItem orderItem){
        this.orderItem = orderItem;

        tv_ItemName.setText(orderItem.menuItem.food.name);
        tv_Num.setText(String.valueOf(orderItem.count));

        updateCost();
        updateState();
    }

    public void cancelItemClicked(){
        if(!AppSecurity.getSignInUser().userRole.name.equals("Manager")){
            String orgStyle = btn_Remove.getStyle();
            btn_Remove.setStyle(orgStyle + "-fx-background-color: colorRed;");

            Timer timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    btn_Remove.setStyle(orgStyle);
                }
            }, 1000);

            return;
        }

        orderItem.state = "Canceled";

        updateCost();
        updateState();
    }

    public void selectItemClicked(){

    }

    public void changeStateClicked(){
        switch (orderItem.state){
            case "Ordered"      -> orderItem.state = "Preparing";
            case "Preparing"    -> orderItem.state = "Prepared";
            case "Prepared"     -> orderItem.state = "Served";
        }

        updateState();
    }

    private void updateState(){
        switch (orderItem.state){
            case "Ordered"      -> dot_State.setStyle("-fx-fill: colorLightGray;");
            case "Preparing"    -> dot_State.setStyle("-fx-fill: colorBlue;");
            case "Prepared"     -> dot_State.setStyle("-fx-fill: colorOrange;");
            case "Served"       -> dot_State.setStyle("-fx-fill: colorGreen;");
            case "Payed", "Canceled" -> {
                dot_State.setStyle("-fx-fill: colorRed;");
                hbox_btn.getChildren().remove(btn_Remove);
                hbox_btn.getChildren().remove(btn_Select);
            }
        }

        btn_State.setText(orderItem.state);
    }

    private void updateCost(){
        if(!orderItem.state.equals("Canceled"))
            tv_Cost.setText(String.valueOf((orderItem.menuItem.food.cost * orderItem.count)));
        else
            tv_Cost.setText(String.valueOf((0)));

        OrderViewController.orderViewController.updateCost();
    }

}
