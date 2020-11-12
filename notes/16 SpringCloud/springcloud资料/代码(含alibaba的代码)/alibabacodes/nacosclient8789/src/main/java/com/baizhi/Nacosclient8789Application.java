package com.baizhi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  //开启服务注册与发现
public class Nacosclient8789Application {

	public static void main(String[] args) {
		SpringApplication.run(Nacosclient8789Application.class, args);
	}

}
