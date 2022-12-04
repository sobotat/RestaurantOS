package com.restaurantos.gateways;

import com.restaurantos.Food;
import com.restaurantos.Menu;
import com.restaurantos.MenuItem;
import com.restaurantos.OrderItem;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

public class MenuItemGateway implements Gateway<MenuItem> {
    private static final Logger logger = LogManager.getLogger(MenuItemGateway.class.getName());

    @Override
    public MenuItem find(int id) {
        MenuItem menuItem = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT menu_item_id, menu_id, food_id, count FROM menu_item WHERE menu_item_id = ?;")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Menu Item
                    int menuItemId = resultSet.getInt(1);
                    int menu_id = resultSet.getInt(2);
                    int foodId = resultSet.getInt(3);
                    int count = resultSet.getInt(4);

                    Menu menu = new MenuGateway().find(menu_id);

                    menuItem = new MenuItem( menuItemId, menu, foodId, count);
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

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT menu_item_id, food_id, count FROM menu_item WHERE menu_id = ?;")){
            statement.setInt(1, menu.getMenuId());
            try(ResultSet resultSet = statement.executeQuery()){

                while (resultSet.next()) {
                    // Menu Item
                    int menuItemId = resultSet.getInt(1);
                    int foodId = resultSet.getInt(2);
                    int count = resultSet.getInt(3);

                    menuItems.add( new MenuItem( menuItemId, menu, foodId, count));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "MenuItem DB exception :> " + e.getSQLState());
        }

        return menuItems;
    }

    @Override
    public void create(MenuItem obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void update(MenuItem obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void delete(MenuItem obj) {
        logger.log(Level.WARN, "Not Implemented");
    }
}
