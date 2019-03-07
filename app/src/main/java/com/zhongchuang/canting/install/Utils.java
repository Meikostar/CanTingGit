package com.zhongchuang.canting.install;

import android.text.TextUtils;

import com.hyphenate.util.HanziToPinyin;
import com.zhongchuang.canting.easeui.bean.USER;

import java.io.File;
import java.util.ArrayList;

import io.valuesfeng.picker.universalimageloader.utils.L;

/**
 * Created by asia on 10/12/16.
 */

public class Utils {
    public static void setUserInitialLetter(USER user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring
                        (0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(user.english_name)) {
            if (TextUtils.isEmpty(user.name)) {
                letter = new GetInitialLetter().getLetter(user.english_name);
            }else {
                letter=new GetInitialLetter().getLetter(user.name);
            }
        }else {
            letter=user.filter;
        }
        user.setInitialLetter(letter);
    }
    public static final String TAG = "Utils";

    public static boolean checkRooted() {
        boolean result = false;
        try {
            result = new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getGroupImgStr(ArrayList<String> strList) {
        String userGroups = null;
        if (strList == null) {
            return "";
        }
        for (int i = 0; i < strList.size(); i++) {
            L.e("strList" + strList.get(i));
            if (i == 0)
                userGroups = strList.get(i);
            else{
                if (TextUtils.isEmpty(strList.get(i))){
                    userGroups+=",";
                }else {
                    userGroups += "," + strList.get(i);
                }
            }

        }
        return userGroups;
    }
}
