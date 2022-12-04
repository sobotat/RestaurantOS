package com.restaurantos_domain;

import com.restaurantos.Main;
import com.restaurantos.controllers.Controller;
import com.restaurantos_db.UserGateway;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppSecurity {
    private static final Logger logger = LogManager.getLogger(AppSecurity.class.getName());
    private static User signInUser = null;

    public static boolean login(String email, String password){
        email = email.trim();
        password = password.trim();

        UserGateway userGateway = new UserGateway();
        User user = userGateway.findByEmailAndPassword(email, password);

        if(user != null){
            signInUser = user;

            Main.switchScene(Main.mainScene);
            Controller.controller.updateUserInfo();

            logger.log(Level.INFO, "User Logged in App");
            return true;
        }

        logger.log(Level.ERROR, "User not found");
        return false;
    }

    public static boolean logout(){
        signInUser = null;
        Main.switchScene(Main.loginScene);

        logger.log(Level.INFO, "User Logged out of App");
        return false;
    }

    public static User getSignInUser() {
        return signInUser;
    }

    public static boolean haveAuthForDelete(){
        return signInUser.getUserRole().getName().equals("Manager");
    }

    public static void changePassword(String email, String password){
        UserGateway userGateway = new UserGateway();
        boolean status = userGateway.updatePassword(email, password);

        if(status)
            System.out.println("Password was changed");
        else
            System.out.println("Password change failed");
    }

    public static class ManagerAuth{
        private static final Logger logger = LogManager.getLogger(ManagerAuth.class.getName());
        private User orgUser;

        public ManagerAuth() {
            logger.log(Level.INFO, "Auth Manager");

            this.orgUser = AppSecurity.getSignInUser();
            AppSecurity.logout();
        }

        public void logoutManager(){
            logger.log(Level.INFO, "Logout Auth Manager");

            AppSecurity.login(orgUser.getEmail(), orgUser.getPassword());
            orgUser = null;
        }
    }
}
