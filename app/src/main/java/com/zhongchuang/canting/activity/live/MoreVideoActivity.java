package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.fragment.live.VideoLiveMoreFragment;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreVideoActivity extends BaseAllActivity implements  OtherContract.View {


    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;

    private VideoLiveMoreFragment videofragment;

    protected FragmentTransaction mTransaction;
    private OtherPresenter presenter;


    private String id;
    private int type;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_more_video);
        ButterKnife.bind(this);



        id= getIntent().getStringExtra("id");
        type= getIntent().getIntExtra("type",0);
        videofragment = new VideoLiveMoreFragment(this,id,type);
        mTransaction = getSupportFragmentManager().beginTransaction();
        //pass parameters to chat fragment
        videofragment.setArguments(getIntent().getExtras());
        mTransaction.replace(R.id.fragment_container, videofragment);
        mTransaction.commit();

    }

    @Override
    public void bindEvents() {


    }

    private String userInfoId;


    @Override
    public void initData() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    protected void onResume() {
        super.onResume();
    }















    @Override
    public <T> void toEntity(T entity, int type) {
        aliLive aliLive = (aliLive) entity;
        if (aliLive != null && TextUtil.isNotEmpty(aliLive.pushurl)) {
            SpUtil.putString(this, "live_url", aliLive.pushurl);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

        showToasts(msg);
    }


}




