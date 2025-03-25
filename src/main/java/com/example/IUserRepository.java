package com.example;

import java.util.ArrayList;

public interface IUserRepository {
    User getUser(String username);
    ArrayList<User> getUsers();
    void save();
    void load();
}
