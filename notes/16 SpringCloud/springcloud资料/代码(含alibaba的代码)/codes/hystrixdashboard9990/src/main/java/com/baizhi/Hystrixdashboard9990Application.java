package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class Hystrixdashboard9990Application {

	public static void main(String[] args) {
		SpringApplication.run(Hystrixdashboard9990Application.class, args);
	}

}
