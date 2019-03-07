package com.zhongchuang.canting.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.adapter.RecordAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.MainChild;
import com.zhongchuang.canting.fragment.message.QFriendCircleFragment;
import com.zhongchuang.canting.presenter.MainFragmentPresenter;
import com.zhongchuang.canting.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class QfriendActivity extends BaseActivity1 {


    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.zhuye_geren)
    ImageView zhuyeGeren;
    @BindView(R.id.serch_edit)
    TextView serchEdit;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    private Intent perIntent;
    private Unbinder bind;

    private MainFragmentPresenter presenter;

    private View view;


    private int state;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private String ids;
    private String id;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_record_db);
        ButterKnife.bind(this);
        llBg.setVisibility(View.GONE);
        ids=getIntent().getStringExtra("ids");
        id=getIntent().getStringExtra("id");
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {
        addFragment();
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(0);
        viewpagerMain.setScanScroll(false);
    }

    private RecordAdapter adapter;
    private QFriendCircleFragment handFragment3;

    private void addFragment() {
        mFragments = new ArrayList<>();

        handFragment3 = new QFriendCircleFragment();
        handFragment3.setType(1);
        handFragment3.setData(ids);
        handFragment3.setId(id);
        mFragments.add(handFragment3);

    }

    private void initLiveStreamBanner(List<MainChild> imgMeiShiReses) {

    }


    @Override
    public void onResume() {
        super.onResume();

//        mainBannertwo.startTurning(4000);
//        mainBannerThree.startTurning(4000);
    }

    @Override
    public void onStop() {
        super.onStop();
        dimessProgress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }



}

