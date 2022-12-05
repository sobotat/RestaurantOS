package com.restaurantos.controllers;

import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.Food;
import com.restaurantos_domain.OrderItem;
import com.restaurantos_domain.unit_of_works.OrderItemUnitOfWork;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class OrderItemViewController {
    private static final Logger logger = LogManager.getLogger(OrderItemViewController.class.getName());

    @FXML
    Button btn_State, btn_Select, btn_Remove;
    @FXML
    Circle dot_State;
    @FXML
    HBox hbox_Root, hbox_btn;
    @FXML
    Text tv_ItemName, tv_Num, tv_Cost;

    boolean selected;
    String orgStyleSelected;

    OrderItem orderItem;
    public static LinkedList<OrderItemViewController> orderItemViewControllers;

    public void setItem(OrderItem orderItem){
        this.orderItem = orderItem;
        this.orgStyleSelected = btn_Select.getStyle();

        if(orderItem.getMenuItem() == null || orderItem.getMenuItem().getFood() == null)
            return;

        tv_ItemName.setText(orderItem.getMenuItem().getFood().getName());

        tv_Num.setText(String.valueOf(orderItem.getCount()));

        updateCost();
        updateState();
    }

    public void cancelItemClicked(){
        if(!AppSecurity.haveAuthForDelete()){
            warning(btn_Remove);
            logger.log(Level.INFO, "User dont have Authority");
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
        updateSelected();
    }

    public void selectItemClicked(){
        if(!orderItem.getState().equals("Served")) {
            warning(btn_Select);
            return;
        }

        selected = !selected;
        updateSelected();
    }

    public void unselectItem(){
        selected = false;
        updateSelected();
    }

    public void changeStateClicked(){
        if(AppSecurity.haveAuthForCookOrder() &&
          (orderItem.getState().equals("Ordered") || orderItem.getState().equals("Preparing"))) {
            switch (orderItem.getState()) {
                case "Ordered" -> orderItem.setState("Preparing");
                case "Preparing" -> orderItem.setState("Prepared");
            }
        }else if(AppSecurity.haveAuthForCreateOrder() &&
                 orderItem.getState().equals("Prepared")) {
            orderItem.setState("Served");
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
            case "Paid"         -> {
                dot_State.setStyle("-fx-fill: colorDarkGreen;");
                hbox_btn.getChildren().remove(btn_Remove);
                hbox_btn.getChildren().remove(btn_Select);
            }
            case "Canceled"     -> {
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

    private void updateSelected(){

        if(selected) {
            String style = orgStyleSelected +
                            "-fx-background-color: colorAccent;" +
                            "-fx-border-color: colorLightGray;" +
                            "-fx-border-radius: 20" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 3, 0, 0, 0);";
            btn_Select.setStyle(style);
        }else
            btn_Select.setStyle(orgStyleSelected);
    }

    private void warning(Node node){
        String orgStyle = node.getStyle();
        node.setStyle(orgStyle + "-fx-background-color: colorRed;");

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                node.setStyle(orgStyle);
            }
        }, 1000);
    }
}
