package com.zhongchuang.canting.activity.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.RigsterActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangPaySmsPActivity extends BaseAllActivity implements OtherContract.View{



    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.login_phone)
    TextView loginPhone;
    @BindView(R.id.login_yanzhen_send)
    TextView sendCode;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.et_code)
    ClearEditText etCode;
   private TimeCount timeCount;


    private OtherPresenter presenter;
    private String mobileNumber;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_pay_sms);
        ButterKnife.bind(this);
        timeCount = new TimeCount(60000, 1000);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         mobileNumber = SpUtil.getMobileNumber(this);
        presenter=new OtherPresenter(this);
        if (TextUtil.isNotEmpty(mobileNumber)) {
            loginPhone.setText(mobileNumber);
        }
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode(mobileNumber);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timeCount!=null){
            timeCount.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void bindEvents() {
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtil.isEmpty(etCode.getText().toString().trim())){
                    showToasts(getString(R.string.qsryzm));
                    return;
                }
                showProgress(getString(R.string.jyz));
                presenter.payCheckCode(mobileNumber,etCode.getText().toString());

            }
        });


    }
    @Override
    public <T> void toEntity(T entity, int type) {
            dimessProgress();
            startActivity(new Intent(ChangPaySmsPActivity.this,PaySettingActivity.class));
            finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }

    public void getCode(String mobile) {


        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", mobile);
        map.put("smsType",  6+"");
        map.put("type", 1+"");
        map.put("companyType", CanTingAppLication.CompanyType);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getCall(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse bean) {
                timeCount.start();
                sendCode.setBackground(getResources().getDrawable(R.drawable.hui_blue_rectangle));
                sendCode.setEnabled(false);
                ToastUtils.showNormalToast(getString(R.string.fscg));
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
                sendCode.setEnabled(true);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    //计时器
 class TimeCount extends CountDownTimer {

     public TimeCount(long millisInFuture, long countDownInterval) {
         super(millisInFuture, countDownInterval);
     }

     @Override
     public void onTick(long millisUntilFinished) {
         if(sendCode!=null){
             sendCode.setEnabled(false);
             sendCode.setText(millisUntilFinished / 1000 + getString(R.string.scshq));
         }

     }

     @Override
     public void onFinish() {
         if(sendCode!=null){
             sendCode.setText(getString(R.string.cshq));
             sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
             sendCode.setEnabled(true);
         }

     }
 }

    private ProgressDialog mDialog;




    @Override
    public void initData() {

    }


    private void loginHx(final CodeCheckBean.DataBean db) {
    }


}