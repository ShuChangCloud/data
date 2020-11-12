package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@ServletComponentScan(basePackages = "com.baizhi.filters")
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class Configserver7878Application {

	public static void main(String[] args) {
		SpringApplication.run(Configserver7878Application.class, args);
	}

}
