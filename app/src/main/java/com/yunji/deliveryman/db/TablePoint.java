package com.yunji.deliveryman.db;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class TablePoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    public Integer id;

    @DatabaseField(columnName = "tableName")
    public String tableName;
    @DatabaseField(columnName = "tablePoint")
    public String tablePoint;

    @DatabaseField(columnName = "pointType")//0是餐桌  1是厨房 2是充电桩
    public Integer pointType;

    public TablePoint() {
    }

    public TablePoint(String tableName, String tablePoint, Integer pointType) {
        this.tableName = tableName;
        this.tablePoint = tablePoint;
        this.pointType = pointType;
    }

    @Override
    public String toString() {
        return "TablePoint{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", tablePoint='" + tablePoint + '\'' +
                ", pointType=" + pointType +
                '}';
    }
}
