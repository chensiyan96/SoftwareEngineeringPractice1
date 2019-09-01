package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.demo.util.JSONModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value="Article对象", description="文章")
@Document(indexName = "blog", type = "article")
public class Article implements Serializable, JSONModel {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "主键")
    @TableId(value = "articleId", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty(value = "用户外键")
    private Integer userId;

    @ApiModelProperty(value = "文章标题")
    @NotBlank(message="文章标题不能为空")
    private String title;

    @ApiModelProperty(value = "文章描述")
    private String description;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "格式化后的内容")
    private String contentFormat;

    @ApiModelProperty(value = "阅读量")
    private int readNum;

    @ApiModelProperty(value = "点赞量")
    private int likeNum;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "附件")
    private String appendix;

    @ApiModelProperty(value = "创建时间")
    private long createTime;

    @ApiModelProperty(value = "更新时间")
    private long updateTime;

    @ApiModelProperty(value = "分类类别")
    private String categoryId;

    @ApiModelProperty(value = "是否公开")
    private boolean isPublic;

    @ApiModelProperty(value = "是否置顶")
    private boolean isTop;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("articleId", articleId);
        json.put("userId", userId);
        json.put("title", title);
        json.put("description", description);
        json.put("content", content);
        json.put("contentFormat", contentFormat);
        json.put("readNum", readNum);
        json.put("likeNum", likeNum);
        json.put("cover", cover);
        json.put("appendix", appendix);
        json.put("createTime", createTime);
        json.put("updateTime", updateTime);
        json.put("categoryId", categoryId);
        json.put("isPublic", isPublic);
        json.put("isTop", isTop);
        return json;
    }
}
