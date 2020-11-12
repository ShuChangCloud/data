package com.baizhi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class FileController {


    @Value("${upload.dir}")
    private String realPath;

    @Value("${server.port}")
    private int port;

    @PostMapping("/file/upload")
    public Map<String,Object> upload(@RequestPart("photo") MultipartFile photo) throws IOException {
        Map<String, Object> map = new HashMap<>();
        log.info("接收到的文件名称: "+photo.getOriginalFilename());

        try{
            //1.修改文件名
            String ext = FilenameUtils.getExtension(photo.getOriginalFilename());
            String newFileName = UUID.randomUUID().toString()+"."+ext;
            //2.文件上传
            photo.transferTo(new File(realPath,newFileName));

            //3.返回响应存在头像地址
            String url = "http://localhost:"+port+"/"+newFileName;
            map.put("url",url);
            map.put("state","true");
        }catch (Exception e){
            e.printStackTrace();
            map.put("state","false");
        }
        return map;
    }
}
