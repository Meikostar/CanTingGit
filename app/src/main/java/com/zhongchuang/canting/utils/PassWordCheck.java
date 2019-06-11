package com.zhongchuang.canting.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/11/15.
 */

public class PassWordCheck {
    public static boolean judgePhoneNums(String passWord) {

        return isMatchLength(passWord) && isPassWordNO(passWord);

    }


    public static boolean isMatchLength(String passWord) {
        if (passWord.isEmpty()) {
            return false;
        } else {
            //return passWord.length() == length ? true : false;

            return passWord.length() >= 8 || passWord.length() <= 20;
        }
    }

    //匹配
    public static boolean isPassWordNO(String passWord) {
       /*
       *  ^ 匹配一行的开头位置
            (?![0-9]+$) 预测该位置后面不全是数字
            (?![a-zA-Z]+$) 预测该位置后面不全是字母
            [0-9A-Za-z] {8,16} 由8-16位数字或这字母组成
            $ 匹配行结尾位置
            注：(?!xxxx) 是正则表达式的负向零宽断言一种形式，标识预该位置后不是xxxx字符。
       *
       * */
        String telRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if (TextUtils.isEmpty(passWord)) return false;
        else return passWord.matches(telRegex);
    }
}
