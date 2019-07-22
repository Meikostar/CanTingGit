package com.zhongchuang.canting.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.utils.EaseCommonUtils;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.GameDownBean;
import com.zhongchuang.canting.utils.LogUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DemoDBManager {
    static private DemoDBManager dbMgr = new DemoDBManager();
    private DbOpenHelper dbHelper;
    
    private DemoDBManager(){
        dbHelper = DbOpenHelper.getInstance(CanTingAppLication.getInstance().getApplicationContext());
    }
    
    public static synchronized DemoDBManager getInstance(){
        if(dbMgr == null){
            dbMgr = new DemoDBManager();
        }
        return dbMgr;
    }
    
    /**
     * save contact list
     * 
     * @param contactList
     */
    synchronized public void saveContactList(List<EaseUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db.isOpen()) {

            db.delete(UserDao.TABLE_NAME, null, null);
            for (EaseUser user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
                if(user.getNickname() != null)
                    values.put(UserDao.COLUMN_NAME_NICK, user.getNickname());
                if(user.getAvatar() != null)
                    values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }

    }

    /**
     * get contact list
     * 
     * @return
     */
    synchronized public Map<String, EaseUser> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, EaseUser> users = new Hashtable<String, EaseUser>();

        if (db.isOpen()) {
            LogUtil.d("查询数据库是打开的");
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                EaseUser user = new EaseUser(username);
                user.setNickname(nick);
                user.setAvatar(avatar);
//                LogUtil.d("查询的数据是name="+username+",,nick="+nick+",,,avatar="+avatar);
                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)
                        || username.equals(Constant.CHAT_ROOM)|| username.equals(Constant.CHAT_ROBOT)) {
                        user.setInitialLetter("");
                } else {
                    EaseCommonUtils.setUserInitialLetter(user);
                }
                users.put(username, user);
            }
            cursor.close();
        }
        LogUtil.d("查询数据库操作结束");
        return users;
    }
    
    /**
     * delete a contact
     * @param username
     */
    synchronized public void deleteContact(String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }
    
    /**
     * save a contact
     * @param user
     */
    synchronized public void saveContact(EaseUser user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
        if(user.getNickname() != null)
            values.put(UserDao.COLUMN_NAME_NICK, user.getNickname());
        if(user.getAvatar() != null)
            values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        if(db.isOpen()){
            db.replace(UserDao.TABLE_NAME, null, values);
        }
    }
    
    public void setDisabledGroups(List<String> groups){
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }
    
    public List<String> getDisabledGroups(){
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }
    
    public void setDisabledIds(List<String> ids){
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }
    
    public List<String> getDisabledIds(){
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }
    
    synchronized private void setList(String column, List<String> strList){
        StringBuilder strBuilder = new StringBuilder();
        
        for(String hxid:strList){
            strBuilder.append(hxid).append("$");
        }
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null,null);
        }
    }
    
    synchronized private List<String> getList(String column){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME,null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }
        
        cursor.close();
        
        String[] array = strVal.split("$");
        
        if(array.length > 0){
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, array);
            return list;
        }
        
        return null;
    }


    /**
     * 获得已安装游戏列表
     * @return
     */
    synchronized public List<GameDownBean.DataBean> getGameList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<GameDownBean.DataBean> list = new ArrayList<>();

        if (db.isOpen()) {

            Cursor cursor = db.rawQuery("select * from " +  "game" /* + " desc" */, null);
            while (cursor.moveToNext()) {
                String gameLogo = cursor.getString(cursor.getColumnIndex("gamelogo"));
                String gameName = cursor.getString(cursor.getColumnIndex("gamename"));
                String gameDesc = cursor.getString(cursor.getColumnIndex("gamedesc"));
                String gamPackName = cursor.getString(cursor.getColumnIndex("gamepackname"));
                String gameId = cursor.getString(cursor.getColumnIndex("gameid"));
                GameDownBean.DataBean user = new GameDownBean.DataBean();
                user.setGameId(gameId);
                user.setGameLogo(gameLogo);
                user.setGameName(gameName);
                user.setGameDescription(gameDesc);
                user.setGamePackage(gamPackName);

                list.add(user);
            }
            cursor.close();
        }
        LogUtil.d("查询数据库操作结束");
        return list;
    }

    /**
     * delete a game
     * @param gameid
     */
    synchronized public void deleteGame(String gameid){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("game", "gameid" + " = ?", new String[]{gameid});
        }
    }

    /**
     * save a game
     * @param user
     */
    synchronized public void saveGame(GameDownBean.DataBean user){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( "gamelogo", user.getGameLogo());
        values.put( "gamename", user.getGameName());
        values.put( "gamedesc", user.getGameDescription());
        values.put( "gamepackname", user.getGamePackage());
        values.put( "gameid", user.getGameId());

        if(db.isOpen()){
            db.replace("game", null, values);
        }
    }

    
    synchronized public void closeDB(){
        if(dbHelper != null){
            dbHelper.closeDB();
        }
        dbMgr = null;
    }
    

}
