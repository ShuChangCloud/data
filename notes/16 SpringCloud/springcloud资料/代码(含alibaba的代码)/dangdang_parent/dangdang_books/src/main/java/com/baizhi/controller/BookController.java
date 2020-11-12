package com.baizhi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @GetMapping("/test")
    public String test(){
        System.out.println("test ok ~~~");
        return "test error";
    }
}
