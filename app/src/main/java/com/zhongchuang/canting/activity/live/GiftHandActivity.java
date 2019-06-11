package com.zhongchuang.canting.activity.live;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.fragment.mall.HandFragment;
import com.zhongchuang.canting.fragment.mall.SeachFragment;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康提醒
 */
public class GiftHandActivity extends BaseActivity1 {


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
    @BindView(R.id.tv_state4)
    TextView tvState4;
    @BindView(R.id.ivlines)
    ImageView ivlines;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.ll_find)
    LinearLayout llFind;
    private int type;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_hand_gift);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        selectPotion(1);

    }


    private int status;

    @Override
    public void bindEvents() {

        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {


            }

            @Override
            public void navigationimg() {

            }
        });
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
        llFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPotion(4);
            }
        });

    }


    @Override
    public void initData() {
        addFragment();
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(3);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(0);
        viewpagerMain.setScanScroll(false);
    }


    private HandFragment handFragment1;
    private HandFragment handFragment2;
    private HandFragment handFragment3;
    private SeachFragment handFragment4;

    private void addFragment() {
        mFragments = new ArrayList<>();
        handFragment1 = new HandFragment(this);
        handFragment2 = new HandFragment(this);
        handFragment3 = new HandFragment(this);
        handFragment4 = new SeachFragment(this);
        handFragment1.setType(1);
        handFragment2.setType(2);
        handFragment3.setType(3);
        mFragments.add(handFragment1);
        mFragments.add(handFragment2);
        mFragments.add(handFragment3);
        mFragments.add(handFragment4);

    }


    public void selectPotion(int poition) {
        switch (poition) {
            case 1:
                tvState1.setTextColor(getResources().getColor(R.color.blue));
                tvState2.setTextColor(getResources().getColor(R.color.color6));
                tvState3.setTextColor(getResources().getColor(R.color.color6));
                tvState4.setTextColor(getResources().getColor(R.color.color6));

                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                viewpagerMain.setCurrentItem(0);
                break;
            case 2:
                tvState1.setTextColor(getResources().getColor(R.color.color6));
                tvState2.setTextColor(getResources().getColor(R.color.blue));
                tvState3.setTextColor(getResources().getColor(R.color.color6));
                tvState4.setTextColor(getResources().getColor(R.color.color6));

                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                line3.setVisibility(View.INVISIBLE);
                viewpagerMain.setCurrentItem(1);
                break;
            case 3:
                tvState1.setTextColor(getResources().getColor(R.color.color6));
                tvState2.setTextColor(getResources().getColor(R.color.color6));
                tvState3.setTextColor(getResources().getColor(R.color.blue));
                tvState4.setTextColor(getResources().getColor(R.color.color6));

                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.VISIBLE);
                viewpagerMain.setCurrentItem(2);
                break;
            case 4:
                tvState1.setTextColor(getResources().getColor(R.color.color6));
                tvState2.setTextColor(getResources().getColor(R.color.color6));
                tvState3.setTextColor(getResources().getColor(R.color.color6));
                tvState4.setTextColor(getResources().getColor(R.color.blue));

                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.INVISIBLE);
                line3.setVisibility(View.INVISIBLE);
                viewpagerMain.setCurrentItem(3);
                break;
        }
    }



}
