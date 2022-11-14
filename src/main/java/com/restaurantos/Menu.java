package com.restaurantos;

import com.restaurantos.gateways.MenuItemGateway;

import java.util.Date;
import java.util.LinkedList;

public class Menu {
    int menuId;
    Date date;
    Date created_date;

    public Menu(int menuId, Date date, Date created_date) {
        this.menuId = menuId;
        this.date = date;
        this.created_date = created_date;
    }

    public void addMenuItem(MenuItem menuItem){
        menuItem.menu = this;

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        menuItemGateway.create(menuItem);
    }

    public MenuItem getMenuItem(int index){
        MenuItemGateway menuItemGateway = new MenuItemGateway();
        LinkedList<MenuItem> menuItems = menuItemGateway.findAllForMenu(menuId);

        return menuItems.get(index);
    }

    public MenuItem getMenuItemById(int id){
        return new MenuItemGateway().find(id);
    }
}
