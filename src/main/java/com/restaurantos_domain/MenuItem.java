package com.restaurantos_domain;

import com.restaurantos_db.FoodGateway;

public class MenuItem {
    private int menuItemId;
    private Menu menu;
    private final int foodId;
    private Food food;
    private int count;
    private double cost;

    public MenuItem(int menuItemId, Menu menu, int foodId, int count, double cost) {
        this.menuItemId = menuItemId;
        this.menu = menu;
        this.foodId = foodId;
        this.food = null;
        this.count = count;
        this.cost = cost;
    }

    public MenuItem(int menuItemId, Menu menu, Food food, int count, double cost) {
        this.menuItemId = menuItemId;
        this.menu = menu;
        this.foodId = food.getFoodId();
        this.food = food;
        this.count = count;
        this.cost = cost;
    }

    public Food getFood() {
        if(food != null)
            return food;

        FoodGateway foodGateway = new FoodGateway();
        this.food = foodGateway.find(foodId);
        return food;
    }

    // Setters
    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    public void setFood(Food food) {
        this.food = food;
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
    public double getCost() { return cost; }
}
