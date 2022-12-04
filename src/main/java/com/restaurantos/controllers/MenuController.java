package com.restaurantos.controllers;

import com.restaurantos.Menu;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;

public class MenuController {

    @FXML
    VBox vbox_Background, vbox_Root;

    @FXML
    Text tv_MenuId, tv_Date;

    Menu menu;
    public static LinkedList<MenuController> menuControllers;

    public void setItem(Menu menu) {
        this.menu = menu;

        tv_MenuId.setText("Menu " + menu.getMenuId());
        tv_Date.setText(menu.getDate().getDay() + "." + menu.getDate().getMonth() + "." + menu.getDate().getYear());
    }
}
