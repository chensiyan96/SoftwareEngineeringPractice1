package com.example.demo.controller;

import com.example.demo.dao.ArticleRepository;
import com.example.demo.model.Article;
import com.example.demo.mq.RabbitMqConstants;
import com.example.demo.service.ArticleService;
import com.example.demo.util.Result;
import com.google.common.collect.Lists;
import com.rabbitmq.client.Channel;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
public class ArticleEsController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;


    /**
     * 搜索标题，描述，内容
     */
    @GetMapping("articles/search")
    public Result search(@RequestParam("keywords") String keywords) {
        // 对所有索引进行搜索
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keywords);

        Iterable<Article> listIt = articleRepository.search(queryBuilder);

        //Iterable转list
        List<Article> articleList = Lists.newArrayList(listIt);

        return Result.ok().put("articleList", articleList);
    }

    @RabbitListener(queuesToDeclare = @Queue(RabbitMqConstants.REFRESH_ES_INDEX_QUEUE))
    public void refresh(@NotNull Message message, @NotNull Channel channel) {
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            articleRepository.deleteAll();
            List<Article> list = articleService.allPublic();
            articleRepository.saveAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
