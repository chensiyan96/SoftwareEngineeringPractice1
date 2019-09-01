package com.example.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.Serializable;

@Data
@Configuration
public class CloudStorageConfig implements Serializable {

    @Value("${oss.qiniu.domain}")
    private String qiniuDomain;

    @Value("${oss.qiniu.prefix}")
    private String qiniuPrefix;

    @Value("${oss.qiniu.accessKey}")
    private String qiniuAccessKey;

    @Value("${oss.qiniu.secretKey}")
    private String qiniuSecretKey;

    @Value("${oss.qiniu.bucketName}")
    private String qiniuBucketName;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("1GB");
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("1GB");
        return factory.createMultipartConfig();
    }

}
