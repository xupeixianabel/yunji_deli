package com.yunji.deliveryman.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yunji.deliveryman.R;
import com.yunji.deliveryman.bean.FoodTable;
import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.mqtt.MqttManager;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MqttActivity extends AppCompatActivity {

    //public static final String URL = "tcp://192.168.0.104:1883";
    public static final String URL = "tcp://mqtt.yunjichina.com.cn:1883";

    private List<FoodTable> foodTableList = new ArrayList<>();

    private String userName = "";

    private String password = "";

    private String clientId = "";


    private String jsonStr = "{\n" +
            "\"tasks\": [\n" +
            "{ \"tableName\":\"7091桌\" , \"tablePoint\":\"7091\" },\n" +
            "{ \"tableName\":\"7092桌\" , \"tablePoint\":\"7092\" },\n" +
            "{ \"tableName\":\"7093桌\" , \"tablePoint\":\"7093\" }\n" +
            "]\n" +
            "}";



    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private String parseJSONWithJSONObject(String jsonData) {
        JSONObject jsonObject = null;
        String name = "";
        try {
            jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("tasks");
            for (int i = 0; i < jsonArray.length(); i++) {
                FoodTable foodTable = new FoodTable();
                foodTable.mTableName =
                        new JSONObject(jsonArray.get(i).toString()).getString("tableName");
                foodTableList.add(foodTable);
                MyLogcat.showLog("denglixuan jsonArray" + jsonArray.getString(i));
                MyLogcat.showLog("denglixuan tableName" + foodTable.mTableName+" ");
                FL.e(Constants.show_log, "denglixuan jsonArray" + jsonArray.getString(i));
                FL.e(Constants.show_log, "denglixuan tableName" + foodTable.mTableName+" ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (clientId.length() > 20) {
                            clientId = "";
                        }
                        clientId = clientId + "@@@" + System.currentTimeMillis();
                        boolean b = MqttManager.getInstance().creatConnect(URL, userName, password, clientId);

                        MyLogcat.showLog("isConnected: " + b + "");
                        FL.e(Constants.show_log, "isConnected: " + b + "");
                    }
                }).start();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MqttManager.getInstance().publish(Constants.topicCommand, 0, jsonStr.toString().getBytes());
                    }
                }).start();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MqttManager.getInstance().subscribe(Constants.topicNotification, 0);
                    }
                }).start();
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            MqttManager.getInstance().disConnect();
//                        } catch (MqttException e) {
//
//                        }
//                    }
//                }).start();

            }
        });
        EventBus.getDefault().register(this);
        initNotification();

    }

    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this);
    }

    private void startNotification(MqttMessage message) {


        Intent ngintent = new Intent();
        ngintent.setAction(Intent.ACTION_MAIN);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, ngintent, 0);

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("Mqtt服务");
        notificationBuilder.setContentText(message.toString());
        notificationBuilder.setContentIntent(contentIntent);
        notificationBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notificationBuilder.build());

    }


    /**
     * 订阅接收到的消息
     * 这里的Event类型可以根据需要自定义, 这里只做基础的演示
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MqttMessage message) {
        startNotification(message);
        parseJSONWithJSONObject(message.toString());
        MyLogcat.showLog("denglixuan message:" + message.toString());
        FL.e(Constants.show_log, "denglixuan message:" + message.toString());
        Toast.makeText(MqttActivity.this, "收到", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
