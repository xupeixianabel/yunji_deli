package com.yunji.deliveryman.bean;

import java.io.Serializable;


public class Layer implements Serializable
{

/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	public String name;//
	public String name2;

	public Layer() {
	}

	public Layer(String name, String name2) {
		this.name = name;
		this.name2 = name2;

	}

	@Override
	public String toString() {
		return "Layer{" +
				"name='" + name + '\'' +
				", name2='" + name2 + '\''
				;
	}
}

