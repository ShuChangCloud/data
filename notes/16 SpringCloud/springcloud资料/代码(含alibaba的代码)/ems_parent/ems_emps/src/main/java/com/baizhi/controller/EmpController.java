package com.baizhi.controller;

import com.baizhi.clients.FileClients;
import com.baizhi.entity.Emp;
import com.baizhi.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class EmpController {

    @Autowired
    private EmpService empService;

    @Autowired
    private FileClients fileClients;

    /**
     * 保存员工信息
     */
    @PostMapping("/emp/save")
    public Map<String,Object> save(Emp emp, MultipartFile photo){
        Map<String,Object> map  = new HashMap<>();
        try {
            //1.文件上传
            log.info("接收的文件信息:"+photo.getOriginalFilename());
            Map<String, Object> result = fileClients.upload(photo);
            if(result.get("state").equals("false")) throw new RuntimeException("提示:头像保存失败!!!");
            log.info("头像地址: [{}]",result.get("url").toString());
            //2.保存员工信息
            emp.setPath(result.get("url").toString());
            empService.save(emp);
            map.put("msg","提示:员工信息保存成功!");
            map.put("state",true);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg","提示:员工信息保存失败!");
            map.put("state",false);
        }
        return map;
    }

    /**
     * 员工列表接口
     */
    @GetMapping("/emp/findAll")
    public List<Emp> findAll(){
        return empService.findAll();
    }
}
