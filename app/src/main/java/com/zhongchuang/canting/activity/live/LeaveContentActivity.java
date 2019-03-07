package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.DbRecordActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.wxapi.WXPayEntryActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaveContentActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.tv_save)
    TextView tvSave;

    private int type;
    private int status;
    private String data;

    private BasesPresenter presenter;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_leave);
        ButterKnife.bind(this);
        presenter=new BasesPresenter(this);
        data=getIntent().getStringExtra("data");
        if(TextUtil.isNotEmpty(data)){
            etContent.setText(data);
        }

    }

    @Override
    public void bindEvents() {

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etContent.getText().toString().trim())) {
                    presenter.upRoomInfo("",etContent.getText().toString().trim(),"","");

                }else {
                    showToasts(getString(R.string.nrbnwk));
                }
            }
        });
    }

    @Override
    public void initData() {
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {
                Intent intent = new Intent(LeaveContentActivity.this, DbRecordActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
            }

            @Override
            public void navigationimg() {

            }
        });

    }


    private void upRoomInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> map = new HashMap<>();
        map.put("token", SpUtil.getToken(this));
        map.put("userInfoId", SpUtil.getUserInfoId(this));
        map.put("roomInfoId", SpUtil.getString(this, "room_id", ""));
        map.put("roomImage", "");
        map.put("directOverview", "");
        map.put("leaveMassege", etContent.getText().toString().trim());

        HXRequestService api = retrofit.create(HXRequestService.class);


        Call<BaseResponse> baseResponseCall = api.upRoomInfo(map);
        baseResponseCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        showToasts(getString(R.string.bczg));
        closeKeyBoard();
        finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        showToasts(getString(R.string.bcsb));
        closeKeyBoard();
    }
}
