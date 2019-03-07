package com.zhongchuang.canting.fragment.mall;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Shops;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.TwoNevgBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/10/25.
 */

public class ShopCarFragment extends BaseFragment {



    @BindView(R.id.base_bar)
    TwoNevgBar baseBar;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.tv_editor)
    TextView tv_editor;
    Unbinder unbinder;
    private List<Fragment> mFragments;
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private int current = 0;
    public ShopCarFragment() {
    }

    public int  type;
    public void setType(int type){
        this.type=type;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_car, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private int status=0;//0 完成1 删除
    private Subscription mSubscription;

    private void initView() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.CAR_CHANGEA){
                    status=0;
                    tv_editor.setText(getString(R.string.bj));
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        setViewPagerListener();
        setNevgBarChangeListener();
        addFragment();
        tv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status==0){
                    status=1;
                    tv_editor.setText(getString(R.string.shanchu));
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CAR_CHANGE,status));
                }else {
                    status=0;
                    tv_editor.setText(getString(R.string.bj));
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CAR_CHANGE,status));
                }
            }
        });
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(current);
    }
    private Carfagment carFragment1;
    private Carfagment carFragment2;
    private void addFragment() {
        mFragments = new ArrayList<>();
//        if(type!=0){
//            carFragment1 = new Carfagment(3);
//        }else {
//            carFragment1 = new Carfagment(1);
//        }

        carFragment2 = new Carfagment(type);
        mFragments.add(carFragment2);


    }
    private void setNevgBarChangeListener() {
        baseBar.setOnChangeListener(new TwoNevgBar.OnChangeListener() {
            @Override
            public void onChagne(int currentIndex) {
                current = currentIndex;
                baseBar.setSelect(currentIndex);
                viewpagerMain.setCurrentItem(currentIndex);
            }
        });
    }

    private void setViewPagerListener() {
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                baseBar.setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    private Shops shops;
    private List<Shops> datas = new ArrayList<>();


}
