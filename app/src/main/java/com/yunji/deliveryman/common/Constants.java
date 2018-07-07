package com.yunji.deliveryman.common;


import android.os.Environment;

import java.io.File;

public class Constants {

    //定义客户端和服务器端的称呼
    String NAME_SERVER = "服务器";
    String NAME_CLIENT = "客户端";

    //收到了SocketMessage消息
    String ACTION_SOCKET_MESSSAGE = "com.yunji.deliveryman.common";
    //Socket当前的状态
    String ACTION_SOCKET_STATUS = "action_socket_status";

    public static final int MSG_GET_SUCCESS = 111;
    public static final int MSG_GET_FAILED = 112;
    public static final int MSG_POST_SUCCESS = 113;
    public static final int MSG_POST_FAILED = 114;

    public static final  String URL_API_NAME = "url_api_name";
    //消息类型
    public static int MESSAGE_ACTIVE = -1;//心跳包
    public static int MESSAGE_EVENT = 0;//事件包
    public static  int MESSAGE_CLOSE = 3;//断开连接


    public static  String LAYER_NUM = "layer_num";//几层托板

    // 1代表一层任务开始2代表一层任务到达
    // 3 代表二层任务开始4代表二层任务到达
    // 5 代表三层任务开始6代表三层任务到达
    // 7 代表返回厨房开始8代表返回厨房任务到达

    public static final String LOCAL_APK_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;//第三方库，只支持一层目录
    public static final int MSG_GET_VERSION = 24;
    public static final int MSG_GET_VERSION_SUCCESS = 25;
    public static final int MSG_GET_VERSION_FAIL = 26;
    public static final int MSG_SERVER_ERROR = 27;
    public static final String HTTP_APK_URL="http://api.mp.yunjichina.com.cn/openapi/applications/version/latest?appkey=yjfbf9ad878ab15336";

    //定义服务器的ip和端口号
//     String SERVER_HOST = "192.168.0.100";
    public static final   String SERVER_HOST = "127.0.0.1";
    public static final int SERVER_PORT = 31001;

    public static final int SOCKET_CONNECT_TIMEOUT = 15;//设置Socket连接超时为6秒
    public static final  int SOCKET_ACTIVE_TIME = 60;//发送心跳包的时间间隔为60秒

    //巡游，召唤
    public static final int MSG_TIME_OUT = 1;
    public static final int MSG_TIME_OUT_TIME = 60*1000;
    public static final int MSG_TIME_OUT_TIME_FOR_SUCCESS = 30*1000;

    public static String TASK_TABLE_NAME = "task_table_name";//选择的桌子的名字
    //是否打印Log
    public static final  boolean IS_SHOWLOG = true;
    public static final  String WORK_ON_STAE = "work_on_stae";//是否上班
    /******************************************************************/ //软件发布相关
    //1，
    public static final  int DB_VERSION = 1;

     //2，修改    versionCode 1 ， versionName "1.0"
    //3，机器人ID，（本项目的RobotId,发卡器里的RobotId,mqtt里的RobotId)
     public static   String robotId = "";//目前账号管理里的数据。以后以版本升级管理平台为主




    /**************************** MQTT相关 ************************/
    public static   String URL = "tcp://mqtt.yunjichina.com.cn:1883";
//    public static   String URL = "tcp://192.168.0.157:1883";
    public static    String topicNotification = "robot/mirages/++/topic/notification";
    public static    String topicCommand = "robot/mirages/++/topic/command";
    public static   String topicBarricade = "robot/yunhai/++/topic/notification";//订阅路障话题
    public static   String topicCruise = "robot/yunhai/++/topic/robot_cruise_info";//订阅巡游话题
//    public static   String topicBarricade = "robot/yunhai/"+"YH01SH01181800002"+"/topic/notification";//订阅路障话题
//    public static   String topicCruise = "robot/yunhai/"+"YH01SH01181800002"+"/topic/robot_cruise_info";//订阅巡游话题





    /*MyLogcat标签*/
    public static final String show_log = "_log";
    public static final String show_log_status = "log_status";
    public static final String show_log_arrived_status = "arrived_status";
    public static final String show_log_status_state = "status_state";
}
