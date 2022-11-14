package com.restaurantos.gateways;

import java.util.LinkedList;

public abstract class UnitOfWork<T> {

    public LinkedList<T> create;
    public LinkedList<T> update;
    public LinkedList<T> delete;
    public LinkedList<T> clean;

    public UnitOfWork(){
        create = new LinkedList<>();
        update = new LinkedList<>();
        delete = new LinkedList<>();
        clean = new LinkedList<>();
    }
    public abstract void commit();
}
