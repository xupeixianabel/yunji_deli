package com.yunji.deliveryman.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.yunji.deliveryman.common.CommonConstant;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PublicUtils {
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

    public  static String getNewTopic(String topic, String productId) {
        topic = topic.replace("++", productId);
        MyLogcat.showLog("新替换后的topic="+topic);
        FL.e(Constants.show_log, "新替换后的topic="+topic);
        return topic;
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
     */
    public static boolean isConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info == null){
            return false;
        }
        NetworkInfo.State state = info.getState();
        if(state != NetworkInfo.State.CONNECTED){
            return false;
        }
        return true;
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
            MyLogcat.showLog("解析异常：" + e.getMessage().toString());
            FL.e(Constants.show_log, "解析异常：" + e.getMessage().toString());
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
//            context.sendBroadcast(intent);
        }
    }
  /*  *//**
     * 根据消息对象构建Json对象
     *
     * @param message
     * @return
     */
    /*public static JSONObject initJsonObject(SocketMessage message) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (message.type != Constants.MESSAGE_ACTIVE)
                jsonObject.put("message", message.message);
            else
                jsonObject.put("message", "");
            jsonObject.put("type", message.type);
            jsonObject.put("message", message.message);
            jsonObject.put("userId", message.userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }*/

   /**
     * 解析Json数据
     *
     * @param json
     * @return
     */
   /* public static SocketMessage parseJson(String json) {
        SocketMessage message = new SocketMessage();
        try {
            JSONObject jsonObject = new JSONObject(json);
            message.type=jsonObject.optInt("type");
            if (message.type == Constants.MESSAGE_EVENT)
                message.message=jsonObject.optString("message");
            message.userId=jsonObject.optString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }*/
    /**
     * 修改可读写权限
     */
    public static void setFileEnable(String AbsolutePath){
        try {
            File destFile = new File(AbsolutePath);
            if(destFile != null && destFile.exists()){
                if(!destFile.canWrite() || !destFile.canRead())
                {
                    String command = "chmod 777 " + destFile.getAbsolutePath();
                    MyLogcat.showLog("修改可读写权限"+command);
                    FL.e(Constants.show_log, "修改可读写权限"+command);
                    Runtime runtime = Runtime.getRuntime();
                    Process proc = runtime.exec(command);
                }
            }
        } catch (Exception e) {
            MyLogcat.showLog("修改可读写权限异常");
            FL.e(Constants.show_log, "修改可读写权限异常");
            e.printStackTrace();
        }
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static double getVersionCode(Context mContext) {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            String packageName = mContext.getPackageName();
            int flags = 0;
            if (packageManager != null) {
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, flags);
                if (packageInfo != null) {
                    int versionCode = packageInfo.versionCode;
                    if (versionCode != 0) {
                        return versionCode;
                    }
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
