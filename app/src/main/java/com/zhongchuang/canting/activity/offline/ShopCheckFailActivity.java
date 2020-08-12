package com.zhongchuang.canting.activity.offline;

import android.view.View;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 主播审核失败
 */

public class ShopCheckFailActivity extends BaseActivity1 {
    @BindView(R.id.tv_fail_reason)
    TextView tvFailReason;

    @Override
    public void initViews() {
        setContentView(R.layout.ui_shop_check_fail);
        ButterKnife.bind(this);

        tvFailReason.setText("审核失败");
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
                gotoActivity(StoreDetailActivity.class);
                finish();
                break;
        }
    }

}
