
package com.example.demo.service;

import com.example.demo.config.CloudStorageConfig;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class CloudStorageService {

    protected CloudStorageConfig config;


    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String path = sdf.format(new Date()) + "/" + uuid;

        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }

        return path + suffix;
    }

    private UploadManager uploadManager;
    private String token;
    private Auth auth;

    public CloudStorageService(@NotNull CloudStorageConfig config){
        this.config = config;
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        auth = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey());
        token = auth.uploadToken(config.getQiniuBucketName());
    }

    public String upload(byte[] data, String path) {
        try {
            token = auth.uploadToken(config.getQiniuBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("云服务错误");
        }

        return config.getQiniuDomain() + "/" + path;
    }

    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RuntimeException("云服务错误");
        }
    }

    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQiniuPrefix(), suffix));
    }

    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
    }
}
