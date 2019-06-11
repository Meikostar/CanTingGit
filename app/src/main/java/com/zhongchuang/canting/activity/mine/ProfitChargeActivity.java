package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 王鹏兑换
 */
public class ProfitChargeActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.login_yanzhen_yanzhenma)
    ClearEditText loginYanzhenYanzhenma;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    private BasesPresenter presenter;
    private String profit;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_profit_charge);
        ButterKnife.bind(this);
        profit = getIntent().getStringExtra("profit");
        presenter = new BasesPresenter(this);
        if (TextUtil.isNotEmpty(profit)) {
            tvJf.setText("￥"+profit);
            tvDetail.setText("当前可提现金额"+profit+"   ");
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
                startActivity(new Intent(ProfitChargeActivity.this, RecommendDetailActivity.class));
            }

            @Override
            public void navigationimg() {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToasts("功能开发中，敬请期待！");
            }
        });

    }

    private String userInfoId;

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    protected void onResume() {
        super.onResume();

        if (presenter != null) {
            presenter.getUserIntegral();
        }
    }

    public List<INTEGRALIST> list = new ArrayList<>();
    public List<INTEGRALIST> data = new ArrayList<>();
    public int currpage = 1;


    @Override
    public <T> void toEntity(T entity, int type) {

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

        dimessProgress();
    }



}




