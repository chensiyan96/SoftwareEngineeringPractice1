package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "Comment对象", description = "评论")
public class Comment implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "commentId", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty(value = "用户外键")
    private Integer userId;

    @ApiModelProperty(value = "文章外键")
    private Integer articleId;

    @ApiModelProperty(value = "父评论外键")
    private Integer parentId;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容")
    private String content;

    public Comment(int userId, int articleId, String content) {
        this.userId = userId;
        this.articleId = articleId;
        this.content = content;
    }

    public Comment(int userId, int articleId, int parentId, String content) {
        this.userId = userId;
        this.articleId = articleId;
        this.parentId = parentId;
        this.content = content;
    }
}
