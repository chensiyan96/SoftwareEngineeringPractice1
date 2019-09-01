package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JSONUtils {

    public static String successResponse() throws JSONException {
        JSONObject response = new JSONObject();
        response.put("success", true);
        return response.toString();
    }

    public static <T> String successResponse(T data) throws JSONException {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("data", data);
        return response.toString();
    }

    public static String failResponse(String msg) throws JSONException {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("msg", msg);
        return response.toString();
    }

    @NotNull
    public static <T extends JSONModel> List<JSONObject> arrayToJSONs(@NotNull List<T> ts) throws JSONException {
        List<JSONObject> res = new ArrayList<>();
        for (T t : ts) {
            res.add(t.toJSON());
        }
        return res;
    }

    private final static ObjectMapper objMapper = new ObjectMapper();

    public static <T> T toObj(String jsonString, Class<T> clazz) {
        objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            return objMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object obj) {
        if(obj instanceof Integer || obj instanceof Long || obj instanceof Float ||
                obj instanceof Double || obj instanceof Boolean || obj instanceof String){
            return String.valueOf(obj);
        }
        try {
            return objMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
