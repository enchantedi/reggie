package com.itheima.reggie.controller;

import com.itheima.reggie.common.OssTemplate;
import com.itheima.reggie.common.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
public class FileController {

    @Autowired
    private OssTemplate ossTemplate;

    //文件上传
    @PostMapping("/common/upload")
    public ResultInfo upload(MultipartFile file) throws IOException {
        if (file != null && file.getSize() > 0) {
            String uploadPath = ossTemplate.upload(file.getOriginalFilename(), file.getInputStream());
            log.info("文件上传之后的访问路径是:{}", uploadPath);
            return ResultInfo.success(uploadPath);
        }
        return ResultInfo.error("文件上传失败!");
    }
}