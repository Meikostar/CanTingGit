package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.AddressListActivity;
import com.zhongchuang.canting.activity.mall.DbRecordActivity;
import com.zhongchuang.canting.activity.mall.EditorOrderActivity;
import com.zhongchuang.canting.activity.mall.RechargeJfActivity;
import com.zhongchuang.canting.activity.pay.ALiPayActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.wxapi.WXPayEntryActivity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.txt_unit)
    TextView txtUnit;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.ll_usa)
    LinearLayout llUsa;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.tv_zfb)
    TextView tvZfb;
    @BindView(R.id.cb_zfb)
    CheckBox cbZfb;
    @BindView(R.id.ll_zfb)
    LinearLayout llZfb;
    @BindView(R.id.lines)
    View lines;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.cb_wx)
    CheckBox cbWx;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.tv_pp)
    TextView tvPp;
    @BindView(R.id.cb_paypal)
    CheckBox cbPaypal;
    @BindView(R.id.ll_paypal)
    LinearLayout llPaypal;
    @BindView(R.id.ll_china)
    LinearLayout llChina;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    private int type = 1;
    private int status;
    private String bal;
    private double rate = 1.0;
    private BasesPresenter presenter;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_recharge);

        ButterKnife.bind(this);
        etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        presenter = new BasesPresenter(this);
        if (TextUtils.isEmpty(bal)) {
            bal = "0.00";
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
                Intent intent = new Intent(RechargeActivity.this, DbRecordActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
            }

            @Override
            public void navigationimg() {

            }
        });
        llPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZfb.setVisibility(View.INVISIBLE);
                tvWx.setVisibility(View.INVISIBLE);
                tvPp.setVisibility(View.VISIBLE);
                if (TextUtil.isNotEmpty(etContent.getText().toString())) {
                    tvPp.setText("(" + (rate * Double.valueOf(etContent.getText().toString())) + "￥" + ")");
                }

                type = 3;
                cbPaypal.setChecked(true);
                cbWx.setChecked(false);
                cbZfb.setChecked(false);
            }
        });
        llWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZfb.setVisibility(View.INVISIBLE);
                tvWx.setVisibility(View.VISIBLE);
                tvPp.setVisibility(View.INVISIBLE);
                if (TextUtil.isNotEmpty(etContent.getText().toString())) {
                    tvWx.setText("(" + (rate * Double.valueOf(etContent.getText().toString())) + "￥" + ")");
                }

                cbPaypal.setChecked(false);
                type = 2;
                cbWx.setChecked(true);
                cbZfb.setChecked(false);
            }
        });
        llZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZfb.setVisibility(View.VISIBLE);
                tvWx.setVisibility(View.INVISIBLE);
                tvPp.setVisibility(View.INVISIBLE);
                if (TextUtil.isNotEmpty(etContent.getText().toString())) {
                    tvZfb.setText("(" + (rate * Double.valueOf(etContent.getText().toString())) + "￥" + ")");
                }

                type = 1;
                cbPaypal.setChecked(false);
                cbWx.setChecked(false);
                cbZfb.setChecked(true);
            }
        });
        etContent.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void afterTextChanged4ClearEdit(Editable s) {
                if (TextUtil.isNotEmpty(s.toString())) {
                    if (type == 1) {
                        tvZfb.setVisibility(View.VISIBLE);
                        tvWx.setVisibility(View.INVISIBLE);
                        tvPp.setVisibility(View.INVISIBLE);
                        tvZfb.setText("(" + (rate * Double.valueOf(s.toString())) + "￥" + ")");
                    } else if (type == 2) {
                        tvZfb.setVisibility(View.INVISIBLE);
                        tvWx.setVisibility(View.VISIBLE);
                        tvPp.setVisibility(View.INVISIBLE);
                        tvWx.setText("(" + (rate * Double.valueOf(s.toString())) + "￥" + ")");
                    } else if (type == 3) {
                        tvZfb.setVisibility(View.INVISIBLE);
                        tvWx.setVisibility(View.INVISIBLE);
                        tvPp.setVisibility(View.VISIBLE);
                        tvPp.setText("(" + (rate * Double.valueOf(s.toString())) + "￥" + ")");
                    }
                }else {
                    tvZfb.setVisibility(View.INVISIBLE);
                    tvWx.setVisibility(View.INVISIBLE);
                    tvPp.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void changeText(CharSequence s) {

            }
        });

        tvCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    actual_amount = etContent.getText().toString();
                    if (type == 1) {

                        presenter.rechargeInteger(Integer.valueOf(etContent.getText().toString()) + "", "2");
                    } else if (type == 2) {
                        presenter.rechargeIntegers(Integer.valueOf(etContent.getText().toString()) + "", "1");
                    } else if (type == 3) {
                        presenter.rechargeIntegerss(Integer.valueOf(etContent.getText().toString()) + "", "3");
                    }
                    showProgress(getString(R.string.czz));
                }
            }
        });

    }

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
//            .clientId("AbRWviKkeFvCy5nfE41aLxY8sS6AVGVymTZDhb1SAVyULO2sKcFvk1ALzm7weAHgSzInMv7-ZzsVfQKO");
            .clientId("ATxM3pja08ScAwqB2crhgTVp0S-81tZcS_H-S54vinXbxwJzY9fkvTS4CdpJ5QDXCp1Yn6AjWCRmvLWH");
    private String actual_amount;

    public void onBuyPressed() {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal((Double.valueOf(actual_amount) * (100.0 / 687)) + ""), "USD", "Recharge",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, RQ_PAYPAL_PAY);
    }

    @Override
    public void initData() {

    }


    private int RQ_WEIXIN_PAY = 12;
    private int RQ_PAYPAL_PAY = 16;
    private int RQ_ALIPAY_PAY = 10;


    private String paypalId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RQ_WEIXIN_PAY) {
                showToasts(getString(R.string.chongzcg));
                finish();
//           if (requestCode == RQ_WEIXIN_PAY) {
//                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
//            }
            } else if (requestCode == 10) {
                showToasts(getString(R.string.chongzcg));
                finish();
            } else if (requestCode == RQ_PAYPAL_PAY) {
                presenter.integerSuccess(paypalId);

            }
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS, ""));
        }

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 8) {
            WEIXINREQ weixinreq = (WEIXINREQ) entity;
            if (weixinreq != null) {
                Intent intent = new Intent(this, WXPayEntryActivity.class);
                intent.putExtra("weixinreq", weixinreq);
                startActivityForResult(intent, RQ_WEIXIN_PAY);
            } else {
                showToasts(getString(R.string.chongzcg));
                finish();
            }

        } else if (type == 9) {
            String alisign = (String) entity;
            if (alisign != null) {
                Intent intent = new Intent(this, ALiPayActivity.class);
                intent.putExtra("signedstr", alisign);
                startActivityForResult(intent, RQ_ALIPAY_PAY);

            }

        } else if (type == 16) {
            paypalId = (String) entity;
            onBuyPressed();

        } else if (type == 18) {
            showToasts(getString(R.string.chongzcg));
            finish();
        }

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
