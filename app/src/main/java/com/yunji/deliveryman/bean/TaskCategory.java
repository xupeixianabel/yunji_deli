package com.yunji.deliveryman.bean;

import java.io.Serializable;

 
public class TaskCategory implements Serializable 
{

	private static final long serialVersionUID = 1L;
 	
	public int  id;//任务分类id
	public String  title;//任务分类title
	 
	public TaskCategory() {
	}

	public TaskCategory(String title) {
		super();
		this.title = title;
	}
 
}

