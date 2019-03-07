package com.zhongchuang.canting.easeui.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.bean.GROUPS;


/**
 * Created by syj on 2016/12/7.
 * 群组数据库
 */
public class GROUPSOpenHelper  extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "concrete_groups.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<GROUPS, Integer> mGroupsDao = null;

    private static GROUPSOpenHelper mInstance;

    public GROUPSOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static GROUPSOpenHelper getInstance(){
        if (mInstance == null) {
            // BaseApplication替换成自己项目的Application子类实例即可
            mInstance = new GROUPSOpenHelper(CanTingAppLication.getInstance());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, GROUPS.class);
        } catch (SQLException e) {
            Log.e(GROUPSOpenHelper.class.getName(), "Unable to create datbases", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, GROUPS.class, true);

            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(GROUPSOpenHelper.class.getName(),
                    "Unable to upgrade database from version " + oldVer+ " to new "
                            + newVer, e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<GROUPS,Integer> getGroupsDao() throws SQLException {
        if(mGroupsDao == null){
            try {
                mGroupsDao = getDao(GROUPS.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return mGroupsDao;
    }
}
