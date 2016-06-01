package com.buycolle.aicang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.buycolle.aicang.bean.SearchEntity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joe on 15/11/13.
 */
public class SearchKeybaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "sqlite-search.db";
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private SearchKeybaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SearchEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, SearchEntity.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUserTable() {
        try {
            TableUtils.dropTable(getConnectionSource(), SearchEntity.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUserTable() {
        try {
            TableUtils.createTable(getConnectionSource(), SearchEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SearchKeybaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized SearchKeybaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (SearchKeybaseHelper.class) {
                if (instance == null)
                    instance = new SearchKeybaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
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
}
