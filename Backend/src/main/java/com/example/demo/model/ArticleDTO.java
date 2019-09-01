package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleDTO extends Article {
    private List<Tag> tagList;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put("tagList", tagList);
        return json;
    }
}
