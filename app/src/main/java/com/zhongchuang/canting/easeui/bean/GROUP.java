package com.zhongchuang.canting.easeui.bean;

import com.zhongchuang.canting.been.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mykar on 17/6/5.
 */
public class GROUP extends BaseResponse implements Serializable  {
//    "group_id": "10",
//            "user_id": "34",
//            "group_name": "群名",
//            "group_time": "1496385098",
//            "group_explain": "群说明",
//            "group_hx": "2147483647",
//            "user_num": "1",#群成员数
//    "group_type": "0",#0公开群1非公开
//    "name": "",#群主中文名
//    "english_name": "",#群主英文名
//    "user_avatar": "",#群主头像
//    "hx_username": "1873935517234"#群主环信名
public int group_id;
    public int user_id;
    public String group_name;
    public String groupname;
    public String group_hx;
    public long group_time;
    public String group_explain;
    public int user_num;
    public int group_type;
    public String name;
    public String english_name;
    public String user_avatar;
    public boolean admin;
    public String hx_username;
    public String groupid;
    public List<String> user_avatars;
    public List<GROUP> data;
    public List<String> headimage;



    public String getGroup_explain() {
        return group_explain;
    }

    public void setGroup_explain(String group_explain) {
        this.group_explain = group_explain;
    }

    public List<String> getUser_avatars() {
        return user_avatars;
    }

    public void setUser_avatars(List<String> user_avatars) {
        this.user_avatars = user_avatars;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_hx() {
        return group_hx;
    }

    public void setGroup_hx(String group_hx) {
        this.group_hx = group_hx;
    }

    public long getGroup_time() {
        return group_time;
    }

    public void setGroup_time(long group_time) {
        this.group_time = group_time;
    }



    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public int getGroup_type() {
        return group_type;
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }
}
