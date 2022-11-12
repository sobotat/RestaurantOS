package com.restaurant_os.restaurantos;

import java.util.Date;
import java.util.LinkedList;

public class User {
    public static LinkedList<User> users;

    int userId;
    String firstName;
    String lastName;
    Date bornDate;
    String email;
    String password;
    UserRole userRole;

    public User(int userId, String firstName, String lastName, Date bornDate, String email, String password, UserRole userRole) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bornDate = bornDate;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static class UserRole {
        int roleId;
        String name;
        String description;

        public UserRole(int roleId, String name, String description) {
            this.roleId = roleId;
            this.name = name;
            this.description = description;
        }
    }
}
