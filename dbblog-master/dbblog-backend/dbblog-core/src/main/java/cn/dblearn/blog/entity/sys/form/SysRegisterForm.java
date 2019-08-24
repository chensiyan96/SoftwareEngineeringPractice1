package cn.dblearn.blog.entity.sys.form;

import lombok.Data;

@Data
public class SysRegisterForm {
    private String username;
    private String email;
    private String password;
    private String captcha;
    private String uuid;
}
