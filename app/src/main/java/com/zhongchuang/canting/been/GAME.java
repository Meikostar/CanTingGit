package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mykar on 17/4/26.
 */
public class GAME extends BaseResponse implements Serializable {
    //    "gameInfos": [
//    {
//        "gameId": 1,
//            "gameName": "王者农药",
//            "linkUrl": "https://www.baidu.com/",
//            "desc": "小学生游戏",
//            "interval": 3000000,
//            "type": 0,
//            "imgUrl": "http://oud4e96lb.bkt.clouddn.com/FpsVGQFAWWv2Dlctreg7A-QegqiK"
//        "classifyId": 1,
//            "classifyName": "冒险"
    public List<GAME> data;
    public List<GAME> friendList;
    public String id;
    public long classifyId;
    public long create_time;
    public boolean isChoose;
    public boolean havaMessage;
    public int type;
    public int hasNext;
    public int cont;
    public String head_image;
    public String gameName;
    public String nickname;
    public String chat_user_id;
    public String groupBackImage;
    public String create_by;
    public String chat_group_name;
    public String sort_id;
    public String sortId;
    public double money;
    public String linkUrl;
    public String desc;
    public String interval;
    public String imgUrl;
    public String directTypeName;
    public String chatGroupName;

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }



    public long getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(long classifyId) {
        this.classifyId = classifyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
