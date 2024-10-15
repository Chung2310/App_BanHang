package com.example.quanlybanhang.model.EventBus;

import com.example.quanlybanhang.model.User;

public class SuaXoaEventUser {
    User user;

    public SuaXoaEventUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
