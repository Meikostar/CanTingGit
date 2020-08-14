package com.zhongchuang.canting.activity.offline;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.EditorOrderActivity;
import com.zhongchuang.canting.activity.mall.OrderDetailActivity;
import com.zhongchuang.canting.adapter.OrderDelAdapter;
import com.zhongchuang.canting.adapter.OrderMangerAdapter;
import com.zhongchuang.canting.adapter.recycle.GiftReCycleAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.OfflineBean;
import com.zhongchuang.canting.been.Oparam;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Params;
import com.zhongchuang.canting.been.RecommendListDto;
import com.zhongchuang.canting.been.SmgBaseBean3;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.Constants;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/25.
 */

public class AuditOfflinefagment extends BaseFragment implements BaseContract.View {


    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    private Unbinder bind;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页

    private EntityAuditeStoreAdapter adapter;

    public AuditOfflinefagment() {
    }

    private int type = 0;

    public void setType(int type) {
        this.type = type;

    }

    private BasesPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audit_offline, container, false);
        bind = ButterKnife.bind(this, view);
        adapter = new EntityAuditeStoreAdapter(null,getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        presenter = new BasesPresenter(this);

        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage=1;
                getShopList(TYPE_PULL_REFRESH);

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);


        mSuperRecyclerView.setAdapter(adapter);
        reflash();
        initView();
        return view;
    }
    private List<Oparam> dat = new ArrayList<>();
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
    private OrderParam order = new OrderParam();
    private void initView() {

        adapter.setButtonClickListener(new EntityAuditeStoreAdapter.ButtonClickListener() {
            @Override
            public void buttomClick(int type, int id) {
                showPopwindows(id,type);

            }
        });

    }


    public void showPopwindows(final int id,int type) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText("确定同意该商家入驻吗？");
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
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
                showPress(getString(R.string.qrz));
                if(type == 1){//同意
                    OfflineBean offlineBean = new OfflineBean();
                    offlineBean.id = id;
                    offlineBean.audit_status = 1+"";

                    presenter.updateOfflineShop(offlineBean);
                }else {//拒绝
                    OfflineBean offlineBean = new OfflineBean();
                    offlineBean.id = id;
                    offlineBean.audit_status = 2+"";

                    presenter.updateOfflineShop(offlineBean);
                }
                dialog.dismiss();
            }
        });
    }

    private OrderDelAdapter delAdapter;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();

    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(presenter==null){
                presenter = new BasesPresenter(this);
            }

//            showPress("加载中...");
          getShopList(TYPE_PULL_REFRESH);
            //相当于Fragment的onResume

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
    private List<RecommendListDto> datas = new ArrayList<>();
    private int cout = 12;

    private List<RecommendListDto> dats = new ArrayList<>();

    public void onDataLoaded(int loadType, final boolean haveNext, List<RecommendListDto> list) {
        if(list ==null){
            adapter.setNewData(dats);
            return;
        }
        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            dats.clear();
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        } else {
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        }

        adapter.setNewData(dats);

        adapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        getShopList(TYPE_PULL_MORE);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }

    private int loadtype;
    private void getShopList(int loadtype) {
        this.loadtype = loadtype;
        //showLoadDialog();
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("account_source", "2");
        if(type!=0){
            map.put("audit_status", type+"");
        }


        presenter.getShopList(map);

    }
    @Override
    public <T> void toEntity(T entity, int types) {
         hidePress();
        if (types == 2) {
            SmgBaseBean3 baseBean3 = (SmgBaseBean3) entity;
            if(baseBean3!=null && baseBean3.data!=null && baseBean3.data.list!=null){
                onDataLoaded(loadtype, baseBean3.data.list.size() == Constants.PAGE_SIZE, baseBean3.data.list);
            }else {
                onDataLoaded(loadtype, true, null);
            }


        } else if (types == 123) {
            getShopList(TYPE_PULL_REFRESH);
        } else {

        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        hidePress();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
