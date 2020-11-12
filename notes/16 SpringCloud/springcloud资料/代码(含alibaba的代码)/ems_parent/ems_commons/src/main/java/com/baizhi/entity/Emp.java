package com.baizhi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Emp {
    private Integer id;
    private String name;
    private String path;
    private String salary;
    private Integer age;
}
