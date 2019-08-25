package com.example.demo;

import javax.validation.constraints.NotBlank;

public class sysUser {
    @ApiModelProperty(value = "主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @NotBlank(message = "用户名不能为空" , groups = {AddGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空" ,groups = AddGroup.class)
    @ApiModelProperty(value = "密码")
    private String password;

}
