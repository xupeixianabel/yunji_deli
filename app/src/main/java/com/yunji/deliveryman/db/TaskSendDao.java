package com.yunji.deliveryman.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 封装TaskSend表的操作
 */
public class TaskSendDao {

	private Dao<TaskSend, Integer> taskSendDao;

	public TaskSendDao(Context context){
		try {
			
			this.taskSendDao = DatabaseHelper.getHelper(context).getDao(TaskSend.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入数据
	 */
	public void add(TaskSend mTaskSend)
    {  
        try  
        {
			taskSendDao.create(mTaskSend);
        } catch (Exception e)  
        {  
        	e.printStackTrace(); 
        }
    }
	
	public void update(TaskSend mTaskSend)
    {  
        try  
        {
			taskSendDao.update(mTaskSend);
        } catch (Exception e)  
        {  
        	e.printStackTrace(); 
        }
    }
	public List<TaskSend> queryByTaskLayer(int taskLayer){
		List<TaskSend> list = new ArrayList<TaskSend>();
		try {
			QueryBuilder<TaskSend, Integer> builder = taskSendDao.queryBuilder();
			Where<TaskSend, Integer> where = builder.where();
			where.eq("taskLayer",taskLayer);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TaskSend> queryTaskSendByTaskSate(int taskState){
		List<TaskSend> list = new ArrayList<TaskSend>();
		try {
			QueryBuilder<TaskSend, Integer> builder = taskSendDao.queryBuilder();
			Where<TaskSend, Integer> where = builder.where();
			where.eq("taskState",taskState);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TaskSend> queryByTablePointId(int tablePointId){
		List<TaskSend> list = new ArrayList<TaskSend>();
		try {
			QueryBuilder<TaskSend, Integer> builder = taskSendDao.queryBuilder();
			Where<TaskSend, Integer> where = builder.where();
			where.eq("tablePointId",tablePointId);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query(); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TaskSend> queryAllTaskSend(){
		List<TaskSend> list = new ArrayList<TaskSend>();
		try {
			list = taskSendDao.queryForAll();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public void deleteByTaskSendTaskLayer(int taskLayer){
		try {
			DeleteBuilder<TaskSend, Integer> builder = taskSendDao.deleteBuilder();
			Where<TaskSend, Integer> where = builder.where();
			where.eq("taskLayer",taskLayer);
//			where.and();
//			where.eq("isDel",false);
			builder.setWhere(where);
			 
			builder.delete(); 
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除所有TaskSend
	 */
	public void deleteAllTaskSend(){
		
		try {
			DeleteBuilder<TaskSend, Integer> builder = taskSendDao.deleteBuilder();
			builder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
