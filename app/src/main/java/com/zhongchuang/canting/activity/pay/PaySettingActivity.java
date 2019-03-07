package com.zhongchuang.canting.activity.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.CityPickerActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaySettingActivity extends BaseAllActivity implements OtherContract.View{


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.login_pass)
    ClearEditText loginPass;
    @BindView(R.id.login_pass_again)
    ClearEditText loginPassAgain;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    private OtherPresenter presenter;
    private int type;//1 firt  0 second
    @Override
    public void initViews() {
        setContentView(R.layout.activity_pay_setting);
        ButterKnife.bind(this);
        type=getIntent().getIntExtra("type",0);
        presenter=new OtherPresenter(this);
        if(type==1){
            tvSearch.setText(getString(R.string.szzfmm));
            tvHint.setText(R.string.szzfmmyykcjf);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==11){
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.PAYSET_FIN,""));
            CanTingAppLication.isSetting=true;
            dimessProgress();
            showToasts(getString(R.string.szcg));
            finish();
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }



    private ProgressDialog mDialog;


    @Override
    public void bindEvents() {
       tvSure.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(TextUtil.isEmpty(loginPass.getText().toString())){
                   showToasts(getString(R.string.qsrmm));
                   return;
               } if(TextUtil.isEmpty(loginPassAgain.getText().toString())){
                   showToasts(getString(R.string.qqrmm));
                   return;
               }if(!loginPass.getText().toString().trim().equals(loginPassAgain.getText().toString().trim())){
                   showToasts(getString(R.string.lcmmbyz));
                   return;
               }
               if(loginPassAgain.getText().toString().length()!=6){
                   showToasts(getString(R.string.mmwlws));
                   return;
               }
               showProgress(getString(R.string.szz));
               presenter.setPaymentPassword(loginPass.getText().toString(),loginPassAgain.getText().toString());
           }
       });
    }

    @Override
    public void initData() {

    }




}