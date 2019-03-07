package com.zhongchuang.canting.activity.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
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
import rx.Subscription;
import rx.functions.Action1;

public class ChangePayActivity extends BaseAllActivity implements OtherContract.View{


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.old_pass)
    ClearEditText oldPass;
    @BindView(R.id.login_pass)
    ClearEditText loginPass;
    @BindView(R.id.login_pass_again)
    ClearEditText loginPassAgain;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_forget_pswd)
    TextView tvForgetPswd;
     private OtherPresenter presenter;
     private Subscription mSubscription;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_change_pay);
        ButterKnife.bind(this);

        presenter=new OtherPresenter(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.PAYSET_FIN) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
     if(mSubscription!=null){
         mSubscription.unsubscribe();
     }
    }

    @Override
    public void onResume() {
        super.onResume();

    }




    private ProgressDialog mDialog;


    @Override
    public void bindEvents() {
      tvForgetPswd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(ChangePayActivity.this,ChangPaySmsPActivity.class));
          }
      });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isEmpty(oldPass.getText().toString())){
                    showToasts(getString(R.string.qsrjmm));
                    return;
                }
                if(TextUtil.isEmpty(loginPass.getText().toString())){
                    showToasts(getString(R.string.qsrmm));
                    return;
                }
                if(loginPass.getText().toString().trim().equals(oldPass.getText().toString().trim())){
                    showToasts(getString(R.string.ggmmbnhymmyz));
                    return;
                }
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
                }if(oldPass.getText().toString().length()!=6){
                    showToasts(getString(R.string.jmmcw));
                    return;
                }
                showProgress(getString(R.string.ghzs));
                presenter.alterPaymentPassword(oldPass.getText().toString(),loginPass.getText().toString(),loginPassAgain.getText().toString());
            }
        });
    }

    @Override
    public void initData() {

    }


    private void loginHx(final CodeCheckBean.DataBean db) {
    }


    public void changePassWord(String code, String phone, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", phone);
        map.put("code", code);
        map.put("pwd", password);
        map.put("password", password);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.updatePwd(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse bean) {
                ToastUtils.showNormalToast(getString(R.string.xgcg));
                if (mDialog != null) {
                    mDialog.dismiss();
                }

                finish();

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtils.showNormalToast(t);
            }
        });
    }

    @Override
    public <T> void toEntity(T entity, int type) {
       dimessProgress();
       showToasts(getString(R.string.ghcg));
       finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
         dimessProgress();
         showToasts(msg);
    }
}