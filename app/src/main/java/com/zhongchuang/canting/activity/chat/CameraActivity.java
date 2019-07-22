package com.zhongchuang.canting.activity.chat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.cameralibrary.CaptureButton;
import com.zhongchuang.canting.allive.cameralibrary.JCameraView;
import com.zhongchuang.canting.allive.cameralibrary.listener.ClickListener;
import com.zhongchuang.canting.allive.cameralibrary.listener.ErrorListener;
import com.zhongchuang.canting.allive.cameralibrary.listener.JCameraListener;
import com.zhongchuang.canting.allive.cameralibrary.util.DeviceUtil;
import com.zhongchuang.canting.allive.cameralibrary.util.FileUtil;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.waitLoading.ShapeLoadingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraActivity extends AppCompatActivity implements   OtherContract.View {
    private JCameraView jCameraView;
    private VODUploadClient uploader;
    private OtherPresenter presenter;
    private CountDownTimer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        presenter=new OtherPresenter(this);
        presenter.getLiveToken();
        uploader = new VODUploadClientImpl(getApplicationContext());
        if (mTimer == null) {
            mTimer = new CountDownTimer((long) (60 * 10 * 1000), 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Log.e("zpan", "======onFinish=====");
                }
            };

        }
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        jCameraView.setTip("");
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("CJT", "camera error");
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("发送中...")
                .build();
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
//                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(101, intent);
                CanTingAppLication.video_path=path+"%#%1";
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                String path = FileUtil.saveBitmap("JCamera", firstFrame);
                Log.i("CJT", "url = " + url + ", Bitmap = " + path);

                getVideoThumb(url);
                videoLength = getFileOrFilesSize(url, 3);
                timeLength = getLocalVideoDuration(url);
                mOutputPath=url;
                shapeLoadingDialog.show();
                initVideo();

            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                if(CaptureButton.state==CaptureButton.STATE_RECORDERING){

                }else {
                    CameraActivity.this.finish();
                }

            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                startActivityForResult(new Intent(CameraActivity.this,MoreMediaActivity.class),100);
//                Toast.makeText(CameraActivity.this,"Right", Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("CJT", DeviceUtil.getDeviceModel());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==101){
            String url = data.getStringExtra("path");
            String mimeType = data.getStringExtra("mimeType");

            if(mimeType.contains("video")){
                getVideoThumb(url);
                videoLength = getFileOrFilesSize(url, 3);
                timeLength = getLocalVideoDuration(url);
                mOutputPath=url;
                shapeLoadingDialog.show();
                initVideo();
            }else if(mimeType.contains("image")) {
                Intent intent = new Intent();
                intent.putExtra("path", url);
                setResult(101, intent);
                CanTingAppLication.video_path=url+"%#%1";
                finish();
            }else {
                Intent intent = new Intent();
                setResult(103, intent);
                finish();
            }

        }
    }

    public static int getLocalVideoDuration(String videoPath) {
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return duration;
    }

    private double videoLength;
    private int timeLength;
    public  double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return FormetFileSize(blockSize, sizeType);
    }
    private  long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    private  long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();

        }
        return size;
    }
    private  double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    public   void getVideoThumb(String path) {

        MediaMetadataRetriever media = new MediaMetadataRetriever();

        media.setDataSource(path);
        mThumbnailPath = FileUtil.saveBitmap("JCamera", media.getFrameAtTime());
        getUpToken(mThumbnailPath);

    }
    private void getUpToken(final String path) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);


        Call<TOKEN> call = api.getUpToken();
        call.enqueue(new Callback<TOKEN>() {
            @Override
            public void onResponse(Call<TOKEN> call, Response<TOKEN> response) {
                upFlile(path, response.body().data.upToken);
            }

            @Override
            public void onFailure(Call<TOKEN> call, Throwable t) {

            }
        });
    }
    private String imgUrl;
    public void upFlile(String path, String token) {


        QiniuUtils.getInstance().upFile(path, token, new QiniuUtils.CompleteListener() {
            @Override
            public void completeListener(String urls) {
                imgUrl=QiniuUtils.baseurl+urls;

            }
        });

    }
    private String mThumbnailPath;
    public void initVideo(){
        String ossName  = "video/" + SpUtil.getUserInfoId(this)+"/"+ TimeUtil.formatTtimeNames(System.currentTimeMillis())+"."+TimeUtil.formatRedTime(System.currentTimeMillis()); //vodPath + index + ".mp4";
        url= Constant.APP_FILE_NAME+ossName;
        uploader.addFile(mOutputPath, "oss-cn-shenzhen.aliyuncs.com", Constant.FILE_NAME, ossName, getVodInfo());
        uploader.start();
    }
    private String mOutputPath = "";
    private VodInfo getVodInfo() {
        VodInfo vodInfo = new VodInfo();
        vodInfo.setTitle("标题" );
        vodInfo.setDesc("描述." );
        vodInfo.setCateId(1);
        vodInfo.setIsProcess(true);
        vodInfo.setIsShowWaterMark(false);
        vodInfo.setPriority(7);
        return vodInfo;
    }
    private String url;
    private ShapeLoadingDialog shapeLoadingDialog;

    private Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(shapeLoadingDialog!=null){
                shapeLoadingDialog.dismiss();
            }


            if(TextUtil.isNotEmpty(imgUrl)){
                CanTingAppLication.video_path=url+"%#%2"+"%#%"+imgUrl+"%#%"+videoLength+"%#%"+timeLength;
                Intent intent = new Intent();
                intent.putExtra("path", mOutputPath);
                setResult(101, intent);
                finish();
            }else {
                handler.sendMessageDelayed(new Message(),200);
            }

            return false;
        }
    });


    private VODUploadCallback callback = new VODUploadCallback() {
        @Override
        public void onUploadSucceed(UploadFileInfo info) {
            OSSLog.logDebug("onsucceed ------------------" + info.getFilePath());
            handler.sendEmptyMessage(1);



        }

        @Override
        public void onUploadFailed(UploadFileInfo info, String code, String message) {
            OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);


        }

        @Override
        public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
            OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);


        }

        @Override
        public void onUploadTokenExpired() {
            OSSLog.logError("onExpired ------------- ");
            // 实现时，重新获取STS临时账号用于恢复上传
//            uploader.resumeWithToken(accessKeyId, accessKeySecret, secretToken, expireTime);
        }

        @Override
        public void onUploadRetry(String code, String message) {
            OSSLog.logError("onUploadRetry ------------- ");
        }

        @Override
        public void onUploadRetryResume() {
            OSSLog.logError("onUploadRetryResume ------------- ");
        }

        @Override
        public void onUploadStarted(UploadFileInfo uploadFileInfo) {
            OSSLog.logError("onUploadStarted ------------- ");

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        aliLive aliLive= (aliLive) entity;
        if(aliLive!=null&& TextUtil.isNotEmpty(aliLive.token)){
            type=1;
            uploader.init(aliLive.accessKeyId, aliLive.accesskeysecret, aliLive.token, aliLive.expiration, callback);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
