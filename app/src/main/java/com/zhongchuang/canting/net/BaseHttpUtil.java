package com.zhongchuang.canting.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/11/8.
 */

public class BaseHttpUtil {

    protected Context mContext;
    protected boolean isDebug;


    protected Gson initGson() {
//        Gson gson = new GsonBuilder()
//                .serializeNulls()                //支持null字段输出
//                .setLenient()
//                .create();
        return new Gson();
    }

    protected OkHttpClient initOkHttp() {
        try {
            File cacheFile = new File(mContext.getCacheDir().getAbsolutePath(), "cacheData");
            long cacheSize = 1024 * 1024 * 10;
            Cache cache = new Cache(cacheFile, cacheSize);

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();


            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(6000, TimeUnit.SECONDS)
                    .connectTimeout(60000, TimeUnit.SECONDS)
                    .writeTimeout(60000, TimeUnit.SECONDS)
                    .addInterceptor(new TokenInterceptor())
//                    .cache(cache)
                    .sslSocketFactory(socketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private okhttp3.logging.HttpLoggingInterceptor getLogInterceptor() {
        okhttp3.logging.HttpLoggingInterceptor loggingInterceptor = new okhttp3.logging.HttpLoggingInterceptor();

        if (isDebug) {
            loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.NONE);
        }
        return loggingInterceptor;
    }




}
