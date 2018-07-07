package com.yunji.deliveryman.bean;

import java.io.Serializable;


public class Point implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String marker_name;
	private int key;
	private int floor;

	public String getMarker_name() {
		return marker_name;
	}

	public void setMarker_name(String marker_name) {
		this.marker_name = marker_name;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	@Override
	public String toString() {
		return "Point{" +
				"marker_name='" + marker_name + '\'' +
				", key='" + key + '\'' +
				", floor='" + floor + '\'' +
				'}';
	}
}

