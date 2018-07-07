package com.yunji.deliveryman.db;

import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;

//任务
public class TaskSend implements Serializable
{

	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	public Integer id;

	@DatabaseField(columnName = "taskLayer")
	public Integer  taskLayer;//第几层

	@DatabaseField(columnName = "tablePointId")
	public Integer tablePointId;

	@DatabaseField(columnName = "taskState")
	public Integer  taskState;//-1已取消 0未到达，1已到达,请取餐，2离开，已完成

	@DatabaseField(columnName = "isCancel")
	public Boolean isCancel;

	@DatabaseField(columnName = "isShow")
	public Boolean isShow;

	public TaskSend() {
	}

	public TaskSend(Integer taskLayer, Integer tablePointId, Integer taskState, Boolean isCancel, Boolean isShow) {
		this.taskLayer = taskLayer;
		this.tablePointId = tablePointId;
		this.taskState = taskState;
		this.isCancel = isCancel;
		this.isShow = isShow;
	}

	@Override
	public String toString() {
		return "TaskSend{" +
				"id=" + id +
				", taskLayer=" + taskLayer +
				", tablePointId=" + tablePointId +
				", taskState=" + taskState +
				", isCancel=" + isCancel +
				", isShow=" + isShow +
				'}';
	}
}

