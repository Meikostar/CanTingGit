package com.zhongchuang.canting.been;

import java.io.Serializable;

public class UserDto implements Serializable {
    private UserInfoDto data;

    public UserInfoDto getData() {
        return data;
    }

    public void setData(UserInfoDto data) {
        this.data = data;
    }
}

