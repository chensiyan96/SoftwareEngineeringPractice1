package com.example.demo.util;

import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JSONUtils {
    public static String successResponse() throws Exception {
        JSONObject response = new JSONObject();
        response.put("success", true);
        return response.toString();
    }

    public static <T> String successResponse(T data) throws Exception {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("data", data);
        return response.toString();
    }

    public static String failResponse(String msg) throws Exception {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("msg", msg);
        return response.toString();
    }
}
