package com.baizhi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ClientController {

    @Value("${server.port}")
    private int port;

    @Value("${name}")
    private String name;

    @GetMapping("/client/init")
    public String init(){
        return "当前姓名为: "+name;
    }
}
