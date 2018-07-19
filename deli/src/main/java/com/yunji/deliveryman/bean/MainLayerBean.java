package com.yunji.deliveryman.bean;

import java.io.Serializable;

public class MainLayerBean implements Serializable{
    private String layer;
    private String plateLayer;
    private int districtPosition;

    private int deliveryState;//0未放置，1放置，未到达，2放置已完成，3放置已取消。
    public MainLayerBean() {
    }
    public MainLayerBean(String layer, String plateLayer, int districtPosition, int deliveryState) {
        this.layer = layer;
        this.plateLayer = plateLayer;
        this.districtPosition = districtPosition;
        this.deliveryState = deliveryState;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getPlateLayer() {
        return plateLayer;
    }

    public void setPlateLayer(String plateLayer) {
        this.plateLayer = plateLayer;
    }

    public int getDistrictPosition() {
        return districtPosition;
    }

    public void setDistrictPosition(int districtPosition) {
        this.districtPosition = districtPosition;
    }

    public int getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(int deliveryState) {
        this.deliveryState = deliveryState;
    }
}
