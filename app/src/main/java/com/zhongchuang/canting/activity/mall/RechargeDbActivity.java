package com.zhongchuang.canting.activity.mall;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeDbActivity extends BaseActivity1 {


    @BindView(R.id.txt_unit)
    TextView txtUnit;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.ll_usa)
    LinearLayout llUsa;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.cb_zfb)
    CheckBox cbZfb;
    @BindView(R.id.cb_wx)
    CheckBox cbWx;
    @BindView(R.id.ll_china)
    LinearLayout llChina;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.lines)
    View lines;
    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_balances)
    TextView tvBalances;
    private int type;
    private int status;
    private String bal;



    @Override
    public void initViews() {
        setContentView(R.layout.activity_recharge_db);
        ButterKnife.bind(this);
    }

    @Override
    public void bindEvents() {
        cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status = 0;
                    cbWx.setChecked(false);
                } else {
                    status = 1;
                    cbWx.setChecked(true);
                }
            }
        });
        cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status = 1;
                    cbZfb.setChecked(false);
                } else {
                    status = 0;
                    cbZfb.setChecked(true);
                }
            }
        });
        tvCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    if (type == 0) {
                        toCharge(etContent.getText().toString().trim());
                    } else {
                        if (status == 0) {
                            exchangeGlod(etContent.getText().toString().trim());

                        } else {
                            exchangeWx(etContent.getText().toString().trim());
                        }
                    }

                }
            }
        });
    }

    @Override
    public void initData() {
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {
                startActivity(new Intent(RechargeDbActivity.this,DbRecordActivity.class));
            }

            @Override
            public void navigationimg() {

            }
        });
        type = getIntent().getIntExtra("type", 0);
        bal = getIntent().getStringExtra("bal");
        if (TextUtils.isEmpty(bal)) {
            bal = "0.00";
        }
        if (type != 0) {
            lines.setVisibility(View.VISIBLE);
            llBalance.setVisibility(View.VISIBLE);
            tvBalance.setText("(" + bal + ")");
            cbWx.setClickable(true);
            cbWx.setChecked(false);
            txtUnit.setText("兑换值：");
        }
    }


    private int RQ_WEIXIN_PAY = 1;

    public void toCharge(final String totaFee) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("payPrice", totaFee);
        map.put("payType", 1 + "");
        map.put("equipmentStatus", "1");
        map.put("companyType", CanTingAppLication.CompanyType);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.recharge(map).enqueue(new BaseCallBack<WEIXINREQ>() {
            @Override
            public void onSuccess(WEIXINREQ weixinreq) {

                Intent intent = new Intent(RechargeDbActivity.this, WXPayEntryActivity.class);
                intent.putExtra("weixinreq", weixinreq.data);
                startActivityForResult(intent, RQ_WEIXIN_PAY);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS, ""));
        }
    }


    public void exchangeGlod(String Amount) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("amount", Integer.valueOf(Amount) + "");
        netService api = HttpUtil.getInstance().create(netService.class);
        api.exchangeGlod(map).enqueue(new BaseCallBack<BEAN>() {
            @Override
            public void onSuccess(BEAN bean) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.NOTIFY_MONEY, bean));
                finish();
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    private String anchorid;

    public void exchangeWx(String Amount) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("payPrice", Amount);
        map.put("payType", 1 + "");
        map.put("equipmentStatus", "1");
        map.put("companyType", CanTingAppLication.CompanyType);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.exchangeWx(map).enqueue(new BaseCallBack<WEIXINREQ>() {
            @Override
            public void onSuccess(WEIXINREQ weixinreq) {

                Intent intent = new Intent(RechargeDbActivity.this, WXPayEntryActivity.class);
                intent.putExtra("weixinreq", weixinreq.data);
                startActivityForResult(intent, RQ_WEIXIN_PAY);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
}
