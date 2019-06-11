package com.zhongchuang.canting.activity.mine;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.adapter.recycle.ChargeDetailRecycleAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.fragment.ChargetDetailFragment;
import com.zhongchuang.canting.fragment.ChatHandFragment;
import com.zhongchuang.canting.fragment.SeachChargeFragment;
import com.zhongchuang.canting.fragment.message.SeachChatFragment;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康提醒
 */
public class ChargetHandActivity extends BaseActivity1 {


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
        setContentView(R.layout.activity_charge_detail);
        ButterKnife.bind(this);
        status=getIntent().getIntExtra("type",0);
        state=getIntent().getIntExtra("status",0);
        if(status==1){
            navigationBar.setNaviTitle(getString(R.string.czkyjf));
            type=4;
        }else if(status==2){
            type=3;
            navigationBar.setNaviTitle(getString(R.string.szjzmx));
        }else if(status==3){
            type=1;
            navigationBar.setNaviTitle(getString(R.string.llkyjf));
        }else if(status==4){
            type=2;
            navigationBar.setNaviTitle(getString(R.string.zbkyjf));
        }

//        tvState1.setText("今日");
//        tvState2.setText("本周");
//        tvState3.setText("本月");
//        tvState4.setText("查找");

        navigationBar.setNavigationBarListener(this);
        selectPotion(1);

    }


    private int status;
    private int state;

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


    private ChargetDetailFragment handFragment1;
    private ChargetDetailFragment handFragment2;
    private ChargetDetailFragment handFragment3;
    private SeachChargeFragment handFragment4;

    private void addFragment() {
        mFragments = new ArrayList<>();
        handFragment1 = new ChargetDetailFragment(this);
        handFragment2 = new ChargetDetailFragment(this);
        handFragment3 = new ChargetDetailFragment(this);
        handFragment4 = new SeachChargeFragment(this);
        handFragment1.setType(0,state==0?status:state);
        handFragment2.setType(1,state==0?status:state);
        handFragment3.setType(2,state==0?status:state);
        handFragment4.setType(type);
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
