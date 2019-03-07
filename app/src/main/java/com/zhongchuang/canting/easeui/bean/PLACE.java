package com.zhongchuang.canting.easeui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by syj on 2016/12/6.
 */
public class PLACE implements Serializable {

    /**
     * pid : 1
     * place_name : 工地
     * place_positioning :
     * gid : 1
     * place_type : 1
     */
//    "group_id": "3",
//            "user_id": "34",
//            "group_name": "群名",
//            "group_time": "1496390210",
//            "group_explain": "群说明",
//            "group_hx": "17817142362113",
//            "user_num": "3",
//            "group_type": "0",#0公开1非公开
//    "group_user_id": "3",
//            "add_time": "1496390210",
//            "status": "1",#0成员1群主
//    "nickname": "",#群昵称
//    "group_sys": "0",#0默认接收1不接收群消息
//    "user_avatars": [#头像
//    "",
//            "http://120.76.129.214/eqc/Uploads/app/img/2017-05-31/592e66a9dc6c4.jpg",
//            ""
//            ]
//            "users": [
//    {
//        "user_id": "34",
//            "nickname": "",
//            "hx_username": "1873935517234",
//            "name": "",
//            "user_avatar":''//头像
//        "english_name": ""
//    },

    private int approval;
    private int group_sys;
    private int status;
    private int applicate_status;
    private int group_user_id;
    private int group_type;
    private int user_num;
    private int user_id;
    private String group_id;
    private String nickname;
    private String name;
    private String english_name;
    private String hx_username;
    private String group_name;
    private long group_time;
    private long add_time;
    private String group_explain;
    private String group_hx;

    private List<String> user_avatar;
    private List<USER_AVATAR> users;

    public int getApplicate_status() {
        return applicate_status;
    }

    public void setApplicate_status(int applicate_status) {
        this.applicate_status = applicate_status;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public List<String> getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(List<String> user_avatar) {
        this.user_avatar = user_avatar;
    }

    public List<USER_AVATAR> getUsers() {
        return users;
    }

    public void setUsers(List<USER_AVATAR> users) {
        this.users = users;
    }

    public int getGroup_sys() {
        return group_sys;
    }

    public void setGroup_sys(int group_sys) {
        this.group_sys = group_sys;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGroup_type() {
        return group_type;
    }

    public void setGroup_type(int group_type) {
        this.group_type = group_type;
    }

    public int getGroup_user_id() {
        return group_user_id;
    }

    public void setGroup_user_id(int group_user_id) {
        this.group_user_id = group_user_id;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public long getGroup_time() {
        return group_time;
    }

    public void setGroup_time(long group_time) {
        this.group_time = group_time;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public String getGroup_explain() {
        return group_explain;
    }

    public void setGroup_explain(String group_explain) {
        this.group_explain = group_explain;
    }

    public String getGroup_hx() {
        return group_hx;
    }

    public void setGroup_hx(String group_hx) {
        this.group_hx = group_hx;
    }


}
