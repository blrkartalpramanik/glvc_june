package com.hrs.user.service.service;

import com.hrs.user.service.entity.User;

import java.util.List;

public interface UserServices {
    User saveUser(User user);

    List<User> getALlUser();

    User getUser(String userId);

//    User
}
