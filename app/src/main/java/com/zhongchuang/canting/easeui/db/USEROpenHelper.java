package com.zhongchuang.canting.easeui.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.bean.USER;


/**
 * Created by syj on 2016/12/1.
 * 个人数据库
 */
public class USEROpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "concrete_user.db";
    private static final int DATABASE_VERSION = 1;
    private static ConnectionSource connectionSource;
    private Dao<USER, Integer> mUserInfoDao = null;

    private static USEROpenHelper mInstance;

    public USEROpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        connectionSource = new AndroidConnectionSource(this);
    }

    public static USEROpenHelper getInstance() {
        if (mInstance == null) {
            // BaseApplication替换成自己项目的Application子类实例即可
            mInstance = new USEROpenHelper(CanTingAppLication.getInstance());
        }

        return mInstance;
    }


    public static void clearDate() {
        try {
            TableUtils.clearTable(connectionSource, USER.class);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, USER.class);

        } catch (SQLException e) {
            Log.e(USEROpenHelper.class.getName(), "Unable to create datbases", e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int
            oldVer, int newVer) {
        try {
            TableUtils.dropTable(connectionSource, USER.class, true);

            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(USEROpenHelper.class.getName(),
                    "Unable to upgrade database from version " + oldVer + " to new "
                            + newVer, e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<USER, Integer> getUserDao() throws SQLException {
        if (mUserInfoDao == null) {
            try {
                mUserInfoDao = getDao(USER.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return mUserInfoDao;
    }
}
