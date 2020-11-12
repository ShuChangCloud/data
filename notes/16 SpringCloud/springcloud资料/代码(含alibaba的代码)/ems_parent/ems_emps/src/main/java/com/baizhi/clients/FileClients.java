package com.baizhi.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient("files")
public interface FileClients {


    //调用文件上传服务  注意:使用openfeign 传递参数含有文件类型时必须指定consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    @PostMapping(value = "/file/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String,Object> upload(@RequestPart("photo") MultipartFile photo);
}
