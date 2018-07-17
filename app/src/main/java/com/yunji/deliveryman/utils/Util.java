package com.yunji.deliveryman.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.yunji.deliveryman.bean.HttpMessage;
import com.yunji.deliveryman.common.CommonConstant;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }
    //方法  返回true为包含中文；false不包含
    public static boolean isContainsChinese(String str)
    {
        Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())    {
            flg = true;
        }
        return flg;
    }


    /**
     * 是否联网
     *
     * @param context
     * @return boolean
     */
    @SuppressWarnings("deprecation")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String JSONTokener(String in) {
        // consume an optional byte order mark (BOM) if it exists
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }
        return in;
    }
    public static String updateTableNameStr(String tableName){
        String newTableName =tableName;
        if(!TextUtils.isEmpty(tableName)){
            if(!tableName.contains("餐桌")){
                newTableName =newTableName+"餐桌";
            }
        }
        return newTableName;
    }
    /**
     * 返回结果
     */
    public static String parseMessage(String jsonStr){
//SocketMessage {type=0, message='{"code":"01002","description":"The move task is finished.","level":"info","type":"notification"}', userId='null'}
        boolean success = false;
        String result ="";
        try {
            JSONObject joObject = new JSONObject(JSONTokener(jsonStr));
            result =    joObject.optString("code");

        } catch (Exception e) {
            e.printStackTrace();
            MyLogcat.showLog("解析异常=="+e.getMessage().toString());
            FL.e(Constants.show_log, "解析异常=="+e.getMessage().toString());
        }
        return result;
    }
    /**
     * 返回CancelMove 结果
     */
    public static Boolean parseCancelMoveMessage(String jsonStr){
//{
//        "type": "response",
//                "command": "/api/move/cancel",
//                "uuid": "",
//                "status": "OK",
//                "error_message": ""
//    }
        boolean success = false;
        String result ="";
        try {
            JSONObject joObject = new JSONObject(JSONTokener(jsonStr));
            result =    joObject.optString("command");
            if(CommonConstant.ConnectUrl.cancel.equals(result)){
                result =    joObject.optString("status");
                if("OK".equals(result)){
                    success = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MyLogcat.showLog("解析异常=="+e.getMessage().toString());
            FL.e(Constants.show_log, "解析异常=="+e.getMessage().toString());
        }
        return success;
    }

    public static  void showDorbar(Context context, boolean isShow) {
        Intent intent= null;
        if(isShow){
            intent=new Intent("com.android.systembar.show");
            context.sendBroadcast(intent);

        }else{
            intent=new Intent("com.android.systembar.hide");
            context.sendBroadcast(intent);

//            if (context instanceof Activity)
//            hideBottomUIMenu((Activity) context);
        }
    }



 /*   public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }*/






    /*  *//**
     * 根据消息对象构建Json对象
     *
     * @param message
     * @return
     */
    public static JSONObject initJsonObject(HttpMessage message) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("message", message.message);
            jsonObject.put("requestType", message.requestType);
            jsonObject.put("hostName", message.hostName);
            jsonObject.put("apiName", message.apiName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取版本号
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * s
     * 获取版本名
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
