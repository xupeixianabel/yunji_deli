package com.yunji.deliveryman.common;

public interface CommonConstant {
    String SDKVersionName = "1.1.1";
    String BROADCASR_TEST = "BROADCASR_TEST";

    interface ResponseType {
        String response = "response";
        String notification = "notification";
        String callback = "callback";
    }

    interface ConnectUrl {
        String queryList = "/api/markers/query_list";
        String move = "/api/move?marker=";
        String turnAround = "/api/joy_control?angular_velocity=%1$.4f&linear_velocity=0";
        String cancel = "/api/move/cancel";
        String status = "/api/robot_status";
        String insert = "/api/markers/insert?name=%1$s&type=%2$d";
        String joyControl = "/api/joy_control?angular_velocity=%1$.2f&linear_velocity=%2$.2f";
        String estop = "/api/estop?flag=%1$b";
        String positionAdjust = "/api/position_adjust?marker=%1$s";
        String requestData = "/api/request_data?topic=%1$s&switch=%2$s&frequency=%3$d";
        String requestDataCom = "/api/request_data";
        String setParams = "/api/set_params";
        String getParams = "/api/get_params";
        String getVersion = "/api/software/get_version";
        String requestWifi = "/api/wifi/list";
        String connectWifi = "/api/wifi/connect?SSID=%1$s&password=%2$s";
        String getCurrentWifi = "/api/wifi/get_active_connection";
        String getCurrentWifiInfo = "/api/wifi/info";
        String getMapList = "/api/map/list";
        String setCurrentMap = "/api/map/set_current_map";
        String getCurrentMap = "/api/map/get_current_map";
        String shutdown = "/api/shutdown?reboot=%1$b";
        String softwareCheckUpdate = "/api/software/check_for_update";
        String softwareUpdate = "/api/software/update";
        String hardwareCheckUpdate = "/api/hardware/check_for_update";
        String hardwareUpdate = "/api/hardware/update";
        String getRobotInfo = "/api/robot_info";
    }

    interface PropertyKey {
        String productId = "productId";
        String sn = "sn";
        String code = "code";
        String VOICERLOCAL = "voicerLocal";
        String SPEED = "speed";
        String apkVersion = "apkVersion";
        String cfgVersion = "cfgVersion";
        String versionCheck = "versionCheck";
        String isCloseFaceDetect = "isCloseFaceDetect";
        String isCloseAutoCharge = "isCloseAutoCharge";
        String leadToFinishSet = "leadToFinishSet";
        String strollCount = "strollCount";
        String strollToFinishSet = "strollToFinishSet";

        String isStrollInterrupt = "isStrollInterrupt";
        String isLeadInterrupt = "isLeadInterrupt";
        String robotStatus = "robotStatus";
        String lastRobotStatus = "lastRobotStatus";

        //巡游被中断
        String isStrollBroken = "isStrollBroken";
        //引领被中断
        String isLeadBroken = "isLeadBroken";


        //当前任务
        String currentWork = "currentWork";
    }

    interface LogStatus {
        String start = "start";
        String cancel = "cancel";
        String success = "success";
        String fail = "fail";
    }

    interface LogAction {
        String sleep = "sleep";
        String awaken = "awaken";
        String openMicrophone = "openMicrophone";
        String closeMicrophone = "closeMicrophone";
        String startCharge = "开始回桩";
        String stopCharge = "开始下桩";
        String check = "check";
        String download = "download";
        String install = "install";
    }

    interface LogType {
        String apk = "apk";
        String config = "config";
    }

    //任务状态:1.静止;2.巡游;3.引领
    interface RobotStatus {
        String idle = "0";
        String stroll = "1";
        String lead = "2";
        String voiceLead = "3";
    }


    /**
     * 恢复任务类型
     */
    interface RecoveryType {
        String stroll = "1";
        String lead = "2";
        String voiceLead = "3";
    }
}
