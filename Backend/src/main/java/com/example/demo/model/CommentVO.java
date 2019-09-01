package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentVO extends Comment {

    public CommentVO() {
    }

    public CommentVO(@NotNull Comment comment, String avatar) {
        this.setArticleId(comment.getArticleId());
        this.setCommentId(comment.getCommentId());
        this.setContent(comment.getContent());
        this.setCreateTime(comment.getCreateTime());
        this.setParentId(comment.getParentId());
        this.setUserId(comment.getUserId());
        this.setAvatar(avatar);
    }

    public String userName;
    public String avatar;
    public List<JSONObject> commentList;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put("userName", userName);
        json.put("avatar", avatar);
        json.put("commentList", new JSONArray(commentList));
        return json;
    }
}
