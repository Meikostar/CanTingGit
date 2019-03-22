package com.zhongchuang.canting.allive.editor.publish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.sdk.android.vod.upload.model.SvideoInfo;
import com.zhongchuang.canting.R;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.qupai.editor.AliyunICompose;
import com.aliyun.qupaiokhttp.HttpRequest;
import com.aliyun.qupaiokhttp.RequestParams;
import com.aliyun.qupaiokhttp.StringHttpRequestCallback;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by macpro on 2017/11/8.
 */

public class UploadActivity extends Activity implements  OtherContract.View {

    public static final String KEY_UPLOAD_VIDEO = "video_path";
    public static final String KEY_UPLOAD_THUMBNAIL = "video_thumbnail";
    public static final String KEY_UPLOAD_DESC = "video_desc";

    private ImageView mIvLeft;
    private TextView mTitle;
    private TextureView mTextureView;
    private ProgressBar mProgress;
    private TextView mVideoDesc;
    private TextView mProgressText;
    private MediaPlayer mMediaPlayer;
    private String mVideoPath;
    private String mThumbnailPath;
    private String mDesc;
    private boolean mIsUpload;
    private AliyunICompose mComposeClient;
   private OtherPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_svideo_activity_upload);
        presenter=new OtherPresenter(this);
        presenter.getLiveToken();
        initView();
        mVideoPath = getIntent().getStringExtra(KEY_UPLOAD_VIDEO);
        mThumbnailPath = getIntent().getStringExtra(KEY_UPLOAD_THUMBNAIL);
        mDesc = getIntent().getStringExtra(KEY_UPLOAD_DESC);
        mVideoDesc.setText(mDesc);

        mTextureView.setSurfaceTextureListener(mlistener);
        mComposeClient = ComposeFactory.INSTANCE.getInstance();

    }
   private String token;
    private void initView(){
        mTitle = (TextView) findViewById(R.id.tv_center);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(R.string.my_video);
        mVideoDesc = (TextView) findViewById(R.id.video_desc);
        mTextureView = (TextureView) findViewById(R.id.texture);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvLeft.setVisibility(View.VISIBLE);
        mIvLeft.setImageResource(R.drawable.aliyun_svideo_icon_cancel);
        mProgress = (ProgressBar) findViewById(R.id.upload_progress);
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mProgressText = (TextView) findViewById(R.id.progress_text);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(mIsUpload){
            mComposeClient.resumeUpload();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIsUpload){
            mComposeClient.pauseUpload();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mMediaPlayer != null){
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mComposeClient.release();
    }




    private final AliyunICompose.AliyunIUploadCallBack mUploadCallback = new AliyunICompose.AliyunIUploadCallBack() {
        @Override
        public void onUploadSucceed(String videoId, String imageUrl) {
            String imageUrls=imageUrl;
            String videoIds=videoId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.GONE);

                    mProgressText.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.aliyun_svideo_icon_composite_success, 0, 0, 0);
                    mProgressText.setText(R.string.upload_success);
                    mProgressText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressText.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            });

        }

        @Override
        public void onUploadFailed(String code, String message) {
            Log.e(AliyunTag.TAG, "onUploadFailed, errorCode:"+code+", msg:"+message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.setProgress(0);
                    mProgressText.setText(R.string.upload_failed);
                    mProgressText.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressText.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            });

        }

        @Override
        public void onUploadProgress(final long uploadedSize, final long totalSize) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int progress = (int)((uploadedSize * 100)/totalSize);
                    mProgress.setProgress(progress);
                    mProgressText.setText("正在上传 " + progress + "%");
                }
            });

        }

        @Override
        public void onUploadRetry(String code, String message) {

        }

        @Override
        public void onUploadRetryResume() {

        }

        @Override
        public void onSTSTokenExpired() {

        }
    };

    private final TextureView.SurfaceTextureListener mlistener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            initVideoPlayer();
            try{
                mMediaPlayer.setDataSource(mVideoPath);
                mMediaPlayer.setSurface(new Surface(surface));
                mMediaPlayer.prepareAsync();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private void initVideoPlayer(){
        if(mMediaPlayer != null){
            return ;
        }
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setScreenOnWhilePlaying(true);

        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setLooping(true);

    }

    private final MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            int vw = mp.getVideoWidth();
            int vh = mp.getVideoHeight();

            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            mTextureView.getLayoutParams().width = screenWidth;
            mTextureView.getLayoutParams().height = (int)((float)vh * screenWidth / vw);
        }
    };

    @Override
    public <T> void toEntity(T entity, int type) {
        SvideoInfo info = new SvideoInfo();
        info.setTitle("video");
        info.setDesc(mDesc);
        info.setCateId(1);
        aliLive aliLive= (aliLive) entity;
        if(aliLive!=null&& TextUtil.isNotEmpty(aliLive.token)){
            token=aliLive.token;
            int rv = mComposeClient.uploadWithVideoAndImg(
                    mThumbnailPath,
                    CanTingAppLication.sk,
                    CanTingAppLication.stcry,
                    token,
                    TimeUtil.formatTime(System.currentTimeMillis()),
                    info, false,
                    mUploadCallback);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
