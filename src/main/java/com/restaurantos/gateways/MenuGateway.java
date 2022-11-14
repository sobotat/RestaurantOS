package com.restaurantos.gateways;

import com.restaurantos.Menu;

import java.util.Date;

public class MenuGateway implements Gateway<Menu> {

    @Override
    public Menu find(int id) {
        return null;
    }

    public Menu findForDay(Date date){
        return null;
    }

    @Override
    public void create(Menu obj) {

    }

    @Override
    public void update(Menu obj) {

    }

    @Override
    public void delete(Menu obj) {

    }
}
