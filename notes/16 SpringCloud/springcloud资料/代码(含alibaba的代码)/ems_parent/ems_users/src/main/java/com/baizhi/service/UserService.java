package com.baizhi.service;

import com.baizhi.entity.User;

public interface UserService {

    void save(User user);

    User login(User user);
}
