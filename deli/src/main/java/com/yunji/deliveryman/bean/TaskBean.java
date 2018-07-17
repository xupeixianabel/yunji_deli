package com.yunji.deliveryman.bean;

import java.io.Serializable;

public class TaskBean implements Serializable{
    private String layer;//F1,F2,F3,F4
    private String district;
    private int districtPosition;
    private boolean isChecked;//取消任务时用到的，是否选择了取消。

    public TaskBean() {
    }
    public TaskBean(String layer, String district,int districtPosition) {
        this.layer = layer;
        this.district = district;
        this.districtPosition = districtPosition;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
    public int getDistrictPosition() {
        return districtPosition;
    }

    public void setDistrictPosition(int districtPosition) {
        this.districtPosition = districtPosition;
    }

}
