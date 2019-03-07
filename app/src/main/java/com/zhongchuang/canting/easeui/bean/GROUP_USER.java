package com.zhongchuang.canting.easeui.bean;




import java.io.Serializable;

/**
 * Created by syj on 2016/12/6.
 */
public class GROUP_USER implements Serializable {



    /**
     * guid : 31
     * gid : 9
     * user_id : 27
     * nickname :
     * user_type : 0
     * user_positioning : 0,0
     * is_positioning : 0
     * is_disturb : 0
     * is_map : 0
     * is_top : 0
     * add_time : 0
     * is_host : 1
     */

    private int guid;
    private int gid;
    private int user_id;
    private String nickname;
    private int user_type;
    private String user_positioning;
    private int is_positioning;
    private int is_disturb;
    private int is_map;
    private int is_top;
    private int add_time;
    private int is_host;
    private USER userinfo;
    private int is_group_user;

    public int getIs_group_user() {
        return is_group_user;
    }

    public void setIs_group_user(int is_group_user) {
        this.is_group_user = is_group_user;
    }

    public USER getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(USER userinfo) {
        this.userinfo = userinfo;
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_positioning() {
        return user_positioning;
    }

    public void setUser_positioning(String user_positioning) {
        this.user_positioning = user_positioning;
    }

    public int getIs_positioning() {
        return is_positioning;
    }

    public void setIs_positioning(int is_positioning) {
        this.is_positioning = is_positioning;
    }

    public int getIs_disturb() {
        return is_disturb;
    }

    public void setIs_disturb(int is_disturb) {
        this.is_disturb = is_disturb;
    }

    public int getIs_map() {
        return is_map;
    }

    public void setIs_map(int is_map) {
        this.is_map = is_map;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getIs_host() {
        return is_host;
    }

    public void setIs_host(int is_host) {
        this.is_host = is_host;
    }
}
