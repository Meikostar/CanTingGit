package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/26.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.iv_img)
    ImageView img;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.rl_bgs)
    RelativeLayout rlBgs;
    private int status;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        rlBg.setVisibility(View.GONE);
        setLoginMessage();
        updateActivity(SpUtil.getLangueType(this));

        Handler handler = new Handler();
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    if (TextUtil.isEmpty(avar)) {
                        if (SpUtil.getInt(SplashActivity.this, "loginCounts", 0) == 1 || SpUtil.getInt(SplashActivity.this, "loginCounts", 0) == 2) {
                            Intent registToZhuFrag = new Intent(SplashActivity.this, PersonMessageActivity.class);
                            registToZhuFrag.putExtra("type", 1);
                            startActivity(registToZhuFrag);
                            finish();
                        } else {
                            goActivity();
                        }
                    } else {
                        goActivity();
                    }


                } else {
                    goActivity();
                }


            }
        }, 2000);


    }
    /**
     * 刷新语言
     */
    public void updateActivity(String sta) {
        CanTingAppLication.LangueType=sta;
        SpUtil.putString(this,"LangueType",sta);
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }
    public void goActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivitys.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    public boolean isLogin;
    public String avar;


    private void setLoginMessage() {
        String token = SpUtil.getString(this, "token", "");
        avar = SpUtil.getString(this, "ava", "");
        isLogin = !TextUtils.isEmpty(token);


    }

}
