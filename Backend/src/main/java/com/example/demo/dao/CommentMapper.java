package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentVO> queryCommentByArticle(int articleId);

    List<Comment> queryCommentByParentId (int parentId);
}
