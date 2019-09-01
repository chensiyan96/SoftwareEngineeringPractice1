package com.example.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.Contract;

@Data
@ApiModel(value="Follow", description="点赞记录")
public class Follow {
    @ApiModelProperty(value = "被关注者外键")
    private int hostId;
    @ApiModelProperty(value = "关注者外键")
    private int fansId;

    @Contract(pure = true)
    public Follow(int hostId, int fansId) {
        this.hostId = hostId;
        this.fansId = fansId;
    }
}
