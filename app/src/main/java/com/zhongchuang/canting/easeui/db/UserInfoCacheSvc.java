package com.zhongchuang.canting.easeui.db;

import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.zhongchuang.canting.easeui.bean.USER;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by syj on 2016/12/1.
 * 个人数据库操作
 */
public class UserInfoCacheSvc {

    public static USER getByChatUserName(String chatUserName) {
        Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();
        try {
            USER model = dao.queryBuilder().where().eq("hx_username", chatUserName).queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static USER getById(String id) {
        Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();
        try {
            USER model = dao.queryBuilder().where().eq("user_id", id)
                    .queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createOrUpdate(int user_id, String chatUserName, String avatarUrl,
                                         String easemob, String friend_nickname) {
        try {
            Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();

            USER user = getByChatUserName(easemob);

//			if (!StringUtils.isNullOrEmpty(avatarUrl)){
//				avatarUrl = ImgSize.GetThumbnail(avatarUrl, ImgSize.ThumbMod_Crop, 100, 100);
//			}

            int changedLines = 0;
            if (user == null) {
                user = new USER();
                user.user_id = user_id;
                user.english_name = chatUserName;
                user.user_avatar = avatarUrl;
                user.hx_username = easemob;
                user.name = friend_nickname;
                changedLines = dao.create(user);
            } else {
                user.user_id = user_id;
                user.english_name = chatUserName;
                user.user_avatar = avatarUrl;
                user.hx_username = easemob;
                user.name = friend_nickname;
                changedLines = dao.update(user);
            }

            if (changedLines > 0) {
                Log.i("UserInfoCacheSvc", "操作成功~");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserInfoCacheSvc", "操作异常~");
        }
        return false;
    }


    public static boolean DeleteById(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();
        try {
            USER model = dao.queryBuilder().where().eq("user_id", id).queryForFirst();
            int changedLines = 0;
            if (model != null) {
                changedLines = dao.delete(model);
            }
            if (changedLines > 0) {
                Log.i("UserInfoCacheSvc", "操作成功~");
                return true;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("UserInfoCacheSvc", "操作异常~");
        }
        return false;
    }

    public static List<USER> UserAll() {
        Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除所有数据
     */
    public static void removeAll() {
        List<USER> images = UserAll();
        Dao<USER, Integer> dao = USEROpenHelper.getInstance().getUserDao();
        for (USER img : images) {
            try {
                dao.delete(img);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
