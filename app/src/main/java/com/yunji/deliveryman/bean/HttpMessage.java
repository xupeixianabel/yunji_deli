package com.yunji.deliveryman.bean;

import java.io.Serializable;

public class HttpMessage implements Serializable{

    public String requestType;
    public String hostName;
    public String apiName;
    public String message;


    public HttpMessage() {
    }

    @Override
    public String toString() {
        return "HttpMessage{" +
                "requestType=" + requestType +
                ", hostName='" + hostName + '\'' +
                ", apiName='" + apiName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
