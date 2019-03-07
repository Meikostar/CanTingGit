package com.zhongchuang.canting.activity.star;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutlActivity extends BaseTitle_Activity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;
    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.person_code)
    TextView personCode;

    @Override
    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_about, null);
    }
    private String title;
    private String titles;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        title=getIntent().getStringExtra("title");
        titles=getIntent().getStringExtra("titles");
        content=getIntent().getStringExtra("content");
        tvTitle.setText(title);
        personName.setText(titles);
        personCode.setText(content);
        initView();

    }


    private void initView() {
       ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }

    private String userInfoId;


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean isTitleShow() {
        return false;
    }


    private List<ZhiBo_GuanZhongBean.DataBean> cooks = new ArrayList<>();


    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }


}

