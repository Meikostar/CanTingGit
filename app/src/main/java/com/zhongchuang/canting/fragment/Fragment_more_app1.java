package com.zhongchuang.canting.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.MainActivity;
import com.zhongchuang.canting.activity.OtherAppActivity;
import com.zhongchuang.canting.activity.chat.ChatSplashActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallActivity;
import com.zhongchuang.canting.activity.offline.AuditOfflineActivity;
import com.zhongchuang.canting.activity.shop.AppStoreActivity;
import com.zhongchuang.canting.adapter.HomeItemdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/10/27.
 */

public class Fragment_more_app1 extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.grid_content1)
    NoScrollGridView gridContent1;
    @BindView(R.id.card)
    CardView card;


    private RegisterPresenter presenter;
    private boolean isLogin= HomeActivitys.isLogin;
    private TimeCount timeCount;
    private HomeItemdapter homedapter;


    private GAME messageGroup=HomeActivitys.messageGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        homedapter = new HomeItemdapter(getActivity());

        gridContent1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //商城

                        Intent intentSupe = new Intent(getActivity(), AuditOfflineActivity.class);
                        startActivity(intentSupe);
                        break;
                    case 10: //商城
                        CanTingAppLication.CompanyType=Constant.type[0];

                        Intent intentsss = new Intent(getActivity(), OtherAppActivity.class);
//                        intentsss.putExtra("data", indepent1[0]);
                        intentsss.putExtra("type", 0);
                        startActivity(intentsss);

                        break;
                    case 11://乐聊
                        CanTingAppLication.CompanyType=Constant.type[1];
                        Intent intent = new Intent(getActivity(), OtherAppActivity.class);
//                        intent.putExtra("data", indepent1[1]);
                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 12://乐聊
                        CanTingAppLication.CompanyType=Constant.type[2];
                        Intent intentss = new Intent(getActivity(), OtherAppActivity.class);
//                        intentss.putExtra("data", indepent1[2]);
                        intentss.putExtra("type", 2);
                        startActivity(intentss);


                }

            }
        });
        if(!SpUtil.isSuper()){
            setData();
            gridContent1.setAdapter(homedapter);
        }

        return view;
    }

    private int type;
    private int cont;
    private List<HOMES> datas = new ArrayList<>();
    public  int[] imgs = {R.drawable.homes_super};
    public String[] names = {"店铺审核"};

    public void setData() {

        datas.clear();
        cont = 0;
        for (int url : imgs) {
            HOMES homes = new HOMES();
            homes.name = names[cont];
            homes.url = url;
            datas.add(homes);
        }

        if(homedapter!=null){
            homedapter.setData(datas);
            homedapter.notifyDataSetChanged();
        }else {
            homedapter = new HomeItemdapter(getActivity());
            homedapter.setData(datas);
            homedapter.notifyDataSetChanged();
        }




        cont = 0;



    }
    public void setType(int type) {
        this.type = type;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    //计时器
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


        }

        @Override
        public void onFinish() {


        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        unbinder.unbind();
        if(timeCount!=null){
            timeCount.cancel();
        }


    }

    private ProgressDialog mDialog;


    private void initData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}




