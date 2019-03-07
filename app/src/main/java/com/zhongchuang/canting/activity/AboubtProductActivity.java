package com.zhongchuang.canting.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.NavigationBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboubtProductActivity extends BaseActivity1 {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_aboubt_product);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        tvVersion.setText(getString(R.string.dqbbs)+   StringUtil.getVersion(this));
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {

    }



}
