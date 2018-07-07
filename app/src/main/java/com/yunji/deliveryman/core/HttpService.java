package com.yunji.deliveryman.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;

import com.yunji.deliveryman.MyApplication;
import com.yunji.deliveryman.bean.HttpMessage;
import com.yunji.deliveryman.bean.RobotStatusResponse;
import com.yunji.deliveryman.bus.ResponseEvent;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.net.HttpResponseTask;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.JsonUtils;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.MyToast;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class HttpService extends Service {

    //网络是否连接标记
    private static boolean netConnectFlag;
    //监控服务被杀死重启的广播，保持服务不被杀死
    private BroadcastReceiver restartReceiver;

    public final static String hostName = "192.168.0.157";

    private static IHttpServiceInterface mIHttpServiceInterface;

    private String jsonStr = "{\n" +
            "\t\"code\": 0,\n" +
            "\t\"data\": {\n" +
            "\t\t\"OpenCloseFlag\": 0,\n" +
            "\t\t\"charge_state\": 1,\n" +
            "\t\t\"door_close_count\": 108,\n" +
            "\t\t\"door_open_count\": 0,\n" +
            "\t\t\"estop\": false,\n" +
            "\t\t\"message\": \"\",\n" +
            "\t\t\"power_percent\": 100,\n" +
            "\t\t\"state\": \"before_transport\",\n" +
            "\t\t\"status\": \"RUNNING\",\n" +
            "\t\t\"take_out\": 255,\n" +
            "\t\t\"target\": \"goback\",\n" +
            "\t\t\"tasks\": 74,\n" +
            "\t\t\"total_distance\": 2534.50610351562\n" +
            "\t}\n" +
            "}";


    @Override
    public IBinder onBind(Intent intent) {
        return new HttpBinder();
    }

    HttpHandler httpHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //收到Service被杀死的广播，立即重启
        restartReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action) && action.equals("httpService_killed")) ;
                Intent sIntent = new Intent(HttpService.this, HttpService.class);
                startService(sIntent);
            }
        };
        registerReceiver(restartReceiver, new IntentFilter("httpService_killed"));
        httpHandler = new HttpHandler(HttpService.this);

        MyApplication.getInstance().httpHandler = httpHandler;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        httpHandler = new HttpHandler(HttpService.this);
        MyApplication.getInstance().httpHandler = httpHandler;
        super.onStart(intent, startId);
    }

    private void sendMessage(HttpMessage httpMessage) {
        HttpResponseTask httpResponseTask = new HttpResponseTask(MyApplication.getInstance().httpHandler, httpMessage);
        httpResponseTask.execute();
    }


    public static class HttpHandler extends Handler {

        final WeakReference<HttpService> mService;

        public HttpHandler(HttpService httpService) {
            mService = new WeakReference<HttpService>(httpService);
        }

        @Override
        public void handleMessage(Message msg) {
            HttpService service = mService.get();
            if (service == null) {
                super.handleMessage(msg);
                return;
            }
            super.handleMessage(msg);
            if (service != null) {
                String apiName = "";
                switch (msg.what) {
                    case Constants.MSG_GET_SUCCESS:
                        apiName = msg.getData().getString(Constants.URL_API_NAME);
//                        MyLogcat.showLogStatus("get return success" +msg.obj.toString());
//                        FL.e(Constants.show_log_status, "get return success" +msg.obj.toString());
                        if (msg.obj != null) {
                            if (!TextUtils.isEmpty(msg.obj.toString())) {
                                String strJson = "";
                                int code = -1;
                                try {
                                    String responseStr = msg.obj.toString();
                                    if (responseStr.startsWith("[")) {
                                        FL.e("------------[]  []----------------",responseStr);
                                        JSONArray responsArray=new JSONArray(responseStr);
                                    } else {
                                        JSONObject jsonObject = new JSONObject(responseStr);
                                        strJson = jsonObject.getString("data");
                                        code = jsonObject.getInt("code");
                                        if (code == 0) {
                                            netConnectFlag = true;
                                            if (!TextUtils.isEmpty(strJson)) {
//                                            MyLogcat.showLog("服务器连接成功，data有数据！");
//                                            FL.e(Constants.show_log, "服务器连接成功，data有数据！");
                                                switch (apiName) {
                                                    case "taskstatus":
                                                        if (strJson != null) {
//                                                        MyLogcat.showLogStatusState(strJson);
//                                                        FL.e(Constants.show_log_status_state, strJson);
                                                            RobotStatusResponse robotResponse = JsonUtils.fromJson(strJson
                                                                    , RobotStatusResponse.class);
                                                            if (robotResponse != null) {
//                                                            MyLogcat.showLogStatus(robotResponse.toString());
//                                                            FL.e(Constants.show_log_status, robotResponse.toString());
                                                                mIHttpServiceInterface.updateMessageListStatus(robotResponse, apiName);
                                                            } else {
//                                                            MyLogcat.showLogStatus("taskstatus  strJson为空");
//                                                            FL.e(Constants.show_log_status, "taskstatus  strJson为空");
                                                            }
                                                        } else {
//                                                        MyLogcat.showLogStatus("taskstatus  strJson为空");
//                                                        FL.e(Constants.show_log_status, "taskstatus  strJson为空");
                                                        }
                                                        break;
                                                    case "settarget_noback":
                                                        mIHttpServiceInterface.updateMessageList(apiName);
                                                        MyLogcat.showLog("发起任务成功！");
                                                        FL.e(Constants.show_log, "发起任务成功！");
                                                        break;
                                                    case "cancel":
                                                        MyLogcat.showLog("取消成功！");
                                                        FL.e(Constants.show_log, "取消成功！");
                                                        mIHttpServiceInterface.updateMessageList(apiName);
                                                        break;
                                                    case "forcecharge":
                                                        MyLogcat.showLog("回充电桩成功！");
                                                        FL.e(Constants.show_log, "回充电桩成功！");
                                                        mIHttpServiceInterface.updateMessageList(apiName);
                                                        break;

                                                    case "cruise_sound":
                                                        MyLogcat.showLog("获取语音列表成功！");
                                                        FL.e("----------巡游------------------", "获取语音列表成功！");
                                                        mIHttpServiceInterface.httpResponse(strJson, 0);
//                                                    EventBus.getDefault().post(new ResponseEvent(strJson,0));
                                                        break;
                                                    case "markers":
                                                        MyLogcat.showLog("获取点列表成功！");
                                                        FL.e("----------巡游------------------", "获取点表成功！");
                                                        mIHttpServiceInterface.httpResponse(strJson, 1);
//                                                    EventBus.getDefault().post(new ResponseEvent(strJson,1));
                                                        break;
                                                    case "start_cruise":
                                                        MyLogcat.showLog("巡游成功！");
                                                        FL.e("----------巡游------------------", "巡游成功！");
                                                        mIHttpServiceInterface.httpResponse(strJson, 2);
//                                                    EventBus.getDefault().post(new ResponseEvent(strJson,2));
                                                        break;

                                                    case "robot_no":
                                                        MyLogcat.showLog("获取机器人id");
                                                        FL.e("----------机器人id------------", "机器人id");
                                                        mIHttpServiceInterface.httpResponse(strJson, 3);
//                                                    EventBus.getDefault().post(new ResponseEvent(strJson,2));
                                                        break;

                                                }
                                            } else {
                                                MyLogcat.showLog("服务器连接成功，data没有数据！");
                                                FL.e(Constants.show_log, "服务器连接成功，data没有数据！");
                                            }


                                        } else {
                                            if (apiName.equals("cruise_sound") || apiName.equals("markers") || apiName.equals("start_cruise")) {
                                                FL.e("----------巡游------------------", "接口失败！");
                                                try {
                                                    mIHttpServiceInterface.httpResponse(jsonObject.getString("message"), -1);
                                                } catch (Exception e) {

                                                }
                                            }

                                            netConnectFlag = false;
                                            MyLogcat.showLog("服务器连接失败！");
                                            FL.e(Constants.show_log, "服务器连接失败！");
                                        }

                                    }
                                } catch (JSONException e) {
                                    netConnectFlag = false;
                                    e.printStackTrace();
                                }
                            } else {
                                MyLogcat.showLog("服务器返回数据为空");
                                FL.e(Constants.show_log, "服务器返回数据为空");
                            }
                        } else {
                            MyLogcat.showLog("服务器返回数据为空");
                            FL.e(Constants.show_log, "服务器返回数据为空");
                        }
                        break;
                    case Constants.MSG_GET_FAILED:
                        MyLogcat.showLog("get return failed");
                        FL.e(Constants.show_log, "get return failed");
//                        MyToast.showToast(mService.get(), apiName + "获取get失败");
                        break;
                    case Constants.MSG_POST_SUCCESS:
                        MyLogcat.showLog("post return success");
                        FL.e(Constants.show_log, "post return success");
//                        MyToast.showToast(service, apiName + "获取post成功");
                        break;
                    case Constants.MSG_POST_FAILED:
                        MyLogcat.showLog("post return failed");
                        FL.e(Constants.show_log, "post return failed");
//                        MyToast.showToast(service, apiName + "获取post失败");
                        break;
                    default:
                        break;

                }
            }
        }
    }

    ;


    public interface IHttpServiceInterface {
        void sendMessage(HttpMessage message);

        void updateMessageListStatus(RobotStatusResponse robotResponse, String apiName);

        void updateMessageList(String apiName);

        void httpResponse(String response, int type);
    }

    public class HttpBinder extends Binder {
        public HttpHandler myHandler;

        /**
         * 客户端向服务器端发送消息
         *
         * @param message
         */
        public void sendMessage(HttpMessage message) {
            HttpService.this.sendMessage(message);
        }

        public void addHttpServiceListener(IHttpServiceInterface mIHttpServiceInterface) {
            HttpService.this.mIHttpServiceInterface = mIHttpServiceInterface;
        }

        public boolean getNetConnectFlag() {
            return netConnectFlag;
        }
    }

    /**
     * 销毁Service
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        httpHandler.removeMessages(Constants.MSG_GET_SUCCESS);
        httpHandler.removeMessages(Constants.MSG_GET_FAILED);
        sendBroadcast(new Intent("httpService_killed"));
        unregisterReceiver(restartReceiver);
    }


}
