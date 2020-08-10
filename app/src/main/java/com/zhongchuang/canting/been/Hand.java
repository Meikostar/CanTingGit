package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class Hand extends BaseResponse{

//  "is_enabled": 0,  //1在直播 0.没在直播
//          "leave_massege": "0",  //未直播留言
//          "direct_see_name": "002",  //房间名称
//          "favorite_type": 2,  //1已关注  0黑名单 2未关注
//   "create_time": 1534229617000, //送礼时间
//           "integral": 5,  //礼物兑换值
//           "gift_name": "",  //礼物名称
//           "user_nick_name": "",  //送礼用户昵称
//           "user_info_id": "urio1020226423073275904" //送礼用户id
    public List<Hand> data;
    public String is_enabled;
    public String id;
    public long create_time;
    public long endTime;
    public long startTime;
    public String remarkName;
    public String chatType;
    public String leave_massege;
    public String favorite_type;
    public String friendId;
    public String integralTotal;
    public String user_nick_name;
    public String user_info_id;
    public String room_image;
    public String head_image;
    public String gift_image;
    public String giftCount;
    public String integral;
    public String gift_name;
    public String direct_see_name;
    public String integralCount;
    public String room_info_id;
    public String anchors_id;
    public String fans_num;


}
