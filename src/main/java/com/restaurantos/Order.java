package com.restaurantos;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

public class Order {
    public static LinkedList<Order> orders;

    public int orderId;
    public int tableId;
    Date created_date;
    boolean payed;
    public LinkedList<OrderItem> orderItems;

    public Order(int orderId, int tableId, Date created_date, boolean payed) {
        if(orders == null)
            orders = new LinkedList<>();

        this.orderId = orderId;
        this.tableId = tableId;
        this.created_date = created_date;
        this.payed = payed;
        this.orderItems = new LinkedList<>();

        Food food = new Food(1, new Food.FoodType(0, "Food"), "Name", "Description", "ABC", 120);
        MenuItem menuItem = new MenuItem(1, new Menu(1, new Date(), new Date()), food, 40);

        LinkedList<OrderItem> ritems = new LinkedList<>();
        ritems.add(new OrderItem(1, this, menuItem, 2, "Ordered"));
        ritems.add(new OrderItem(1, this, menuItem, 3, "Prepared"));
        ritems.add(new OrderItem(1, this, menuItem, 5, "Served"));
        ritems.add(new OrderItem(1, this, menuItem, 4, "Preparing"));
        ritems.add(new OrderItem(1, this, menuItem, 5, "Served"));

        for(int i = 0; i < new Random().nextInt(3, 6); i++){
            orderItems.add(ritems.get(new Random().nextInt(0, ritems.size())));
        }
    }

    public double getCost(){
        double cost = 0;
        for (OrderItem item : orderItems){
            if(!item.state.equals("Canceled"))
                cost += item.menuItem.food.cost * item.count;
        }
        return cost;
    }

    public String getStatus(){
        int numOfOrdered = 0;
        int numOfPreparing = 0;
        int numOfPrepared = 0;
        int numOfServed = 0;
        int numOfPayed = 0;
        int numOfActive = orderItems.size();

        for(OrderItem item: orderItems){
            switch (item.state){
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
