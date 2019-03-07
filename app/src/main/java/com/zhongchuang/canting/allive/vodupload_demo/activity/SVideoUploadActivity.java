package com.zhongchuang.canting.allive.vodupload_demo.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.vod.upload.VODSVideoUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClient;
import com.alibaba.sdk.android.vod.upload.VODSVideoUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.common.utils.StringUtil;
import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.alibaba.sdk.android.vod.upload.session.VodHttpClientConfig;
import com.alibaba.sdk.android.vod.upload.session.VodSessionCreateInfo;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodupload_demo.utils.FileUtils;
import com.zhongchuang.canting.allive.vodupload_demo.utils.NetWatchdog;

import java.io.File;

/**
 * 短视频上传场景示例
 * Created by Mulberry on 2017/11/24.
 */
public class SVideoUploadActivity extends AppCompatActivity {

    private static final String TAG = "VOD_UPLOAD";
    Button btnUpload;
    Button btnCancel;
    Button btnResume;
    Button btnPause;
    TextView tvProgress;
    private long  progress;

    private String videoPath;//视频路径
    private String imagePath;//封面图片路径

    //以下四个值由开发者的服务端提供,参考文档：https://help.aliyun.com/document_detail/28756.html（STS介绍）
    // AppServer STS SDK参考：https://help.aliyun.com/document_detail/28788.html
    private String accessKeyId = "STS.HNx7kDUhNKaV42psc898WPh49";//子accessKeyId
    private String accessKeySecret = "4i5dtPtQMLcFif7WcvHzqpVEFPuWr2cMS64mTJsjdpwX" ;//子accessKeySecret
    private String securityToken = "CAIShwJ1q6Ft5B2yfSjIqY3NfNHwuLdv/KO9NhTBl2NtNbd7v62f2zz2IHtKenZsCegav/Q3nW1V7vsdlrBtTJNJSEnDKNF36pkS6g66eIvGvYmz5LkJ0AMvx7J3T0yV5tTbRsmkZvW/E67fSjKpvyt3xqSAAlfGdle5MJqPpId6Z9AMJGeRZiZHA9EkSWkPtsgWZzmrQpTLCBPxhXfKB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392qesP1P5UyZ8YvC4nthLRMG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLGKrIIzfFclN/hiQvMZ9KWjj55mveDfmoHw0RFJMPGNr7Ie1VZgqhqAAa8uMRKc9yPV0xCYbp/geizLRhkXAasL6q73vyZOyMbrb9a1hV41EE8o1t3+VWZ1Og41gxDoR304xHvPksNXUcioLA2UH7LjVA5kOVDUvCAxXJ/D++N0I7lK68yXwgSXKV8uYqD7I1+Dpco/IDxVZWhjQQApQk81JepCHOaIqEig";
    private String expriedTime = "2018-01-05T12:31:08Z";//STS的过期时间

    private String requestID = null;//传空或传递访问STS返回的requestID

    VODSVideoUploadClient vodsVideoUploadClient;
    private NetWatchdog netWatchdog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svideo_upload);
        getIntentExtra();

        getFileTask.execute();

        btnUpload = (Button)findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCreateFile){
                    Toast.makeText(v.getContext(),"Please Wait, The videoPath and imagePath is null",Toast.LENGTH_LONG).show();
                    return;
                } else if(StringUtil.isEmpty(accessKeyId)) {
                    Toast.makeText(v.getContext(),"The specified parameter accessKeyId cannot be null",Toast.LENGTH_LONG).show();
                    return;
                } else if(StringUtil.isEmpty(accessKeySecret)) {
                    Toast.makeText(v.getContext(),"The specified parameter \"accessKeySecret\" cannot be null",Toast.LENGTH_LONG).show();
                    return;
                } else if(StringUtil.isEmpty(securityToken)) {
                    Toast.makeText(v.getContext(),"The specified parameter \"securityToken\" cannot be null",Toast.LENGTH_LONG).show();
                    return;
                } else if(StringUtil.isEmpty(expriedTime)) {
                    Toast.makeText(v.getContext(),"The specified parameter \"expriedTime\" cannot be null",Toast.LENGTH_LONG).show();
                    return;
                } else if(!(new File(videoPath)).exists()) {
                    Toast.makeText(v.getContext(),"The specified parameter \"videoPath\" file not exists",Toast.LENGTH_LONG).show();
                    return;
                } else if(!(new File(imagePath)).exists()) {
                    Toast.makeText(v.getContext(),"The specified parameter \"imagePath\" file not exists",Toast.LENGTH_LONG).show();
                    return;
                }

                //参数请确保存在，如不存在SDK内部将会直接将错误throw Exception
                // 文件路径保证存在之外因为Android 6.0之后需要动态获取权限，请开发者自行实现获取"文件读写权限".
                VodHttpClientConfig vodHttpClientConfig = new VodHttpClientConfig.Builder()
                        .setMaxRetryCount(2)
                        .setConnectionTimeout(15 * 1000)
                        .setSocketTimeout(15 * 1000)
                        .build();

                SvideoInfo svideoInfo = new SvideoInfo();
                svideoInfo.setTitle(new File(videoPath).getName());
                svideoInfo.setDesc("");
                svideoInfo.setCateId(1);

                VodSessionCreateInfo vodSessionCreateInfo =new  VodSessionCreateInfo.Builder()
                        .setImagePath(imagePath)
                        .setVideoPath(videoPath)
                        .setAccessKeyId(accessKeyId)
                        .setAccessKeySecret(accessKeySecret)
                        .setSecurityToken(securityToken)
                        .setRequestID(requestID)
                        .setExpriedTime(expriedTime)
                        .setIsTranscode(true)
                        .setSvideoInfo(svideoInfo)
                        .setPartSize(500 * 1024)
                        .setVodHttpClientConfig(vodHttpClientConfig)
                        .build();
                vodsVideoUploadClient.uploadWithVideoAndImg(vodSessionCreateInfo, new VODSVideoUploadCallback() {
                    @Override
                    public void onUploadSucceed(String videoId, String imageUrl) {
                        Log.d(TAG,"onUploadSucceed"+ "videoId:"+ videoId + "imageUrl" + imageUrl);
                    }

                    @Override
                    public void onUploadFailed(String code, String message) {
                        Log.d(TAG,"onUploadFailed" + "code" + code + "message" + message);
                    }

                    @Override
                    public void onUploadProgress(long uploadedSize, long totalSize) {
                        Log.d(TAG,"onUploadProgress" + uploadedSize * 100 / totalSize);
                        progress = uploadedSize * 100 / totalSize;
                        handler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onSTSTokenExpried() {
                        Log.d(TAG,"onSTSTokenExpried");
                        //STS token过期之后刷新STStoken，如正在上传将会断点续传
                        accessKeyId = "STS.GzVkp3WpkLr97TdEUsght1Miz";
                        accessKeySecret = "7dvXT6PHN2uiGmyVKj5gxYHjXg9AnPnjHafmeSxusXHt";
                        securityToken = "CAIShwJ1q6Ft5B2yfSjIprnjIMqHuq9K+7DSNXLVoVUma+dY3ojCmDz2IHtKenZsCegav/Q3nW1V7vsdlrBtTJNJSEnDKNF36pkS6g66eIvGvYmz5LkJ0CFkgKx3T0yV5tTbRsmkZvW/E67fSjKpvyt3xqSAAlfGdle5MJqPpId6Z9AMJGeRZiZHA9EkSWkPtsgWZzmrQpTLCBPxhXfKB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392qesP1P5UyZ8YvC4nthLRMG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLGKrIIzfFclN/hiQvMZ9KWjj55mveDfmoHw0RFJMPGNr7Ie1VZgqhqAAZbSW08OCELjDZHWX4jRWvIJVEEPiXZ1eW4M1NmiKiDi7RSx4R7PuNLOWS51tJbZJZxi99KpstJtVff1T5Ss9pP1PKPGmnvrP13heb0lhgE3nlJkreTlNGmqxcb1VIA9CGNeAh5IjIR7mBgKxX7vcOt+sBkcd9ibs4XI5sYvtuC3";
                        expriedTime = "2018-01-05T09:17:25Z";
                        vodsVideoUploadClient.refreshSTSToken(accessKeyId,accessKeySecret,securityToken,expriedTime);
                    }

                    @Override
                    public void onUploadRetry(String code, String message) {
                        //上传重试的提醒
                        Log.d(TAG,"onUploadRetry" + "code" + code + "message" + message);
                    }

                    @Override
                    public void onUploadRetryResume() {
                        //上传重试成功的回调.告知用户重试成功
                        Log.d(TAG,"onUploadRetryResume");
                    }
                });
            }
        });

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vodsVideoUploadClient.cancel();


            }
        });
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vodsVideoUploadClient.resume();
            }
        });

        btnPause = (Button) findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vodsVideoUploadClient.pause();
            }
        });

        tvProgress = (TextView) findViewById(R.id.tv_progress);

        vodsVideoUploadClient = new VODSVideoUploadClientImpl(this.getApplicationContext());
        vodsVideoUploadClient.init();
        netWatchdog = new NetWatchdog(this);
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                if (vodsVideoUploadClient!=null){
                    vodsVideoUploadClient.resume();
                }


            }

            @Override
            public void onNetUnConnected() {
                vodsVideoUploadClient.pause();
            }

        });

        netWatchdog.startWatch();

    }

    private void getIntentExtra(){
        accessKeyId = getIntent().getStringExtra("accessKeyId");
        accessKeySecret = getIntent().getStringExtra("accessKeySecret");
        securityToken = getIntent().getStringExtra("securityToken");
        expriedTime = getIntent().getStringExtra("expiration");
    }

    @Override
    protected void onResume() {
        super.onResume();
        vodsVideoUploadClient.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        vodsVideoUploadClient.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vodsVideoUploadClient.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // UI只允许在主线程更新。
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"progress:"+progress);
            //更新进度
            tvProgress.setText("进度：" + String.valueOf(progress) );

        }
    };

    private boolean isCreateFile;
    private AsyncTask getFileTask =new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] objects) {
            videoPath = FileUtils.createFileFromInputStream(SVideoUploadActivity.this,"aliyunmedia.mp4").getPath();
            imagePath = FileUtils.createFileFromInputStream(SVideoUploadActivity.this,"001.png").getPath();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            isCreateFile = true;
            super.onPostExecute(o);
        }
    };
}
