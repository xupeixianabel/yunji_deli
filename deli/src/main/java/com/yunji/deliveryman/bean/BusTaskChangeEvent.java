package com.yunji.deliveryman.bean;


import com.yunji.deliveryman.bus.IBus;

/**
 * Created by wanglei on 2017/1/30.
 */

public class BusTaskChangeEvent implements IBus.IEvent {
    @Override
    public int getTag() {
        return 0;
    }

    public BusTaskChangeEvent(String layer) {
        this.layer = layer;
    }
    private String layer;

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }
}
