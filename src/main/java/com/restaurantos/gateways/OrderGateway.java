package com.restaurantos.gateways;

import com.restaurantos.Order;

import java.util.LinkedList;

public class OrderGateway implements Gateway<Order> {
    private static LinkedList<Order> orders;

    public OrderGateway(){
        if(orders == null)
            orders = new LinkedList<>();
    }

    @Override
    public Order find(int id) {
        for (Order order: orders) {
            if(order.orderId == id){
                return order;
            }
        }

        return null;
    }

    public LinkedList<Order> findAllForDay(){
        return orders;
    }

    @Override
    public void create(Order obj) {
        obj.orderId = orders.size();
        orders.add(obj);
    }

    @Override
    public void update(Order obj) {
        for (Order order: orders) {
            if(order.orderId == obj.orderId){
                order = obj;
                return;
            }
        }
    }

    @Override
    public void delete(Order obj) {
        orders.remove(obj);
    }
}
