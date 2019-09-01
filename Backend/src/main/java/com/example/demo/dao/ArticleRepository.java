package com.example.demo.dao;

import com.example.demo.model.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
}
