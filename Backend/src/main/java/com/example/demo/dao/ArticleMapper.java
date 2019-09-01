package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.model.Article;
import com.example.demo.model.ArticleVO;
import com.example.demo.model.TimelinePost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {


    /**
     * 查询列表
     */
    List<ArticleVO> listArticleVo(Page<ArticleVO> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件查询分页
     */
    List<ArticleVO> queryPageCondition(Page<ArticleVO> page, @Param("params") Map<String, Object> params);
    List<ArticleVO> queryPageCondition2(Page<ArticleVO> page, @Param("params") Map<String, Object> params, int userId);

    /**
     * 更新阅读记录
     */
    void updateReadNum(Integer id);

    /**
     * 获取简单的对象
     */
    ArticleVO getSimpleArticleVo(Integer articleId);

    /**
     * 判断类别下是否有文章
     */
    int checkByCategory(Integer categoryId);

    List<ArticleVO> hotArticles();
    List<ArticleVO> hotArticlesUser(int userId);
    List<ArticleVO> newArticles();
    List<ArticleVO> newArticlesUser(int userId);

    List<TimelinePost> getTimelinePost(int userId);

    List<Article> allPublic();

}
