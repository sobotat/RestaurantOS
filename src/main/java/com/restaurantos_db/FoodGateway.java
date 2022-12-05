package com.restaurantos_db;

import com.restaurantos_db.identity_maps.OrderIdentityMap;
import com.restaurantos_domain.Food;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;

public class FoodGateway implements Gateway<Food> {
    private static final Logger logger = LogManager.getLogger(FoodGateway.class.getName());

    @Override
    public Food find(int id) {
        Food food = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT food.food_id, food.name, food.description, food.allergens, food.cost, type.type_id, type.name "
                                                                                      + "FROM food JOIN type ON type.type_id = food.type_id WHERE food_id = ?;")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Food
                    int foodId = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    String allergens = resultSet.getString(4);
                    double cost = resultSet.getDouble(5);

                    // Type
                    int typeId = resultSet.getInt(6);
                    String typeName = resultSet.getString(7);

                    food = new Food(foodId, new Food.FoodType(typeId, typeName), name, description, allergens, cost);
                    statement.close();
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Food DB exception :> " + e.getSQLState());
        }

        return food;
    }

    public LinkedList<Food> findAllFoods(){
        LinkedList<Food> foods = new LinkedList<>();

        try (Statement statement = Gateway.DBConnection.getConnection().createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT food.food_id, food.name, food.description, food.allergens, food.cost, type.type_id, type.name "
                                                               + "FROM food JOIN type ON type.type_id = food.type_id;")){

                while (resultSet.next()) {
                    // Food
                    int foodId = resultSet.getInt(1);
                    String name = resultSet.getString(2);
                    String description = resultSet.getString(3);
                    String allergens = resultSet.getString(4);
                    double cost = resultSet.getDouble(5);

                    // Type
                    int typeId = resultSet.getInt(6);
                    String typeName = resultSet.getString(7);

                    foods.add( new Food(foodId, new Food.FoodType(typeId, typeName), name, description, allergens, cost));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Food DB exception :> " + e.getSQLState());
        }

        return foods;
    }

    @Override
    public boolean create(Food obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`food` ( `type_id`, `name`, `description`, `allergens`, `cost`) VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getFoodType().getTypeId());
            preparedStatement.setString( 2, obj.getName());
            preparedStatement.setString( 3, obj.getDescription());
            preparedStatement.setString( 4, obj.getAllergens());
            preparedStatement.setDouble( 5, obj.getCost());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Food
                    obj.setFoodId(resultSet.getInt(1));
                }
                preparedStatement.close();

                OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
                orderIdentityMap.clear();
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Food DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Food obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`food` SET `type_id` = ?, `name` = ?, `description` = ?, `allergens` = ?, `cost` = ? WHERE `food_id` = ?;")){
            preparedStatement.setInt(1, obj.getFoodType().getTypeId());
            preparedStatement.setString( 2, obj.getName());
            preparedStatement.setString( 3, obj.getDescription());
            preparedStatement.setString(4, obj.getAllergens());
            preparedStatement.setDouble(5, obj.getCost());
            preparedStatement.setInt(6, obj.getFoodId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Food DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(Food obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`food` WHERE `food_id` = ?")){
            preparedStatement.setInt(1, obj.getFoodId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Food DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
