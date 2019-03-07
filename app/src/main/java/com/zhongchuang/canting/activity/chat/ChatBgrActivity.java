package com.zhongchuang.canting.activity.chat;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.ChatBgAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Type;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatBgrActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.grid_content)
    NoScrollGridView grid_content;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    private BasesPresenter presenter;
    private ChatBgAdapter adapter;
    private int choosType;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_chat_bg);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        choosType= SpUtil.getInt(this,"choosTyp",0);
        presenter = new BasesPresenter(this);
        adapter = new ChatBgAdapter(this);
        grid_content.setAdapter(adapter);
//        presenter.appList();
    }

    @Override
    public void bindEvents() {
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Type> data = adapter.getData();
                for(int i=0;i<data.size();i++){
                    if(data.get(i).isChoose){
                        SpUtil.putInt(ChatBgrActivity.this,"choosTyp",i);
                    }
                }

                finish();
            }
        });
    }

    @Override
    public void initData() {
        setData();
    }


    public void setData() {
        for(int i=0;i<7;i++){
            Type type = new Type();
            if(i==choosType){
                type.isChoose=true;
            }
            type.type=i;
            list.add(type);
        }
        adapter.setData(list);
    }

   private List<Type> list = new ArrayList<>();
    @Override
    public <T> void toEntity(T entity, int type) {
        List<Type> list = (List<Type>) entity;

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
    }



}
