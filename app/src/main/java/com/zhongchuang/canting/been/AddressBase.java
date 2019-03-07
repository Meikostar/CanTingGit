package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class AddressBase extends BaseResponse implements Serializable{
//     "create_time": "2018-06-05 14:54:42",
//             "link_number": "11",                                 //    联系电话
//             "address_id": 1,                                      //地址id
//             "shipping_address":"玉律路B栋20m",   //收货地址
//             "user_info_id": "11",                                //用户id
//             "shipping_name": "11",                              //收件人名
//             "is_default": false

    public long create_time;
    public String link_number;
    public String is_default;
    public String address_id;
    public String shipping_address;
    public String detailed_address;
    public String shipping_name;
    public String user_info_id;
    public  List<AddressBase> data;

    public List<AddressBase> getData() {
        return data;
    }

    public void setData(List<AddressBase> data) {
        this.data = data;
    }
}
