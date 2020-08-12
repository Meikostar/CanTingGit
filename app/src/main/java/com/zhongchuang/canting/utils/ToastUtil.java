package com.zhongchuang.canting.utils;


import android.text.TextUtils;
import android.widget.Toast;

import com.zhongchuang.canting.app.CanTingAppLication;

public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static void toast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Toast.makeText(CanTingAppLication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    //多次触发只显示一次
    public static void showToast(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(CanTingAppLication.getInstance(), s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    /**
     * 取消toast的显示
     */
    public static void cancle() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
