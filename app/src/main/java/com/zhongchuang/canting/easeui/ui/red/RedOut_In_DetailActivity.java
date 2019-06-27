package com.zhongchuang.canting.easeui.ui.red;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.ChatMenberActivity;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.GrapRed;
import com.zhongchuang.canting.been.GrapRedDetail;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.easeui.adapter.RedItemRecycleAdapter;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.utils.RotateYAnimation;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.MCheckBox;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RedPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedOut_In_DetailActivity extends BaseAllActivity implements BaseContract.View {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_type)
    ImageView tvType;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    private BasesPresenter presenter;

    private LinearLayoutManager layoutManager;
    private LiveStreamPresenter presenters;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页
    private String id;
    private int state = 2;//1 收入 2 支出
    private RedPopupWindow mWindowAddPhoto;
    public void initViews() {
        setContentView(R.layout.activity_out_in_red_detail);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        mWindowAddPhoto = new RedPopupWindow(RedOut_In_DetailActivity.this);
        layoutManager = new LinearLayoutManager(this);
        mSuperRecyclerView.setLayoutManager(layoutManager);
//        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        adapter = new RedItemRecycleAdapter(this);

        mSuperRecyclerView.setAdapter(adapter);
        adapter.setState(2);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                presenter.getDetails(time,1 + "", state + "", TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }


            }
        };
        time0 = TimeUtil.formatToYear(System.currentTimeMillis());
        time1=(Integer.valueOf(time0)-1)+"";
        time=time0;
        mSuperRecyclerView.setRefreshListener(refreshListener);
        initView();
        mSuperRecyclerView.setRefreshing(false);


        id = getIntent().getStringExtra("id");
        if (TextUtil.isNotEmpty(id)) {
            showProgress("");
            presenter.getLuckInfo(id);

        }



    }

    private String time0 ;
    private String time1 ;
    private ImageView iv_img;
    private TextView tv_name;
    private TextView tv_content;
    private LinearLayout ll_time;
    private TextView tv_time;
    private TextView tv_red_cout;
    private TextView tv_best_cout;
    private TextView tv_amount_for_show;
    private LinearLayout ll_in;
    private LinearLayout ll_out;

    private void initView() {
        View  view = View.inflate(this, R.layout.red_head_view, null);
        tv_name = view.findViewById(R.id.tv_name);
        tv_content = view.findViewById(R.id.tv_content);
        tv_time = view.findViewById(R.id.tv_time);
        tv_red_cout = view.findViewById(R.id.tv_red_cout);
        tv_best_cout = view.findViewById(R.id.tv_best_cout);
        tv_amount_for_show = view.findViewById(R.id.tv_amount_for_show);
        ll_time = view.findViewById(R.id.ll_time);
        ll_in = view.findViewById(R.id.ll_in);
        ll_out = view.findViewById(R.id.ll_out);

        iv_img = view.findViewById(R.id.iv_img);

        ll_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
            }
        });
//        showPress();
        adapter.setHeaderView(view);

        refresh();
    }

    public void setData(GrapRedDetail entity, int type) {

        if (type == 1) {

            ll_out.setVisibility(View.VISIBLE);
            ll_in.setVisibility(View.GONE);
            tv_amount_for_show.setText(entity.number);
            tv_content.setText(entity.red_envelope_count);

        } else {
            ll_out.setVisibility(View.GONE);
            ll_in.setVisibility(View.VISIBLE);

            tv_content.setText(entity.count);
            tv_amount_for_show.setText(entity.number);
            tv_best_cout.setText(entity.bestNumber);

        }

        Glide.with(this)
                .load(SpUtil.getAvar(this)).asBitmap().transform(new CircleTransform(this))
                .into(iv_img);

        tv_time.setText(SpUtil.getName(this));
        if (entity.redEnvelopeList != null) {
            adapter.setDatas(entity.redEnvelopeList);
        }


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

    private RotateYAnimation animation;

    public void bindEvents() {

        mWindowAddPhoto.setListener(new RedPopupWindow.ChooseListener() {
            @Override
            public void choose(int type) {
                state=type;
                refresh();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mWindowAddPhoto.showAtLocation(tvType, Gravity.BOTTOM, 0, 0);


            }
        });
    }


    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private RedItemRecycleAdapter adapter;


    private List<GrapRed> datas = new ArrayList<>();

    public void onDataLoaded(int loadType, final boolean haveNext, List<GrapRed> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            datas.clear();
            for (GrapRed info : list) {
                datas.add(info);
            }
        } else {
            for (GrapRed info : list) {
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
                    presenter.getDetails(time,currpage + "", state + "", TYPE_PULL_MORE);
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
    public <T> void toEntity(T entity, int type) {
        if(type==69){
            RedInfo redInfo= (RedInfo) entity;
        }else {
            GrapRedDetail info = (GrapRedDetail) entity;
            setData(info, state);
            if (info.redEnvelopeList == null) {
                return;
            }
            onDataLoaded(type, info.redEnvelopeList.size() == 12, info.redEnvelopeList);
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
    private View views = null;
    private int chat_type=1;
    TextView tvLel;
    ImageView ivClose;
    MCheckBox ivType1;
    LinearLayout llLel;
    TextView tvZb;
    MCheckBox ivType2;
    LinearLayout llZb;
    private String time;

    LinearLayout llCz;
    private MarkaBaseDialog dialog;
    public void showPopwindow() {

        if(views==null){
            views = View.inflate(this, R.layout.red_year_type, null);
            tvLel= views.findViewById(R.id.tv_lel);
            tvZb= views.findViewById(R.id.tv_zb);
            tvLel.setText(time1);
            tvZb.setText(time0);
            ivType1= views.findViewById(R.id.iv_type1);
            ivType2= views.findViewById(R.id.iv_type2);

            llLel= views.findViewById(R.id.ll_lel);

            llZb= views.findViewById(R.id.ll_zb);


            dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
            dialog.show();

            llLel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time=time1;
                    dialog.dismiss();
                }
            });
            llZb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    time=time0;
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    presenter.getDetails(time,1 + "", state + "", TYPE_PULL_REFRESH);
                }
            });
        }else {
            dialog.show();
        }



    }

}
