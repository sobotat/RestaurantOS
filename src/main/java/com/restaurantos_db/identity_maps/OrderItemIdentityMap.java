package com.restaurantos_db.identity_maps;

import com.restaurantos_domain.Order;
import com.restaurantos_domain.OrderItem;

import java.util.HashMap;
import java.util.LinkedList;

public class OrderItemIdentityMap implements IdentityMap<OrderItem>{

    private static final HashMap<Integer, OrderItem> mapById = new HashMap<>();
    private static final HashMap<Integer, LinkedList<OrderItem>> mapByOrderId = new HashMap<>();

    @Override
    public void insert(OrderItem obj) {
        mapById.put(obj.getOrderItemId(), obj);

        if(!mapByOrderId.containsKey(obj.getOrder().getOrderId()))
            mapByOrderId.put(obj.getOrder().getOrderId(), new LinkedList<>());
        mapByOrderId.get(obj.getOrder().getOrderId()).add(obj);
    }

    @Override
    public OrderItem get(int id) {
        return mapById.get(id);
    }

    public LinkedList<OrderItem> get(Order order){
        LinkedList<OrderItem> orderItems = new LinkedList<>();
        orderItems.addAll(mapByOrderId.getOrDefault(order.getOrderId(), new LinkedList<>()));
        //System.out.println("Returning from IdentityMap " + orderItems.size() + " Items for order " + order.orderId);
        return orderItems;
    }

    @Override
    public void clear() {
        mapById.clear();
        mapByOrderId.clear();
    }

    public void clearForOrder(Order order){
        LinkedList<OrderItem> orderItems = mapByOrderId.get(order.getOrderId());
        if(orderItems != null)
            orderItems.clear();
    }
}
