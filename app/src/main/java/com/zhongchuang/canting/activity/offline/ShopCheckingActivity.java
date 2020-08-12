package com.zhongchuang.canting.activity.offline;

import android.view.View;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.widget.ActionbarView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 主播审核中提示
 */

public class ShopCheckingActivity extends BaseActivity1 {

    private ActionbarView actionbar;

    @Override
    public void initViews() {
        setContentView(R.layout.ui_shop_checking);
        ButterKnife.bind(this);
        actionbar = findViewById(R.id.custom_action_bar);
        actionbar.setTitle("等待审核");
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.btn_sure})
    public void toOnclick(View view){
        switch (view.getId()){
            case R.id.btn_sure:
                finish();
                break;
        }
    }
}
