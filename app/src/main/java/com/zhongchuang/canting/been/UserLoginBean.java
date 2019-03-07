package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/11/15.
 */

public class UserLoginBean extends BaseResponse{

    /**
     * status : 301
     * message : 成功
     * data : {"userInfoId":"urio932923690650173440","token":"386662","nickname":null,"ringLetterName":"urio932923690650173440","ringLetterPwd":"FikgFFOHUbXfCRJ7rM+d2390DxAxmNekCm46TUwRd9wAPxK+gNXphESXT0LLLcXjRUhV2HtIMQKk6dOp1QJJthxDaSuyyu1fDz3oYQR1ukH4qeZFEuII1B1XVYNU9qVT5/D+rnDSnZ6EEIkncdVGiCWogCugo+hkMYdACC9QFX8="}
     */

    private UserInfo data;


    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }


}
