package com.example.demo.util;

import java.util.HashMap;

public class Result extends HashMap<String, Object> {

    public Result() {
        put("code", 200);
        put("msg", "success");
    }

    public static Result ok() {
        return new Result();
    }

    public static Result registerCode(String code) {
        return new Result().put("registerCode", code);
    }

    public static Result error(Integer code, String msg) {
        return new Result().put("code", code).put("msg", msg);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        //将HashMap对象本身返回
        return this;
    }
}
