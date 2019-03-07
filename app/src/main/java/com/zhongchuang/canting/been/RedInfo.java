package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class RedInfo extends BaseResponse{
//        "id": 13,
//                "userInfoId": "urio1054316485159157760",  //发红包的用户id
//                "redEnvelopeCount": 100,                  //红包总积分数
//                "redEnvelopeNumber": 12,                  //红包个数
//                "redEnvelopeSmaill": "11,6,2,5,19,14,1,9,17,4,11,1",
//                "redEnvelopeType": 1,                     //1群里的红包  2个人红包
//                "redEnvelopeGroupid": "1",                //群id
//                "leavMessage": "111111",                  //红包留言
//                "createTime": 1545199757102
//                "redEnvelopeId": 14
//          "grab_envelope_count": 3,
//                  "head_image": "http://119.23.235.1:8080/pic_file/100320d535782f.jpg",
//                  "create_time": 1545206941000,
//                  "nickname": "小张",
//                  "best_luck": 1,
//                  "grab_user_id": "urio1068686865059545088"
    public long create_time;
    public long time;
    public int isAll;
    public int chatType;
    public String id;
    public String userInfoId;
    public String red_envelope_smaill;
    public String grab_envelope_count;
    public String sendHeadImage;
    public String redEnvelopeCount;
    public String sendRemarkName;
    public int grabRedenvelope;
    public String leav_message;
    public String head_image;
    public String nickname;
    public String best_luck;
    public String grab_user_id;
    public String red_envelope_count;
    public String RemainNumbers;
    public String redEnvelopeNumber;
    public String redEnvelopeSmaill;
    public String redEnvelopeType;
    public String redEnvelopeGroupid;
    public String leavMessage;
    public String user_info_id;
    public String red_envelope_number;
    public String redEnvelopeId;

    public RedInfo data;
    public List<RedInfo> grabRedList;

}
