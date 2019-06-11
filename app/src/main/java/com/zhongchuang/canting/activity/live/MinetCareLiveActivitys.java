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
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.MainChild;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MinetCareLiveActivitys extends BaseActivity1 implements BaseContract.View {


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
        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presenter = new BasesPresenter(this);
        presenter.anchorsList();
        adapter = new LiveCareAdapter(this);

        listview.setAdapter(adapter);
    }

    @Override
    public void bindEvents() {
        adapter.setListener(new LiveCareAdapter.selectItemListener() {
            @Override
            public void delete(Care data, int poistion) {
              if(poistion==1){
                  Intent intentc = new Intent(MinetCareLiveActivitys.this, ChatActivity.class);
                  intentc.putExtra("userId", data.anchors_id);
                  startActivity(intentc);
              }else if(poistion==2){
                  if(data.is_enabled.equals("1")){
//                      Intent intent = new Intent(MinetCareLiveActivitys.this, DemoGuest.class);
//                      intent.putExtra("room",data.room_info_id);
//                      intent.putExtra("id",data.room_info_id);
//                      startActivity(intent);
                  }else {
                      showToasts(getString(R.string.zbhwkb));
                  }

              }else if(poistion==3){
               showProgress(getString(R.string.qxzs));
               presenter.focusTV(data.room_info_id,data.user_info_id,"2");
              }
            }
        });
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

        if(type==6){
            dimessProgress();
            showTomast(getString(R.string.xgcg));
        }else if(type==2) {
            presenter.anchorsList();
        }else if(type==3||type==4) {

        }else {
            dimessProgress();
            List<Care> data= (List<Care>) entity;
            if(data!=null&&data.size()>0){
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
                adapter.setData(data);
            }else {
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

