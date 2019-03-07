package com.zhongchuang.canting.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.live.LiveActivity;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.easeui.ui.MessageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ChatSplashActivity extends AppCompatActivity {
    @BindView(R.id.iv_img)
    ImageView img;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.rl_bgs)
    RelativeLayout rlBgs;
    private GAME messageGroup;

    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);

        messageGroup = (GAME) getIntent().getSerializableExtra("data");
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            img.setImageResource(R.drawable.live_bg);
        } else {
            rlBgs.setVisibility(View.VISIBLE);
            img.setImageResource(R.mipmap.splash_05);
        }
        Handler handler = new Handler();
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (type == 1) {
                    Intent intent2 = new Intent(ChatSplashActivity.this, LiveActivity.class);
                    intent2.putExtra("data", messageGroup);
                    startActivity(intent2);
                    finish();
                } else {
                    Intent intents = new Intent(ChatSplashActivity.this, MessageActivity.class);
                    if (messageGroup == null) {
                        return;
                    }
                    intents.putExtra("data", messageGroup);
                    startActivity(intents);
                    finish();
                }


            }
        }, 1500);


    }
}
