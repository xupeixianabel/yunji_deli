package com.yunji.deliveryman.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import android.text.TextUtils;

import com.yunji.deliveryman.MyApplication;
import com.yunji.deliveryman.bean.HttpMessage;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.net.HttpResponseTask;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;
import com.yunji.deliveryman.utils.Util;

public class getStatusService extends Service {

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private BroadcastReceiver restartReceiver;
    private int MSG_WORK = 333;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        restartReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.isEmpty(action) && action.equals("getStatusService_killed")) ;
                Intent sIntent = new Intent(getStatusService.this, getStatusService.class);
                startService(sIntent);
            }
        };
        registerReceiver(restartReceiver, new IntentFilter("getStatusService_killed"));

        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                HttpMessage message = new HttpMessage();
                message.requestType = "GET";
                message.apiName = "taskstatus";
                message.message = "";
                HttpResponseTask httpResponseTask = new HttpResponseTask(MyApplication.getInstance().httpHandler, message);
                if (Util.isNetworkAvailable(getApplicationContext())) {
                    httpResponseTask.execute();
                } else {
                    MyLogcat.showLogStatus("当前网络已经断开！");
                    FL.e(Constants.show_log_status, "当前网络已经断开！");
                }

//                MyLogcat.showLogStatus("5555555555轮询网络！");
//                FL.e(Constants.show_log_status, "5555555555轮询网络！");
                mHandler.sendEmptyMessageDelayed(MSG_WORK, 1000);
            }
        };
        mHandler.sendEmptyMessage(MSG_WORK);    //鍙戦�佸伐浣滄秷鎭�,瑙﹀彂娑堟伅Handler鎵ц
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        sendBroadcast(new Intent("getStatusService_killed"));
        unregisterReceiver(restartReceiver);
    }
}
