package com.baizhi.service;

import com.baizhi.entity.Emp;

import java.util.List;

public interface EmpService {


    List<Emp> findAll();

    void save(Emp emp);
}
