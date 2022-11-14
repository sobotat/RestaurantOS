package com.restaurantos.gateways;

public interface Gateway<T> {

    T find(int id);
    void create(T obj);
    void update(T obj);
    void delete(T obj);

}
