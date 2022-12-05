package com.restaurantos_db;

import com.restaurantos_db.identity_maps.OrderIdentityMap;
import com.restaurantos_domain.Order;
import com.restaurantos_domain.Table;
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
        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `order_id`, `table_id`, `created_date`, `paid`, `createdBy` FROM `restaurantos-db`.`order` WHERE order_id = ?;")){
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    Date createdDate = resultSet.getDate(3);
                    boolean paid = resultSet.getBoolean(4);
                    int createdBy = resultSet.getInt(5);

                    Table table = new TableGateway().find(tableId);

                    order = new Order( orderId, table, createdDate.toLocalDate(), paid, createdBy);
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

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `order_id`, `table_id`, `paid`, `created_by` FROM `restaurantos-db`.`order` WHERE created_date = ?;")){
            Date dateTmp = Date.valueOf(date);
            statement.setDate(1, dateTmp);
            try(ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    boolean paid = resultSet.getBoolean(3);
                    int createdById = resultSet.getInt(4);

                    Table table = new TableGateway().find(tableId);
                    orders.add( new Order( orderId, table, date, paid, createdById));
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
            try(ResultSet resultSet = statement.executeQuery("SELECT `order_id`, `table_id`, `created_date`, `paid`, `created_by` FROM `restaurantos-db`.`order`;")){

                while (resultSet.next()) {
                    // Order
                    int orderId = resultSet.getInt(1);
                    int tableId = resultSet.getInt(2);
                    Date date = resultSet.getDate(3);
                    boolean paid = resultSet.getBoolean(4);
                    int createdById = resultSet.getInt(5);

                    Table table = new TableGateway().find(tableId);
                    orders.add( new Order( orderId, table, date.toLocalDate(), paid, createdById));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return orders;
    }

    @Override
    public boolean create(Order obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`order` ( `table_id`, `created_date`, `paid`, `created_by`) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getTable().getTableId());
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));
            preparedStatement.setBoolean( 3, obj.isPaid());
            preparedStatement.setInt(4, obj.getCreatedById());

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
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Order obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`order` SET `table_id` = ?, `created_date` = ?, `paid` = ? WHERE `order_id` = ?;")){
            preparedStatement.setInt( 1, obj.getTable().getTableId());
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));
            preparedStatement.setBoolean( 3, obj.isPaid());
            preparedStatement.setInt(4, obj.getOrderId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(Order obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`order` WHERE `order_id` = ?")){
            preparedStatement.setInt(1, obj.getOrderId());
            preparedStatement.execute();

            OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
            orderIdentityMap.clear();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
