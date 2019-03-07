package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ItemBean {
    public int productLogo;
    public String productName;
    public String productPrice;

    public ItemBean() {
    }

    public ItemBean(int productLogo, String productName, String productPrice) {
        this.productLogo = productLogo;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public int getProductLogo() {
        return productLogo;
    }

    public void setProductLogo(int productLogo) {
        this.productLogo = productLogo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
