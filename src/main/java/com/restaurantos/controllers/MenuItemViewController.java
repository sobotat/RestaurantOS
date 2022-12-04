package com.restaurantos.controllers;

import com.restaurantos.AppSecurity;
import com.restaurantos.MenuItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MenuItemViewController {

    @FXML
    Button btn_State, btn_Select, btn_Remove;
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

    public void cancelItemClicked(){

    }

    public void selectItemClicked(){

    }

    public void changeStateClicked() {

    }
}
