package com.zhongchuang.canting.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class AppRegister extends BroadcastReceiver {
	public static final String APP_ID = "wxff6ae6cbdc1a3817";//微信支付appidAppID：wxd96b8dd3e5733967
	@Override
	public void onReceive(Context context, Intent intent) {
		 IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(APP_ID);
	}
}
