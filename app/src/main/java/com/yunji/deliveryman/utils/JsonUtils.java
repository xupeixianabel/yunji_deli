package com.yunji.deliveryman.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class JsonUtils {
    private static Gson mGson = new GsonBuilder().serializeNulls().create();

    public static String toJson(Object src) {
        return mGson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return mGson.fromJson(json, classOfT);
    }
}