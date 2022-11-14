package com.restaurantos;

import com.restaurantos.controllers.Controller;
import com.restaurantos.gateways.UserGateway;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppSecurity {
    private static final Logger logger = LogManager.getLogger(AppSecurity.class.getName());
    private static User signInUser = null;

    public static boolean login(String email, String password){

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

            AppSecurity.login(orgUser.email, orgUser.password);
            orgUser = null;
        }
    }
}
