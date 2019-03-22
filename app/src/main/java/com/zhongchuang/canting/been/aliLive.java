package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class aliLive extends BaseResponse{

//    "chatrooms_id": "76526982856705",  //聊天室ID
//            "rtmp_url": "rtmp://alive.chushenduojin.cn/zhixing/stream_urio1098110334129930240?auth_key=1552904258-0-0-071591c54f5c62e146e6159768feac50", //拉流地址
//            "affiliations_count": 1,  //聊天室当前认数
//            "max_users": 5000,  //聊天室总上限人数
//            "chatrooms_name": "聊天22"  //聊天室名称



    public long create_time;
    public String pushurl;
    public String chatrooms_id;
    public String rtmp_url;
    public String affiliations_count;
    public String max_users;
    public String chatrooms_name;
    public String requestId;
    public String accessKeyId;
    public String accesskeysecret;
    public String expiration;
    public String token;

    public aliLive  data;
    public aliLive  liveUrl;

}
