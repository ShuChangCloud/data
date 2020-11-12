package com.baizhi.fallback;

import com.baizhi.clients.ProductClient;
import com.baizhi.entity.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductClientFallBack implements ProductClient {



    @Override
    public String showMsg() {
        return "当前服务已经被降级!!!";
    }


    @Override
    public Map<String, Object> findAll() {
        Map<String,Object> result = new HashMap<>();
        result.put("status","false");
        result.put("msg","当前查询所有不可用,服务已被降级!!!");
        return result;
    }

    @Override
    public Map<String, Object> findOne(String productId) {
        Map<String,Object> result = new HashMap<>();
        result.put("status","false");
        result.put("msg","当前查询信息不可用,服务已被降级!!!");
        return result;
    }

    @Override
    public Map<String, Object> save(String name) {
        Map<String,Object> result = new HashMap<>();
        result.put("status","false");
        result.put("msg","当前保存商品信息不可用,服务已被降级!!!");
        return result;
    }

    @Override
    public Map<String, Object> update(Product product) {
        Map<String,Object> result = new HashMap<>();
        result.put("status","false");
        result.put("msg","当前修改不可用,服务已被降级!!!");
        return result;
    }
}
