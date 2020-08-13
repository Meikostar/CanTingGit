package com.zhongchuang.canting.activity.offline;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.fragment.mall.OrderMangerfagment;
import com.zhongchuang.canting.presenter.OnChangeListener;
import com.zhongchuang.canting.widget.FiveNevgBar;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class AuditOfflineActivity extends BaseActivity1 {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_state1)
    TextView tvState1;
    @BindView(R.id.line1)
    ImageView line1;
    @BindView(R.id.tv_state2)
    TextView tvState2;
    @BindView(R.id.line2)
    ImageView line2;
    @BindView(R.id.tv_state3)
    TextView tvState3;
    @BindView(R.id.line3)
    ImageView line3;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private int cout=0;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_audit_offline);
        ButterKnife.bind(this);
        cout=getIntent().getIntExtra("type",0);
        addFragment();
        navigationBar.setNavigationBarListener(this);
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(3);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(cout);

        viewpagerMain.setScanScroll(false);

    }

    private AuditOfflinefagment fragment1;
    private AuditOfflinefagment fragment2;
    private AuditOfflinefagment fragment3;


    private void addFragment() {
        mFragments = new ArrayList<>();
        fragment1 = new AuditOfflinefagment();
        fragment1.setType(0);
        fragment2 = new AuditOfflinefagment();
        fragment2.setType(1);
        fragment3 = new AuditOfflinefagment();
        fragment3.setType(2);

        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);


    }

    public void selectPotion(int poition) {
        switch (poition) {
            case 1:
                tvState1.setTextColor(getResources().getColor(R.color.blue));
                tvState2.setTextColor(getResources().getColor(R.color.color6));
                tvState3.setTextColor(getResources().getColor(R.color.color6));


                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                viewpagerMain.setCurrentItem(0);
                break;
            case 2:
                tvState1.setTextColor(getResources().getColor(R.color.color6));
                tvState2.setTextColor(getResources().getColor(R.color.blue));
                tvState3.setTextColor(getResources().getColor(R.color.color6));


                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                viewpagerMain.setCurrentItem(1);
                break;
            case 3:
                tvState1.setTextColor(getResources().getColor(R.color.color6));
                tvState2.setTextColor(getResources().getColor(R.color.color6));
                tvState3.setTextColor(getResources().getColor(R.color.blue));


                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                viewpagerMain.setCurrentItem(2);
                break;
        }
    }
    @Override
    public void bindEvents() {

        tvState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPotion(1);
            }
        });
        tvState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPotion(2);
            }
        });
        tvState3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPotion(3);
            }
        });

    }


    @Override
    public void initData() {

    }


}
