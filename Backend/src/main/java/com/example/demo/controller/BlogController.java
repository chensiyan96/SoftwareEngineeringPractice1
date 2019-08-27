package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import com.example.demo.service.MailCaptchaService;
import com.example.demo.service.ImageCaptchaService;
import com.example.demo.util.JSONUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("/")
public class BlogController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailCaptchaService mailCaptchaService;
    @Autowired
    private ImageCaptchaService imageCaptchaService;
    @Autowired
    private CommentService commentService;

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("getEncryptionKey")
    public String getEncryptionKey() throws JSONException {
        Random ra = new Random();
        int key = ra.nextInt();
        int uuid = ra.nextInt();
        JSONObject data_json = new JSONObject();
        data_json.put("key", key);
        data_json.put("uuid", uuid);
        return JSONUtils.successResponse(data_json);
    }

    @GetMapping("captcha.jpg")
    public void getImageCaptcha(@NotNull HttpServletResponse response, @NotNull String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        BufferedImage image = imageCaptchaService.createCaptcha(uuid);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @PostMapping("sendEmailCaptcha")
    public String sendEmailCaptcha(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        String email = req_json.getString("email").toLowerCase();
        mailCaptchaService.sendCaptcha(email);
        return JSONUtils.successResponse();
    }

    @PostMapping("register")
    public String register(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        String uuid = req_json.getString("uuid");
        String data = req_json.getString("data");
        JSONObject data_json = new JSONObject(decrypt(data));

        String email = data_json.getString("email").toLowerCase();
        String captcha = data_json.getString("captcha");
        if (!mailCaptchaService.checkCaptcha(email, captcha)) {
            return JSONUtils.failResponse("验证码错误或已失效");
        }
        if (userService.isEmailExist(email)) {
            return JSONUtils.failResponse("邮箱已被注册");
        }
        String username = data_json.getString("username");
        if (userService.isUsernameExist(username)) {
            return JSONUtils.failResponse("用户名已经存在");
        }
        String password = data_json.getString("password");
        String salt = RandomStringUtils.randomAlphanumeric(32);
        String password_hash = new Sha256Hash(password, salt).toHex();
        if (!userService.save(new User(email, username, password_hash, salt))) {
            return JSONUtils.failResponse("添加用户失败");
        }
        return JSONUtils.successResponse();
    }

    @PostMapping("login")
    public String login(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        String uuid = req_json.getString("uuid");
        String data = req_json.getString("data");
        JSONObject data_json = new JSONObject(decrypt(data));

        String captcha = data_json.getString("captcha");
        if (!imageCaptchaService.checkCaptcha(uuid, captcha)) {
            return JSONUtils.failResponse("验证码错误");
        }
        String email = data_json.getString("email").toLowerCase();
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return JSONUtils.failResponse("用户不存在");
        }
        String password = data_json.getString("password");
        if (!user.checkPassword(password)) {
            return JSONUtils.failResponse("密码错误");
        }
        return JSONUtils.successResponse();
    }

    @PostMapping("createComment")
    public String createComment(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        int userId = 1;
        String content = req_json.getString("content");
        Comment comment;
        if (req_json.has("articleId")) {
            int articleId = req_json.getInt("articleId");
            comment = new Comment(userId, articleId, content);
        }
        else {
            int parentId = req_json.getInt("parentId");
            int articleId = 1;
            comment = new Comment(userId, articleId, parentId, content);
        }
        if (!commentService.save(comment)) {
            return JSONUtils.failResponse("新建评论失败");
        }
        return JSONUtils.successResponse();
    }

    @GetMapping("showComments/{articleId}")
    public String showComments(@PathVariable int articleId) throws JSONException {
        System.out.println(articleId);
        return JSONUtils.successResponse();
    }

    @DeleteMapping("deleteComment")
    public String deleteComment(int commentId) throws JSONException {
        if (!commentService.removeById(commentId)) {
            return JSONUtils.failResponse("删除评论失败");
        }
        return JSONUtils.successResponse();
    }

    private static String encrypt(String src) {
        return src;
    }
    private static String decrypt(String src) {
        return src;
    }

}
