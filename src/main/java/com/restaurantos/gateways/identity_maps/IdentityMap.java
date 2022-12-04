package com.restaurantos.gateways.identity_maps;

public interface IdentityMap<T> {

    void insert(T obj);
    T get(int id);
    void clear();
}
