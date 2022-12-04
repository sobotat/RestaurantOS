package com.restaurantos_db;

import com.restaurantos_domain.Table;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;

public class TableGateway implements Gateway<Table> {
    private static final Logger logger = LogManager.getLogger(TableGateway.class.getName());

    @Override
    public Table find(int id) {
        Table table = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT table_id, capacity, reserved FROM `restaurantos-db`.`table` WHERE table_id = ?;")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Table
                    int tableId = resultSet.getInt(1);
                    int capacity = resultSet.getInt(2);
                    boolean reserved = resultSet.getBoolean(3);

                    table = new Table(tableId, capacity, reserved);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table DB exception :> " + e.getSQLState());
        }

        return table;
    }

    public LinkedList<Table> findAllTables(){
        LinkedList<Table> tables = new LinkedList<>();

        try (Statement statement = Gateway.DBConnection.getConnection().createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT table_id, capacity, reserved FROM `restaurantos-db`.`table`;")){

                while (resultSet.next()) {
                    // Table
                    int tableId = resultSet.getInt(1);
                    int capacity = resultSet.getInt(2);
                    boolean reserved = resultSet.getBoolean(3);

                    tables.add( new Table(tableId, capacity, reserved));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table DB exception :> " + e.getSQLState());
        }

        return tables;
    }

    @Override
    public boolean create(Table obj) {

        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`table` ( `capacity`, `reserved`) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getCapacity());
            preparedStatement.setBoolean(2, obj.isReserved());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Order
                    obj.setTableId(resultSet.getInt(1));
                }
                preparedStatement.close();
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean update(Table obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`table` SET `capacity` = ?, `reserved` = ? WHERE `table_id` = ?;")){
            preparedStatement.setInt( 1, obj.getCapacity());
            preparedStatement.setBoolean(2, obj.isReserved());
            preparedStatement.setInt(3, obj.getTableId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(Table obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`table` WHERE `table_id` = ?")){
            preparedStatement.setInt(1, obj.getTableId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Table DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
