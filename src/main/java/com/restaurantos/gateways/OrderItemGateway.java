package com.restaurantos.gateways;

import com.restaurantos.OrderItem;

import java.util.LinkedList;

public class OrderItemGateway implements Gateway<OrderItem> {

    private static LinkedList<OrderItem> orderItems;

    public OrderItemGateway(){
        if (orderItems == null){
            orderItems = new LinkedList<>();
        }
    }

    @Override
    public OrderItem find(int id) {
        for(OrderItem orderItem : orderItems){
            if (orderItem.orderItemId == id)
                return orderItem;
        }

        return null;
    }

    public LinkedList<OrderItem> findAllForOrder(int order_id){
        LinkedList<OrderItem> out = new LinkedList<>();
        for (OrderItem orderItem : orderItems){
            if(orderItem.order.orderId == order_id){
                out.add(orderItem);
            }
        }

        return out;
    }

    @Override
    public void create(OrderItem obj) {
        orderItems.add(obj);
    }

    @Override
    public void update(OrderItem obj) {
        for(OrderItem orderItem : orderItems){
            if(orderItem.orderItemId == obj.orderItemId){
                orderItem = obj;
                return;
            }
        }
    }

    @Override
    public void delete(OrderItem obj) {
        orderItems.remove(obj);
    }
}
