package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.LiveCareAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.MainChild;
import com.zhongchuang.canting.been.MainPageBean;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.MainFragmentPresenter;
import com.zhongchuang.canting.presenter.impl.MainFragmentPresenterImpl;
import com.zhongchuang.canting.viewcallback.ZhuYeViewCallBack;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HostCareLiveActivity extends BaseActivity1 implements ZhuYeViewCallBack {


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

    private MainFragmentPresenter presenter;

    private View view;



  private int state;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_care);
        ButterKnife.bind(this);
        state=getIntent().getIntExtra("state",0);

        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getIntegral();
        presenter = new MainFragmentPresenterImpl(this);
        adapter = new LiveCareAdapter(this);
        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {

    }

    private LiveCareAdapter adapter;



    private void initLiveStreamBanner(List<MainChild> imgMeiShiReses) {

    }


    public void getIntegral() {
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getUserIntegralLog(map).enqueue(new BaseCallBack<INTEGRALIST>() {
            @Override
            public void onSuccess(INTEGRALIST bean) {
                if(bean!=null&&bean.data!=null&&bean.data.size()>0){
                    loadingView.showPager(LoadingPager.STATE_SUCCEED);

                }else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                    loadingView.setContent(getString(R.string.zwmx));
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
    public void onResultSuccess(MainPageBean mainPageBean) {


    }


    private List<ZhiBo_GuanZhongBean.DataBean> cooks = new ArrayList<>();


    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }

    @Override
    public void onFail(int code, String msg) {
        dimessProgress();
        ToastUtils.showNormalToast(msg);
    }


}

