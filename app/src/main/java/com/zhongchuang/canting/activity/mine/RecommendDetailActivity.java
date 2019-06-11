package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.RechargeActivity;
import com.zhongchuang.canting.activity.RechargeIntegerActivity;
import com.zhongchuang.canting.activity.chat.SumbitJfActivity;
import com.zhongchuang.canting.adapter.recycle.ChargeDetailRecycleAdapter;
import com.zhongchuang.canting.adapter.recycle.RecommendDetailRecycleAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.been.Profit;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 王鹏兑换
 */
public class RecommendDetailActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private BasesPresenter presenter;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private LinearLayoutManager mLinearLayoutManager;
    private RecommendDetailRecycleAdapter adapter;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private final int TYPE_REMOVE = 3;
    private int state;
    private Ingegebean bean;
    private int status;
    private String content;
    private String jf;

    @Override
    public void initViews() {
        setContentView(R.layout.recommend_activity);
        ButterKnife.bind(this);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        presenter = new BasesPresenter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        navigationBar.setNavigationBarListener(this);
        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        adapter = new RecommendDetailRecycleAdapter(this);
        mSuperRecyclerView.setAdapter(adapter);
        adapter.setType(state);
        state = getIntent().getIntExtra("type", 1);
        bean = (Ingegebean) getIntent().getSerializableExtra("bean");
        content = getIntent().getStringExtra("content");

        reflash();
        // mSuperRecyclerView.setRefreshing(false);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSuperRecyclerView.showMoreProgress();

                presenter.getProfitList(TYPE_PULL_REFRESH, currpage + "");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSuperRecyclerView != null) {
                            mSuperRecyclerView.hideMoreProgress();

                        }

                    }
                }, 2000);
            }
        };
        mSuperRecyclerView.setRefreshListener(refreshListener);

    }

    private void reflash() {
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

    @Override
    public void bindEvents() {

    }

    private String userInfoId;

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    protected void onResume() {
        super.onResume();
        reflash();

    }

    public List<Profit> list = new ArrayList<>();
    public List<Profit> data = new ArrayList<>();
    public int currpage = 1;

    public void onDataLoaded(int loadtype, final boolean haveNext, List<Profit> datas) {

        if (loadtype == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            for (Profit info : datas) {
                list.add(info);
            }
        } else {
            for (Profit info : datas) {
                list.add(info);
            }
        }
        adapter.setDatas(list);
        adapter.notifyDataSetChanged();
        mSuperRecyclerView.setLoadingMore(false);
        mSuperRecyclerView.hideMoreProgress();
        /**
         * 判断是否需要加载更多，与服务器的总条数比
         */
        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();
                    presenter.getProfitList(TYPE_PULL_MORE, currpage + "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (haveNext)
                                mSuperRecyclerView.hideMoreProgress();


                        }
                    }, 2000);
                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }
    }

    @Override
    public <T> void toEntity(T entity, int type) {

            List<Profit> data = (List<Profit>) entity;
            if (data != null) {
                loadingView.showPager(LoadingPager.STATE_SUCCEED);

                onDataLoaded(type, false, data);


            } else {
                adapter.notifyDataSetChanged();
                if (loadingView == null) {
                    return;
                }
                loadingView.setContent(getString(R.string.zwsj));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

        dimessProgress();
        adapter.notifyDataSetChanged();
        loadingView.setContent(getString(R.string.zwsj));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
    }



}




