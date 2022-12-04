package com.restaurantos_db;

import com.restaurantos_db.identity_maps.OrderIdentityMap;
import com.restaurantos_domain.Menu;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
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
                    java.sql.Date date = resultSet.getDate(2);
                    Date createdDate = resultSet.getDate(3);

                    menu = new Menu( menuId, date.toLocalDate(), createdDate.toLocalDate());
                }
                statement.close();
            }

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Order DB exception :> " + e.getSQLState());
        }

        return menu;
    }

    public Menu findForDay(LocalDate date){
        Menu menu = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT menu_id, created_date FROM menu WHERE date = ?")){
            statement.setDate(1, Date.valueOf(date));
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // Menu
                    int menuId = resultSet.getInt(1);
                    Date createdDate = resultSet.getDate(2);

                    menu = new Menu( menuId, date, createdDate.toLocalDate());
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

                    menus.add( new Menu( menuId, date.toLocalDate(), createdDate.toLocalDate()));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
        }

        return menus;
    }

    @Override
    public boolean create(Menu obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `menu` ( `date`, `created_date`) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setDate( 1, Date.valueOf(obj.getDate()));
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Menu
                    obj.setMenuId(resultSet.getInt(1));
                }
                preparedStatement.close();

                OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
                orderIdentityMap.clear();
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Menu obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `menu` SET `date` = ?, `created_date` = ? WHERE `menu_id` = ?;")){
            preparedStatement.setDate( 1, Date.valueOf(obj.getDate()));
            preparedStatement.setDate(2, Date.valueOf(obj.getCreatedDate()));
            preparedStatement.setInt(3, obj.getMenuId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(Menu obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `menu` WHERE `menu_id` = ?")){
            preparedStatement.setInt(1, obj.getMenuId());
            preparedStatement.execute();

            OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
            orderIdentityMap.clear();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Menu DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
