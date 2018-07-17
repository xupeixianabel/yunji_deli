package com.yunji.deliveryman.bean;

import java.io.Serializable;

public class MainLayerBean implements Serializable{
    private String layer;
    private String plateLayer;
    private int districtPosition;
    private boolean hasChoosed;

    public MainLayerBean() {
    }
    public MainLayerBean(String layer, String plateLayer, int districtPosition, boolean hasChoosed) {
        this.layer = layer;
        this.plateLayer = plateLayer;
        this.districtPosition = districtPosition;
        this.hasChoosed = hasChoosed;
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

    public boolean isHasChoosed() {
        return hasChoosed;
    }

    public void setHasChoosed(boolean hasChoosed) {
        this.hasChoosed = hasChoosed;
    }
}
