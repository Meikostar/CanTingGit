package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopTypeBean extends BaseResponse {

    List<ShopType> data;

    public List<ShopType> getData() {
        return data;
    }

    public void setData(List<ShopType> data) {
        this.data = data;
    }
}
