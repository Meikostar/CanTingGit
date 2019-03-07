package com.zhongchuang.canting.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/9.
 */

public class BaseResponse implements Serializable {
    public String message;
    public int status;



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



    @Override
    public String toString() {
        return "BaseString{" +
                "message='" + message + '\'' +
                ", status=" + status +

                '}';
    }
}
