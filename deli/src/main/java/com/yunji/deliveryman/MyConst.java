package com.yunji.deliveryman;


import com.yunji.deliveryman.bean.TaskBean;

import java.util.ArrayList;

public class MyConst {
    // #dev model
    public static boolean DEV = true;
    // #log
    public static String LOG_TAG = "------delivery------";


    public static ArrayList<TaskBean> tasks = new ArrayList<TaskBean>();


    public final static int DELIVERY_START = 1;
    public final static int DELIVERY_END = 2;
    public final static int ACTION_CANCELED = 3;
    public final static int ACTION_ABNORMAL = 4;
    public final static int BATTERY_CHARGING = 5;



    public final static String PLAT_LAYER="PLAT_LAYER";


}
