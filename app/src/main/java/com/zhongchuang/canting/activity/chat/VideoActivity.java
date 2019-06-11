package com.zhongchuang.canting.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.cameralibrary.util.DeviceUtil;


public class VideoActivity extends AppCompatActivity {

    private String path;
//    private JzvdStd mVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_videos);
        path=getIntent().getStringExtra("path");
//
//        mVideoView = (JzvdStd) findViewById(R.id.video_preview);
//        mVideoView.setUp(path, "饺子闭眼睛");
//        mVideoView.thumbImageView.setImageDrawable(getResources().getDrawable(R.drawable.splash_1));
//        mVideoView.getHolder().addCallback(this);
//        // 设置这两句切换时会直接进入横屏全屏模式
//        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向
//        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向
//
////        设置播放器播放地址,标题
//        jzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
//                ,JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "好孩子" );

//        //设置播放器封面
//        Glide.with(this)
//                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
//                .into(jzvdStd.thumbImageView);

        //设置视频保存路径



        Log.i("CJT", DeviceUtil.getDeviceModel());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
    }
    private MediaPlayer mMediaPlayer;
    private Bitmap captureBitmap;   //捕获的图片
    private Bitmap firstFrame;      //第一帧图片
    private String videoUrl;        //视频URL


    @Override
    public void onBackPressed() {
//        if (mVideoView.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        mVideoView.removeAllViews();
    }
}
