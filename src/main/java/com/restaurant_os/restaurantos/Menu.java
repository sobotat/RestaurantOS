package com.restaurant_os.restaurantos;

import java.util.Date;
import java.util.LinkedList;

public class Menu {
    int menuId;
    Date date;
    Date created_date;
    LinkedList<MenuItem> menuItems;

    public Menu(int menuId, Date date, Date created_date) {
        this.menuId = menuId;
        this.date = date;
        this.created_date = created_date;
        this.menuItems = new LinkedList<>();
    }

    public void addMenuItem(MenuItem menuItem){
        this.menuItems.add(menuItem);
    }

    public MenuItem getMenuItem(int index){
        return this.menuItems.get(index);
    }

    public MenuItem getMenuItemById(int id){
        for (MenuItem item : this.menuItems){
            if(id == item.menuItemId)
                return item;
        }
        return null;
    }
}
