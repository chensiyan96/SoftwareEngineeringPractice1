package com.example.demo.util;


import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface JSONModel
{
	JSONObject toJSON() throws JSONException;
}
