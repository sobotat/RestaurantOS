package com.restaurantos_db;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public interface Gateway<T> {

    T find(int id);
    boolean create(T obj);
    boolean update(T obj);
    boolean delete(T obj);

    class DBConnection{
        private static final Logger logger = LogManager.getLogger(DBConnection.class.getName());
        private static final String connectionStr = "jdbc:mysql://localhost:3306/restaurantos-db";
        private static Connection connection = null;
        private static Timer timer;

        public static Connection getConnection() throws SQLException {
            resetTimer();

            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(connectionStr, "restaurantos", "restaurantos1234");
                logger.log(Level.INFO, "DB Connection Opened");
                return connection;
            }

            return connection;
        }

        public static void close(){
            try {
                if(connection != null) {
                    connection.close();
                    connection = null;

                    logger.log(Level.INFO, "DB Connection Closed");
                }

                timer = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static void resetTimer(){
            if(timer != null)
                timer.cancel();
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    close();
                }
            }, 30000);
        }
    }
}
