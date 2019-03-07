package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class USER extends  BaseResponse{

    public String userInfoId = "";
    public String token = "";
    public String nickname = "";
    public String nicknameInitials = "";
    public String ringLetterName = "";
    public String ringLetterPwd = "";
    public String head_image = "";
    public String birthday = "";
    public String user_group_name = "";
    public String accountId = "";
    public String user_info_id = "";
    public String personalitySign = "";
    public String birthdayDay = "";
    public String sex;
    public List<USER> data;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRingLetterName() {
        return ringLetterName;
    }

    public void setRingLetterName(String ringLetterName) {
        this.ringLetterName = ringLetterName;
    }

    public String getRingLetterPwd() {
        return ringLetterPwd;
    }

    public void setRingLetterPwd(String ringLetterPwd) {
        this.ringLetterPwd = ringLetterPwd;
    }

    public String getHeadImage() {
        return head_image;
    }

    public void setHeadImage(String headImage) {
        this.head_image = headImage;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPersonalitySign() {
        return personalitySign;
    }

    public void setPersonalitySign(String personalitySign) {
        this.personalitySign = personalitySign;
    }

    public String getBirthday() {
        return birthdayDay;
    }

    public void setBirthday(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
