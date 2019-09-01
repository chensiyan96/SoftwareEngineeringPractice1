package com.example.demo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.demo.util.JSONModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.jetbrains.annotations.Contract;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "User对象", description = "用户")
public class User implements Serializable, JSONModel {

    private static final long serialVersionUID = 1L;

    public static String passwordHash(@NotNull String password, @NotNull String salt) {
        return new Sha256Hash(new Sha256Hash(password).toHex(), salt).toHex();
    }

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
    @ApiModelProperty(value = "加盐哈希过的密码")
    private String password;

    @ApiModelProperty(value = "密码盐")
    private String salt;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "描述")
    private String info;

    @Contract(pure = true)
    public User() {

    }

    @Contract(pure = true)
    public User(Integer userId, String email, String username, String password, String salt, String avatar, String info) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.avatar = avatar;
        this.info = info;
    }

    @Contract(pure = true)
    public User(String email, String username, String password, String salt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public boolean checkPassword(String password) {
        String password_hash = passwordHash(password, salt);
        return this.password.equals(password_hash);
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("email", email);
        json.put("username", username);
        json.put("avatar", avatar);
        json.put("info", info);
        return json;
    }
}