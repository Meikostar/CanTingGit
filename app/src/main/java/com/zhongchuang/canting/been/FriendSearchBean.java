package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class FriendSearchBean {


    /**
     * status : 301
     * message : 成功
     * data : [{"personalitySign":"个性签名","headImage":"http://avatar.csdn.net/5/C/6/1_threemaster.jpg","mobileNumber":"13035846174","ringLetterName":"urio932923690650173440","nickname":"张三","userInfoId":"urio930686418261377024"}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * personalitySign : 个性签名
         * headImage : http://avatar.csdn.net/5/C/6/1_threemaster.jpg
         * mobileNumber : 13035846174
         * ringLetterName : urio932923690650173440
         * nickname : 张三
         * userInfoId : urio930686418261377024
         */

        private String personalitySign;
        private String headImage;
        private String mobileNumber;
        private String ringLetterName;
        private String nickname;
        public String isFriends ;
        private String userInfoId;

        public String getPersonalitySign() {
            return personalitySign;
        }

        public void setPersonalitySign(String personalitySign) {
            this.personalitySign = personalitySign;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getRingLetterName() {
            return ringLetterName;
        }

        public void setRingLetterName(String ringLetterName) {
            this.ringLetterName = ringLetterName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(String userInfoId) {
            this.userInfoId = userInfoId;
        }
    }
}
