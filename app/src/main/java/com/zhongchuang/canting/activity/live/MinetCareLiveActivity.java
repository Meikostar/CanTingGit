package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.adapter.CareMineAdapter;
import com.zhongchuang.canting.adapter.LiveCareAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.been.Favor;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.MainChild;
import com.zhongchuang.canting.been.MainPageBean;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.MainFragmentPresenter;
import com.zhongchuang.canting.presenter.impl.MainFragmentPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.ZhuYeViewCallBack;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MinetCareLiveActivity extends BaseActivity1 implements BaseContract.View {


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


    private String state;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_care);
        ButterKnife.bind(this);
        state = getIntent().getStringExtra("data");
        serchEdit.setText(R.string.wdfs);
        tvCity.setVisibility(View.VISIBLE);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        presenter.focusList(state, "1");
        adapter = new CareMineAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentc = new Intent(MinetCareLiveActivity.this, BlackCareLiveActivity.class);
                intentc.putExtra("data", state);
                startActivity(intentc);
            }
        });
        adapter.setListener(new CareMineAdapter.selectItemListener() {
            @Override
            public void delete(Care data, int poistion) {
                if (poistion == 0) {
                    Intent intentc = new Intent(MinetCareLiveActivity.this, ChatActivity.class);
                    intentc.putExtra("userId", data.user_info_id);
                    startActivity(intentc);
                } else {
                    showProgress(getString(R.string.tjz));
                    presenter.upFavoriteType(state, "0", data.user_info_id);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private CareMineAdapter adapter;


    private void initLiveStreamBanner(List<MainChild> imgMeiShiReses) {

    }


    public void getIntegral() {
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getUserIntegralLog(map).enqueue(new BaseCallBack<INTEGRALIST>() {
            @Override
            public void onSuccess(INTEGRALIST bean) {
                if (bean != null && bean.data != null && bean.data.size() > 0) {
                    loadingView.showPager(LoadingPager.STATE_SUCCEED);

                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                    loadingView.setContent(getString(R.string.zwfls));
                }


            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);

                ToastUtils.showNormalToast(t);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
   if(presenter!=null){
       presenter.focusList(state, "1");
   }

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

        if (type == 6) {
            showTomast(getString(R.string.xgcg));
            presenter.focusList(state, "1");
        } else {
            dimessProgress();
            List<Care> data = (List<Care>) entity;
            if (data != null && data.size() > 0) {
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(data);
            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
                loadingView.setContent(getString(R.string.zwfls));
            }
        }

    }


    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}

