package com.buycolle.aicang.dao;

import android.content.Context;

import com.buycolle.aicang.bean.SearchEntity;
import com.buycolle.aicang.db.SearchKeybaseHelper;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by joe on 15/11/13.
 */
public class SearchKeyDao {
    private Context context;
    private Dao<SearchEntity, Integer> cityDaoOpe;
    private SearchKeybaseHelper helper;

    public SearchKeyDao(Context context) {
        this.context = context;
        try {
            helper = SearchKeybaseHelper.getHelper(context);
            cityDaoOpe = helper.getDao(SearchEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //保存全部数据
    public void saveAllData(ArrayList<SearchEntity> cityEntities) {
        try {
            AndroidDatabaseConnection db = new AndroidDatabaseConnection(
                    helper.getWritableDatabase(), true);
            db.setAutoCommit(false);
            for (SearchEntity city : cityEntities) {
                cityDaoOpe.createOrUpdate(city);
            }
            db.commit(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //清理所有数据
    public void clearAll() {
        helper.dropUserTable();
        helper.createUserTable();
    }

    //获取所有数据
    public ArrayList<SearchEntity> getAllData() {
        try {
            return (ArrayList<SearchEntity>) cityDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存一个数据，如果多了就删除
     *
     * @param type
     */
    public void add(SearchEntity entity, String type) {
        ArrayList<SearchEntity> searchEntities = getAllByType(type);
        try {
            if (searchEntities.size() >= 10) {
                cityDaoOpe.delete(searchEntities.get(searchEntities.size() - 1));
            }

            for (SearchEntity searchEntity : searchEntities) {
                if (searchEntity.getKeyword().equals(entity.getKeyword())) {
                    cityDaoOpe.delete(searchEntity);
                }
            }
            AndroidDatabaseConnection db = new AndroidDatabaseConnection(
                    helper.getWritableDatabase(), true);
            db.setAutoCommit(false);
            cityDaoOpe.createOrUpdate(entity);
            db.commit(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //获取所有对应的列表
    public ArrayList<SearchEntity> getAllByType(String type) {
        try {
            return (ArrayList<SearchEntity>) cityDaoOpe
                    .queryBuilder().orderBy("id", false).distinct().selectColumns("keyword").where().eq("type", type).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<SearchEntity> getAll(String type) {
        try {
            return (ArrayList<SearchEntity>) cityDaoOpe
                    .queryBuilder().orderBy("id", false).where().eq("type", type).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除对应的保存信息
     *
     * @param type
     */
    public void deleteAllByType(String type) {
        ArrayList<SearchEntity> searchEntities = getAll(type);
        try {
            cityDaoOpe.delete(searchEntities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        helper.close();
    }


}
