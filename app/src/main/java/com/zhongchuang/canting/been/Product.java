package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */

public class Product extends BaseResponse{

//            "pro_price": 220,                   //商城价
//                    "shop_id": 103,                     //店铺id
//                    "picture_url": "https://123456.jpg",
//                    "integral_price": 220,              //积分价
//                    "product_sku_id": 45863,            //商品sku编码
//                    "id": 6,
//                    "pro_site": 1,                      //产品位置
//                    "product_platform_id": 4586,        //产品平台编码
//                    "pro_name": "耐克拖鞋",              //产品名
//                    "m_id": 103                         //商家id
//              "totalPrice": null,
//                      "totalIntegralPrice": null,
//                      "totalNumber": 4,
//                      "proSite": 1,
//                      "merName": "耐克旗舰店", //店铺名
    public String pro_price;
    public String totalPrice;
    public String totalIntegralPrice;
    public String totalNumber;
    public String proSite;
    public String merName;
    public String shop_id;
    public String proSku;
    public String picture_url;
    public String picture_sku_url;
    public String integral_price;
    public String express_content;
    public String product_sku_id;
    public String id;
    public String company_type;

    public String pro_site;
    public String product_platform_id;
    public String pro_name;
    public String number;
    public String product_sku_url;
    public String m_id;
    public String p_stock;
    public String user_info_id;
    public String ship_address;
    public String mer_name;
    public String second_category_name;
    public boolean isChoose;


//    mer_name //店铺名称  ,ship_address //发货地址
    public List<Product> data;
    public List<Product> protList;
}
