package com.baizhi.controller;

import com.baizhi.clients.ProductClient;
import com.baizhi.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class TestFeignController {


    @Autowired
    private ProductClient productClient;



    //用来测试调用post方式服务进行对象的参数传递
    @GetMapping("/feign/test3")
    public Map<String,Object> test3(Product product){
        log.info("接收到的商品信息:[{}]",product);
        Map<String, Object> msg = productClient.update(product);
        log.info("返回的信息:[{}]",msg);
        return msg;
    }

    //参数post方式调用传递参数
    @GetMapping("/feign/test2")
    public Map<String,Object> test2(String name){
        log.info("用来测试Openfiegn的POST方式参数传递");
        Map<String, Object> msg = productClient.save(name);
        log.info("调用返回信息:[{}]",msg);
        return msg;
    }
    //测试get方式调用传递参数
    @GetMapping("/feign/test1")
    public Map<String,Object> test1(String id){
        log.info("用来测试Openfiegn的GET方式参数传递");
        Map<String, Object> msg = productClient.findOne(id);
        log.info("调用返回信息:[{}]",msg);
        return msg;
    }


    @GetMapping("/feign/test")
    public Map<String, Object> test() {
        log.info("进入测试feign调用的方法....");
        //String msg = productClient.showMsg();
        Map<String, Object> map = productClient.findAll();
        //log.info("调用商品服务返回的信息: [{}]", msg);
        log.info("调用商品服务返回的信息: [{}]", map);
        return map;
    }
}
