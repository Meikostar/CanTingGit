package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class HXFriendListBean {

    public String message;
    public int status;
    public List<HXFriend> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HXFriend> getData() {
        return data;
    }

    public void setData(List<HXFriend> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HXFriendListBean{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}
