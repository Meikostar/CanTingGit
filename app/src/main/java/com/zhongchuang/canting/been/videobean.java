package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class videobean extends BaseResponse{
//
//  "video_name": "00000",  //视频名称
//          "video_url": "",        //视频地址
//          "create_time": 1553151496000,  //创建时间
//          "id": 1,
//          "cover_image": "111111",     //封面地址
//          "user_info_id": "urio1098110334129930240",
//          "type": 0,                   //1直播录制视频  2自己上传的视频
//          "request_id": "2EA2459A-C584-40DF-8EC3-C2A79AB4BCEB"



    public long create_time;
    public String video_name;
    public String room_info_id;
    public String video_url;
    public String id;
    public String cover_image;
    public String user_info_id;
    public String type;
    public int video_type;
    public String new_type;
    public String request_id;

    public List<videobean> data;


}
