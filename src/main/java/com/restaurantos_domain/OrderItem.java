package com.restaurantos_domain;

import com.restaurantos_db.MenuItemGateway;
import com.restaurantos_db.UserGateway;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private final int menuItemId;
    private MenuItem menuItem;
    private int count;
    private String state;
    private Integer cookedById;
    private User cookedBy;

    public OrderItem(int orderItemId, Order order, int menuItemId, int count, String state, Integer cookedById) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItemId = menuItemId;
        this.menuItem = null;
        this.count = count;
        this.state = state;
        this.cookedById = cookedById;
        this.cookedBy = null;
    }

    public OrderItem(int orderItemId, Order order, MenuItem menuItem, int count, String state, Integer cookedById) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItemId = menuItem.getMenuItemId();
        this.menuItem = menuItem;
        this.count = count;
        this.state = state;
        this.cookedById = cookedById;
        this.cookedBy = null;
    }

    public MenuItem getMenuItem() {
        if(menuItem != null)
            return menuItem;

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        this.menuItem = menuItemGateway.find(menuItemId);
        return  menuItem;
    }

    public User getCookedBy() {
        if(cookedBy != null || cookedById == null)
            return cookedBy;

        UserGateway userGateway = new UserGateway();
        cookedBy = userGateway.find(cookedById);
        return cookedBy;
    }

    // Setters
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    // Getters
    public int getOrderItemId() {
        return orderItemId;
    }
    public Order getOrder() {
        return order;
    }
    public int getMenuItemId() {
        return menuItemId;
    }
    public int getCount() {
        return count;
    }
    public String getState() {
        return state;
    }
    public Integer getCookedById(){
        return cookedById;
    }
}
