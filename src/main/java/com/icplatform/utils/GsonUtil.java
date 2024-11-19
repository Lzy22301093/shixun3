package com.icplatform.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 将 Java 对象转换为 JSON 字符串
    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将 JSON 字符串转换为 List<String>
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }




    // 将 JSON 字符串转换为 Java 对象（返回 JsonObject）
    public static <T> T fromJsonToJsonObject(String json) {
        try {
            return gson.fromJson(json, (Class<T>) Object.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将 JSON 字符串转换为 Java 对象（返回 JsonArray）
    public static <T> T fromJsonToJsonArray(String json) {
        try {
            return gson.fromJson(json, (Class<T>) Object.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
