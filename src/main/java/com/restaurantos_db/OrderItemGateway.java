package com.restaurantos_db;

import com.restaurantos_db.identity_maps.OrderItemIdentityMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.restaurantos_domain.Order;
import com.restaurantos_domain.OrderItem;

import java.sql.*;
import java.util.LinkedList;

public class OrderItemGateway implements Gateway<OrderItem> {
    private static final Logger logger = LogManager.getLogger(OrderItemGateway.class.getName());

    @Override
    public OrderItem find(int id) {
        OrderItem orderItem = null;

        // IdentityMap
        OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
        orderItem = orderItemIdentityMap.get(id);
        if(orderItem != null)
            return orderItem;

        // Database
        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `order_item_id`, `order_id`, `menu_item_id`, `count`, `state`, `cooked_by` FROM `restaurantos-db`.`order_item` WHERE order_item_id = ?")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Order Item
                    int orderItemId = resultSet.getInt(1);
                    int order_id = resultSet.getInt(2);
                    int menuItemId = resultSet.getInt(3);
                    int count = resultSet.getInt(4);
                    String state = resultSet.getString(5);
                    Integer cookedById = resultSet.getInt(6);

                    Order order = new OrderGateway().find(order_id);

                    orderItem = new OrderItem( orderItemId, order, menuItemId, count, state, cookedById);
                    orderItemIdentityMap.insert(orderItem);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem DB exception :> " + e.getSQLState());
        }

        return orderItem;
    }

    public LinkedList<OrderItem> findAllForOrder(Order order){
        LinkedList<OrderItem> orderItems = new LinkedList<>();

        // IdentityMap
        OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
        orderItems.addAll(orderItemIdentityMap.get(order));
        if(!orderItems.isEmpty())
            return orderItems;

        // Database
        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `order_item_id`, `menu_item_id`, `count`, `state`, `cooked_by` FROM `restaurantos-db`.`order_item` WHERE `order_id` = ?")){
            statement.setInt(1, order.getOrderId());
            try(ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()) {
                    // Order Item
                    int orderItemId = resultSet.getInt(1);
                    int menuItemId = resultSet.getInt(2);
                    int count = resultSet.getInt(3);
                    String state = resultSet.getString(4);
                    Integer cookedById = resultSet.getInt(5);

                    OrderItem orderItem = new OrderItem( orderItemId, order, menuItemId, count, state, cookedById);
                    orderItems.add(orderItem);
                    orderItemIdentityMap.insert(orderItem);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem DB exception :> " + e.getSQLState());
        }

        return orderItems;
    }

    @Override
    public boolean create(OrderItem obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`order_item` ( `order_id`, `menu_item_id`, `count`) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getOrder().getOrderId());
            preparedStatement.setInt(2, obj.getMenuItem().getMenuItemId());
            preparedStatement.setInt( 3, obj.getCount());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Order
                    obj.setOrderItemId(resultSet.getInt(1));
                }
                preparedStatement.close();
            }

            OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
            orderItemIdentityMap.clearForOrder(obj.getOrder());
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean update(OrderItem obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`order_item` SET `order_id` = ?, `menu_item_id` = ?, `count` = ?, `state` = ? WHERE `order_item_id` = ?;")){
            preparedStatement.setInt( 1, obj.getOrder().getOrderId());
            preparedStatement.setInt(2, obj.getMenuItem().getMenuItemId());
            preparedStatement.setInt( 3, obj.getCount());
            preparedStatement.setString(4, obj.getState());
            preparedStatement.setInt(5, obj.getOrderItemId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(OrderItem obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`order_item` WHERE `order_item_id` = ?")){
            preparedStatement.setInt(1, obj.getOrderItemId());
            preparedStatement.execute();

            OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
            orderItemIdentityMap.clearForOrder(obj.getOrder());
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "OrderItem DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
