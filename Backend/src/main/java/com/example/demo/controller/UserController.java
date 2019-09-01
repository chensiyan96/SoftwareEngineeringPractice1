package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.IOUtils;
import com.example.demo.model.TimelineYear;
import com.example.demo.model.UserVO;
import com.example.demo.service.TokenService;
import com.example.demo.model.User;
import com.example.demo.service.*;
import com.example.demo.util.JSONUtils;
import com.example.demo.util.Result;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    private final boolean useRSA = true;

    @Autowired
    private UserService userService;
    @Autowired
    private MailCaptchaService mailCaptchaService;
    @Autowired
    private ImageCaptchaService imageCaptchaService;
    @Autowired
    private RSAService rsaService;
    @Autowired
    private UuidService uuidService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TimelineService timelineService;

    private BASE64Decoder base64Decoder = new BASE64Decoder();

    @GetMapping("getPublicKey")
    public String getPublicKey() throws JSONException {
        KeyPair keyPair = rsaService.generateKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();
        String uuid = uuidService.createUuidForPrivateKey(rsaService.saveKeyForEncodedBase64(priKey));
        JSONObject data_json = new JSONObject();
        data_json.put("key", rsaService.saveKeyForEncodedBase64(pubKey));
        data_json.put("uuid", uuid);
        return JSONUtils.successResponse(data_json);
    }

    @GetMapping("captcha.jpg")
    public void getImageCaptcha(@NotNull HttpServletResponse response, @NotNull String uuid, @NotNull String useless) throws IOException {
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
    public String register(@RequestBody String req) throws Exception {
        JSONObject req_json = new JSONObject(req);
        JSONObject data_json = decryptData(req_json);
        if (data_json == null) {
            return JSONUtils.failResponse("解密失败");
        }
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
        String password_hash = User.passwordHash(password, salt);
        if (!userService.save(new User(email, username, password_hash, salt))) {
            return JSONUtils.failResponse("添加用户失败");
        }
        return loginSuccess(userService.getUserByEmail(email));
    }

    @PostMapping("login")
    public String login(@RequestBody String req) throws Exception {
        JSONObject req_json = new JSONObject(req);
        String uuid = req_json.getString("uuid");
        JSONObject data_json = decryptData(req_json);
        if (data_json == null) {
            return JSONUtils.failResponse("解密失败");
        }
        String captcha = data_json.getString("captcha");
        if (!imageCaptchaService.checkCaptcha(uuid, captcha)) {
            return JSONUtils.failResponse("验证码错误");
        }
        String email = data_json.getString("email").toLowerCase();
        if (!userService.isEmailExist(email)) {
            return JSONUtils.failResponse("用户不存在");
        }
        User user = userService.getUserByEmail(email);
        String password = data_json.getString("password");
        if (!user.checkPassword(password)) {
            return JSONUtils.failResponse("密码错误");
        }
        return loginSuccess(user);
    }

    @PostMapping("logout")
    public String logout() throws JSONException {
        int userId = ((User)SecurityUtils.getSubject().getPrincipal()).getUserId();
        tokenService.deleteToken(userId);
        return JSONUtils.successResponse();
    }

    @PostMapping("changePassword")
    public String changePassword(@RequestBody String req) throws IOException, JSONException {
        JSONObject req_json = new JSONObject(req);
        JSONObject data_json = decryptData(req_json);
        if (data_json == null) {
            return JSONUtils.failResponse("解密失败");
        }
        User user = tokenService.getCurrentUser();
        String old_password = data_json.getString("old");
        if (!user.checkPassword(old_password)) {
            return JSONUtils.failResponse("原始密码错误");
        }
        user.setPassword(User.passwordHash(data_json.getString("new"), user.getSalt()));
        userService.updateById(user);
        return JSONUtils.successResponse();
    }

    @PostMapping("resetPassword")
    public String resetPassword(@RequestBody String req) throws IOException, JSONException {
        JSONObject req_json = new JSONObject(req);
        JSONObject data_json = decryptData(req_json);
        if (data_json == null) {
            return JSONUtils.failResponse("解密失败");
        }
        String email = data_json.getString("email").toLowerCase();
        String captcha = data_json.getString("captcha");
        if (!mailCaptchaService.checkCaptcha(email, captcha)) {
            return JSONUtils.failResponse("验证码错误或已失效");
        }
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return JSONUtils.failResponse("用户不存在");
        }
        String salt = RandomStringUtils.randomAlphanumeric(32);
        user.setPassword(User.passwordHash(data_json.getString("password"), salt));
        userService.updateById(user);
        return loginSuccess(user);
    }

    @PostMapping("changeInfo")
    public String changeInfo(@RequestBody String req) throws JSONException {
        JSONObject req_json = new JSONObject(req);
        User user = tokenService.getCurrentUser();
        if (req_json.has("username")) {
            user.setUsername(req_json.getString("username"));
        }
        if (req_json.has("info")) {
            user.setInfo(req_json.getString("info"));
        }
        userService.updateById(user);
        return JSONUtils.successResponse();
    }

    @PutMapping("followUser/{hostId}")
    public String followUser(@PathVariable int hostId) throws JSONException {
        int fansId = tokenService.getCurrentUserId();
        if (fansId == hostId) {
            return JSONUtils.failResponse("自己不能关注自己");
        }
        if (userService.followUser(hostId, fansId)) {
            return JSONUtils.successResponse();
        }
        return JSONUtils.failResponse("您已经关注过该用户了");
    }

    @DeleteMapping("cancelFollow/{hostId}")
    public String cancelFollow(@PathVariable int hostId) throws JSONException {
        int fansId = tokenService.getCurrentUserId();
        if (userService.cancleFollow(hostId, fansId)) {
            return JSONUtils.successResponse();
        }
        return JSONUtils.failResponse("您没有关注过该用户");
    }

    @GetMapping("timeline")
    public Result timeline() {
        List<TimelineYear> timelineYearList = timelineService.listTimeLine(tokenService.getCurrentUserId());
        return Result.ok().put("timelineList", timelineYearList);
    }

    @GetMapping("recommendUsers")
    public String recommendUsers() throws JSONException {
        List<UserVO> users = userService.recommendUsers();
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(users)));
    }

    @GetMapping("followUsers")
    public String followUsers() throws JSONException {
        User user = tokenService.getCurrentUser();
        if (user == null) {
            return JSONUtils.successResponse(new JSONArray());
        }
        List<UserVO> users = userService.getFollowUserByFansId(user.getUserId());
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(users)));
    }

    @GetMapping("fans")
    public String fans() throws JSONException {
        List<UserVO> users = userService.getFollowUserByHostId(tokenService.getCurrentUserId());
        return JSONUtils.successResponse(new JSONArray(JSONUtils.arrayToJSONs(users)));
    }

    private String loginSuccess(@NotNull User user) throws JSONException {
        JSONObject data_json = new JSONObject();
        data_json.put("token", tokenService.createToken(user.getUserId()));
        data_json.put("userId", user.getUserId());
        data_json.put("username", user.getUsername());
        data_json.put("avatar", user.getAvatar());
        data_json.put("info", user.getInfo());
        return JSONUtils.successResponse(data_json);
    }

    @Nullable
    private JSONObject decryptData(@NotNull JSONObject req_json) throws JSONException, IOException {
        String uuid = req_json.getString("uuid");
        String data = req_json.getString("data");
        PrivateKey priKey = rsaService.getPrivateKey(uuidService.getPrivateKeyByUuid(uuid));
        if (useRSA) {
            byte[] b = rsaService.decrypt(base64Decoder.decodeBuffer(data), priKey);
            return b == null ? null : new JSONObject(new String(b));
        }
        return new JSONObject(data);
    }

}
