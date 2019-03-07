package com.zhongchuang.canting.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.DbRecordActivity;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播和乐聊 积分提取
 */
public class SumbitJfActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.txt_unit)
    TextView txtUnit;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_charge)
    TextView tvCharge;
    @BindView(R.id.tv_type)
    TextView tvType;
    private int type;
    private String cout;
    private int status;
    private String bal;
    private BasesPresenter presenter;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_submit_jf);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 1);
        cout = getIntent().getStringExtra("cout");
        presenter = new BasesPresenter(this);
        if (TextUtil.isNotEmpty(cout)) {
            tvJf.setText(cout);
        }
        if (type == 1) {
            tvType.setText(R.string.zbktqjfs);

        } else {
            tvType.setText(R.string.llktqjfs);
        }
    }

    @Override
    public void bindEvents() {

        tvCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    if(Integer.valueOf(etContent.getText().toString())==0){
                        showToasts(getString(R.string.tqjfbnw));
                        return;
                    }
                    showProgress(getString(R.string.tqz));

                    presenter.deposit(etContent.getText().toString().trim(), type + "");

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
                Intent intent = new Intent(SumbitJfActivity.this, RecordDetailActivity.class);
                intent.putExtra("type",type==1?2:1);
                intent.putExtra("state",1);
                startActivity(intent);

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

            txtUnit.setText(R.string.jfs);
        }
    }


    private int RQ_WEIXIN_PAY = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            finish();
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS, ""));
        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
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
