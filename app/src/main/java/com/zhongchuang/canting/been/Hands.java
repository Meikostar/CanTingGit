package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class Hands extends BaseResponse{

//      "is_enabled": 0,  //1正在直播  0没有直播
//              "anchors_id": "urio1023064898860482560",  //主播id
//              "play_type": 1,  //1没有被禁播  0已被禁播
//              "leave_massege": "0",  //未直播留言
//              "direct_see_name": "002",  //房间名称
//              "favorite_type": 0,  //1粉丝  0被主播添加到黑名单
//              "room_image": "http://120.78.196.53:8080/images/2.png",
//              "sex": "3",
//              "favorite_time": 1534314528000,  //开始关注的时间
//              "room_info_id": 30,  //房间id
//              "user_info_id": "urio1020226423073275904", //用户id
//              "remarks": "\"\""  //直播公告说明
//       "head_image": "1111",
//               "user_nick_name": "urio1033242364883238912",
//               "user_info_id": "urio1033242364883238912",
//               "integralTotal": 29
    public List<Hand> founsList;
    public List<Hand> giftList;
    public Hands data;
    public String founsCount;
    public String giftCount;
    public String direct_see_name;
    public String integralTotal;
    public String user_nick_name;
    public String user_info_id;
    public String room_image;
    public String head_image;
    public String room_info_id;
    public String direct_overview;



}
