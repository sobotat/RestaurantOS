package com.restaurantos;

import com.restaurantos.gateways.FoodGateway;

public class MenuItem {
    private int menuItemId;
    private Menu menu;
    private final int foodId;
    private Food food;
    private int count;

    public MenuItem(int menuItemId, Menu menu, int foodId, int count) {
        this.menuItemId = menuItemId;
        this.menu = menu;
        this.foodId = foodId;
        this.food = null;
        this.count = count;
    }

    public MenuItem(int menuItemId, Menu menu, Food food, int count) {
        this.menuItemId = menuItemId;
        this.menu = menu;
        this.foodId = food.getFoodId();
        this.food = food;
        this.count = count;
    }

    public Food getFood() {
        if(food != null)
            return food;

        FoodGateway foodGateway = new FoodGateway();
        this.food = foodGateway.find(foodId);
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    // Setters
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    // Getters
    public int getMenuItemId() {
        return menuItemId;
    }
    public Menu getMenu() {
        return menu;
    }
    public int getFoodId() {
        return foodId;
    }
    public int getCount() {
        return count;
    }
}
