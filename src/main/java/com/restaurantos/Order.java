package com.restaurantos;

import java.util.Date;
import java.util.LinkedList;

public class Order {
    public static LinkedList<Order> orders;

    public int orderId;
    public int tableId;
    Date created_date;
    public String status;
    boolean payed;
    public LinkedList<OrderItem> orderItems;

    public Order(int orderId, int tableId, Date created_date, boolean payed) {
        if(orders == null)
            orders = new LinkedList<>();

        this.orderId = orderId;
        this.tableId = tableId;
        this.created_date = created_date;
        this.status = "Ordered";
        this.payed = payed;
        this.orderItems = new LinkedList<>();

        Food food = new Food(1, new Food.FoodType(0, "Food"), "Name", "Description", "ABC", 120);
        MenuItem menuItem = new MenuItem(1, new Menu(1, new Date(), new Date()), food, 40);
        this.orderItems.add(new OrderItem(1, this, menuItem, 1, "Payed"));
        this.orderItems.add(new OrderItem(1, this, menuItem, 2, "Ordered"));
        this.orderItems.add(new OrderItem(1, this, menuItem, 3, "Prepared"));
        this.orderItems.add(new OrderItem(1, this, menuItem, 4, "Preparing"));
        this.orderItems.add(new OrderItem(1, this, menuItem, 5, "Served"));
    }

    public double getCost(){
        double cost = 0;
        for (OrderItem item : orderItems){
            if(!item.state.equals("Canceled"))
                cost += item.menuItem.food.cost * item.count;
        }
        return cost;
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
    }

    public OrderItem getOrderItem(int index){
        return this.orderItems.get(index);
    }

    public OrderItem getOrderItemById(int id){
        for (OrderItem item : this.orderItems){
            if(id == item.orderItemId)
                return item;
        }
        return null;
    }
}
