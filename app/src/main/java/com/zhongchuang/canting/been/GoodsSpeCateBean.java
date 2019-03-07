package com.zhongchuang.canting.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GoodsSpeCateBean implements Serializable {

    private GoodsSpr goodsSpr;
    private String gStaDetId;
    private String staItemName;

    public GoodsSpr getGoodsSpr() {
        return goodsSpr;
    }

    public void setGoodsSpr(GoodsSpr goodsSpr) {
        this.goodsSpr = goodsSpr;
    }

    public String getgStaDetId() {
        return gStaDetId;
    }

    public void setgStaDetId(String gStaDetId) {
        this.gStaDetId = gStaDetId;
    }

    public String getStaItemName() {
        return staItemName;
    }

    public void setStaItemName(String staItemName) {
        this.staItemName = staItemName;
    }

    private class GoodsSpr implements Serializable{
        private String gSpeId;
        private String goodsId;
        private String goodsLogo;
        private String isPutaway;
        private String price;
        private String goodsInventory;
        private String speContent;
        private String isDelete;
        private String createTime;
        private String modifyTime;
        private String createBy;

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

        public String getGoodsLogo() {
            return goodsLogo;
        }

        public void setGoodsLogo(String goodsLogo) {
            this.goodsLogo = goodsLogo;
        }

        public String getIsPutaway() {
            return isPutaway;
        }

        public void setIsPutaway(String isPutaway) {
            this.isPutaway = isPutaway;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoodsInventory() {
            return goodsInventory;
        }

        public void setGoodsInventory(String goodsInventory) {
            this.goodsInventory = goodsInventory;
        }

        public String getSpeContent() {
            return speContent;
        }

        public void setSpeContent(String speContent) {
            this.speContent = speContent;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }
    }
}
