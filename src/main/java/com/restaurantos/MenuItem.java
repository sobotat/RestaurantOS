package com.restaurantos;

public class MenuItem {
    int menuItemId;
    Menu menu;
    public Food food;
    int count;

    public MenuItem(int menuItemId, Menu menu, Food food, int count) {
        this.menuItemId = menuItemId;
        this.menu = menu;
        this.food = food;
        this.count = count;
    }
}
