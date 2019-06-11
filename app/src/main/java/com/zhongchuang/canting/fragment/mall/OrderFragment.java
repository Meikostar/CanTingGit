package com.zhongchuang.canting.fragment.mall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.ShopMallDetailActivity;
import com.zhongchuang.canting.adapter.recycle.GoodRecyAdapter;
import com.zhongchuang.canting.adapter.recycle.ItemRecycleAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.widget.DivItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/8.
 */

@SuppressLint("ValidFragment")
public class OrderFragment extends BaseFragment  {
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    Unbinder unbinder;

    private GoodRecyAdapter adapter;

    private Context mContext;

    public OrderFragment() {
    }

    public OrderFragment(Context context) {
        this.mContext = context;
    }
    private int type=1;
    public void setType(int type){
        this.type=type;
    }

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhibo_hot, container, false);
        unbinder = ButterKnife.bind(this, view);


        layoutManager = new LinearLayoutManager(getActivity());
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new GoodRecyAdapter(getActivity());
        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                requestData();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

        itemClick();
//        requestData();

        return view;
    }

    private void requestData() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", currpage+"");
        map.put("pageSize", "10");
        map.put("type", 1+"");
        map.put("findStr", "1");
        map.put("userLng", "");
        map.put("userLat", "");
        map.put("userInfoId", CanTingAppLication.userId);
        showPress();

    }
    private void reflash(){
        if(mSuperRecyclerView!=null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable(){
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }
    private List<ZhiBo_GuanZhongBean.DataBean> cooks=new ArrayList<>();
    public void onDataLoaded(int loadType, final boolean haveNext, List<ZhiBo_GuanZhongBean.DataBean> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            cooks.clear();
            for (ZhiBo_GuanZhongBean.DataBean info : list) {
                cooks.add(info);
            }
        } else {
            for (ZhiBo_GuanZhongBean.DataBean info : list) {
                cooks.add(info);
            }
        }

//        adapter.setData(cooks);



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
                            requestData();

                        }
                    }, 2000);
                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }


    }

    private void itemClick() {
        //条目点击事件的操作
        //TODO
       adapter.setItemCikcListener(new GoodRecyAdapter.ItemClikcListener() {
           @Override
           public void itemClick(String data, String type, int poition) {
               Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
               intent.putExtra("id", data);
               intent.putExtra("type", type);
               startActivity(intent);
           }
       });


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
    }


}
