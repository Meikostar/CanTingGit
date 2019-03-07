package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.RechargeActivity;
import com.zhongchuang.canting.activity.RechargeIntegerActivity;
import com.zhongchuang.canting.activity.chat.SumbitJfActivity;
import com.zhongchuang.canting.adapter.recycle.ChargeDetailRecycleAdapter;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.Detialbean;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 王鹏兑换
 */
public class WpChargeActivity extends BaseAllActivity implements BaseContract.View {

    @BindView(R.id.line)
    View line;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_jf)
    TextView tvJf;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.ll_more)
    LinearLayout llMore;

    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private BasesPresenter presenter;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private LinearLayoutManager mLinearLayoutManager;
    private ChargeDetailRecycleAdapter adapter;
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
        setContentView(R.layout.activity_wp_charge);
        ButterKnife.bind(this);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        presenter = new BasesPresenter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        adapter = new ChargeDetailRecycleAdapter(this);
        mSuperRecyclerView.setAdapter(adapter);
        adapter.setType(state);
        state=getIntent().getIntExtra("type",1);
        bean= (Ingegebean) getIntent().getSerializableExtra("bean");
        content=getIntent().getStringExtra("content");
        if(state==1){
            status=4;
            tvTitle.setText(R.string.czkyjf);
            tvContent.setText(R.string.czjfkyygw);
            tvHint.setVisibility(View.GONE);
            tvSubmit.setText(getString(R.string.chongzhi));
        }else if(state==2){
            status=3;
            tvTitle.setText(R.string.szjz);
            tvContent.setText(R.string.hqjfkyygw);
            tvHint.setVisibility(View.GONE);
            tvSubmit.setText(getString(R.string.ljhq));
        }else if(state==3){
            status=1;
            tvTitle.setText(getString(R.string.llkyjf));
            tvContent.setText(R.string.llktqsyjf);
            tvHint.setText(R.string.zllsytqdll);
            jf=bean.chat_presenter_integral;
        }else if(state==4){
            status=2;
            tvTitle.setText(R.string.zbkyjf);
            tvContent.setText(R.string.zbktqsyjf);
            jf=bean.direct_gift_integral;

        }
        if(!TextUtil.isEmpty(content)){
            tvJf.setText(content);
        }
        reflash();
        // mSuperRecyclerView.setRefreshing(false);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                 mSuperRecyclerView.showMoreProgress();

                presenter.integralDetails(TYPE_PULL_REFRESH, currpage + "", status + "", 0 + "", "", "","5");

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==1){
                    startActivity(new Intent(WpChargeActivity.this, RechargeActivity.class));
                }else if(state==2) {

                    Intent intent = new Intent(WpChargeActivity.this, RechargeIntegerActivity.class);
                    startActivity(intent);
                }else if(state==3) {
                    Intent intent = new Intent(WpChargeActivity.this, SumbitJfActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("cout", jf);
                    startActivity(intent);
                }else if(state==4) {
                    Intent intent = new Intent(WpChargeActivity.this, SumbitJfActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("cout", jf);
                    startActivity(intent);
                }

            }
        });
        llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WpChargeActivity.this, ChargetHandActivity.class);
                intent.putExtra("type",state);
                intent.putExtra("status",status);
                startActivity(intent);

            }
        });
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
        if(presenter!=null){
            presenter.getUserIntegral();
        }
    }

    public List<INTEGRALIST> list = new ArrayList<>();
    public List<INTEGRALIST> data = new ArrayList<>();
    public int currpage = 1;

    public void onDataLoaded(int loadtype, final boolean haveNext, List<INTEGRALIST> datas) {

        if (loadtype == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            for (INTEGRALIST info : datas) {
                list.add(info);
            }
        } else {
            for (INTEGRALIST info : datas) {
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
        if(type==19){
            Ingegebean    bean = (Ingegebean) entity;


            if(state==1){
                if (TextUtil.isNotEmpty(bean.money_buy_integral)) {
                    tvJf.setText(bean.money_buy_integral);
                }

            }else if(state==2){

                if (TextUtil.isNotEmpty(bean.jewel_integral)) {
                    tvJf.setText(bean.jewel_integral);
                }

            }else if(state==3){
                jf=bean.chat_presenter_integral;
                if (TextUtil.isNotEmpty(bean.chat_integral)) {
                    tvJf.setText(bean.chat_integral);
                }

            }else if(state==4){
                jf=bean.direct_gift_integral;
                if (TextUtil.isNotEmpty(bean.direct_integral)) {
                    tvJf.setText(bean.direct_integral);
                }

            }




        }else {
            List<INTEGRALIST> data = (List<INTEGRALIST>) entity;
            if(data!=null){
                loadingView.showPager(LoadingPager.STATE_SUCCEED);

                onDataLoaded(type, false, data);


            }else {
                adapter.notifyDataSetChanged();
                if(loadingView==null){
                    return;
                }
                loadingView.setContent(getString(R.string.zwsj));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
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




