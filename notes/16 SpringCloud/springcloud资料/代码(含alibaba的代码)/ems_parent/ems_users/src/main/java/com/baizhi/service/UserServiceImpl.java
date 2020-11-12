package com.baizhi.service;

import com.baizhi.dao.UserDAO;
import com.baizhi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDAO userDAO;


    @Override
    public User login(User user) {
        User userDB = userDAO.findByUserName(user.getUsername());
        if(userDB==null)throw new RuntimeException("提示:用户名输入错误!!!");
        if(!userDB.getPassword().equals(user.getPassword()))throw  new RuntimeException("提示:密码输入错误!!!");
        return userDB;
    }


    @Override
    public void save(User user) {
        User userDB = userDAO.findByUserName(user.getUsername());
        if(userDB!=null)throw new RuntimeException("提示:当前用户名已经存在!!!");
        user.setStatus("已激活");
        user.setRegisterTime(new Date());
        userDAO.save(user);
    }
}
