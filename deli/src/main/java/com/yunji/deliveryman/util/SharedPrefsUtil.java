package com.yunji.deliveryman.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunji.deliveryman.DeliApplication;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefsUtil {
    public final static String SETTING = "db_deli";


    public static void put(String key, int value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static void put(String key, long value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public static void put(String key, boolean value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static void put(String key, String value) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static int get(String key, int defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean get(String key, boolean defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static String get(String key, String defValue) {
        return DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static void remove(String key) {
        DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().remove(key).commit();
    }


    public static void saveTask(ArrayList<String> data) {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(data); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("taskList", jsonStr); //存入json串
        editor.commit();  //提交
    }

    public static ArrayList<String> getTaskList() {
        SharedPreferences sp = DeliApplication.getmApplication().getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String str = sp.getString("taskList", "");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (!Kits.Empty.check(str)) {
            Gson gson = new Gson();
            ArrayList<String> taskList = gson.fromJson(str, new TypeToken<ArrayList<String>>() {
            }.getType()); //将json字符串转换成List集合
            return taskList;
        }
        return null;
    }

}
