package com.zhongchuang.canting.easeui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mykar on 17/6/19.
 */
public class USER_AVATAR implements Serializable {
//    "hx_username":"1517037039629",
//            "user_id":"29",
//            "user_avatar":"http://120.76.129.214/eqc/Uploads/app/img/2017-05-31/592e66a9dc6c4.jpg",
//            "nickname":"",
//            "english_name":"too Young",
//            "name":"哈哈"

    private int user_num;
    private int user_id;

    private String hx_username;
    private String user_avatar;
    public String head_image;
    public String user_group_name;
    private String nickname;
    private String english_name;
    public String user_info_id;
    private String name;
    private String remarks;
    private String filter;
    public List<USER_AVATAR> data;

    public String getFilter() {
        return filter;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setFilter(String filter) {
        this.filter = filter;
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

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
