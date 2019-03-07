package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class HXFriend  {

    public String nickname;
    public String chatUserId;
    public String headImage;
    public List<HXFriend> data;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getChatUserId() {
        return chatUserId;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }


    @Override
    public String toString() {
        return "HXFriend{" +
                "nickname='" + nickname + '\'' +
                ", chatUserId='" + chatUserId + '\'' +
                ", headImage='" + headImage + '\'' +
                '}';
    }
}
