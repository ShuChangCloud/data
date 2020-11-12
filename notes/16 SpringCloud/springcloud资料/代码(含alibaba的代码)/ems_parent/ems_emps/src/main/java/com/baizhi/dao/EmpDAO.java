package com.baizhi.dao;

import com.baizhi.entity.Emp;

import java.util.List;

public interface EmpDAO {

    List<Emp> findAll();

    void save(Emp emp);
}
