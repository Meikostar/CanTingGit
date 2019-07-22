package com.zhongchuang.canting.been;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/10.
 */

public class Oparam implements Serializable{

    public String productSkuId;
    public String number;
    public String integralPrice;
    public String company_type;
    public String phoneMessage;

    @Override
    public String toString() {
        return "Oparam{" +
                "productSkuId='" + productSkuId + '\'' +
                ", number='" + number + '\'' +
                ", integralPrice='" + integralPrice + '\'' +
                '}';
    }
}
