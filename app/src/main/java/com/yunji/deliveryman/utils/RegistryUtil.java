package com.yunji.deliveryman.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;


public class RegistryUtil {
    private static String mainBoardModel;
    private String TAG = "RegistryUtil";
    private String flag3288 = "3288";
    private static Context context;
    private static RegistryUtil registry = new RegistryUtil();
    private String writeCommand = "setprop", readCommand = "getprop";
    private final String PREFIX = "persist.sys.";

    private RegistryUtil() {

    }

    public static RegistryUtil init(Context mContext) {
        mainBoardModel = Build.MODEL;
        context = mContext;
        return registry;
    }

    /**
     * 写入注册表
     */
    public void setProp(String key, String value) {

        key = PREFIX + key;

        //获取主板信息
        checkMainBoardModel();

        if (mainBoardModel.contains(flag3288)) {
            //写入信息到3288板
            String cmd = writeCommand + " " + key + " " + value;
            CommandExe.CommandResult commandResult = CommandExe.execCommand(cmd, true);
            if (commandResult.result != 0) {
                Log.e(TAG, "writeCommand执行失败");
                Log.e(TAG, "写入失败");
                return;
            }
            Log.d(TAG, "key:" + key + ",value:" + value + "写入成功");
        }
    }

    private void checkMainBoardModel() {
        if (mainBoardModel == null) {
            Log.e(TAG, "主板信息读取错误");
            return;
        }
    }

    /**
     * 从注册表读取
     */
    public String getProp(String key) {
        key = PREFIX + key;
        String value = null;
        checkMainBoardModel();
         if (mainBoardModel.contains(flag3288)) {
            String cmd = readCommand + " " + key;
            CommandExe.CommandResult commandResult = CommandExe.execCommand(cmd, true);
            if (commandResult.result != 0) {
                Log.e(TAG, "readCommand执行失败");
                Log.e(TAG, "读取失败");
            }
            value = commandResult.successMsg;
            Log.d(TAG, value + "读取成功");
        }
        return value;
    }
}
