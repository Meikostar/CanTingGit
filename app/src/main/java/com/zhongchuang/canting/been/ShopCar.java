package com.zhongchuang.canting.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopCar implements Serializable{

    public String gSpeId;
    public String goodsId;
    public String price;
    public String cartId;
    public String describeContent;
    public int speState;   //  1 代表不是规格商品    2：代表规格商品
    public String goodsName;
    public String shopCarNum;
    public String goodsLogo;
    public boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getgSpeId() {
        return gSpeId;
    }

    public void setgSpeId(String gSpeId) {
        this.gSpeId = gSpeId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getDescribeContent() {
        return describeContent;
    }

    public void setDescribeContent(String describeContent) {
        this.describeContent = describeContent;
    }

    public int getSpeState() {
        return speState;
    }

    public void setSpeState(int speState) {
        this.speState = speState;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getShopCarNum() {
        return shopCarNum;
    }

    public void setShopCarNum(String shopCarNum) {
        this.shopCarNum = shopCarNum;
    }

    public String getGoodsLogo() {
        return goodsLogo;
    }

    public void setGoodsLogo(String goodsLogo) {
        this.goodsLogo = goodsLogo;
    }
}
