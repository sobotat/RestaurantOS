package com.restaurantos_db.identity_maps;

import com.restaurantos_domain.User;

import java.util.HashMap;

public class UserIdentityMap implements IdentityMap<User> {

    static HashMap<Integer, User> map = new HashMap<>();

    @Override
    public void insert(User obj) {
        map.put(obj.getUserId(), obj);
    }

    @Override
    public User get(int id) {
        return map.get(id);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
