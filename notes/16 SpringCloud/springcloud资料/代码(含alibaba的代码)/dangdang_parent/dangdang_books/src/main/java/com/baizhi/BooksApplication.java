package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.swing.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class BooksApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class,args);
    }
}
