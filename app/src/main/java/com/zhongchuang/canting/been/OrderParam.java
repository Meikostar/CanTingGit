package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OrderParam implements Serializable{

    public String userInfoId;
    public String proSite;
    public String addressId;
    public String payType;
    public String integralPayType;
    public String companyType;
    public String transaction_id;
    public List<Oparam> productList;
    public List<OrderParam> data;




}
