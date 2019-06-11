package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.vod.common.utils.ToastUtil;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseLoginActivity;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/14.
 */

public class EditorSecondActivity extends BaseLoginActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.but_save)
    Button butSave;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    private String id;

    private int type = 0;
    private BasesPresenter presenter;
    private String info;
    private String data;

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editor_second);


        ButterKnife.bind(this);

        info =  getIntent().getStringExtra("info");
        data =  getIntent().getStringExtra("data");



        initView();
        setEvents();
    }

    @Override
    protected void _onDestroy() {

    }

    private void initView() {

        if (TextUtil.isNotEmpty(info)) {

                etContent.setText(info);

        }
        if (TextUtil.isNotEmpty(data)) {
            tvTitle.setText(data);
        }


    }

    private String content = "";

    private void setEvents() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etContent.getText().toString();
                if(TextUtil.isNotEmpty(content)){
                    Intent intent = new Intent();
                    intent.putExtra("data",content);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    ToastUtil.showToast(EditorSecondActivity.this,info+"不能为空");
                }

            }
        });
    }




}
