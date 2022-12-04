package com.restaurantos_domain;

import com.restaurantos_db.MenuItemGateway;

import java.time.LocalDate;
import java.util.LinkedList;

public class Menu {
    private int menuId;
    private LocalDate date;
    private LocalDate createdDate;

    public Menu(int menuId, LocalDate date, LocalDate createdDate) {
        this.menuId = menuId;
        this.date = date;
        this.createdDate = createdDate;
    }

    public void addMenuItem(MenuItem menuItem){
        menuItem.setMenu(this);

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        menuItemGateway.create(menuItem);
    }

    public MenuItem getMenuItem(int index){
        MenuItemGateway menuItemGateway = new MenuItemGateway();
        LinkedList<MenuItem> menuItems = menuItemGateway.findAllForMenu(this);

        return menuItems.get(index);
    }

    public MenuItem getMenuItemById(int id){
        return new MenuItemGateway().find(id);
    }

    // Setters
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    // Getters
    public int getMenuId() {
        return menuId;
    }
    public LocalDate getDate() {
        return date;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
