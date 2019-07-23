package com.zhongchuang.canting.activity.mall;

import android.os.Bundle;
import android.widget.ListView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.ListImageAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RegularListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/***
 * 功能描述:
 * 作者:meiko
 * 时间:2017/1/12
 * 版本:1.0
 ***/

public class BanDetailActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.rl_menu)
    ListView rlMenu;
    private ListImageAdapter imgadapter;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_ban_detail);
        ButterKnife.bind(this);
        url=getIntent().getStringExtra("url");
        imgadapter = new ListImageAdapter(this);
        rlMenu.setAdapter(imgadapter);
    }

    private String addressId;

    @Override
    public void bindEvents() {
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {

            }

            @Override
            public void navigationimg() {

            }
        });


    }
   private String url;

    @Override
    public void initData() {

        if (TextUtil.isNotEmpty(url)) {
            String[] split = url.split(",");
            List<String> dat = new ArrayList<>();
            for (String url : split) {
                if(url.contains("http")){
                    dat.add(url);
                }else {
                    dat.add(QiniuUtils.baseurl+url);
                }

            }
            imgadapter.setData(dat);

        }
    }


    @Override
    public <T> void toEntity(T entity, int type) {


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        dimessProgress();
        showToasts(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
