package com.yunji.deliveryman.bean;

import java.io.Serializable;


public class FoodTable implements Serializable
{

/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String layerName;//
	public String layerTableName;
	public String mTableName;//
	public FoodTable() {
	}

	public FoodTable(String layerName, String layerTableName, String mTableName) {
		this.layerName = layerName;
		this.layerTableName = layerTableName;
		this.mTableName = mTableName;
	}

	@Override
	public String toString() {
		return "FoodTable{" +
				"layerName='" + layerName + '\'' +
				", layerTableName='" + layerTableName + '\'' +
				", mTableName='" + mTableName + '\'' +
				'}';
	}
}

