package com.restaurantos.controllers;

import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.Food;
import com.restaurantos_domain.OrderItem;
import com.restaurantos_db.unit_of_works.OrderItemUnitOfWork;
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

        if(orderItem.getMenuItem() == null || orderItem.getMenuItem().getFood() == null)
            return;

        tv_ItemName.setText(orderItem.getMenuItem().getFood().getName());

        tv_Num.setText(String.valueOf(orderItem.getCount()));

        updateCost();
        updateState();
    }

    public void cancelItemClicked(){
        if(!AppSecurity.haveAuthForDelete()){
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

        orderItem.setState("Canceled");

        // Unit Of Work
        OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
        orderItemUnitOfWork.addToUpdate(orderItem);

        // OLD
        /*
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.update(orderItem);
        */

        updateState();
        updateCost();
        updateState();
    }

    public void selectItemClicked(){

    }

    public void changeStateClicked(){
        switch (orderItem.getState()){
            case "Ordered"      -> orderItem.setState("Preparing");
            case "Preparing"    -> orderItem.setState("Prepared");
            case "Prepared"     -> orderItem.setState("Served");
        }

        // Unit of Work
        OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
        orderItemUnitOfWork.addToUpdate(orderItem);

        // OLD
        /*
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.update(orderItem);
        */
        updateState();
    }

    private void updateState(){
        switch (orderItem.getState()){
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

        btn_State.setText(orderItem.getState());
        OrderViewController.orderViewController.updateStatus();
    }

    private void updateCost(){
        if(!orderItem.getState().equals("Canceled")) {
            Food food;
            if ((food = orderItem.getMenuItem().getFood()) != null)
                tv_Cost.setText(String.valueOf((food.getCost() * orderItem.getCount())));
        }else
            tv_Cost.setText(String.valueOf((0)));

        OrderViewController.orderViewController.updateCost();
    }

}
