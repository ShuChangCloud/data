package com.baizhi.dao;

import com.baizhi.entity.User;

public interface UserDAO {


    void save(User user);

    User findByUserName(String name);
}
