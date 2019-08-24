package cn.dblearn.blog.auth.controller;

import cn.dblearn.blog.auth.service.SysCaptchaService;
import cn.dblearn.blog.auth.service.SysUserTokenService;
import cn.dblearn.blog.common.Result;
import cn.dblearn.blog.common.exception.enums.ErrorEnum;
import cn.dblearn.blog.common.util.JsonUtils;
import cn.dblearn.blog.entity.sys.SysUser;
import cn.dblearn.blog.entity.sys.form.SysRegisterForm;
import cn.dblearn.blog.mapper.sys.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysRegisterController {
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @GetMapping("email_captcha")
    public void mailCaptcha(HttpServletResponse response, String email, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("application/json;charset=utf-8");

        //获取验证码
        String code = sysCaptchaService.getMailCaptcha(email, uuid);

        Result r = Result.registerCode(code);
        String json = JsonUtils.toJson(r);
        response.getWriter().print(json);
    }

    @PostMapping("/admin/sys/register")
    public Result register(@RequestBody SysRegisterForm form) {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            // 验证码不正确
            return Result.error(ErrorEnum.CAPTCHA_WRONG);
        }

        if (sysUserMapper.queryExists(form.getUsername(), form.getEmail())) {
            return Result.error("此用户名或邮箱已被注册");
        }

        SysUser user = new SysUser(form.getUsername(), form.getPassword(), form.getEmail());

        //生成token，并保存到redis
        return sysUserTokenService.createToken(user.getUserId());
    }
}
