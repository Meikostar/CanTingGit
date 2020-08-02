package com.zhongchuang.canting.newnet;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.utils.NetWorkUtils;
import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HttpCach 缓存拦截器
 * Created by leo on 2016/9/2.
 */
public class HttpCachInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtils.isNetworkAccessiable(CanTingAppLication.getInstance())) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();

        }
        Response originalResponse = chain.proceed(request);
        if (NetWorkUtils.isNetworkAccessiable(CanTingAppLication.getInstance())) { //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder().removeHeader("Pragma").header("Cache-Control", cacheControl).build();
        } else {
            return originalResponse.newBuilder().removeHeader("Pragma").header("Cache-Control", "public, only-if-cached, max-stale=2419200").build();
        }
    }
}
