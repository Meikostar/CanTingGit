package com.zhongchuang.canting.utils;



import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2018/1/9.
 */

public class QiniuUtils {
    private static UploadManager uploadManager;
    private QiniuUtils(){
    }
    private static QiniuUtils qiniu;
    public static String baseurl="http://chushenduojin.cn/";
    public static QiniuUtils getInstance(){
        if(qiniu==null){
            qiniu=new QiniuUtils();
            Configuration config = new Configuration.Builder()
                    .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                   .zone(FixedZone.zone2)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            uploadManager = new UploadManager(config);
        }
        return qiniu;
    }
    public void setCompleteLlistener(CompleteListener listener){
        this.listener=listener;
    }
    private CompleteListener listener;
    public interface CompleteListener{
        void completeListener(String url);
    }
    public void  upFile(String path,String token,final CompleteListener listener){
        uploadManager.put(path, System.currentTimeMillis()+"chatbg", token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if(info!=null&&info.isOK()) {

                            listener.completeListener(key);
                        }
                    }
                },new UploadOptions(null, null, false, new UpProgressHandler() {
                    @Override
                    public void progress(String key, double percent) {

                    }
                }, new UpCancellationSignal() {
                    @Override
                    public boolean isCancelled() {
                        return false;
                    }
                }));
    }
}
