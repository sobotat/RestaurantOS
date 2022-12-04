package com.restaurantos.gateways.unit_of_works;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class UnitOfWork<T> {
    private static Timer timer;
    public UnitOfWorkCommittedCallBack committedCallBack;

    public void addToCreate(T obj){
        resetTimer();
    }

    public void addToDelete(T obj){
        resetTimer();
    }

    public void addToUpdate(T obj){
        resetTimer();
    }

    public void addToClean(T obj){
        resetTimer();
    }

    public abstract void commit();
    public void resetTimer(){
        if(timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                commit();
                if(committedCallBack != null)
                    committedCallBack.committed();
            }
        }, 5000);
    }

    public interface UnitOfWorkCommittedCallBack{
        void committed();
    }
}
