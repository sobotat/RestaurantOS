package com.restaurant_os.restaurantos;

public class Table {
    int tableId;
    int capacity;
    boolean reserved;

    public Table(int tableId, int capacity, boolean reserved) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.reserved = reserved;
    }
}
