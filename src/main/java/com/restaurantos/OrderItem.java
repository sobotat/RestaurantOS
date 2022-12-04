package com.restaurantos;

import com.restaurantos.gateways.MenuItemGateway;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private final int menuItemId;
    private MenuItem menuItem;
    private int count;
    private String state;

    public OrderItem(int orderItemId, Order order, int menuItemId, int count, String state) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItemId = menuItemId;
        this.menuItem = null;
        this.count = count;
        this.state = state;
    }

    public OrderItem(int orderItemId, Order order, MenuItem menuItem, int count, String state) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItemId = menuItem.getMenuItemId();
        this.menuItem = menuItem;
        this.count = count;
        this.state = state;
    }

    public MenuItem getMenuItem() {
        if(menuItem != null)
            return menuItem;

        MenuItemGateway menuItemGateway = new MenuItemGateway();
        this.menuItem = menuItemGateway.find(menuItemId);
        return  menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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
}
