package com.zhongchuang.canting.activity.chat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.FaceMenberAdapter;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.easeui.widget.GridViewInScroll;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.pswpop.CommonAdapter;
import com.zhongchuang.canting.widget.pswpop.KeybordModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceCreatGroupActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.tv_text3)
    TextView tvText3;
    @BindView(R.id.tv_text4)
    TextView tvText4;
    @BindView(R.id.gird_menber)
    GridViewInScroll girdMenber;
    @BindView(R.id.tv_goto)
    TextView tvGoto;
    private BasesPresenter presenter;
    private FaceMenberAdapter adapter;
    private String data;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_face_group);
        ButterKnife.bind(this);
        data=getIntent().getStringExtra("data");
        if(TextUtil.isNotEmpty(data)){
            String[] cont = data.split(",");
            tvText1.setText(cont[0]);
            tvText2.setText(cont[1]);
            tvText3.setText(cont[2]);
            tvText4.setText(cont[3]);
        }
        presenter = new BasesPresenter(this);
        adapter=new FaceMenberAdapter(this);
        girdMenber.setAdapter(adapter);
    }

    private String mCurrPsw = "";

    @Override
    public void bindEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }





    @Override
    public void initData() {

    }

    @Override
    public <T> void toEntity(T entity, int type) {

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }



}
