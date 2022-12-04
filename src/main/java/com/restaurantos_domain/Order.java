package com.restaurantos_domain;

import com.restaurantos_db.OrderItemGateway;

import java.time.LocalDate;
import java.util.LinkedList;

public class Order {

    private int orderId;
    private Table table;
    private LocalDate createdDate;
    private boolean payed;

    public Order(int orderId, Table table, LocalDate createdDate, boolean payed) {
        this.orderId = orderId;
        this.table = table;
        this.createdDate = createdDate;
        this.payed = payed;
    }

    public double getCost(){
        double cost = 0;

        OrderItemGateway orderItemGateway = new OrderItemGateway();
        LinkedList<OrderItem> orderItems = orderItemGateway.findAllForOrder(this);

        for (OrderItem item : orderItems){
            if(!item.getState().equals("Canceled"))
                cost += item.getMenuItem().getFood().getCost() * item.getCount();
        }
        return cost;
    }

    public String getStatus(){
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        LinkedList<OrderItem> orderItems = orderItemGateway.findAllForOrder(this);

        if(orderItems.isEmpty())
            return "Empty";

        int numOfOrdered = 0;
        int numOfPreparing = 0;
        int numOfPrepared = 0;
        int numOfServed = 0;
        int numOfPayed = 0;
        int numOfActive = orderItems.size();

        for(OrderItem item: orderItems){
            switch (item.getState()){
                case "Ordered"      -> numOfOrdered++;
                case "Preparing"    -> numOfPreparing++;
                case "Prepared"     -> numOfPrepared++;
                case "Served"       -> numOfServed++;
                case "Payed"        -> numOfPayed++;
                case "Canceled"     -> numOfActive--;
            }
        }

        if(numOfActive == 0)
            return "Canceled";
        if(numOfPrepared > 0)
            return "Prepared";
        if(numOfPreparing > 0)
            return "Preparing";
        if(numOfOrdered > 0)
            return "Ordered";

        return (numOfPayed == numOfActive ? "Payed" : "Served");
    }

    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);

        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.create(orderItem);
    }

    public OrderItem getOrderItem(int index){
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        LinkedList<OrderItem> orderItems = orderItemGateway.findAllForOrder(this);

        return orderItems.get(index);
    }

    public OrderItem getOrderItemById(int id){
        return new OrderItemGateway().find(id);
    }

    // Setters
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }
    public Table getTable() {
        return table;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public boolean isPayed() {
        return payed;
    }
}
