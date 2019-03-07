package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/12/8.
 */

public class UserInfo extends  BaseResponse{
//"totalAmount": null,
//        "totalGlod": null
    public double totalAmount;
    public double otherIntegral;
    public double rechargeIntegral;
    public String userInfoId = "";
    public double totalGlod ;
    public double userIntegral ;
    public double userTokenMoney ;
    public String token = "";
    public String userLoginToken = "";
    public String nickname = "";
    public String idCardFrontImg = "";
    public String ringLetterName = "";
    public String ringLetterPwd = "";
    public String headImage = "";
    public String birthday = "";
    public String accountId = "";
    public String personalitySign = "";
    public String birthdayDay = "";
    public String birthdayYear = "";
    public String birthdayMonth = "";
    public String signStr = "";
    public String mobileNumber = "";
    public String sex;
    public UserInfo data;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getToken() {
        return userLoginToken;
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
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
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
