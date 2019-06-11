package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.LiveHandAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主播排行榜
 */
public class LiveHandActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.zhuye_geren)
    ImageView zhuyeGeren;
    @BindView(R.id.serch_edit)
    TextView serchEdit;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private Intent perIntent;
    private Unbinder bind;

    private BasesPresenter presenter;

    private View view;


    private int state;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_live_hand);
        ButterKnife.bind(this);
        state = getIntent().getIntExtra("state", 0);

        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        presenter = new BasesPresenter(this);
        presenter.getHostdirHostList();
        adapter = new LiveHandAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {
        adapter.setListener(new LiveHandAdapter.selectItemListener() {
            @Override
            public void delete(Hand data, int poistion) {

                if(data.favorite_type.equals("1")){
                    showProgress("");
                    presenter.focusTV(data.room_info_id,data.anchors_id, "2");
                }else if(data.favorite_type.equals("2")){
                    showProgress("");
                    presenter.focusTV(data.room_info_id,data.anchors_id, "1");
                }else if(data.favorite_type.equals("0")){
                    showTomast(getString(R.string.czsb));
                }

            }
        });
    }

    @Override
    public void initData() {

    }

    private LiveHandAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();

//        mainBannertwo.startTurning(4000);
//        mainBannerThree.startTurning(4000);
    }

    @Override
    public void onStop() {
        super.onStop();
        dimessProgress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 1||type==2) {
            showToasts(getString(R.string.czcg));
            presenter.getHostdirHostList();
        } else {
            dimessProgress();
            List<Hand> hand = (List<Hand>) entity;
            if (hand != null && hand.size() != 0) {
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
            } else {

                loadingView.setContent(getString(R.string.zwphbsj));
                loadingView.showPager(LoadingPager.STATE_EMPTY);

            }
            adapter.setData(hand);
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

