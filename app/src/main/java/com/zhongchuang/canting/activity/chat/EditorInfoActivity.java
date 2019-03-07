package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.ChatSetAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorInfoActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_rename)
    LinearLayout llRename;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    private BasesPresenter presenter;
    private ChatSetAdapter adapter;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_editor_order);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);

//        presenter.appList();
    }

    @Override
    public void bindEvents() {
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditorInfoActivity.this, ChatBgrActivity.class));
            }
        });
        llRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorInfoActivity.this, AddGroupActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }


    public void setData() {

    }


    @Override
    public <T> void toEntity(T entity, int type) {
//        List<GAME> list = (List<GAME>) entity;
//        adapter.setDatas(list);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
    }


}
