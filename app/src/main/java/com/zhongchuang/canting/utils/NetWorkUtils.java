package com.zhongchuang.canting.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



/**
 * Created by Administrator on 2017/8/25.
 */

public class NetWorkUtils {

    private static NetWorkUtils netWorkUtils;
    private Context mContext;


    public static NetWorkUtils getApp(){
        if (netWorkUtils==null){
            netWorkUtils = new NetWorkUtils();
        }

        return netWorkUtils;
    }

    public void init(Context context){
        mContext = context;
    }


    /**
     * 检测网络是否可用
     *
     * @param context 上下文
     * @return true:可用； false:不可用
     */
    public static boolean isNetworkAccessiable(Context context){
        try{
            if(context != null){
                ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(CM != null){
                    NetworkInfo info = CM.getActiveNetworkInfo();
                    if(info != null){
                        return info.isConnected();
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public boolean isNetworkConnected( ) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }

        return false;
    }


    /**
     * 判断WIFI网络是否可用
     * @param context
     * @return
     */
    public boolean isWifiConnected( ) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     */
    public boolean isMobileConnected( ) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 获取当前网络连接的类型信息
     * @param context
     * @return
     */
    public int getConnectedType( ) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

}
