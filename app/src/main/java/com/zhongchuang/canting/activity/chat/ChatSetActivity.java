package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.ChatSetAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.easeui.ui.GroupSetActivity;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatSetActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    private BasesPresenter presenter;
    private ChatSetAdapter adapter;
    private GAME game;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_chat_setting);
        ButterKnife.bind(this);
        game= (GAME) getIntent().getSerializableExtra("data");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        adapter = new ChatSetAdapter(this);
        if(game!=null&&game.data!=null){
            game.data.remove(game.data.remove(game.data.size()-1));
            adapter.setDatas(game.data);
        }
        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatSetActivity.this, AddGroupActivity.class);
                startActivity(intent);
            }
        });
        adapter.setListener(new ChatSetAdapter.ItemBuyClick() {
            @Override
            public void itemClik(int type,GAME game) {
                if (type == 1) {
                    showProgress("");
                    presenter.groupSort(1+"",game.sortId);
                } else if (type == 2) {
                    showProgress("");
                  presenter.groupSort(2+"",game.sortId);
                } else if (type == 3) {
                    Intent intent = new Intent(ChatSetActivity.this, com.zhongchuang.canting.activity.chat.GroupSetActivity.class);
                    intent.putExtra("name",game);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {

    }


    public void setData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(presenter!=null){
            presenter.getChatGroupList();
        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==2){
            if(presenter!=null){
                presenter.getChatGroupList();
            }
        }else {
            dimessProgress();
             game= (GAME) entity;

            adapter = new ChatSetAdapter(this);
            adapter.setListener(new ChatSetAdapter.ItemBuyClick() {
                @Override
                public void itemClik(int type,GAME game) {
                    if (type == 1) {
                        showProgress("");
                        presenter.groupSort(1+"",game.sortId);
                    } else if (type == 2) {
                        showProgress("");
                        presenter.groupSort(2+"",game.sortId);
                    } else if (type == 3) {
                        Intent intent = new Intent(ChatSetActivity.this, com.zhongchuang.canting.activity.chat.GroupSetActivity.class);
                        intent.putExtra("name",game);
                        startActivity(intent);
                    }
                }
            });
            game.data.remove(game.data.remove(game.data.size()-1));
            listview.setAdapter(adapter);
            adapter.setDatas(game.data);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
        showToasts(msg);
    }



}
