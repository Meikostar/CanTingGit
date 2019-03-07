package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.DbRecordActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.City;
import com.zhongchuang.canting.been.OrderType;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;


public class BalanceActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    private BasesPresenter presenter;
    private String data;



    @Override
    public void initViews() {
        setContentView(R.layout.balance_view);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        data= getIntent().getStringExtra("data");
        if(TextUtil.isNotEmpty(data)){
            tvBalance.setText(data);
        }else {
            presenter.userInfo();
        }

    }

    @Override
    public void bindEvents() {
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {
                Intent intent = new Intent(BalanceActivity.this, DbRecordActivity.class);
                intent.putExtra("state",1);
                startActivity(intent);
            }

            @Override
            public void navigationimg() {

            }
        });
        tvCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BalanceActivity.this,RechargeActivity.class));
            }
        });
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.CHAEGE_SUCCESS){
                    presenter.userInfo();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
    }

    @Override
    public void initData() {

    }

    private Subscription mSubscription;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        OrderType orderType = (OrderType) entity;
        if(orderType!=null&&TextUtil.isNotEmpty(orderType.user_integral)){
            tvBalance.setText(orderType.user_integral);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}

