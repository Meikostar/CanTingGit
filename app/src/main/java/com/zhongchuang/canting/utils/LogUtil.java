package com.zhongchuang.canting.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/11/29.
 */

public class LogUtil {

    private static final String IFUN = "IFun_TAG";


    public static void d(String msg){
        Log.d(IFUN,msg);
    }

    public static void i(String msg){
        Log.i(IFUN,msg);
    }

    public static void err(String msg){
        Log.e(IFUN,msg);
    }

}
