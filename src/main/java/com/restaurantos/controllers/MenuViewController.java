package com.restaurantos.controllers;

import com.restaurantos.Main;
import com.restaurantos_domain.Menu;
import com.restaurantos_domain.MenuItem;
import com.restaurantos_db.MenuItemGateway;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class MenuViewController extends ViewController {

    public static MenuViewController menuViewController;
    Menu menu;

    public void setUp(Menu menu){
        this.menu = menu;
        menuViewController = this;

        tv_LeftTitle.setText("Menu");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        tv_RightTitle.setText(formatter.format(menu.getDate()));
        tv_Text.setText("");

        loadMenuItems();
    }

    public void updateViewSize(double width, double height){
        vbox_Root.setPrefWidth(width);
        vbox_Root.setMaxWidth(width);
        vbox_Root.setMinWidth(width);
        vbox_Root.setPrefHeight(height);
        vbox_Root.setMaxHeight(height);
        vbox_Root.setMinHeight(height);
    }

    public void loadMenuItems(){
        MenuItemViewController.menuItemViewControllers = new LinkedList<>();

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        LinkedList<MenuItem> menuItems = menuItemGateway.findAllForMenu(menu);

        Node[] nodes = new Node[menuItems.size()];
        vbox_List.getChildren().clear();
        try {
            for (int i = 0; i < nodes.length; i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("menuitem-item.fxml"));
                nodes[i] = fxmlLoader.load();

                MenuItemViewController menuItemViewController = fxmlLoader.getController();
                menuItemViewController.setItem(menuItems.get(i));

                int h = i;
                nodes[i].setOnMouseEntered(event -> nodes[h].setStyle("-fx-background-color: colorDarkWhite"));
                nodes[i].setOnMouseExited(event -> nodes[h].setStyle("-fx-background-color: colorLightWhite"));

                MenuItemViewController.menuItemViewControllers.add(menuItemViewController);
                vbox_List.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
