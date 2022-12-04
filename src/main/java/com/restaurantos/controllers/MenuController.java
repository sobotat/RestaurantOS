package com.restaurantos.controllers;

import com.restaurantos_domain.Menu;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        tv_Date.setText(formatter.format(menu.getDate()));
    }

    public void flash() {
        String orgStyle = vbox_Background.getStyle();
        vbox_Background.setStyle(orgStyle + "-fx-background-color: colorRed;");

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                vbox_Background.setStyle(orgStyle + "-fx-background-color: colorLightGray;");
            }
        }, 1000);
    }
}
