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
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.activity.live.LiveActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.easeui.ui.MessageActivity;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/26.
 */

public class ChatSplashActivity extends AppCompatActivity implements BaseContract.View {
    @BindView(R.id.iv_img)
    ImageView img;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.rl_bgs)
    RelativeLayout rlBgs;
    private GAME messageGroup;
    private BasesPresenter presenter;
    private int type;
    private  Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        presenter.hostInfo();
        messageGroup = (GAME) getIntent().getSerializableExtra("data");
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            img.setImageResource(R.drawable.live_bg);
        } else {
            rlBgs.setVisibility(View.VISIBLE);
            img.setImageResource(R.mipmap.splash_05);
        }
        handler = new Handler();


    }

    @Override
    public <T> void toEntity(T entity, int type) {
        Host data = (Host) entity;
        if (data != null && TextUtil.isNotEmpty(data.is_direct)) {

            if (data.is_direct.equals("1")) {
                if (TextUtil.isNotEmpty(data.user_integral)) {
                    CanTingAppLication.totalintegral = Double.valueOf(data.user_integral);
                } else {
                    if (TextUtil.isNotEmpty(data.userIntegral)) {
                        CanTingAppLication.totalintegral = Double.valueOf(data.userIntegral);
                    }
                }
                SpUtil.putString(ChatSplashActivity.this, "isAnchor", 1 + "");
            } else {
                if (TextUtil.isNotEmpty(data.userIntegral)) {
                    CanTingAppLication.totalintegral = Double.valueOf(data.userIntegral);
                } else {
                    if (TextUtil.isNotEmpty(data.user_integral)) {
                        CanTingAppLication.totalintegral = Double.valueOf(data.user_integral);
                    }
                }
                SpUtil.putString(ChatSplashActivity.this, "isAnchor", 0 + "");
            }
        }
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ChatSplashActivity.this.type == 1) {
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
        }, 1200);

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
