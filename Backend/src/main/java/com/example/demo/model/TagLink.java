package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章标签多对多维护表
 */
@Data
@ApiModel(value="TagLink对象", description="标签多对多维护表")
@TableName("tag_link")
public class TagLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer linkId;

    private Integer tagId;

    public TagLink() {
    }

    public TagLink(Integer linkId, Integer tagId) {
        this.linkId =  linkId;
        this.tagId = tagId;
    }
}
