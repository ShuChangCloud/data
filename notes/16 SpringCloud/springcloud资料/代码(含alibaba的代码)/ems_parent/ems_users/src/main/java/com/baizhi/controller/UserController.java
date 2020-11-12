package com.baizhi.controller;

import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private UserService userService;


    /**
     * 用户登录接口
     */
    @PostMapping("/user/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            User userDB = userService.login(user);
            map.put("msg","提示:登录成功!");
            map.put("state",true);
            map.put("user",userDB);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg",e.getMessage());
            map.put("state",false);
        }
        return map;
    }

    /**
     * 用户注册接口
     */
    @PostMapping("/user/register")
    public Map<String, Object> save(@RequestBody User user, String code, String codeKey) {
        Map<String, Object> map = new HashMap<>();
        try {
            //1.根据codekey获取redis中验证码
            if (!stringRedisTemplate.hasKey(codeKey)) throw new RuntimeException("提示:验证码过期~~~");
            String oldCode = stringRedisTemplate.opsForValue().get(codeKey);
            if (oldCode.equalsIgnoreCase(code)) {  //2.比较验证码
                //注册用户
                userService.save(user);
                map.put("msg", "提示:用户注册成功!");
                map.put("state", true);
            } else {
                throw new RuntimeException("提示:输入的验证码不正确!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
            map.put("state", false);
        }
        return map;
    }

    /**
     * 生成验证码接口
     */
    @GetMapping("/user/getImage")
    public Map<String, Object> getImage() throws IOException {
        Map<String, Object> map = new HashMap<>();
        //1.生成验证码
        String code = VerifyCodeUtils.generateVerifyCode(4);
        log.info("验证码:" + code);
        //2.存储验证码 redis
        String codeKey = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(codeKey, code, 60, TimeUnit.SECONDS);
        //3.base64转换验证码
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 60, byteArrayOutputStream, code);
        String data = "data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
        //4.响应数据
        map.put("src", data);
        map.put("codeKey", codeKey);
        return map;
    }
}
