package com.restaurantos;

import java.util.Date;
import java.util.LinkedList;

public class User {

    public int userId;
    public String firstName;
    public String lastName;
    Date bornDate;
    public String email;
    public String password;
    public UserRole userRole;

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
        public String name;
        String description;

        public UserRole(int roleId, String name, String description) {
            this.roleId = roleId;
            this.name = name;
            this.description = description;
        }
    }
}
