package com.yunji.deliveryman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yunji.deliveryman.bean.CruiseJsonPost;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefsUtil {
    public final static String SETTING = "select_index";


    public static void put(Context context, String key, int value) {
        context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static void put(Context context, String key, long value) {
        context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public static void put(Context context, String key, boolean value) {
        context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static void put(Context context, String key, String value) {
        context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static int get(Context context, String key, int defValue) {
        return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long get(Context context, String key, long defValue) {
        return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean get(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static String get(Context context, String key, String defValue) {
        return context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static void remove(Context context, String key) {
        context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit().remove(key).commit();
    }



    public static void savePointList(Context context, List<CruiseJsonPost.DataBean.TargetsBean> data){
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonStr=gson.toJson(data); //将List转换成Json
        SharedPreferences.Editor editor = sp.edit() ;
        editor.putString("point_list", jsonStr) ; //存入json串
        editor.commit() ;  //提交
    }

    public static ArrayList<CruiseJsonPost.DataBean.TargetsBean> getPointList(Context context){
        SharedPreferences sp =  context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
         String str = sp.getString("point_list","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if(str!="") {
            Gson gson = new Gson();
            ArrayList<CruiseJsonPost.DataBean.TargetsBean> pointList = gson.fromJson(str, new TypeToken<ArrayList<CruiseJsonPost.DataBean.TargetsBean>>() {}.getType()); //将json字符串转换成List集合
            return pointList;
        }
        return null;
    }

}
