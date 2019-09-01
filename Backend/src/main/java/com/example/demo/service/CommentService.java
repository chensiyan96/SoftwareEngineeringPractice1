package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.CommentVO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> implements IService<Comment> {

    @Autowired
    UserService userService;

    public List<CommentVO> listComments(int articleId) throws JSONException {
        List<CommentVO> allComments = baseMapper.queryCommentByArticle(articleId);
        Map<Integer, List<JSONObject>> commentListMap = new HashMap<>();
        List<CommentVO> parentComments = new ArrayList<>();
        List<CommentVO> childComments = new ArrayList<>();
        for (CommentVO value : allComments) {
            if (value.getParentId() == null) {
                parentComments.add(value);
                value.commentList = new ArrayList<>();
                commentListMap.put(value.getCommentId(), value.commentList);
            }
            else {
                value.commentList = null;
                childComments.add(value);
            }
            User user = userService.getById(value.getUserId());
            value.setAvatar(user.getAvatar());
            value.setUserName(user.getUsername());
        }
        for (CommentVO value : childComments) {
            int rootId = value.getParentId();
            Integer parentId = baseMapper.selectById(rootId).getParentId();
            while (parentId != null) {
                rootId = parentId;
                parentId = baseMapper.selectById(rootId).getParentId();
            }
            commentListMap.get(rootId).add(value.toJSON());
        }
        return parentComments;
    }

}
