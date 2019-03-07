package com.zhongchuang.canting.activity.mall;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.recycle.ItemShopRecycleAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.ShopBean;
import com.zhongchuang.canting.been.Shops;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.GoodsSearchViews;

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

public class ShopDetailActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.gds)
    GoodsSearchViews gds;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.tv_color)
    TextView tvColor;
    private ItemShopRecycleAdapter adapter;

    private LinearLayoutManager layoutManager;
    private LiveStreamPresenter presenters;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private BasesPresenter presenter;
    private String id;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");

        presenter = new BasesPresenter(this);

        layoutManager = new LinearLayoutManager(this);
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new ItemShopRecycleAdapter(this);
        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSuperRecyclerView.showMoreProgress();
                if (TextUtil.isNotEmpty(id)) {
                    showProgress(getString(R.string.jzz));
                    presenter.getShopById(id);
                }

            }
        };
        gds.cancelFocuse();
        mSuperRecyclerView.setRefreshListener(refreshListener);
        gds.registerListener(new GoodsSearchViews.OnSearchBoxClickListener() {
            @Override
            public void onClean() {

            }

            @Override
            public void onCancle() {

            }

            @Override
            public void finishs(int type) {
                if(type==1){
                    finish();
                }

            }

            @Override
            public void onKeyWordsChange(String keyWords) {

            }
        });
        refresh();
    }

    private Shops shops;
    private List<Product> datas = new ArrayList<>();

    private int currpage = 1;//第几页

    public void onDataLoaded(int loadType, final boolean haveNext, List<Product> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            datas.clear();
            for (Product info : list) {
                datas.add(info);
            }
        } else {
            for (Product info : list) {
                datas.add(info);
            }
        }

        adapter.setDatas(datas);

        mSuperRecyclerView.hideMoreProgress();

        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (haveNext)
                                mSuperRecyclerView.hideMoreProgress();
//                            requestData();

                        }
                    }, 2000);
                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }


    }

    private String shopId;

    @Override
    public void bindEvents() {

        adapter.setItemCikcListener(new ItemShopRecycleAdapter.ItemClikcListener() {
            @Override
            public void itemClick(String id, int poition) {
                Intent intent = new Intent(ShopDetailActivity.this, ShopMallDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(getString(R.string.shouczs));
                presenter.addShop(shopId, TextUtil.isEmpty(types) ? "1" : (types.endsWith("1") ? "0" : "1"));
            }
        });


    }


    @Override
    public void initData() {


    }

    public void refresh() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }

    private String types;

    @Override
    public <T> void toEntity(T entity, int type) {
        dimessProgress();
        if (type == 7) {
            ShopBean shopBean = (ShopBean) entity;
            if (shopBean.productList != null && shopBean.productList.size() > 0) {
                datas.clear();
                for (Product info : shopBean.productList) {
                    datas.add(info);
                }
                adapter.setDatas(datas);
                adapter.notifyDataSetChanged();

                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
                ShopBean data = shopBean.merchantInfo;
                if (data != null) {
                    if (TextUtil.isNotEmpty(data.merName)) {
                        tvName.setText(data.merName);
                    }
                    if (TextUtil.isNotEmpty(data.merPhone)) {
                        tvPhone.setText(getString(R.string.sdh) + data.merPhone);
                    }
                    if (TextUtil.isNotEmpty(data.merName)) {
                        tvConnect.setText(getString(R.string.lxrs) + data.linkMan);
                    }
                    if (TextUtil.isNotEmpty(data.merAddress)) {
                        tvAddress.setText(getString(R.string.dpdzs) + data.merAddress);
                    }
                    if (TextUtil.isNotEmpty(data.collectNumber)) {
                        tvCollect.setText(data.collectNumber + getString(R.string.rsc));
                    }

                    shopId = data.id;
                    types = shopBean.isCollect;
                    if (TextUtil.isNotEmpty(shopBean.isCollect)) {
                        if (shopBean.isCollect.equals("1")) {
                            tvColor.setTextColor(getResources().getColor(R.color.color6));
                            ivCollect.setImageResource(R.drawable.collectss);
                            tvColor.setText(R.string.ysc);
                        } else {
                            tvColor.setTextColor(getResources().getColor(R.color.red));
                            ivCollect.setImageResource(R.drawable.collects);
                            tvColor.setText(getString(R.string.sc));

                        }
                    }
                    Glide.with(this).load(StringUtil.changeUrl(data.merImageUrl)).asBitmap().placeholder(R.drawable.moren1).into(img);
                }


            }
        } else if (type == 22) {

            presenter.getShopById(id);
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
