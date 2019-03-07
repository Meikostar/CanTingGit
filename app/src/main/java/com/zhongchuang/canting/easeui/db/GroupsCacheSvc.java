package com.zhongchuang.canting.easeui.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.zhongchuang.canting.easeui.bean.GROUPS;
import com.zhongchuang.canting.install.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syj on 2016/12/7.
 * 群组数据库操作
 */
public class GroupsCacheSvc {

    public static GROUPS getByChatUserName(String chatUserName) {
        Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();
        try {
            GROUPS model = dao.queryBuilder().where().eq("easemob_group_id", chatUserName)
                    .queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GROUPS getById(int id) {
        Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();
        try {
            GROUPS model = dao.queryBuilder().where().eq("guid", id).queryForFirst();
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean createOrUpdate(int guid, String groupname, String easemob_group_id,
                                         String gid, ArrayList<String> group_img, int is_disturb) {
        try {
            Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();

            GROUPS user = getByChatUserName(easemob_group_id);

//			if (!StringUtils.isNullOrEmpty(avatarUrl)){
//				avatarUrl = ImgSize.GetThumbnail(avatarUrl, ImgSize.ThumbMod_Crop, 100, 100);
//			}

            int changedLines = 0;
            if (user == null) {
                user = new GROUPS();
                user.setGuid(guid);
                user.setGroupname(groupname);
                user.setEasemob_group_id(easemob_group_id);
                user.setGid(gid);
                user.setGroup_img_str(Utils.getGroupImgStr(group_img));
                changedLines = dao.create(user);
            } else {
                user.setGuid(guid);
                user.setGroupname(groupname);
                user.setGid(gid);
                user.setGroup_img(group_img);
                user.setIs_disturb(is_disturb);
                changedLines = dao.update(user);
            }

            if (changedLines > 0) {
                Log.i("GroupsCacheSvc", "操作成功~");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("GroupsCacheSvc", "操作异常~");
        }
        return false;
    }

    public static boolean createOrUpdate(GROUPS model) {

        if (model == null) return false;
        try {
            Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();
            GROUPS user;
            user = getByChatUserName(model.getEasemob_group_id());
            int changedLines = 0;
            if (user == null) {
                changedLines = dao.create(model);
            } else {
                changedLines = dao.update(model);
            }
            if (changedLines > 0) {
                Log.i("GroupsCacheSvc", "操作成功~");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("GroupsCacheSvc", "操作异常~");
        }

        return false;
    }

    public static List<GROUPS> GroupAll() {
        Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();
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
        List<GROUPS> images = GroupAll();
        Dao<GROUPS, Integer> dao = GROUPSOpenHelper.getInstance().getGroupsDao();
        for (GROUPS img : images) {
            try {
                dao.delete(img);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
