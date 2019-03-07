package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class ShopBean extends BaseResponse{
//       "merchantInfo": {
//        "id": 111,
//                "merchantId": 0,
//                "collectNumber": 0,  //收藏数量
//                "linkMan": "王吉佬",  //联系人
//                "merName": "王老吉旗舰店",  //店铺名
//                "merAddress": "null",   //店铺地址
//                "merPhone": "18565638231", //联系电话
//                "merPassword": "8888",
//                "merIdcard": "46598765432143498721",
//                "createTime": 1529461164000,
//                "merType": "0",
//                "merImageUrl": "\t\r\ntest.jpg"

    public String id;
    public String merchantId;
    public String collectNumber;
    public String linkMan;
    public String merName;
    public String merAddress;
    public String merPhone;
    public String merPassword;
    public String merIdcard;
    public String createTime;
    public String merType;
    public String merImageUrl;
    public String url;
    public String isCollect;
    public String cont;
    public ShopBean data;
    public ShopBean merchantInfo;
    public List<Product> productList;


}
