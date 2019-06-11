package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.ShopMallDetailActivity;
import com.zhongchuang.canting.adapter.HomeBannerAdapter;
import com.zhongchuang.canting.adapter.HomeItemdapter;
import com.zhongchuang.canting.adapter.recycle.ItemRecycleAdapter;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class HomeMallActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.ll_searh)
    LinearLayout llSearh;
    @BindView(R.id.iv_seach)
    ImageView ivSeach;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    private int pos = 1;
    private Subscription mSubscription;
    //    private ProfileInfoHelper infoHelper;
    private int type = 1;

    private ItemRecycleAdapter adapter;

    private LinearLayoutManager layoutManager;
    private LiveStreamPresenter presenters;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页
    @Override
    public void initViews() {
        setContentView(R.layout.activity_home_mall);
        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(this);
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new ItemRecycleAdapter(this);
        mSuperRecyclerView.setAdapter(adapter);
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

        mSuperRecyclerView.setRefreshListener(refreshListener);
        presenter = new BasesPresenter(this);
        presenter.getHomeBanner("1");
        initView();

    }

    private BannerView bannerView;
    private NoScrollGridView gridContent;
    private NoScrollGridView gridContent1;
    private LinearLayout ll_special;
    private ImageView ivGO;
    private HomeItemdapter homedapter;
    private HomeBannerAdapter bannerAdapter;

    private BasesPresenter presenter;
    private void initView() {
        View view = View.inflate(this, R.layout.home_head_view, null);
        bannerView = view.findViewById(R.id.bannerView);

        gridContent = view.findViewById(R.id.grid_content);
        gridContent1 = view.findViewById(R.id.grid_content1);

        ll_special = view.findViewById(R.id.ll_special);
        ivGO = view.findViewById(R.id.iv_go);
        homedapter = new HomeItemdapter(this);
        bannerAdapter = new HomeBannerAdapter(this);
        ivGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SearchGoodActivity.class);
//                intent.putExtra("type", 1);
//                startActivity(intent);

            }
        });
        ll_special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ShopSpecialActivity.class);
//                intent.putExtra("state", 1);
//                startActivity(intent);

            }
        });


        gridContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(getActivity(), ShopTypeSearchActivity.class);
//                intent.putExtra("type", 1);
//                intent.putExtra("id", category.get(position).id);
//                startActivity(intent);

            }
        });
        bannerView.setAdapter(bannerAdapter);
        gridContent.setAdapter(homedapter);

        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {
//                Banner banner = (Banner) o;
//                Intent intent = new Intent(this, ShopMallDetailActivity.class);
//                intent.putExtra("id", banner.product_sku_id);
//                intent.putExtra("type", 1);
//                startActivity(intent);
            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });
//        showPress();
        adapter.setHeaderView(view);
        presenter = new BasesPresenter(this);
        presenter.getHomeBanner("1");
        adapter.setItemCikcListener(new ItemRecycleAdapter.ItemClikcListener() {
            @Override
            public void itemClick(String data, int poition) {
                Intent intent = new Intent(HomeMallActivity.this, ShopMallDetailActivity.class);
                intent.putExtra("id", data);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
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
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.LOGIN_FINISH) {

                    Intent intent = new Intent(HomeMallActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("status", 1);
                    startActivity(intent);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
    }

    private String userInfoId;

    private List<Product> datas = new ArrayList<>();
    private int cout = 12;

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
                        presenter.getProductList(TYPE_PULL_MORE, currpage + "", cout + "", "", "1", "0", "1");
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
    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }


    protected void onResume() {
        super.onResume();
    }

    private void logOut() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
//                        tvLogin.setText("登录");

                        Log.d(TAG, "main+12: " + "登出成功");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        }).start();

    }



    List<Banner> category;
    List<Banner> banners;
    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 6) {
            Home home = (Home) entity;
            category = home.category;
            banners = home.banner;
            bannerAdapter.setData(banners);
//            homedapter.setData(category);
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

    }
}




