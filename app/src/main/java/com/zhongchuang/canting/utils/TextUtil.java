package com.zhongchuang.canting.utils;


import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONObject;

import java.io.InputStream;

/***
 * 功能描述: 字符串处理工具类
 * 作者:chenwei
 * 时间:2016/8/9
 * 版本:1.0
 ***/
public class TextUtil {
    public static JSONObject getJson(String fileName,Context context) {
        try {
            AssetManager s = context.getAssets();
            InputStream is = s.open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, "utf-8");
            is.close();
            JSONObject obj;
            obj = new JSONObject(json);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str){
        return str == null || str.length() == 0;
    }

    /**
     * Returns true if the string is not null and more then 0-length.
     * @param str the string to be examined
     * @return false if str is null or zero length
     */
    public static boolean isNotEmpty(CharSequence str){
        return str != null && str.length() != 0;
    }

    /**
     * 可以判断出“       ”
     * @param str
     * @return
     */
    public static boolean isEmptyIncludeSpace(CharSequence str){
       if(isEmpty(str)){
           return true;
       }else {
           str=str.toString().trim();
           return isEmpty(str);
       }
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }
}
