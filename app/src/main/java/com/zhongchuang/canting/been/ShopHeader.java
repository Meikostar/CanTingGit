package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class ShopHeader {

    public List<GoodsInfo> threeGoods;
    public List<Banner> active;
    public String guanggao;

    public List<GoodsInfo> getThreeGoods() {
        return threeGoods;
    }

    public void setThreeGoods(List<GoodsInfo> threeGoods) {
        this.threeGoods = threeGoods;
    }

    public List<Banner> getActive() {
        return active;
    }

    public void setActive(List<Banner> active) {
        this.active = active;
    }

    public String getGuanggao() {
        return guanggao;
    }

    public void setGuanggao(String guanggao) {
        this.guanggao = guanggao;
    }
}
