package com.yunji.deliveryman.utils;


import android.util.Log;

import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;

/**
 * 封装日志打印功能
 */
public class MyLogcat {

    public static void showLog(String message) {
        try{
            if (Constants.IS_SHOWLOG) {
                FL.e(Constants.show_log, message);
                Log.e(Constants.show_log, message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showLogStatus(String message) {
        try{
            if (Constants.IS_SHOWLOG) {
                FL.e(Constants.show_log_status, message);
                Log.e(Constants.show_log_status, message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showLogArrivedStatus(String message) {
            try{
                if (Constants.IS_SHOWLOG) {
                    FL.e(Constants.show_log_arrived_status, message);
                    Log.e(Constants.show_log_arrived_status, message);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public static void showLogStatusState(String message) {

        try{
            if (Constants.IS_SHOWLOG) {
                FL.e(Constants.show_log_status_state, message);
                Log.e(Constants.show_log_status_state, message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
