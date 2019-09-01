package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.ArticleMapper;
import com.example.demo.dao.CategoryMapper;
import com.example.demo.dao.LoveMapper;
import com.example.demo.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private LoveMapper loveMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(@NotNull ArticleDTO article) {
        baseMapper.insert(article);
        tagService.saveTagAndNew(article.getTagList(), article.getArticleId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(@NotNull ArticleDTO article) {
        // 删除多对多所属标签
        tagService.deleteTagLink(article.getArticleId());
        // 更新所属标签
        tagService.saveTagAndNew(article.getTagList(), article.getArticleId());
        // 更新博文
        baseMapper.updateById(article);
    }

    public PageUtils queryPageCondition(Map<String, Object> params) {
        Page<ArticleVO> page = new Query<ArticleVO>(params).getPage();
        List<ArticleVO> articleList = baseMapper.queryPageCondition(page, params);
        for (ArticleVO a : articleList) {
            a.setAuthor(userService.getById(a.getUserId()).getUsername());
            a.setLikeNum(loveMapper.getLikeCountByArticleId(a.getArticleId()));
        }
        // 封装ArticleVo
        page.setRecords(articleList);
        return new PageUtils(page);
    }

    public PageUtils queryPageCondition(Map<String, Object> params, int userId) {
        Page<ArticleVO> page = new Query<ArticleVO>(params).getPage();
        List<ArticleVO> articleList = baseMapper.queryPageCondition2(page, params, userId);
        for (ArticleVO a : articleList) {
            a.setAuthor(userService.getById(a.getUserId()).getUsername());
            a.setLikeNum(loveMapper.getLikeCountByArticleId(a.getArticleId()));
        }
        // 封装ArticleVo
        page.setRecords(articleList);
        return new PageUtils(page);
    }

    public ArticleVO getArticleVo(Integer articleId) {
        Article article = baseMapper.selectById(articleId);
        if (article == null) {
            return null;
        }
        article.setLikeNum(loveMapper.getLikeCountByArticleId(articleId));
        ArticleVO articleVo = new ArticleVO();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setTagList(tagService.listByLinkId(articleId));
        articleVo.setAuthor(userService.getById(articleVo.getUserId()).getUsername());
        return articleVo;
    }

    public void updateReadNum(int articleId) {
        baseMapper.updateReadNum(articleId);
    }

    public boolean likeArticle(int userId, int articleId) {
        if (loveMapper.isRecordExist(userId, articleId)) {
            return false;
        }
        loveMapper.insert(new Love(userId, articleId));
        return true;
    }

    public List<ArticleVO> hotArticles() {
        List<ArticleVO> articleList = baseMapper.hotArticles();
        for (ArticleVO a : articleList) {
            a.setAuthor(userService.getById(a.getUserId()).getUsername());
            a.setLikeNum(loveMapper.getLikeCountByArticleId(a.getArticleId()));
        }
        return articleList;
    }

    public List<ArticleVO> hotArticles(int userId) {
        List<ArticleVO> articleList = baseMapper.hotArticlesUser(userId);
        for (ArticleVO a : articleList) {
            a.setAuthor(userService.getById(a.getUserId()).getUsername());
            a.setLikeNum(loveMapper.getLikeCountByArticleId(a.getArticleId()));
        }
        return articleList;
    }

    public List<ArticleVO> newArticles() {
        List<ArticleVO> articleList = baseMapper.newArticles();
        for (ArticleVO a : articleList) {
            a.setAuthor(userService.getById(a.getUserId()).getUsername());
            a.setLikeNum(loveMapper.getLikeCountByArticleId(a.getArticleId()));
        }
        return articleList;
    }

    public List<Article> allPublic() {
        return baseMapper.allPublic();
    }

}
