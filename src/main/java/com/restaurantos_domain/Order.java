package com.restaurantos_domain;

import com.restaurantos_db.OrderItemGateway;
import com.restaurantos_db.UserGateway;

import java.time.LocalDate;
import java.util.LinkedList;

public class Order {

    private int orderId;
    private Table table;
    private LocalDate createdDate;
    private boolean paid;
    private int createdById;
    private User createdBy;

    public Order(int orderId, Table table, LocalDate createdDate, boolean paid, int createdById) {
        this.orderId = orderId;
        this.table = table;
        this.createdDate = createdDate;
        this.paid = paid;
        this.createdById = createdById;
        this.createdBy = null;
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
        if(paid)
            return "Paid";

        OrderItemGateway orderItemGateway = new OrderItemGateway();
        LinkedList<OrderItem> orderItems = orderItemGateway.findAllForOrder(this);

        if(orderItems.isEmpty())
            return "Empty";

        int numOfOrdered = 0;
        int numOfPreparing = 0;
        int numOfPrepared = 0;
        int numOfServed = 0;
        int numOfPaid = 0;
        int numOfActive = orderItems.size();

        for(OrderItem item: orderItems){
            switch (item.getState()){
                case "Ordered"      -> numOfOrdered++;
                case "Preparing"    -> numOfPreparing++;
                case "Prepared"     -> numOfPrepared++;
                case "Served"       -> numOfServed++;
                case "Paid"         -> numOfPaid++;
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

        return (numOfPaid == numOfActive ? "Paid" : "Served");
    }

    public User getCreatedBy(){
        if(createdBy != null)
            return createdBy;

        UserGateway userGateway = new UserGateway();
        createdBy = userGateway.find(createdById);
        return createdBy;
    }

    // Setters
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
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
    public boolean isPaid() {
        return paid;
    }
    public int getCreatedById() {
        return createdById;
    }
}
