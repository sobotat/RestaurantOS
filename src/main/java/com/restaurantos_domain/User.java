package com.restaurantos_domain;

import com.lambdaworks.crypto.SCryptUtil;

import java.time.LocalDate;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private LocalDate bornDate;
    private String email;
    private String password;
    private UserRole userRole;

    public User(int userId, String firstName, String lastName, LocalDate bornDate, String email, String password, UserRole userRole) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bornDate = bornDate;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public static String hashPassword(String password){
        return SCryptUtil.scrypt(password, 16, 16, 16);
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    // Getters
    public int getUserId() {
        return userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public LocalDate getBornDate() {
        return bornDate;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public UserRole getUserRole() {
        return userRole;
    }

    public static class UserRole {
        private int roleId;
        private String name;
        private String description;

        public UserRole(int roleId, String name, String description) {
            this.roleId = roleId;
            this.name = name;
            this.description = description;
        }

        // Getters
        public int getRoleId() {
            return roleId;
        }
        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
    }
}
