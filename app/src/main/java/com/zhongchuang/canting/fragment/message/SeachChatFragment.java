package com.zhongchuang.canting.fragment.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.recycle.ChatDelReCycleAdapter;
import com.zhongchuang.canting.adapter.recycle.HandGitReCycleAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.TimeSelectorDialog;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.Date;
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
public class SeachChatFragment extends BaseFragment implements BaseContract.View {


    Unbinder unbinder;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.tv_datefrom)
    TextView tvDatefrom;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.llbg)
    LinearLayout llbg;
    @BindView(R.id.tv_dateto)
    TextView tvDateto;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.llbgs)
    LinearLayout llbgs;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.ll_searh)
    LinearLayout llSearh;


    private ChatDelReCycleAdapter adapter;
    private BasesPresenter presenter;
    private Context mContext;

    public SeachChatFragment() {
    }

    public SeachChatFragment(Context context) {
        this.mContext = context;
    }

    private int type = 1;
    private int status = 1;

    public void setType(int type) {
        this.type = type;
    }

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);


        layoutManager = new LinearLayoutManager(getActivity());
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        presenter = new BasesPresenter(this);
        adapter = new ChatDelReCycleAdapter(getActivity());
        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                presenter.getRankingList(currpage+"","",type+"","","",TYPE_PULL_REFRESH);

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
        loadingView.showPager(LoadingPager.STATE_SUCCEED);

        llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(1);
            }
        });
        llbgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog(2);
            }
        });
        itemClick();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtil.isNotEmpty(tvDatefrom.getText().toString())&&TextUtil.isNotEmpty(tvDateto.getText().toString())){
                    presenter.getChatDetailForTimeSearch(type+"",TimeUtil.getStringToDate(tvDatefrom.getText().toString())+"", TimeUtil.getStringToDate(tvDateto.getText().toString())+"");
                    loadingView.showPager(LoadingPager.STATE_LOADING);

                }else {
                    showTomast(getString(R.string.qsrrq));
                }
            }
        });
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
                }
            });
        }
    }
    private TimeSelectorDialog selectorDialog;
    public void showDailog(final  int type){
        if (selectorDialog == null) {
            selectorDialog = new TimeSelectorDialog(getActivity());
        }
        selectorDialog.setDate(new Date(System.currentTimeMillis()))
                .setBindClickListener(new TimeSelectorDialog.BindClickListener() {
                    @Override
                    public void time(String time) {
                        if(type==1){
                            tvDatefrom.setText(time);
                        }else {
                            tvDateto.setText(time);
                        }
                    }
                });
        selectorDialog.show(loadingView);
    }
    private List<Hand> datas = new ArrayList<>();
    private int cout = 12;

    public void onDataLoaded(int loadType, final boolean haveNext, List<Hand> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            datas.clear();
            for (Hand info : list) {
                datas.add(info);
            }
        } else {
            for (Hand info : list) {
                datas.add(info);
            }
        }
        if (datas != null && datas.size() != 0) {

            loadingView.showPager(LoadingPager.STATE_SUCCEED);

        } else {

            loadingView.setContent(getString(R.string.myssdxgnr));
            loadingView.showPager(LoadingPager.STATE_EMPTY);

        }
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();


        mSuperRecyclerView.hideMoreProgress();

//        if (haveNext) {
//            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
//                @Override
//                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
//                    currpage++;
//                    mSuperRecyclerView.showMoreProgress();
//                    presenter.getRankingList(currpage+"","",4+"",TimeUtil.formatTime(TimeUtil.getStringToDate(tvDatefrom.getText().toString())), TimeUtil.formatTime(TimeUtil.getStringToDate(tvDateto.getText().toString())),TYPE_PULL_MORE);
//
//                }
//            }, 1);
//        } else {
//            mSuperRecyclerView.removeMoreListener();
//            mSuperRecyclerView.hideMoreProgress();
//
//        }


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

        Hand data = (Hand) entity;
        if(data!=null&&data.data!=null){
            onDataLoaded(type, cout == data.data.size(), data.data);


        }else {
            adapter.notifyDataSetChanged();
            loadingView.setContent(getString(R.string.myssdxgnr));
            loadingView.showPager(LoadingPager.STATE_EMPTY);
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        adapter.notifyDataSetChanged();

        loadingView.setContent(getString(R.string.zwsj));
        loadingView.showPager(LoadingPager.STATE_EMPTY);
    }
}
