package com.yunji.deliveryman.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.yunji.deliveryman.common.Constants;

/**
 * 封装DAO类 用于操作数据库
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "deveriyman.db";
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    private Context context;

    private DatabaseHelper(Context context)//数据库不要修改路径，被删除后，数据无法创建
    {
        super(context, TABLE_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TablePoint.class);
            TableUtils.createTable(connectionSource, TaskSend.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        initData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 根据传入参数，获得具体Dao
     *
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class clazz) {
        Dao dao = null;
        String className = clazz.getName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            try {
                dao = super.getDao(clazz);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

    /**
     * @Todo 初始化数据库内容
     */
    public void initData() {
        TablePointDao tablePointDao = new TablePointDao(context);
        TablePoint mTablePoint = new TablePoint("A区", "A", 0);
        tablePointDao.add(mTablePoint);
        mTablePoint = new TablePoint("B区", "B", 0);
        tablePointDao.add(mTablePoint);
        mTablePoint = new TablePoint("C区", "C", 0);
        tablePointDao.add(mTablePoint);

        mTablePoint = new TablePoint("D区", "D", 0);
        tablePointDao.add(mTablePoint);

        mTablePoint = new TablePoint("802厨房", "E", 1);
        tablePointDao.add(mTablePoint);

        mTablePoint = new TablePoint("802充电桩", "F", 2);
        tablePointDao.add(mTablePoint);

/*		 TablePointDao tablePointDao = new TablePointDao(context);
		 TablePoint mTablePoint = new TablePoint("803餐桌","803",0);
		 tablePointDao.add(mTablePoint);
		 mTablePoint = new TablePoint("7280餐桌","7280",0);
		 tablePointDao.add(mTablePoint);
		mTablePoint = new TablePoint("7281餐桌","7281",0);
		tablePointDao.add(mTablePoint);

		mTablePoint = new TablePoint("7211餐桌","7211",0);
		tablePointDao.add(mTablePoint);
		mTablePoint = new TablePoint("7210餐桌","7210",0);
		tablePointDao.add(mTablePoint);



		mTablePoint = new TablePoint("802厨房","802",1);
		tablePointDao.add(mTablePoint);

		mTablePoint = new TablePoint("802充电桩","801",2);
		tablePointDao.add(mTablePoint);*/
    }

}
