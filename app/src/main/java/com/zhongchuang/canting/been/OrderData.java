package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class OrderData extends BaseResponse{
//       "totalPrice": 12,         //总价
//               "totalNumber": 1,         //总产品数
//               "merName": "厨神多金",     //店铺名
//               "protList": [
//    {
//        "pro_price": 12,       //商城价
//            "integral_price": 10,   //积分价
//            "picture_url": "http://192",
//            "market_price": 30,     //市场价
//            "p_color": "黑",
//            "id": 2,               //产品id
//            "p_specifi": "x",      //产品规格
//            "mer_name": "厨神多金",
//            "ship_address": "深圳市",  //发货地
//            "pro_name": "裙子"     //产品名
//    }
//        ]
    public List<OrderData> data;
    public List<OrderData> protList;

    public long create_time;
    public long make_targain_time;
    public long payment_time;
    public long send_time;
    public String order_type;
    public boolean isChoose;
    public String phone_message;
    public String transaction_id;
    public String order_id;
    public String merchantUserId;
    public String product_sku_id;
    public String user_info_id;
    public String phone_number;
    public String nick_name;
    public String payment_type;
    public String pay_type;
    public String express_id;
    public String address;
    public String totalPrice;
    public String totalIntegralPrice;
    public String orderType;
    public String totalNumber;
    public String mId;
    public String proSite;
    public String profit;
    public String merName;
    public String company_type;
    public String proSku;
    public String picture_sku_url;
    public String pro_site;
    public String pro_price;
    public String integral_price;
    public String phoneMassege;
    public String picture_url;
    public String market_price;
    public String number;
    public String prod_number;
    public String p_color;
    public String id;
    public String p_specifi;
    public String mer_name;
    public String ship_address;
    public String pro_name;


}
