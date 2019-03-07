package com.zhongchuang.canting.easeui.ui.red;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedDetailsActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_amount_for_show)
    TextView tvAmountForShow;
    @BindView(R.id.ll_jf)
    LinearLayout llJf;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private BasesPresenter presenter;


    private String id;

    public void initViews() {
        setContentView(R.layout.activity_red_detail);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        id = getIntent().getStringExtra("id");
        if (TextUtil.isNotEmpty(id)) {
            showProgress("");
            presenter.getLuckInfo(id);

        }

    }


    public void bindEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void setData(RedInfo entity) {


        Glide.with(this)
                .load(entity.sendHeadImage).asBitmap().transform(new CircleTransform(this))
                .into(ivImg);

        tvName.setText(entity.sendRemarkName+ getString(R.string.dhb));

        tvContent.setText(entity.leav_message);



    }

    @Override
    public <T> void toEntity(T entity, int type) {
        RedInfo info= (RedInfo) entity;
        setData(info);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
