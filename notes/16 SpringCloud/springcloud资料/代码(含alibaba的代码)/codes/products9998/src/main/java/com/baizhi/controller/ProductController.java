package com.baizhi.controller;

import com.baizhi.entity.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ProductController {


    @Value("${server.port}")
    private int port;


    @GetMapping("/product/break")
    //@HystrixCommand(fallbackMethod = "testBreakFallBack")
    @HystrixCommand(defaultFallback = "defaultFallBack")
    public String testBreak(Integer id){
        if(id<0){
            throw new RuntimeException("非法参数,id不能小于0");
        }
        return "访问成功,当前查询的id为: "+id;
    }

    //触发熔断的fallback方法
//    public String testBreakFallBack(Integer id){
//        return "当前传入的参数id: "+id +",不是有效参数,触发熔断!";
//    }

    //默认熔断方法
    public String defaultFallBack(){
       return "当前服务不可用,触发熔断!";
    }

    @PostMapping("/product/update")  //requestBody: 将json格式字符串转为对应对象信息
    @HystrixCommand(defaultFallback = "defaultFallBack")
    public Map<String,Object> update(@RequestBody Product product){
        Map<String, Object> map = new HashMap<>();
        log.info("商品服务,接收到商品信息: [{}]",product);
        map.put("status",true);
        map.put("msg","根据商品id查询商品信息成功!当前服务端口:"+port);
        map.put("product",product);
        return map;
    }

    @PostMapping("/product/save")
    //@HystrixCommand(defaultFallback = "defaultFallBack")
    public Map<String,Object> save(@RequestParam("name") String name){
        Map<String, Object> map = new HashMap<>();
        log.info("商品服务,接收到商品name为: [{}]",name);
        map.put("status",true);
        map.put("msg","根据商品id查询商品信息成功!当前服务端口:"+port);
        map.put("name",name);
        return map;
    }

    @GetMapping("/product/findOne")
    public Map<String,Object> findOne(@RequestParam("productId") String productId){
        Map<String, Object> map = new HashMap<>();
        //try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
        log.info("商品服务,接收到商品id为: [{}]",productId);
        map.put("status",true);
        map.put("msg","根据商品id查询商品信息成功!当前服务端口:"+port);
        map.put("productId",productId);
        return map;
    }

    @GetMapping("/product/showMsg")
    public String showMsg(){
        log.info("进入商品服务,展示商品~~~");
        return "进入商品服务,展示商品~~~,当前服务的端口:"+port;
    }

    @GetMapping("/product/findAll")
    public Map<String,Object> findAll(){
        Map<String, Object> map = new HashMap<>();
        log.info("进入商品服务,查询所有商品信息...");
        map.put("status",true);
        map.put("msg","查询所有商品信息成功!,当前服务端口:"+port);
        return map;
    }

}
