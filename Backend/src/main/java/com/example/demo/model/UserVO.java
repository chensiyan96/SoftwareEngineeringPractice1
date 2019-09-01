package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {

    private int likeNum;
    private int articleNum;

    public UserVO(@NotNull User user, int likeNum, int articleNum) {
        super(user.getUserId(), user.getEmail(), user.getUsername(), user.getPassword(), user.getSalt(), user.getAvatar(), user.getInfo());
        this.likeNum = likeNum;
        this.articleNum = articleNum;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put("likeNum", likeNum);
        json.put("articleNum", articleNum);
        return json;
    }
}
