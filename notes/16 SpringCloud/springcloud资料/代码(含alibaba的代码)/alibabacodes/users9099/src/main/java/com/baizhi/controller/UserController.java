package com.baizhi.controller;

import com.baizhi.clients.ProductClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {


    @Autowired
    private DiscoveryClient discoveryClient;


    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductClient productClient;

    @GetMapping("/user/getProductInfo")
    public Map<String,Object> getProductInfo(String productId){

        //第一种调用方式: 1.通过restTemplate直接调用
        //RestTemplate restTemplate = new RestTemplate();
        //String forObject = restTemplate.getForObject("http://localhost:9098/product/find?id=" + productId, String.class);

        //第二种调用方式: restTemplate + ribbon 负载均衡客户端 DiscoveryClient   LoadBalanceClient
//        List<ServiceInstance> productsServiceInstances = discoveryClient.getInstances("products");
//        for (ServiceInstance productsServiceInstance : productsServiceInstances) {
//            log.info("服务地址: [{}]",productsServiceInstance.getUri());
//        }

        //ServiceInstance productServiceInstance = loadBalancerClient.choose("products");
        //log.info("当前处理服务负载均衡客户端主机为:[{}]",productServiceInstance.getUri());
        //String forObject1 = restTemplate.getForObject(productServiceInstance.getUri() + "/product/find?id=" + productId, String.class);

        //第三种注解方式@LoadBalance
        //String forObject = restTemplate.getForObject("http://products/product/find?id=" + productId, String.class);

        Map<String, Object> map = productClient.find(productId);
        log.info("返回的信息:[{}]"+map);
        return map;
    }



}
