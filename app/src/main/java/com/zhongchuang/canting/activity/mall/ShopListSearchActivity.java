package com.zhongchuang.canting.activity.mall;


import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.GoodFragment;
import com.zhongchuang.canting.presenter.OnChangeListener;
import com.zhongchuang.canting.widget.GoodsSearchViews;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.ThreeNevgBar;

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

public class ShopListSearchActivity extends BaseActivity1 {


    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.gds)
    GoodsSearchViews gds;
    @BindView(R.id.tnb)
    ThreeNevgBar tnb;

    private String content;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private int status=1;//1商城2积分商城

    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_search);
        ButterKnife.bind(this);
        content = getIntent().getStringExtra("data");
        status = getIntent().getIntExtra("state", 1);
        addFragment();

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(2);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(0);
        viewpagerMain.setScanScroll(false);
        gds.cancelFocuse();

    }

    private GoodFragment newFragment;
    private GoodFragment countFragment;
    private GoodFragment priceFragment;

    private void addFragment() {
        mFragments = new ArrayList<>();
        newFragment = new GoodFragment();
        newFragment.setType(1, content, status);
        countFragment = new GoodFragment();
        countFragment.setType(2, content, status);
        priceFragment = new GoodFragment();
        priceFragment.setType(3, content, status);
        mFragments.add(newFragment);
        mFragments.add(countFragment);
        mFragments.add(priceFragment);

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
                }else {
                    Intent intent = new Intent(ShopListSearchActivity.this, SearchGoodActivity.class);
                    intent.putExtra("data",content!=null?content:"");
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onKeyWordsChange(String keyWords) {

            }
        });

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
