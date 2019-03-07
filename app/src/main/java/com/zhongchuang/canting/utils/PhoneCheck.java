package com.zhongchuang.canting.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/11/10.
 */

public class PhoneCheck {


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if(TextUtil.isNotEmpty(phoneNums)){
            return true;
        }else {
            return false;
        }


    }
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean judgePhoneNum(String phoneNums) {


        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }
    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()|| str==null) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }


    /**
     * 判断手机格式是否正确
     *
     * @param mobiles
     * @return 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
}
