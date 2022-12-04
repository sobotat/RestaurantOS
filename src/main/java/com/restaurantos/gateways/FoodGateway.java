package com.restaurantos.gateways;

import com.restaurantos.Food;
import com.restaurantos.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
            try(ResultSet resultSet = statement.executeQuery("SELECT food.food_id, food.name, food.description, food.allergens, food.cost, type.type_id, type.name"
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
    public void create(Food obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void update(Food obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void delete(Food obj) {
        logger.log(Level.WARN, "Not Implemented");
    }
}
