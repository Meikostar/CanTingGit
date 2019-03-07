package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FoodOrderBean extends BaseResponse implements Serializable {
//  "order_code": "1", //订单号
//          "address": null, //下单者地址
//          "completed_time": null, //订单完成时间
//          "total_pay_price": 11,  //总支付金额
//          "coupon_offset_price": null, //优惠券抵消的价钱
//          "order_amount": 1, //订单金额
//          "shopName": "1", //商店名称
//          "order_state": "1" //1表示未付款2表示已付款3已接单4表示配送中5表示已送达


    public List<FoodOrderBean> data;
    public String order_code;
    public String address;
    public String completed_time;
    public String greens_image_url;
    public double total_pay_price;
    public String coupon_offset_price;
    public String order_amount;
    public String shopName;
    public int  order_state;
    public String FoodOrdId;



}
