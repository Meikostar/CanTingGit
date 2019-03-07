package com.zhongchuang.canting.easeui.bean;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by syj on 2016/11/24.
 */
public class USERLIST implements Serializable {


    private ArrayList<USER> user_info;
    private ArrayList<USER> user_list;
    private ArrayList<USER> list;
    private ArrayList<USER> friend_requests_list;
    private ArrayList<USER> friends;
    private ArrayList<GROUPS> group;
    private ArrayList<USER> friends_list;

    public ArrayList<USER> getList() {
        return list;
    }

    public void setList(ArrayList<USER> list) {
        this.list = list;
    }

    public ArrayList<USER> getUser_list() {
        return user_list;
    }

    public void setUser_list(ArrayList<USER> user_list) {
        this.user_list = user_list;
    }

    public ArrayList<USER> getFriends_list() {
        return friends_list;
    }

    public void setFriends_list(ArrayList<USER> friends_list) {
        this.friends_list = friends_list;
    }

    public ArrayList<GROUPS> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<GROUPS> group) {
        this.group = group;
    }

    public ArrayList<USER> getFriend_requests_list() {
        return friend_requests_list;
    }

    public void setFriend_requests_list(ArrayList<USER> friend_requests_list) {
        this.friend_requests_list = friend_requests_list;
    }

    public ArrayList<USER> getUser_info() {
        return user_info;
    }

    public void setUser_info(ArrayList<USER> user_info) {
        this.user_info = user_info;
    }

    public ArrayList<USER> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<USER> friends) {
        this.friends = friends;
    }
}
