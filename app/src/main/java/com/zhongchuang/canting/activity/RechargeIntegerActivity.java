package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeIntegerActivity extends BaseTitle_Activity {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.txt_unit)
    TextView txtUnit;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.lines)
    View lines;
    @BindView(R.id.tv_bs)
    TextView tvBs;
    @BindView(R.id.cb_bs)
    CheckBox cbBs;
    @BindView(R.id.tv_zs)
    TextView tvZs;
    @BindView(R.id.cb_zs)
    CheckBox cbZs;
    @BindView(R.id.ll_china)
    LinearLayout llChina;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.ll_bs)
    LinearLayout llBs;
    @BindView(R.id.ll_zs)
    LinearLayout llZs;
    private int type;
    private int status = 1;
    private String bal;

    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_recharge_inter, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getIntegral();


        llZs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    status = 1;
                    cbZs.setChecked(false);
                    cbBs.setChecked(true);

            }
        });
        llBs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    status = 2;
                    cbZs.setChecked(true);
                    cbBs.setChecked(false);

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    if (status == 1) {
                        if (Double.valueOf(etContent.getText().toString()) > bs) {
//                            ToastUtils.showNormalToast(getString(R.string.ndqmyzmdjb));
                        } else {
                            updateIntegral(etContent.getText().toString().trim());
                        }


                    } else {

                        if (Double.valueOf(etContent.getText().toString()) > zs) {
//                            ToastUtils.showNormalToast("您当前没有这么多银币");
                        } else {
                            updateIntegral(etContent.getText().toString().trim());
                        }

                    }

                } else {
                    ToastUtils.showNormalToast(getString(R.string.qsrdhsl));
                }
            }
        });
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(RechargeIntegerActivity.this,ChangeRecordActivity.class));
            }
        });


    }

    @Override
    public boolean isTitleShow() {
        return false;
    }

    private double bs;
    private double zs;

    public void updateIntegral(final String num) {


        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", SpUtil.getMobileNumber(this));
        map.put("num", "-" + num);
        map.put("type", status + "");


        netService api = HttpUtil.getInstance().create(netService.class);
        api.updateIntegral(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse weixinreq) {
                ToastUtils.showNormalToast(getString(R.string.jhcg));
                finish();
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


    public void getIntegral() {


        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", SpUtil.getMobileNumber(this));


        netService api = HttpUtil.getInstance().create(netService.class);
        api.getIntegral(map).enqueue(new BaseCallBack<BEAN>() {
            @Override
            public void onSuccess(BEAN bean) {

                if (bean != null&&bean.data!=null) {
                    if (TextUtil.isNotEmpty(bean.data.bs)) {
                        bs = Double.valueOf(bean.data.bs);
                        tvBs.setText(getString(R.string.sye) + bean.data.bs + ")");
                    }
                    if (TextUtil.isNotEmpty(bean.data.zs)) {
                        zs = Double.valueOf(bean.data.zs);
                        tvZs.setText(getString(R.string.sye) + bean.data.zs + ")");
                    }
                }
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }


}
