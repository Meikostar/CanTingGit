package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class GoodsInfo implements Serializable{

    /**
     * goodsId	商品id
     goodsName	商品名
     goodsLogo	商品logo
     price	商品价格
     goodsInventory	商品库存
     browseCount	浏览次数
     buyCount	购买次数

     */
    public String buyCount;
    public String goodsId;
    public String price;
    public String paramImage;
    public String browseCount;
    public String goodsName;
    public String goodsLogo;
    public int goodsInventory;


    public List<String> image;
    public String speState;
    public String areaName;
    public String describeContent;
    public String content;
    public String gSpeId;






    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
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

    public String getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(String browseCount) {
        this.browseCount = browseCount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsLogo() {
        return goodsLogo;
    }

    public void setGoodsLogo(String goodsLogo) {
        this.goodsLogo = goodsLogo;
    }

    public int getGoodsInventory() {
        return goodsInventory;
    }

    public void setGoodsInventory(int goodsInventory) {
        this.goodsInventory = goodsInventory;
    }


    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getSpeState() {
        return speState;
    }

    public void setSpeState(String speState) {
        this.speState = speState;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDescribeContent() {
        return describeContent;
    }

    public void setDescribeContent(String describeContent) {
        this.describeContent = describeContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getgSpeId() {
        return gSpeId;
    }

    public void setgSpeId(String gSpeId) {
        this.gSpeId = gSpeId;
    }
}
