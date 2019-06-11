package com.zhongchuang.canting.easeui.ui.red;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.easeui.adapter.RedDetailAdapter;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.RegularListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitRedDetailActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
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
    @BindView(R.id.tv_wait)
    TextView tvWait;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.tv_waits)
    TextView tvWaits;
    @BindView(R.id.rl_menu)
    RegularListView rlMenu;
    @BindView(R.id.ll_list)
    LinearLayout llList;
    private BasesPresenter presenter;
    private RedInfo info;
    private RedDetailAdapter adapter;


    public void setData(RedInfo entity) {


        Glide.with(this)
                .load(entity.sendHeadImage).asBitmap().transform(new CircleTransform(this))
                .into(ivImg);

        tvName.setText(entity.sendRemarkName +  getString(R.string.dhb));

        tvContent.setText(entity.leav_message);
        if (entity.grabRedList != null) {
            adapter.setData(entity.grabRedList);
        }
        if(state==1){
            if (entity.isAll == 1) {
                if (entity.chatType == 1) {
                    tvWaits.setText(entity.red_envelope_number +  getString(R.string.ghbs) + entity.time +  getString(R.string.mqwl));
                } else {
                    tvWaits.setText( getString(R.string.ghbg) + entity.red_envelope_count +  getString(R.string.jf));
                }

            } else {

                tvWaits.setText(entity.red_envelope_number +  getString(R.string.ghbs) +  getString(R.string.hs) + entity.RemainNumbers +  getString(R.string.g));
            }
        }else {
            tvWaits.setText( getString(R.string.ghbg) + entity.red_envelope_count +  getString(R.string.jf));
        }


    }

    private String id;

    public void initViews() {
        setContentView(R.layout.activity_one_red_detail);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);


        adapter = new RedDetailAdapter(this);
        rlMenu.setAdapter(adapter);

        id = getIntent().getStringExtra("id");
        state = getIntent().getIntExtra("type",0);
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
        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitRedDetailActivity.this, RedOut_In_DetailActivity.class);
                startActivity(intent);
            }
        });
    }


    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        RedInfo info = (RedInfo) entity;
        setData(info);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
    }


}
