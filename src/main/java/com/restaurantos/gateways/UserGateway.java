package com.restaurantos.gateways;

import com.restaurantos.User;

import java.util.LinkedList;

public class UserGateway implements Gateway<User> {
    private static LinkedList<User> users;

    @Override
    public User find(int id) {
        for(User user: UserGateway.users){
            if(user.userId == id){
                return user;
            }
        }

        return null;
    }

    public User findByEmailAndPassword(String email, String password){
        for(User user: UserGateway.users){
            if(user.email.equals(email) && user.password.equals(password)){
                return user;
            }
        }

        return null;
    }

    public LinkedList<User> findAllUsers(){
        return users;
    }

    @Override
    public void create(User obj) {
        users.add(obj);
    }

    @Override
    public void update(User obj) {
        for(User user: UserGateway.users){
            if(user.userId == obj.userId){
                user = obj;
                return;
            }
        }
    }

    @Override
    public void delete(User obj) {
        users.remove(obj);
    }

    public static void fakeSetOfDatabase(LinkedList<User> users){
        UserGateway.users = users;
    }
}
