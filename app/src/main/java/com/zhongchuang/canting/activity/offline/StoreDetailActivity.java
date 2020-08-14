package com.zhongchuang.canting.activity.offline;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreDetailActivity extends BaseActivity1 {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.rl_gr)
    RelativeLayout rlGr;
    @BindView(R.id.rl_qy)
    RelativeLayout rlQy;
    @BindView(R.id.rl_xx)
    RelativeLayout rl_xx;



    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_sq);
        ButterKnife.bind(this);
        mTitleText.setText("店铺申请");
    }

    @Override
    public void bindEvents() {
        rlGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(PersonRequireActivity.class);
                finish();
            }
        });
        rl_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int state = SpUtil.getInt(StoreDetailActivity.this,"offline_applay",0);
               if(state == 1){
                   showToasts("审核中！");
                   return;
               }
                gotoActivity(EnterpireOfflineRequireActivity.class);
                finish();
            }
        });

        rlQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(EnterpireRequireActivity.class);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_title_back

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
