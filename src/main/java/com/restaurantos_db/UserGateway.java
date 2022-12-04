package com.restaurantos_db;

import com.lambdaworks.crypto.SCryptUtil;
import com.restaurantos_domain.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.LinkedList;

public class UserGateway implements Gateway<User> {
    private static final Logger logger = LogManager.getLogger(UserGateway.class.getName());

    @Override
    public User find(int id) {
        User user = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT user_id, first_name, last_name, born_date, email, role.role_id, role.name, role.description FROM `restaurantos-db`.`user` JOIN `restaurantos-db`.`role` ON role.role_id = user.role_id WHERE user_id = ?")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // User
                    int userId = resultSet.getInt(1);
                    String first_name = resultSet.getString(2);
                    String last_name = resultSet.getString(3);
                    Date born_date = resultSet.getDate(4);
                    String email = resultSet.getString(5);
                    String password = null;

                    // Role
                    int roleId = resultSet.getInt(6);
                    String roleName = resultSet.getString(7);
                    String roleDescription = resultSet.getString(8);

                    user = new User(userId, first_name, last_name, born_date.toLocalDate(), email, password, new User.UserRole(roleId, roleName, roleDescription));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }

        return user;
    }

    public User findByEmailAndPassword(String email, String password){
        User user = null;

        try (PreparedStatement statement = Gateway.DBConnection.getConnection().prepareStatement("SELECT user_id, first_name, last_name, born_date, password, role.role_id, role.name, role.description FROM `restaurantos-db`.`user` JOIN `restaurantos-db`.`role` ON role.role_id = user.role_id WHERE user.email = ?")){
            statement.setString( 1, email);
            try(ResultSet resultSet = statement.executeQuery()){

                if(resultSet.next()) {
                    // User
                    int userId = resultSet.getInt(1);
                    String first_name = resultSet.getString(2);
                    String last_name = resultSet.getString(3);
                    Date born_date = resultSet.getDate(4);
                    String hashedPassword = resultSet.getString(5);

                    // Role
                    int roleId = resultSet.getInt(6);
                    String roleName = resultSet.getString(7);
                    String roleDescription = resultSet.getString(8);

                    if(SCryptUtil.check(password, hashedPassword))
                        user = new User(userId, first_name, last_name, born_date.toLocalDate(), email, hashedPassword, new User.UserRole(roleId, roleName, roleDescription));
                    else
                        logger.log(Level.INFO, "Wrong Password");
                }
                statement.close();
            }
        } catch (SQLException e) {
            if(e.getSQLState().equals("08S01"))
                logger.log(Level.ERROR, "DB is offline");
            else if(e.getSQLState().equals("28000"))
                logger.log(Level.ERROR, "DB login failed");
            else
                logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }

        return user;
    }

    public LinkedList<User> findAllUsers(){
        LinkedList<User> users = new LinkedList<>();

        try (Statement statement = Gateway.DBConnection.getConnection().createStatement()){
            try(ResultSet resultSet = statement.executeQuery("SELECT user_id, first_name, last_name, born_date, email, password, role.role_id, role.name, role.description FROM `restaurantos-db`.`user` JOIN `restaurantos-db`.`role` ON role.role_id = user.role_id")){

                while (resultSet.next()) {
                    // User
                    int userId = resultSet.getInt(1);
                    String first_name = resultSet.getString(2);
                    String last_name = resultSet.getString(3);
                    Date born_date = resultSet.getDate(4);
                    String email = resultSet.getString(5);
                    String password = resultSet.getString(6);

                    // Role
                    int roleId = resultSet.getInt(7);
                    String roleName = resultSet.getString(8);
                    String roleDescription = resultSet.getString(9);

                    users.add(new User(userId, first_name, last_name, born_date.toLocalDate(), email, password, new User.UserRole(roleId, roleName, roleDescription)));
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }

        return users;
    }

    @Override
    public boolean create(User obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("INSERT INTO `restaurantos-db`.`user` ( `first_name`, `last_name`, `born_date`, `email`, `password`, `role_id`) VALUES (?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString( 1, obj.getFirstName());
            preparedStatement.setString( 2, obj.getLastName());
            preparedStatement.setDate(3, Date.valueOf(obj.getBornDate()));
            preparedStatement.setString( 4, obj.getEmail());
            preparedStatement.setString( 5, obj.getPassword());
            preparedStatement.setInt( 6, obj.getUserRole().getRoleId());

            preparedStatement.execute();
            try(ResultSet resultSet = preparedStatement.getGeneratedKeys()){

                if (resultSet.next()) {
                    // Order
                    obj.setUserId(resultSet.getInt(1));
                }
                preparedStatement.close();
            }
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`user` SET `first_name` = ?, `last_name` = ?, `born_date` = ?, `email` = ?, `password` = ?, `role_id` = ? WHERE `user_id` = ?;")){
            preparedStatement.setString( 1, obj.getFirstName());
            preparedStatement.setString( 2, obj.getLastName());
            preparedStatement.setDate(3, Date.valueOf(obj.getBornDate()));
            preparedStatement.setString( 4, obj.getEmail());
            preparedStatement.setString( 5, obj.getPassword());
            preparedStatement.setInt( 6, obj.getUserRole().getRoleId());
            preparedStatement.setInt(7, obj.getUserId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }
        return true;
    }

    public boolean updatePassword(String email, String password) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("UPDATE `restaurantos-db`.`user` SET `password` = ? WHERE `email` = ?;")){

            String hashPassword = User.hashPassword(password);
            preparedStatement.setString( 1, hashPassword);
            preparedStatement.setString( 2, email);

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }
        return false;
    }

    @Override
    public boolean delete(User obj) {
        try (PreparedStatement preparedStatement = Gateway.DBConnection.getConnection().prepareStatement("DELETE FROM `restaurantos-db`.`user` WHERE `user_id` = ?")){
            preparedStatement.setInt(1, obj.getUserId());

            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            logger.log(Level.ERROR, "User DB exception :> " + e.getSQLState());
        }
        return false;
    }
}
