package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.LocalStroage;
import com.sky.utils.MinioUtil;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Api("通用接口")
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    @Autowired
    LocalStroage ls;
    @Autowired
    MinioUtil minioUtil;
    @ApiOperation("上传文件功能")
    @PostMapping("/upload")
    public Result<Object> upload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        log.info("上传图片：{}", file.getOriginalFilename());
        String fileName = minioUtil.uploadFile(file);
        return Result.success(fileName);
    }


}
