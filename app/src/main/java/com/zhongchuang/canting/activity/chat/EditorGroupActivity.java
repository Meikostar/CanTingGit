package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorGroupActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.et_comment)
    ClearEditText etComment;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private BasesPresenter presenter;
    private BannerAdaptes bannerAdapter;

    private String name;
    private String title;
    private String id;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_editor_group);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");


        bannerAdapter = new BannerAdaptes(this);

        if (TextUtil.isNotEmpty(name)) {
            etComment.setText(name);
        }
        if (TextUtil.isNotEmpty(title)) {
            if (title.equals("1")) {
                tvSearch.setText(R.string.xgqlnc);
                etComment.setHint(R.string.qsrnc);
            }
            tvContent.setText(getString(R.string.ncs));
            if (TextUtil.isNotEmpty((SpUtil.getString(this,id+"@@","")))) {
                etComment.setText((SpUtil.getString(this,id+"@@","")));
            }
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                if (TextUtil.isEmpty(etComment.getText().toString().trim())) {
                    showToasts(getString(R.string.qsrnr));
                    return;
                }
                showProgress(getString(R.string.xgz));
                if (TextUtil.isNotEmpty(title)) {
                    presenter.updateOwnerName(etComment.getText().toString(), id);
                } else {
                    presenter.updateGroupsName(etComment.getText().toString(), id);
//                    new Thread(new Runnable() {
//                        public void run() {                             //move to blacklist
//                            try {
//                                EMClient.getInstance().groupManager().changeGroupName(id,etComment.getText().toString());
//                                presenter.updateGroupsName(etComment.getText().toString(), id);
//
//                            } catch (HyphenateException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }).start();

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
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 22) {
            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);
        } else if (type == 12) {
            SpUtil.putString(this, id+"@@", etComment.getText().toString().trim());//环信登录密码
            Intent intent = new Intent();
            intent.putExtra("name",etComment.getText().toString().trim());
            setResult(RESULT_OK,intent);
            showTomast(getString(R.string.xgcg));
            finish();
        } else if (type == 15) {


            Intent intent = new Intent();
            intent.putExtra("name",etComment.getText().toString().trim());
            setResult(RESULT_OK,intent);
            showTomast(getString(R.string.xgcg));
            finish();
        } else {
            showTomast(getString(R.string.tjcg));
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
