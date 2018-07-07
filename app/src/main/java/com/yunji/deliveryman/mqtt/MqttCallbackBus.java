package com.yunji.deliveryman.mqtt;

import android.util.Log;

import com.yunji.deliveryman.common.Constants;
import com.yunji.deliveryman.other.log.FL;
import com.yunji.deliveryman.utils.MyLogcat;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * 使用EventBus分发事件
 */
public class MqttCallbackBus implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        MyLogcat.showLog("denglixuan error:" + cause.getMessage());
        MyLogcat.showLog("denglixuan cause" + cause + " ");
        FL.e(Constants.show_log, "denglixuan error:" + cause.getMessage());
        FL.e(Constants.show_log, "denglixuan cause" + cause + " ");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        MyLogcat.showLog("denglixuan topic ====" + message.toString());
        FL.e(Constants.show_log, "denglixuan topic ====" + message.toString());
        EventBus.getDefault().post(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        MyLogcat.showLog("denglixuan topic ====" +"deliveryComplete");
    }

}
