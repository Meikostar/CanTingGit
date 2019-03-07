package com.zhongchuang.canting.easeui.bean;

import java.io.Serializable;

/**
 * Created by syj on 2016/12/24.
 */
public class VERIFYREPLY implements Serializable {


    /**
     * vid : 12
     * reply_id : 27
     * rid : 78
     * reply_message :
     * v_add_time : 1482545975
     * name : 而 Mr 模式让我破
     */

    private int vid;
    private int reply_id;
    private int rid;
    private String reply_message;
    private int v_add_time;
    private String name;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getReply_id() {
        return reply_id;
    }

    public void setReply_id(int reply_id) {
        this.reply_id = reply_id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getReply_message() {
        return reply_message;
    }

    public void setReply_message(String reply_message) {
        this.reply_message = reply_message;
    }

    public int getV_add_time() {
        return v_add_time;
    }

    public void setV_add_time(int v_add_time) {
        this.v_add_time = v_add_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
