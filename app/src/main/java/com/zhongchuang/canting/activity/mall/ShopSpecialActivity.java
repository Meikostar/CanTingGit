package com.zhongchuang.canting.activity.mall;


import android.support.v4.app.Fragment;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.mall.GoodSpecialFragment;
import com.zhongchuang.canting.widget.GoodsSearchViews;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class ShopSpecialActivity extends BaseActivity1 {


    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.gds)
    GoodsSearchViews gds;


    private String content;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private int status=1;//1商城2积分商城

    @Override
    public void initViews() {
        setContentView(R.layout.activity_specail_product);

        ButterKnife.bind(this);
        content = getIntent().getStringExtra("data");
        status = getIntent().getIntExtra("state", 1);
        addFragment();

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(0);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(0);
        viewpagerMain.setScanScroll(false);
        gds.cancelFocuse();

    }

    private GoodSpecialFragment newFragment;

    private void addFragment() {

        mFragments = new ArrayList<>();
        newFragment = new GoodSpecialFragment();
        newFragment.setType(1, content, status);
        mFragments.add(newFragment);

    }
    private Subscription mSubscription;
    @Override
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
               if(bean.type==SubscriptionBean.SEAECH){
                   finish();
               }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        gds.registerListener(new GoodsSearchViews.OnSearchBoxClickListener() {
            @Override
            public void onClean() {

            }

            @Override
            public void onCancle() {

            }

            @Override
            public void finishs(int type) {
                if(type==1){
                    finish();
                 }


            }

            @Override
            public void onKeyWordsChange(String keyWords) {

            }
        });



    }


    @Override
    public void initData() {


    }


}
