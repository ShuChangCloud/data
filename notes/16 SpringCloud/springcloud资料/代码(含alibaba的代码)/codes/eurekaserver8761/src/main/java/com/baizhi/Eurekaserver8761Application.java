package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eurekaserver8761Application {

    public static void main(String[] args) {
        SpringApplication.run(Eurekaserver8761Application.class, args);
    }

}
