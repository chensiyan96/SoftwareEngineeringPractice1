package com.example.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.Contract;

@Data
@ApiModel(value="Love", description="点赞记录")
public class Love {
    @ApiModelProperty(value = "用户外键")
    private int userId;
    @ApiModelProperty(value = "文章外键")
    private int articleId;

    @Contract(pure = true)
    public Love(int userId, int articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }
}
