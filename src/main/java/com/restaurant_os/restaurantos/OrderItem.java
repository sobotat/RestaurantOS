package com.restaurant_os.restaurantos;

public class OrderItem {
    int orderItemId;
    Order order;
    MenuItem menuItem;
    int count;
    String state;

    public OrderItem(int orderItemId, Order order, MenuItem menuItem, int count, String state) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.menuItem = menuItem;
        this.count = count;
        this.state = state;
    }
}
