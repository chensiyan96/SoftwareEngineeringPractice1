package com.example.demo.config;

import com.example.demo.mq.RabbitMqConstants;
import com.example.demo.mq.RabbitMqUtils;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class InitialConfig {

    @Resource
    private RabbitMqUtils rabbitMqUtils;

    /**
     * 项目启动时重新导入索引
     */
    @PostConstruct
    public void initEsIndex(){
        rabbitMqUtils.send(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE,"blog-search init index");
    }
}
