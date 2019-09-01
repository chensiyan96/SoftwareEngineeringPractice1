package com.example.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleVO extends ArticleDTO {

    private String categoryListStr;
    private String author;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = super.toJSON();
        json.put("categoryListStr", categoryListStr);
        json.put("author", author);
        return json;
    }
}
