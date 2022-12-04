package com.restaurantos_db.identity_maps;

import com.restaurantos_domain.Order;

import java.util.HashMap;

public class OrderIdentityMap implements IdentityMap<Order> {

    private static final HashMap<Integer, Order> map = new HashMap<>();

    @Override
    public void insert(Order obj) {
        map.put(obj.getOrderId(), obj);
    }

    @Override
    public Order get(int id) {
        return map.get(id);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
