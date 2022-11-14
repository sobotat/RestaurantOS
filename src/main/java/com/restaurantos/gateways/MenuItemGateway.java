package com.restaurantos.gateways;

import com.restaurantos.MenuItem;

import java.util.LinkedList;

public class MenuItemGateway implements Gateway<MenuItem> {

    @Override
    public MenuItem find(int id) {
        return null;
    }

    public LinkedList<MenuItem> findAllForMenu(int menu_id){
        return null;
    }

    @Override
    public void create(MenuItem obj) {

    }

    @Override
    public void update(MenuItem obj) {

    }

    @Override
    public void delete(MenuItem obj) {

    }
}
