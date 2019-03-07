package com.zhongchuang.canting.net;

import android.os.Handler;


import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.utils.SpUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/23
 * 版本:
 ***/
public class TokenInterceptor implements Interceptor {
    private static Handler handler = new Handler() {
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SpUtil.getToken(CanTingAppLication.getInstance());


        Request request = chain.request().newBuilder()
                .addHeader("token", token)
                .addHeader("languge",getLangue(CanTingAppLication.LangueType))
                .build();
        return chain.proceed(request);

    }

   public String getLangue(String lan){
        String langue="";
        if(lan.equals("zh-rCN")){
            langue="zh";
        }else if(lan.equals("fan")){
            langue="tw";
        }else {
            langue=lan;
        }
        return langue;
   }

}
