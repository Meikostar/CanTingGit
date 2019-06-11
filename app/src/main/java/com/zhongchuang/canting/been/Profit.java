package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class Profit extends BaseResponse{

//           "register_name": "",    //注册人用户昵称
//                   "create_time": 1555399378000,
//                   "profit_count": 23,     //推荐利润
//                   "user_info_id": "urio1110821194182950912",  //推荐码用户的用户id
//                   "order_id": "dfdf",    //订单id
//                   "product_name": "dsfgdg",  //产品名称
//                   "register_user": "22222"   //注册人用户账号


    public long create_time;
    public String register_name;
    public String profit_count;
    public String rtmp_url;
    public String user_info_id;
    public String order_id;
    public String product_name;
    public String register_user;

    public List<Profit> data;
    public Profit liveUrl;

}
