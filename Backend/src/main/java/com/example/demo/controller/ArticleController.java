package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.mq.annotation.RefreshEsMqSender;
import com.example.demo.config.RedisCacheNames;
import com.example.demo.service.*;
import com.example.demo.util.Result;
import com.example.demo.util.JSONUtils;
import com.example.demo.util.ValidatorUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@CacheConfig(cacheNames = {RedisCacheNames.RECOMMEND, RedisCacheNames.TAG, RedisCacheNames.ARTICLE, RedisCacheNames.TIMELINE})
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @PostMapping("saveArticle")
    //@CacheEvict(allEntries = true)
    @RefreshEsMqSender(sender = "blog-saveArticle")
    public String saveArticle(@NotNull @RequestBody ArticleDTO article) throws JSONException {
        int userId = tokenService.getCurrentUserId();
        article.setUserId(userId);
        for (Tag tag : article.getTagList()) {
            tag.setUserId(userId);
        }
        ValidatorUtils.validateEntity(article);
        long current_time = System.currentTimeMillis();
        article.setCreateTime(current_time);
        article.setUpdateTime(current_time);
        articleService.saveArticle(article);
        return JSONUtils.successResponse();
    }

    @PutMapping("updateArticle")
    //@CacheEvict(allEntries = true)
    @RefreshEsMqSender(sender = "blog-updateArticle")
    public String updateArticle(@NotNull @RequestBody ArticleDTO article) throws JSONException {
        article.setUserId(tokenService.getCurrentUserId());
        ValidatorUtils.validateEntity(article);
        article.setUpdateTime(System.currentTimeMillis());
        articleService.updateArticle(article);
        return JSONUtils.successResponse();
    }

    @PutMapping("likeArticle/{articleId}")
    public String likeArticle(@PathVariable int articleId) throws JSONException {
        int userId = tokenService.getCurrentUserId();
        if (articleService.likeArticle(userId, articleId)) {
            return JSONUtils.successResponse();
        }
        return JSONUtils.failResponse("您已经点赞过这篇文章了");
    }

    @GetMapping("article/{articleId}")
    //@Cacheable
    public Result getArticle(@PathVariable int articleId) {
        ArticleVO article = articleService.getArticleVo(articleId);
        if (article != null) {
            User user = tokenService.getCurrentUser();
            if (user != null) {
                if (user.getUserId().equals(article.getUserId())) {
                    return Result.ok().put("article", article);
                }
            }
            if (!article.isPublic()) {
                return Result.error(403, "该文章是私密的");
            }
            articleService.updateReadNum(articleId);
            return Result.ok().put("article", article);
        }
        else {
            return Result.error(404, "文章不存在");
        }
    }

    @GetMapping("articles")
    //@Cacheable
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = articleService.queryPageCondition(params);
        return Result.ok().put("page",page);
    }

    @GetMapping("articles/{userId}")
    //@Cacheable
    public Result list(@RequestParam Map<String, Object> params, @PathVariable int userId) {
        PageUtils page = articleService.queryPageCondition(params, userId);
        return Result.ok().put("page",page);
    }

    @GetMapping("hotArticles")
    //@Cacheable
    public String hotArticles() throws JSONException {
        List<ArticleVO> articles = articleService.hotArticles();
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(articles)));
    }

    @GetMapping("hotArticles/{userId}")
    //@Cacheable
    public String hotArticles(@PathVariable int userId) throws JSONException {
        List<ArticleVO> articles = articleService.hotArticles(userId);
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(articles)));
    }

    @GetMapping("newArticles")
    //@Cacheable
    public String newArticles() throws JSONException {
        List<ArticleVO> articles = articleService.newArticles();
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(articles)));
    }

    @DeleteMapping("deleteArticle/{articleId}")
    public String deleteArticle(@PathVariable int articleId) throws JSONException {
        Article article = articleService.getById(articleId);
        if (article == null) {
            return JSONUtils.failResponse("文章不存在");
        }
        if (article.getUserId() != tokenService.getCurrentUserId()) {
            return JSONUtils.failResponse("没有权限");
        }
        if (!articleService.removeById(articleId)) {
            return JSONUtils.failResponse("删除文章失败");
        }
        return JSONUtils.successResponse();
    }

    @PostMapping("createComment")
    public String createComment(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        int userId = tokenService.getCurrentUserId();
        String content = req_json.getString("content");
        Comment comment;
        if (!req_json.has("parentId")) {
            int articleId = req_json.getInt("articleId");
            comment = new Comment(userId, articleId, content);
        }
        else {
            int parentId = req_json.getInt("parentId");
            int articleId = commentService.getById(parentId).getArticleId();
            comment = new Comment(userId, articleId, parentId, content);
        }
        comment.setCreateTime(System.currentTimeMillis());
        if (!commentService.save(comment)) {
            return JSONUtils.failResponse("新建评论失败");
        }
        return JSONUtils.successResponse();
    }

    @GetMapping("showComments/{articleId}")
    public String showComments(@PathVariable int articleId) throws JSONException {
        List<CommentVO> comments = commentService.listComments(articleId);
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(comments)));
    }

    @DeleteMapping("deleteComment/{commentId}")
    public String deleteComment(@PathVariable int commentId) throws JSONException {
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return JSONUtils.failResponse("评论不存在");
        }
        if (comment.getUserId() != tokenService.getCurrentUserId()) {
            return JSONUtils.failResponse("没有权限");
        }
        if (!commentService.removeById(commentId)) {
            return JSONUtils.failResponse("删除评论失败");
        }
        return JSONUtils.successResponse();
    }

    @GetMapping("categories")
    //@Cacheable
    public Result listCategory() {
        List<Category> categoryList = categoryService.listCategory();
        return Result.ok().put("categoryList",categoryList);
    }

    @GetMapping("tags/{userId}")
    //@Cacheable
    public Result listTag(@PathVariable int userId) {
        List<TagVO> tagList = tagService.listByUserId(userId);
        return Result.ok().put("tagList",tagList);
    }

    @GetMapping("tags")
    //@Cacheable
    public Result listTag() {
        List<TagVO> tagList = tagService.listByUserId(tokenService.getCurrentUserId());
        return Result.ok().put("tagList",tagList);
    }

}
