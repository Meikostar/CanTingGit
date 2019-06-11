package com.zhongchuang.canting.activity.mall;

import android.support.v4.app.Fragment;

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

public class OrderMangerActivity extends BaseActivity1 {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tnb)
    FiveNevgBar tnb;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private int cout=0;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_order_manger);
        ButterKnife.bind(this);
        cout=getIntent().getIntExtra("type",0);
        addFragment();
        navigationBar.setNavigationBarListener(this);
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(4);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(cout);
        tnb.setSelect(cout);
        viewpagerMain.setScanScroll(false);

    }

    private OrderMangerfagment fragment1;
    private OrderMangerfagment fragment2;
    private OrderMangerfagment fragment3;
    private OrderMangerfagment fragment4;
    private OrderMangerfagment fragment5;

    private void addFragment() {
        mFragments = new ArrayList<>();
        fragment1 = new OrderMangerfagment();
        fragment1.setType(0);
        fragment2 = new OrderMangerfagment();
        fragment2.setType(1);
        fragment3 = new OrderMangerfagment();
        fragment3.setType(2);
        fragment4 = new OrderMangerfagment();
        fragment4.setType(3);
        fragment5 = new OrderMangerfagment();
        fragment5.setType(4);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        mFragments.add(fragment3);
        mFragments.add(fragment4);
        mFragments.add(fragment5);

    }

    @Override
    public void bindEvents() {

        tnb.setOnChangeListener(new OnChangeListener() {
            @Override
            public void onChagne(int currentIndex) {
                viewpagerMain.setCurrentItem(currentIndex);
            }
        });

    }


    @Override
    public void initData() {

    }


}
