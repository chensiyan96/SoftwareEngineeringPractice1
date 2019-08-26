package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.BlogService;
import com.example.demo.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("api")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("getPrivateKey")
    public String getPrivateKey() throws Exception {
        Random ra = new Random();
        int key = ra.nextInt();
        int uuid = ra.nextInt();
        JSONObject data_json = new JSONObject();
        data_json.put("key", key);
        data_json.put("uuid", uuid);
        return JSONUtils.successResponse(data_json);
    }

    @PostMapping("register")
    public String register(@RequestBody String req) throws Exception {
        System.out.println(req);
        JSONObject data_json = decryptReqJSON(req);
        String email = data_json.getString("email").toLowerCase();
        String username = data_json.getString("username");
        String password = data_json.getString("password");
        if (blogService.createUser(new User(email, username, password)) == 0) {
            return JSONUtils.failResponse("添加用户失败");
        }
        return JSONUtils.successResponse();
    }

    @PostMapping("login")
    public String login(@RequestBody String req) throws Exception {
        System.out.println(req);
        JSONObject data_json = decryptReqJSON(req);
        String email = data_json.getString("email").toLowerCase();
        String password = data_json.getString("password");
        User user = blogService.getUserByEmail(email);
        if (user == null) {
            return JSONUtils.failResponse("用户不存在");
        }
        return JSONUtils.successResponse();
    }

    private static String encrypt(String src) {
        return src;
    }
    private static String decrypt(String src) {
        return src;
    }
    private static JSONObject decryptReqJSON(String req) throws Exception {
        JSONObject req_json = new JSONObject(req);
        int uuid = req_json.getInt("uuid");
        String data = req_json.getString("data");
        return new JSONObject(decrypt(data));
    }
}
