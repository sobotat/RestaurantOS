package com.restaurantos_db;

import com.restaurantos_domain.Menu;
import com.restaurantos_domain.MenuItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class MenuItemGateway implements Gateway<MenuItem> {
    private static final Logger logger = LogManager.getLogger(MenuItemGateway.class.getName());

    @Override
    public MenuItem find(int id) {
        MenuItem menuItem = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `menu_item_id`, `menu_id`, `food_id`, `count`, `cost` FROM `restaurantos-db`.`menu_item` WHERE `menu_item_id` = ?;")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Menu Item
                    int menuItemId = resultSet.getInt(1);
                    int menu_id = resultSet.getInt(2);
                    int foodId = resultSet.getInt(3);
                    int count = resultSet.getInt(4);
                    double cost = resultSet.getDouble(5);

                    Menu menu = new MenuGateway().find(menu_id);

                    menuItem = new MenuItem( menuItemId, menu, foodId, count, cost);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }

        return menuItem;
    }

    public LinkedList<MenuItem> findAllForMenu(Menu menu){
        LinkedList<MenuItem> menuItems = new LinkedList<>();

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT `menu_item_id`, `food_id`, `count`, `cost` FROM `restaurantos-db`.`menu_item` WHERE `menu_id` = ?;")){
            statement.setInt(1, menu.getMenuId());
            try(ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()) {
                    // Menu Item
                    int menuItemId = resultSet.getInt(1);
                    int foodId = resultSet.getInt(2);
                    int count = resultSet.getInt(3);
                    double cost = resultSet.getDouble(4);

                    menuItems.add( new MenuItem( menuItemId, menu, foodId, count, cost));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }

        return menuItems;
    }

    @Override
    public boolean create(MenuItem obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`menu_item` ( `menu_id`, `food_id`, `count`, `cost`) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setInt( 1, obj.getMenu().getMenuId());
            preparedStatement.setInt(2, obj.getFood().getFoodId());
            preparedStatement.setInt( 3, obj.getCount());
            preparedStatement.setDouble( 4, obj.getCost());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Order
                    obj.setMenuItemId(resultSet.getInt(1));
                }
                preparedStatement.close();
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean update(MenuItem obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`menu_item` SET `menu_id` = ?, `food_id` = ?, `count` = ?, `cost` = ? WHERE `menu_item_id` = ?;")){
            preparedStatement.setInt( 1, obj.getMenu().getMenuId());
            preparedStatement.setInt(2, obj.getFood().getFoodId());
            preparedStatement.setInt( 3, obj.getCount());
            preparedStatement.setDouble( 4, obj.getCost());
            preparedStatement.setInt(5, obj.getMenuItemId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(MenuItem obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`menu_item` WHERE `menu_item_id` = ?")){
            preparedStatement.setInt(1, obj.getMenuItemId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
