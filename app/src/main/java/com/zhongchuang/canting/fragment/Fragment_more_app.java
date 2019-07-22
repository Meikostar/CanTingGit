package com.zhongchuang.canting.fragment;

import android.app.Activity;
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
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.MainActivity;
import com.zhongchuang.canting.activity.chat.ChatSplashActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallActivity;
import com.zhongchuang.canting.activity.shop.AppStoreActivity;
import com.zhongchuang.canting.adapter.HomeItemdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/10/27.
 */

public class Fragment_more_app extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.grid_content1)
    NoScrollGridView gridContent1;
    @BindView(R.id.card)
    CardView card;


    private RegisterPresenter presenter;

    private TimeCount timeCount;
    private HomeItemdapter homedapter;

    private int[] homeimg1 = {R.drawable.homes_1, R.drawable.homes_2, R.drawable.homes_3,
            R.drawable.homes_4, R.drawable.homes_5, R.drawable.homes_6, R.drawable.homes_7, R.drawable.homes_8};
    private GAME messageGroup=HomeActivitys.messageGroup;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        homedapter = new HomeItemdapter(getActivity());
        gridContent1.setAdapter(homedapter);

        gridContent1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //商城
//                        Intent intentsss = new Intent(HomeActivitys.this, FaceCreatActivity.class);
                        Intent intentsss = new Intent(getActivity(), ShopCompsiteMallActivity.class);
                        intentsss.putExtra("type", 1);
                        startActivity(intentsss);

                        break;
                    case 1://乐聊

                        Intent intent = new Intent(getActivity(), ShopMallActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 2://乐聊
                        Intent intentss = new Intent(getActivity(), ShopMallActivity.class);
                        intentss.putExtra("type", 2);
                        startActivity(intentss);


                        break;
                    case 3://直播

                        if (HomeActivitys.isLogin) {
                            Intent intent2 = new Intent(getActivity(), ChatSplashActivity.class);

//                            intent2.putExtra("data", data);
                            intent2.putExtra("type", 1);
                            startActivity(intent2);
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        break;
                    case 4://我的

                        if (HomeActivitys.isLogin) {
                            Intent intents = new Intent(getActivity(), ChatSplashActivity.class);
                            if (HomeActivitys.messageGroup == null) {
                                return;
                            }
                            intents.putExtra("data", HomeActivitys.messageGroup);
                            startActivity(intents);
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        break;
                    case 5://同城

                        if (HomeActivitys.isLogin) {
                            Intent intent3 = new Intent(getActivity(), MainActivity.class);
                            intent3.putExtra("type", 3);
                            startActivity(intent3);
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                        break;
                    case 6: //应用
                        ShareUtils.showMyShareApp(getActivity(), "", "");


                        break;
                    case 7: //应用
                        Intent intent4 = new Intent(getActivity(), AppStoreActivity.class);
                        intent4.putExtra("type", 1);
                        startActivity(intent4);
                        break;

                }

            }
        });

        initView();

        return view;
    }


    private HomeActivitys activitys;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitys = ((HomeActivitys) activity);//通过强转成宿主activity，就可以获取到传递过来的数据
        activitys.setListener(new HomeActivitys.MessageNotifyListener() {
            @Override
            public void messageNotify(int cout) {
                setData(cout);
            }
        });
    }

    private int type;
    private int cont;
    private List<HOMES> datas = new ArrayList<>();
    public void setData(int cout) {
        String[] indepent1 = {getString(R.string.qyzg), getString(R.string.cjzg), getString(R.string.szds), getString(R.string.zb),
                getString(R.string.ll), getString(R.string.grzx), getString(R.string.appfx), getString(R.string.yy), getString(R.string.appfx)};
        datas.clear();
        cont = 0;
        for (int url : homeimg1) {
            HOMES homes = new HOMES();
            homes.name = indepent1[cont];
            homes.url = url;
            if (cont == 4) {
                homes.cout = cout;
            }
            cont++;
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

    private void initView() {


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




