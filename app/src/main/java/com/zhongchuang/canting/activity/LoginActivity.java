package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.LoginAdapter;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.Login_PassedwordFragment;
import com.zhongchuang.canting.fragment.Login_PhoneFragment;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;


/**
 * Created by Administrator on 2017/10/27.
 */

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.shezhi_regest)
    TextView shezhiRegest;
    @BindView(R.id.tab_FindFragment_title)
    TabLayout tabLoginFragmentTitle;
    @BindView(R.id.vp_FindFragment_pager)
    ViewPager vpLoginFragmentPager;
    @BindView(R.id.iv_back)
    ImageView ivback;
    private FragmentPagerAdapter fAdapter;                 
    //定义adapter


    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表

    private Login_PhoneFragment phoneLoginFragment;              //手机验证码登录fragment
    private Login_PassedwordFragment passedwordLoginFragment;            //注册密码收藏fragment
   private int status;
   private int type;
   private Subscription mSubscription;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        status=getIntent().getIntExtra("status",0);
        type=getIntent().getIntExtra("type",0);

        //登录tab
        initData();

        //tab下的fragment
        initFragments();
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.LOGIN_FINISH){
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
        //数据联动
        fAdapter = new LoginAdapter(getSupportFragmentManager(),list_fragment,list_title);
        vpLoginFragmentPager.setAdapter(fAdapter);
        tabLoginFragmentTitle.setupWithViewPager(vpLoginFragmentPager);

        setEvents();

    }

    private void setEvents() {
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shezhiRegest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logintent = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(logintent);
            }
        });
    }


    private void initFragments() {

        //初始化各fragment
        phoneLoginFragment = new Login_PhoneFragment();
        passedwordLoginFragment = new Login_PassedwordFragment();
        passedwordLoginFragment.setType(type);
        phoneLoginFragment.setType(type);
        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(passedwordLoginFragment);
        list_fragment.add(phoneLoginFragment);


    }


    private void initData() {
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add(getString(R.string.zhdl));
        list_title.add(getString(R.string.sjkjdl));

    }


    @OnClick(R.id.shezhi_regest)
    public void onViewClicked() {
        Intent logintent=new Intent(this,RegistActivity.class);
        startActivity(logintent);
}


    public void gotoZhuyeFrag() {
        if(status!=0){
            finish();
        }else {
            Intent gotoZhuYe=new Intent(this,HomeActivitys.class);
            startActivity(gotoZhuYe);
            finish();
        }


    }
}

