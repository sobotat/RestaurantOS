package com.restaurantos;

public class Table {
    private int tableId;
    private int capacity;
    private boolean reserved;

    public Table(int tableId, int capacity, boolean reserved) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.reserved = reserved;
    }

    // Setters
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    // Getters
    public int getTableId() {
        return tableId;
    }
    public int getCapacity() {
        return capacity;
    }
    public boolean isReserved() {
        return reserved;
    }
}
