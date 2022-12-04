package com.restaurantos_db.unit_of_works;

import com.restaurantos_db.Gateway;
import com.restaurantos_db.identity_maps.OrderItemIdentityMap;
import com.restaurantos_domain.OrderItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;

public class OrderItemUnitOfWork extends UnitOfWork<OrderItem>{
    private static final Logger logger = LogManager.getLogger(OrderItemUnitOfWork.class.getName());
    private static LinkedList<OrderItem> create = new LinkedList<>();
    private static LinkedList<OrderItem> update = new LinkedList<>();
    private static LinkedList<OrderItem> delete = new LinkedList<>();
    private static LinkedList<OrderItem> clean = new LinkedList<>();

    private void create(LinkedList<OrderItem> orderItems){
        if(orderItems.isEmpty())
            return;

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `order_item` ( `order_id`, `menu_item_id`, `count`) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){

            LinkedList<OrderItem> createdItems = new LinkedList<>();
            for(OrderItem orderItem: orderItems) {
                preparedStatement.setInt( 1, orderItem.getOrder().getOrderId());
                preparedStatement.setInt(2, orderItem.getMenuItem().getMenuItemId());
                preparedStatement.setInt( 3, orderItem.getCount());

                preparedStatement.addBatch();
                createdItems.add(orderItem);
            }
            if(createdItems.isEmpty())
                return;

            preparedStatement.executeBatch();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
                Iterator<OrderItem> iterator = createdItems.iterator();
                while (resultSet.next()) {
                    OrderItem orderItem = iterator.next();
                    orderItem.setOrderItemId(resultSet.getInt(1));
                    orderItemIdentityMap.clearForOrder(orderItem.getOrder());
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem UofW DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
    }

    private void delete(LinkedList<OrderItem> orderItems){
        if(orderItems.isEmpty())
            return;

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `order_item` WHERE `order_item_id` = ?")){

            OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
            for(OrderItem orderItem : orderItems) {
                preparedStatement.setInt(1, orderItem.getOrderItemId());
                preparedStatement.addBatch();

                orderItemIdentityMap.clearForOrder(orderItem.getOrder());
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem UofW DB exception :> " + e.getSQLState());
        }
    }

    private void update(LinkedList<OrderItem> orderItems, LinkedList<OrderItem> cleanOrderItems){
        if(orderItems.isEmpty())
            return;

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `order_item` SET `order_id` = ?, `menu_item_id` = ?, `count` = ?, `state` = ? WHERE `order_item_id` = ?;")){

            for(OrderItem orderItem : orderItems) {
                if(cleanOrderItems.contains(orderItem))
                    continue;

                preparedStatement.setInt(1, orderItem.getOrder().getOrderId());
                preparedStatement.setInt(2, orderItem.getMenuItem().getMenuItemId());
                preparedStatement.setInt(3, orderItem.getCount());
                preparedStatement.setString(4, orderItem.getState());
                preparedStatement.setInt(5, orderItem.getOrderItemId());

                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem UofW DB exception :> " + e.getSQLState());
        }
    }

    @Override
    public void commit() {
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
