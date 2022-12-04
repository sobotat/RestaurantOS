package com.restaurantos.gateways;

import com.restaurantos.Food;
import com.restaurantos.Menu;
import com.restaurantos.Order;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;

public class MenuGateway implements Gateway<Menu> {
    private static final Logger logger = LogManager.getLogger(MenuGateway.class.getName());

    @Override
    public Menu find(int id) {
        Menu menu = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT menu_id, date, created_date FROM menu WHERE menu_id = ?")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Menu
                    int menuId = resultSet.getInt(1);
                    Date date = resultSet.getDate(2);
                    Date createdDate = resultSet.getDate(3);

                    menu = new Menu( menuId, date, createdDate);
                }
                statement.close();
            }

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return menu;
    }

    public Menu findForDay(Date date){
        Menu menu = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT menu_id, created_date FROM menu WHERE date = ?")){
            statement.setDate(1, (java.sql.Date) date);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Menu
                    int menuId = resultSet.getInt(1);
                    Date createdDate = resultSet.getDate(2);

                    menu = new Menu( menuId, date, createdDate);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
        }

        return menu;
    }

    public LinkedList<Menu> findAllMenus(){
        LinkedList<Menu> menus = new LinkedList<>();

        try (Statement statement = Gateway.DBConnection.getConnection().createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT menu_id, date, created_date FROM menu")){

                while (resultSet.next()) {
                    // Menu
                    int menuId = resultSet.getInt(1);
                    Date date = resultSet.getDate(2);
                    Date createdDate = resultSet.getDate(3);

                    menus.add( new Menu( menuId, date, createdDate));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
        }

        return menus;
    }

    @Override
    public void create(Menu obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void update(Menu obj) {
        logger.log(Level.WARN, "Not Implemented");
    }

    @Override
    public void delete(Menu obj) {
        logger.log(Level.WARN, "Not Implemented");
    }
}
