package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.ChatSplashActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallActivity;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.CornerTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;


public class OtherAppActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.app_name)
    TextView appName;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.card2)
    CardView card2;
    @BindView(R.id.card3)
    CardView card3;
    @BindView(R.id.card4)
    CardView card4;
    private BasesPresenter presenter;
    private String data;


    private Subscription mSubscription;

    private String content;
    private int rid;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void selectioin(int poistion){
        switch (poistion){
            case 0: //商城
//                        Intent intentsss = new Intent(HomeActivitys.this, FaceCreatActivity.class);
                Intent intentsss = new Intent(OtherAppActivity.this, ShopCompsiteMallActivity.class);
                intentsss.putExtra("type", 1);
                startActivity(intentsss);

                break;
            case 1://乐聊

                Intent intent = new Intent(OtherAppActivity.this, ShopMallActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);

                break;
            case 2://乐聊
                Intent intentss = new Intent(OtherAppActivity.this, ShopMallActivity.class);
                intentss.putExtra("type", 2);
                startActivity(intentss);


                break;
            case 3://直播

                if (HomeActivitys.isLogin) {
                    Intent intent2 = new Intent(OtherAppActivity.this, ChatSplashActivity.class);

//                            intent2.putExtra("data", data);
                    intent2.putExtra("type", 1);
                    startActivity(intent2);
                } else {
                    startActivity(new Intent(OtherAppActivity.this, LoginActivity.class));
                }
                break;

        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_other_app);
        ButterKnife.bind(this);

        content=getIntent().getStringExtra("data");
        appName.setText(content);
        rid=getIntent().getIntExtra("type",0);
        logo.setImageDrawable(getResources().getDrawable(Constant.homeimg[rid]));
//        Glide.with(this)
//                .load(Constant.homeimg[rid])
//                .asBitmap()
//                .skipMemoryCache(true)
//                .placeholder(R.drawable.moren1)
//                .error(R.drawable.moren1)
//                .transform(new CornerTransform(this, DensityUtil.dip2px(8)))
//                .into(logo);
    }

    @Override
    public void bindEvents() {
       ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       card1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               selectioin(0);
           }
       });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectioin(1);
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectioin(2);
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectioin(3);
            }
        });
    }

    @Override
    public void initData() {

    }
}

