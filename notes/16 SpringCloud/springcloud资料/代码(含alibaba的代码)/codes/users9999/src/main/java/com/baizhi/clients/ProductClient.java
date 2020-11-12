package com.baizhi.clients;


import com.baizhi.entity.Product;
import com.baizhi.fallback.ProductClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

//调用商品服务的 openfeign组件
@FeignClient(value = "products",fallback = ProductClientFallBack.class)   //作用:用来标识当前接口是一个 feign 的组件  value:书写调用服务ServiceId 服务名字
public interface ProductClient {


    //展示商品信息
    @GetMapping("/product/showMsg")
    String showMsg();


    //查询所有商品信息
    @GetMapping("/product/findAll")
    Map<String,Object> findAll();


    //根据商品id查询商品信息
    @GetMapping("/product/findOne")  //注意:使用openfeign的GET方式传递参数 参数变量必须通过@requestParam注解进行修饰
                                     //如果不加入该注解会出现:feign.FeignException$MethodNotAllowed: [405]
    Map<String,Object> findOne(@RequestParam("productId") String productId);


    //测试post传递一个参数
    @PostMapping("/product/save")//注意:使用openfeign的post方式传递参数 参数变量必须通过@requestParam注解进行修饰
                                // 如果不加入该注解会出现:feign.FeignException$MethodNotAllowed: [405]
    Map<String,Object> save(@RequestParam("name") String name);



    @PostMapping("/product/update") //注意:使用openfeign的post方式传递对象信息 要求服务提供方和服务调用方都必须使用@RequestBody进行参数声明
    Map<String,Object> update(@RequestBody  Product product);

}
