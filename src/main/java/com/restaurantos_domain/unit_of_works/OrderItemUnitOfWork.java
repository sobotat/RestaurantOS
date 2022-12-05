package com.restaurantos_domain.unit_of_works;

import com.restaurantos_db.OrderItemGateway;
import com.restaurantos_domain.OrderItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class OrderItemUnitOfWork extends UnitOfWork<OrderItem>{
    private static final Logger logger = LogManager.getLogger(OrderItemUnitOfWork.class.getName());
    private static LinkedList<OrderItem> create = new LinkedList<>();
    private static LinkedList<OrderItem> update = new LinkedList<>();
    private static LinkedList<OrderItem> delete = new LinkedList<>();
    private static LinkedList<OrderItem> clean = new LinkedList<>();

    private void create(LinkedList<OrderItem> orderItems){
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.createBatch(orderItems);
    }

    private void update(LinkedList<OrderItem> orderItems, LinkedList<OrderItem> cleanOrderItems){
        LinkedList<OrderItem> updateItems = new LinkedList<>();

        for(OrderItem orderItem : orderItems){
            if(!cleanOrderItems.contains(orderItem))
                updateItems.add(orderItem);
        }

        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.updateBatch(updateItems);
    }

    private void delete(LinkedList<OrderItem> orderItems){
        OrderItemGateway orderItemGateway = new OrderItemGateway();
        orderItemGateway.deleteBatch(orderItems);
    }

    @Override
    protected void commit() {
        if(create.isEmpty() && update.isEmpty() && delete.isEmpty())
            return;

        logger.log(Level.INFO, "Committed OrderItem UorW\n" +
                "[Created] " + create.size() + "\n" +
                "[Deleted] " + delete.size() + "\n" +
                "[Updated] " + (update.size() - clean.size()));

        create(create);
        delete(delete);
        update(update, clean);

        create.clear();
        delete.clear();
        update.clear();
        clean.clear();
    }

    @Override
    public void addToCreate(OrderItem obj){
        super.addToCreate(obj);
        create.add(obj);
    }

    @Override
    public void addToDelete(OrderItem obj){
        super.addToDelete(obj);
        delete.add(obj);
    }

    @Override
    public void addToUpdate(OrderItem obj){
        super.addToUpdate(obj);
        update.add(obj);
    }

    @Override
    public void addToClean(OrderItem obj){
        super.addToClean(obj);
        clean.add(obj);
    }
}
