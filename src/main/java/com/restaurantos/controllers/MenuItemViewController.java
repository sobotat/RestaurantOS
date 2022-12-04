package com.restaurantos.controllers;

import com.restaurantos_db.MenuItemGateway;
import com.restaurantos_domain.AppSecurity;
import com.restaurantos_domain.MenuItem;
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

public class MenuItemViewController {
    private static final Logger logger = LogManager.getLogger(MenuItemViewController.class.getName());

    @FXML
    Button btn_Delete;
    @FXML
    Circle dot_State;
    @FXML
    HBox hbox_Root, hbox_btn;
    @FXML
    Text tv_ItemName, tv_Num, tv_Cost;

    MenuItem menuItem;
    public static LinkedList<MenuItemViewController> menuItemViewControllers;

    public void setItem(MenuItem menuItem){
        this.menuItem = menuItem;

        tv_ItemName.setText(menuItem.getFood().getName());
        tv_Num.setText(String.valueOf(menuItem.getCount()));
        tv_Cost.setText(String.valueOf(menuItem.getFood().getCost()));

        switch (menuItem.getFood().getFoodType().name){
            case "Food"  -> dot_State.setStyle("-fx-fill: colorRed;");
            case "Drink" -> dot_State.setStyle("-fx-fill: colorBlue;");
            default -> dot_State.setStyle("-fx-fill: colorLightGray;");
        }
    }

    public void deleteItemClicked(){
        if(!AppSecurity.haveAuthForDelete()){
            warning(btn_Delete);
            return;
        }

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        boolean status = menuItemGateway.delete(menuItem);

        if(status){
            MenuViewController.menuViewController.loadMenuItems();
            logger.log(Level.INFO, "MenuItem was deleted");
        }else{
            warning(btn_Delete);
        }
    }

    public void editItemClicked(){
        logger.log(Level.WARN, "Not Implemented");
    }

    private void warning(Node node){
        String orgStyle = btn_Delete.getStyle();
        node.setStyle(orgStyle + "-fx-background-color: colorRed;");

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                node.setStyle(orgStyle);
            }
        }, 1000);
    }}
