package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.crypto.hash.Sha256Hash;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "User对象", description = "用户")
public class User implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    public User(String email, String username, String password, String salt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String password) {
        String password_hash = new Sha256Hash(password, salt).toHex();
        return this.password.equals(password_hash);
    }
}