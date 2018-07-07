package com.yunji.deliveryman.bus;

import java.io.Serializable;

public class ResponseEvent  implements Serializable {
    private static final long serialVersionUID = 1L;
    String response;
    int type;//0获取语音列表，1，获取点列表,2,巡游接口回调

    public ResponseEvent(String response, int type) {
        this.response = response;
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ResponseEvent{" +
                "response='" + response + '\'' +
                ", type='" + type + '\'' +
//                ", floor='" + floor + '\'' +
                '}';
    }
}
