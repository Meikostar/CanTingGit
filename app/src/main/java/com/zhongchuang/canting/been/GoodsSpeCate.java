package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GoodsSpeCate extends BaseResponse {
    private List<GoodsSpeCateBean> data;

    public List<GoodsSpeCateBean> getData() {
        return data;
    }

    public void setData(List<GoodsSpeCateBean> data) {
        this.data = data;
    }
}
