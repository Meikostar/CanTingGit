package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class UserInfoBean extends BaseResponse implements Serializable{
    // "movie": null,
             //电影 "create_time": null,
        // "image_url": null, //图片地址
    // "personal_statement": null,
    // "book": null,
    // 小说 "place_notes": null,
    // 地点足记 "label": null,
    // 标签 "emotional_state": null,
    // 感情状态 "birthday_year": "null",
    // 生日年份 "birthday_day": "null",
    // 生日天 "graduate_school": null,
    // 毕业学校 "home_town": null,
    // 家乡 "music": null,
    // 音乐 "personality_sign": null,
    // 个性签名 "nickname": "zm",
    // 昵称 "birthday_month": "null",
    // 生日月份 "user_info_id": null,
    // 用户id "job": null
    // 职业 }, "userIntegralCount": null }

    public long create_time;
    public String movie;
    public String image_url;
    public String birthday;
    public String personal_statement;
    public String book;
    public String place_notes;
    public String emotional_state;
    public String birthday_year;
    public String birthday_day;
    public String birthday_month;
    public String graduate_school;
    public String home_town;
    public String music;
    public String personality_sign;
    public String nickname;
    public String user_info_id;
    public String head_image;
    public String userIntegralCount;
    public String job;
    public String label;
    public String cocial_card;
    public String self_personality;
    public String current_state;
    public String superpower;

    public  UserInfoBean data;


}
