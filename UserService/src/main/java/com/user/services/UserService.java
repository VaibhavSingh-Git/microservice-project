package com.user.services;

import com.user.entities.User;

import java.util.List;

public interface UserService {
    //create user
    User saveUser(User user);

    //get all users
    List<User> getAllUser();

    //get user with userId
    User getUser(String userId);

    //update user
    User updateUser(User user,String id);

    //delete user
    boolean delete(String userId);

}
