package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2018/7/6.
 */

public class ProductDel extends BaseResponse{

//     "sku_enable": true,
//             "integral_price": 1000,                          //积分价
//             "picture_url": "https://123456.jpg",            //商品缩略图
//             "p_stock": 130,                                 //库存
//             "picture_description_url": "https://123456.jpg,https://123456.jpg，https://123456.jpg，https://123456.jpg",        //商品详情说明图
//             "product_platform_id": 4564,                       //商品平台编码
//             "pro_price": 1000,                                 //商城价
//             "product_sku_id": 45641,                           //sku编码
//             "market_price": 1000,                             //市场价
//             "id": 1,
//             "ship_address": "广东省深圳市罗湖区太阳城广场2F2001",   //发货地址
//             "pro_site": 1,
//             "m_id": 103,                                       //商家id
//             "pro_name": "耐克运动鞋"                        //商家id
    public int sku_enable;
    public String picture_description_url;
    public String p_stock;
    public String market_price;
    public double profit;
    public String pro_price;
    public String shop_id;
    public String picture_url;
    public String userInfoId;
    public String integral_price;
    public String product_sku_id;
    public String id;
    public String pro_site;
    public String product_platform_id;
    public String pro_name;
    public String m_id;
    public String mer_name;

    public int express_type;
    public String express_content;
    public String ship_address;
//    mer_name //店铺名称  ,ship_address //发货地址
    public ProductDel data;
}
