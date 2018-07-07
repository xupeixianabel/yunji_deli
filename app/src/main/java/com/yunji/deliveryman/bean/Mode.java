package com.yunji.deliveryman.bean;

import java.io.Serializable;

 
public class Mode implements Serializable
{
	 
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 	
	public String name;//
	public int imgId;
	public Mode() {
	}

	public Mode(String name, int imgId) {
		super();
		this.name = name;
		this.imgId = imgId;
	}

	 
	
}

