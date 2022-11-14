package com.restaurantos.gateways;

import com.restaurantos.Table;

import java.util.LinkedList;

public class TableGateway implements Gateway<Table> {

    @Override
    public Table find(int id) {
        return null;
    }

    public LinkedList<Table> findAllTables(){
        return null;
    }

    @Override
    public void create(Table obj) {

    }

    @Override
    public void update(Table obj) {

    }

    @Override
    public void delete(Table obj) {

    }
}
