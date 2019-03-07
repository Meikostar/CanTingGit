package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.ChatSetAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupSetActivity extends BaseActivity1 implements BaseContract.View {


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
    @BindView(R.id.tv_name)
    TextView tv_name;
    private BasesPresenter presenter;
    private ChatSetAdapter adapter;

    private GAME name;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_group_set);
        ButterKnife.bind(this);
        name = (GAME) getIntent().getSerializableExtra("name");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        if (name != null && TextUtil.isNotEmpty(name.chatGroupName)) {
            tv_name.setText(name.chatGroupName);
        }
//        presenter.appList();
    }

    @Override
    public void bindEvents() {
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupSetActivity.this, ChatBgrActivity.class));
            }
        });
        llRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupSetActivity.this, AddGroupActivity.class);
                intent.putExtra("name", name);
                startActivityForResult(intent, 1);
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow(name.chatGroupName, name.id);
            }
        });
    }

    private MarkaBaseDialog dialog;

    public void showPopwindow(final String name, final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(getString(R.string.qdsc) + name + getString(R.string.fzs));
        dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(getString(R.string.shanchuzhong));
                presenter.deleteGroups(id);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void initData() {

    }


    public void setData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==1){
                String names = data.getStringExtra("name");
                if (TextUtil.isNotEmpty(names)) {
                    name.chatGroupName=names;
                    tv_name.setText(names);
                }
            }
        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        showToasts(getString(R.string.sccg));
        finish();
//        List<GAME> list = (List<GAME>) entity;
//        adapter.setDatas(list);
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
