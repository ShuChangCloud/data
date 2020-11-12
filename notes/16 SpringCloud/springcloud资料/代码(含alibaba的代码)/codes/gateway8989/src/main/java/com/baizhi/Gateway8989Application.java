package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
public class Gateway8989Application {

    public static void main(String[] args) {
         SpringApplication.run(Gateway8989Application.class, args);


    }

}
