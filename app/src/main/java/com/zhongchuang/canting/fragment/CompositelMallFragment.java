package com.zhongchuang.canting.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.SearchGoodActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallDetailActivity;
import com.zhongchuang.canting.activity.mall.ShopSpecialActivity;
import com.zhongchuang.canting.activity.mall.ShopTypeSearchActivity;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.Homedapter;
import com.zhongchuang.canting.adapter.recycle.ItemRecycleAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.Shops;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/25.
 */
@SuppressLint("ValidFragment")
public class CompositelMallFragment extends BaseFragment implements BaseContract.View {


    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;


    private ItemRecycleAdapter adapter;

    private LinearLayoutManager layoutManager;
    private LiveStreamPresenter presenters;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页


    private Intent perIntent;
    private Unbinder bind;
    private ShopCompsiteMallActivity mContext;
    private BasesPresenter presenter;


    public CompositelMallFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhuye, container, false);
        bind = ButterKnife.bind(this, view);
        mSuperRecyclerView= view.findViewById(R.id.super_recycle_view);
        mContext = (ShopCompsiteMallActivity) getActivity();
        layoutManager = new LinearLayoutManager(this.getActivity());
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new ItemRecycleAdapter(getActivity());
        mSuperRecyclerView.setAdapter(adapter);
        initView();
        return view;
    }

    private BannerView bannerView;

    private NoScrollGridView gridContent;
    private TextView tvCity;
    private LinearLayout ll_special;
    private Homedapter homedapter;
    private BannerAdapters bannerAdapter;

    private void initView() {
        View view = View.inflate(getActivity(), R.layout.mall_head_view, null);
        bannerView = view.findViewById(R.id.bannerView);

        gridContent = view.findViewById(R.id.grid_content);
        tvCity = view.findViewById(R.id.tv_city);
        ll_special = view.findViewById(R.id.ll_special);
        homedapter = new Homedapter(getActivity());
        bannerAdapter = new BannerAdapters(getActivity());

        gridContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(TextUtil.isNotEmpty(category.get(position).category_name)){
                    if(category.get(position).category_name.equals(getString(R.string.gengduo))){
                        Intent intent = new Intent(getActivity(), ShopTypeSearchActivity.class);
                        intent.putExtra("type", 3);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), ShopTypeSearchActivity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("id", category.get(position).id);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(getActivity(), ShopTypeSearchActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("id", category.get(position).id);
                    startActivity(intent);

                }
            }
        });
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchGoodActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
        ll_special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopSpecialActivity.class);
                intent.putExtra("state",3);
                startActivity(intent);

            }
        });

        bannerView.setAdapter(bannerAdapter);
        gridContent.setAdapter(homedapter);
//        showPress();
        presenter = new BasesPresenter(this);

        presenter.getHomeBanner("3");
        adapter.setHeaderView(view);

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                presenter.getProductList(TYPE_PULL_REFRESH, 1 + "", cout + "", "", "1", "0", "1");

                        if (mSuperRecyclerView != null) {
                            mSuperRecyclerView.hideMoreProgress();
                        }


            }
        };
        adapter.setItemCikcListener(new ItemRecycleAdapter.ItemClikcListener() {
            @Override
            public void itemClick(String data, int poition) {
                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
                intent.putExtra("id", data);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        mSuperRecyclerView.setRefreshListener(refreshListener);
        refresh();
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
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }


    private Shops shops;

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
        adapter.notifyDataSetChanged();
       if(mSuperRecyclerView!=null){
           mSuperRecyclerView.hideMoreProgress();
       }



        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();
                            if (haveNext)
                                presenter.getProductList(TYPE_PULL_MORE, currpage + "", cout + "", "", "1", "0", "1");
                            if(mSuperRecyclerView!=null){
                                mSuperRecyclerView.hideMoreProgress();
                            }

                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }


    }


    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }

    List<Banner> category;
    List<Banner> banners;
    private List<Product> datas = new ArrayList<>();
    private int cout = 12;

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 6) {
            Home home = (Home) entity;
            category = home.category;
            banners = home.banner;
            bannerAdapter.setData(banners);
            homedapter.setData(category);
        } else {
            List<Product> data = (List<Product>) entity;
            onDataLoaded(type, cout == data.size(), data);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        adapter.notifyDataSetChanged();
    }
}
