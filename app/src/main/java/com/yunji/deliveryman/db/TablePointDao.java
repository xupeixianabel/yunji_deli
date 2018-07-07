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
 * 封装TablePoint表的操作
 */
public class TablePointDao {

	private Dao<TablePoint, Integer> mPointDao;

	public TablePointDao(Context context){
		try {
			
			this.mPointDao = DatabaseHelper.getHelper(context).getDao(TablePoint.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入数据
	 */
	public void add(TablePoint mPoint)
    {  
        try  
        {
			mPointDao.create(mPoint);
        } catch (Exception e)  
        {  
        	e.printStackTrace(); 
        }
    }
	
	public void update(TablePoint mPoint)
    {  
        try  
        {
			mPointDao.update(mPoint);
        } catch (Exception e)  
        {  
        	e.printStackTrace(); 
        }
    }

	public List<TablePoint> queryByTablePointName(String tableName){
		List<TablePoint> list = new ArrayList<TablePoint>();
		try {
			QueryBuilder<TablePoint, Integer> builder = mPointDao.queryBuilder();
			Where<TablePoint, Integer> where = builder.where();
			where.eq("tableName",tableName);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TablePoint> queryByTablePointPoint(String tablePoint){
		List<TablePoint> list = new ArrayList<TablePoint>();
		try {
			QueryBuilder<TablePoint, Integer> builder = mPointDao.queryBuilder();
			Where<TablePoint, Integer> where = builder.where();
			where.eq("tablePoint",tablePoint);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TablePoint> queryByTablePointId(int id){
		List<TablePoint> list = new ArrayList<TablePoint>();
		try {
			QueryBuilder<TablePoint, Integer> builder = mPointDao.queryBuilder();
			Where<TablePoint, Integer> where = builder.where();
			where.eq("id",id);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TablePoint> queryAllPointByPointType(int pointType){
		List<TablePoint> list = new ArrayList<TablePoint>();
		try {

			QueryBuilder<TablePoint, Integer> builder = mPointDao.queryBuilder();
			Where<TablePoint, Integer> where = builder.where();
			where.eq("pointType",pointType);
			builder.setWhere(where);
			if(builder.query()!=null){
				list=builder.query();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<TablePoint> queryAllPoint(){
		List<TablePoint> list = new ArrayList<TablePoint>();
		try {

			list = mPointDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void deleteByTablePoint(int id){
		try {
			DeleteBuilder<TablePoint, Integer> builder = mPointDao.deleteBuilder();
			Where<TablePoint, Integer> where = builder.where();
			where.eq("id",id);
//			where.and();
//			where.eq("isDel",false);
			builder.setWhere(where);

			builder.delete();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除所有Point
	 */
	public void deleteAllTablePoint(){
		
		try {
			DeleteBuilder<TablePoint, Integer> builder = mPointDao.deleteBuilder();
			builder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
