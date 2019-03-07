package com.zhongchuang.canting.net;

import android.content.Context;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/11/8.
 */

public class HttpUtil extends BaseHttpUtil {

    private static HttpUtil instance;

    private Retrofit retrofit;

    public static HttpUtil getInstance(){
        if (instance==null) {
            synchronized (HttpUtil.class) {
                if (instance==null){
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context context,String baseUrl, boolean debug){
        this.mContext = context;
        this.isDebug = debug;

         retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(initOkHttp())
//                .addConverterFactory(GsonJSONConvertFactory.create(initGson()))  //自定义gson解析
                .addConverterFactory(GsonConverterFactory.create(initGson()))          //支持gson
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())    //为了支持Rxjava
                .build();
    }

    public <T> T create(Class<T> clazz){
        return  retrofit.create(clazz);
    }


    public <T> T create(Class<T> clazz,String baseUrl){
        Retrofit retroFit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(initOkHttp())
                .addConverterFactory(GsonConverterFactory.create(initGson()))          //支持gson
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())    //为了支持Rxjava
                .build();
        return  retroFit.create(clazz);
    }






}
