package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopCarBean extends BaseResponse {

    public List<ShopCar> data;

    public List<ShopCar> getData() {
        return data;
    }

    public void setData(List<ShopCar> data) {
        this.data = data;
    }
}
