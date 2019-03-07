package com.zhongchuang.canting.install;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhongchuang.canting.utils.LogUtil;

/**
 * Created by fl on 2017/5/2.
 */

public class BootReceiver extends BroadcastReceiver {

    private OnopenApkListener listener;

    public BootReceiver(OnopenApkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //install listener
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")){
            LogUtil.d("install a apk");
            if (listener!=null){
                listener.openApk();
            }
        }

        //delete listner
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){
            LogUtil.d("delete a apk");
        }
    }
}
