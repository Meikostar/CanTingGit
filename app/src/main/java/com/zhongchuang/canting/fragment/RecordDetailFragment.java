package com.zhongchuang.canting.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.recycle.ChargeDetailRecycleAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/11/8.
 */

@SuppressLint("ValidFragment")
public class RecordDetailFragment extends BaseFragment implements BaseContract.View {

    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private ChargeDetailRecycleAdapter adapter;
    private BasesPresenter presenter;
    private Context mContext;

    public RecordDetailFragment() {
    }

    public RecordDetailFragment(Context context) {
        this.mContext = context;
    }

    private int type = 1;
    private int status = 1;

    public void setType(int type,int status) {
        this.type = type;
        if(status==0){
            this.status = 2;
        }else {
            this.status = status;
        }

    }

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.charge_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);


        layoutManager = new LinearLayoutManager(getActivity());
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        presenter = new BasesPresenter(this);
        tvTotal.setVisibility(View.GONE);
        adapter = new ChargeDetailRecycleAdapter(getActivity());
        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                  mSuperRecyclerView.showMoreProgress();
                presenter.recordIntegralDetails(TYPE_PULL_REFRESH,currpage+"",status+"",type+"","","");


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        itemClick();
        reflash();

        return view;
    }

    private Subscription mSubscription;

    private void reflash() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                    adapter.notifyDataSetChanged();
                    loadingView.showPager(LoadingPager.STATE_SUCCEED);
                }
            });
        }
    }

    private List<INTEGRALIST> datas = new ArrayList<>();
    private int cout = 12;

    public void onDataLoaded(int loadType, final boolean haveNext, List<INTEGRALIST> list) {
        datas.clear();
        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            datas.clear();
            for (INTEGRALIST info : list) {
                datas.add(info);
            }
        } else {
            for (INTEGRALIST info : list) {
                datas.add(info);
            }
        }
        if (datas != null && datas.size() != 0) {

            loadingView.showPager(LoadingPager.STATE_SUCCEED);

        } else {

            loadingView.setContent(getString(R.string.zwsj));
            loadingView.showPager(LoadingPager.STATE_EMPTY);

        }
        adapter.setType(status);
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
        mSuperRecyclerView.hideMoreProgress();

        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();
                    presenter.recordIntegralDetails(TYPE_PULL_MORE,currpage+"",status+"",type+"","","");

                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }


    }

    private void itemClick() {


    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }


    @Override
    public <T> void toEntity(T entity, int type) {
        List<INTEGRALIST> data = (List<INTEGRALIST>) entity;
        if(data!=null){
            onDataLoaded(type, cout == data.size(), data);

            loadingView.showPager(LoadingPager.STATE_SUCCEED);
       }else {
            adapter.notifyDataSetChanged();
            loadingView.setContent(getString(R.string.zwsj));
            loadingView.showPager(LoadingPager.STATE_EMPTY);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        loadingView.setContent(getString(R.string.zwsj));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
    }
}
