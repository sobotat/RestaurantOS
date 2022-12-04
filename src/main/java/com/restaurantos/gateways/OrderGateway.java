package com.restaurantos.gateways;

import com.restaurantos.Order;
import com.restaurantos.Table;
import com.restaurantos.gateways.identity_maps.OrderIdentityMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class OrderGateway implements Gateway<Order> {
    private static final Logger logger = LogManager.getLogger(OrderGateway.class.getName());

    @Override
    public Order find(int id) {
        Order order = null;

        // IdentityMap
        OrderIdentityMap identityMap = new OrderIdentityMap();
        order = identityMap.get(id);
        if(order != null)
            return order;

        // Database
        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT order_id, table_id, created_date, payed FROM `order` WHERE order_id = ?;")){
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    Date createdDate = resultSet.getDate(3);
                    boolean payed = resultSet.getBoolean(4);

                    Table table = new TableGateway().find(tableId);

                    order = new Order( orderId, table, createdDate.toLocalDate(), payed);
                    identityMap.insert(order);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return order;
    }

    public LinkedList<Order> findAllForDay(LocalDate date) {
        LinkedList<Order> orders = new LinkedList<>();

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT order_id, table_id, payed FROM `order` WHERE created_date = ?;")){
            Date dateTmp = Date.valueOf(date);
            statement.setDate(1, dateTmp);
            try(ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    boolean payed = resultSet.getBoolean(3);

                    Table table = new TableGateway().find(tableId);
                    orders.add( new Order( orderId, table, date, payed));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return orders;
    }

    public LinkedList<Order> findAllOrders() {
        LinkedList<Order> orders = new LinkedList<>();

        try (Statement statement = Gateway.DBConnection.getConnection().createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT `order_id`, `table_id`, `created_date`, `payed` FROM `order`;")){

                while (resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    boolean payed = resultSet.getBoolean(4);

                    Table table = new TableGateway().find(tableId);
                    orders.add( new Order( orderId, table, date.toLocalDate(), payed));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return orders;
    }

    @Override
    public void create(Order obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `order` ( `table_id`, `created_date`, `payed`) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getTable().getTableId());
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));
            preparedStatement.setBoolean( 3, obj.isPayed());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Order
                    obj.setOrderId(resultSet.getInt(1));
                }
                preparedStatement.close();

                OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
                orderIdentityMap.clear();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `order` SET `table_id` = ?, `created_date` = ?, `played` = ? WHERE `order_id` = ?;")){
            preparedStatement.setInt( 1, obj.getTable().getTableId());
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));
            preparedStatement.setBoolean( 3, obj.isPayed());
            preparedStatement.setInt(4, obj.getOrderId());

            preparedStatement.execute();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }
    }

    @Override
    public void delete(Order obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `order` WHERE `order_id` = ?")){
            preparedStatement.setInt(1, obj.getOrderId());
            preparedStatement.execute();

            OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
            orderIdentityMap.clear();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }
    }
}
