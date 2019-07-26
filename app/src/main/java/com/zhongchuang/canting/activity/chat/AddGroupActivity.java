package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddGroupActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.et_comment)
    ClearEditText etComment;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    private BasesPresenter presenter;
    private BannerAdaptes bannerAdapter;

    private GAME name;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        name = (GAME) getIntent().getSerializableExtra("name");
        bannerAdapter = new BannerAdaptes(this);
        if (name != null && TextUtil.isNotEmpty(name.chatGroupName)) {
            etComment.setText(name.chatGroupName);
        }
        navigationBar.setNavigationBarListener(this);
        presenter = new BasesPresenter(this);
        presenter.getBanners(2 + "");

    }

    @Override
    public void bindEvents() {
        bannerView.setAdapter(bannerAdapter);


        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {
//                Banner banner= (Banner) o;
//                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
//                intent.putExtra("id", banner.product_sku_id);
//                intent.putExtra("type", 2);
//                startActivity(intent);
            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtil.isNotEmpty(etComment.getText().toString().trim()) && etComment.getText().toString().trim().length() < 9) {
                    if (name != null) {
                        if (TextUtil.isNotEmpty(paths)) {
                            presenter.alterGroupImage(name.id, paths);

                        } else {
                            presenter.alterGroupName(etComment.getText().toString().trim(), name.id, "");
                        }

                    } else {
                        presenter.addGroup(etComment.getText().toString().trim(), "");

                    }

                } else if (TextUtil.isNotEmpty(etComment.getText().toString().trim()) && etComment.getText().toString().trim().length() > 8) {
                    showToasts(getString(R.string.fzmccd));
                } else {
                    showToasts(getString(R.string.fzmcbnwk));
                }
            }
        });
    }

    private String paths = "";

    @Override
    public void initData() {

    }


    public void setData() {

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 22) {
            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);
        } else if (type == 15) {

            showTomast(getString(R.string.tjcg));
            finish();
        } else {

            showTomast(getString(R.string.xgcg));
            Intent intent = new Intent();
            intent.putExtra("name", etComment.getText().toString().trim());
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(msg);
    }



}
