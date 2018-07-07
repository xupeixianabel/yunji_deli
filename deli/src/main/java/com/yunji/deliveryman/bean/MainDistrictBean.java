package com.yunji.deliveryman.bean;

public class MainDistrictBean {
    private int checkedId;
    private int unCheckedId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainDistrictBean() {
    }

    public MainDistrictBean(int checkedId, int unCheckedId, String name) {
        this.checkedId = checkedId;
        this.unCheckedId = unCheckedId;
        this.name=name;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

    public int getUnCheckedId() {
        return unCheckedId;
    }

    public void setUnCheckedId(int unCheckedId) {
        this.unCheckedId = unCheckedId;
    }
}
