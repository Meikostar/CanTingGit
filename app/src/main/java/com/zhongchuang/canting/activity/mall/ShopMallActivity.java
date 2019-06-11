package com.zhongchuang.canting.activity.mall;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.ProvinceModel;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.mall.IntegralMallFragment;
import com.zhongchuang.canting.fragment.mall.MallFragment;
import com.zhongchuang.canting.fragment.mall.Minefagment;
import com.zhongchuang.canting.fragment.mall.ShopCarFragment;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RxBus;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class ShopMallActivity extends BaseAllActivity implements View.OnClickListener {


    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rd_mall)
    RadioButton rdMall;
    @BindView(R.id.rd_malls)
    RadioButton rdMalls;
    @BindView(R.id.rd_shop)
    RadioButton rdShop;
    @BindView(R.id.rd_mine)
    RadioButton rdMine;
    @BindView(R.id.rd_home)
    RadioButton rdHome;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    private MallFragment fragment;
    private IntegralMallFragment fragment1;
    private ShopCarFragment fragment2;
    private Minefagment fragment3;

    protected FragmentTransaction mTransaction;

    private int pos = 1;
    private Subscription mSubscription;
    //    private ProfileInfoHelper infoHelper;
    private int type=1;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_mall);
        ButterKnife.bind(this);

        type = getIntent().getIntExtra("type", 1);

        mTransaction = getSupportFragmentManager().beginTransaction();
        rdGroup.check(R.id.rd_menu_zhuye);
        fragment = new MallFragment();
        mTransaction.replace(R.id.fragment_container, fragment);
        mTransaction.commit();


//        setEvents();
        if(type==1){
            rdMalls.setVisibility(View.GONE);
        }else {
            rdMall.setVisibility(View.GONE);
        }
        setSelect(type);
    }

    @Override
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.LOGIN_FINISH){
                    exitApp();
                    Intent intent = new Intent(ShopMallActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("status",1);
                    startActivity(intent);
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
    }
    private String userInfoId;
    private void exitApp() {
        String olderToken = SpUtil.getString(this, "token", "");//token值
        String code = SpUtil.getString(this, "code", "");
        if (TextUtils.isEmpty(olderToken)) {
            return;
        } else {
            userInfoId = SpUtil.getString(ShopMallActivity.this, "userInfoId", "");
            if (userInfoId != null) {
                SpUtil.remove(ShopMallActivity.this, "userInfoId");//token值
            }
            //误操作  清理   不用处理
            String userId = SpUtil.getString(this, "userId", "");
            String userloid = SpUtil.getString(this, "userloid", "");

            if (olderToken != null) {
                SpUtil.remove(this, "token");//token值
            }

            if (code != null) {
                SpUtil.remove(this, "code");//userId值
            }

            //误操作  清理   不用处理
            if (userId != null) {
                SpUtil.remove(this, "userId");//userId值
            }

            if (userloid != null) {
                SpUtil.remove(this, "userloid");//userId值
            }

            logOut();

        }
    }
    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }


    protected void onResume() {
        super.onResume();
    }

    private void logOut() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
//                        tvLogin.setText("登录");

                        Log.d(TAG, "main+12: " + "登出成功");
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        }).start();

    }
    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }

    public void setSelect(int type) {
        if (type == 1) {
            toMainPage(0);
        } else if (type == 2) {
            toMainPage(1);
        } else if (type == 3) {
            toMainPage(2);
        } else if (type == 4) {
            toMainPage(3);
        }
    }

    @OnClick({R.id.rd_mall, R.id.rd_malls, R.id.rd_shop, R.id.rd_mine, R.id.rd_home})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.rd_mall:
                toMainPage(0);
                break;
            case R.id.rd_malls:
                toMainPage(1);
                break;
            case R.id.rd_shop:
                toMainPage(2);
                break;
            case R.id.rd_mine:
                toMainPage(3);
                break;
            case R.id.rd_home:
                toMainPage(4);
                break;

        }
    }

    private void removeAllFragment(FragmentTransaction mTransaction) {
        if (fragment != null) {
            mTransaction.remove(fragment);
        }
        if (fragment1 != null) {
            mTransaction.remove(fragment1);
        }
        if (fragment2 != null) {
            mTransaction.remove(fragment2);
        }

        if (fragment3 != null) {
            mTransaction.remove(fragment3);
        }

    }


    public void toMainPage(int p) {

        mTransaction = getSupportFragmentManager().beginTransaction();
        removeAllFragment(mTransaction);
        switch (p) {
            case 0:
                rdGroup.check(R.id.rd_mall);
                pos = 0;
                fragment = new MallFragment();
                mTransaction.replace(R.id.fragment_container, fragment);
                mTransaction.commit();
                break;
            case 1:
                rdGroup.check(R.id.rd_malls);
                pos = 1;
                fragment1 = new IntegralMallFragment();
                mTransaction.replace(R.id.fragment_container, fragment1);
                mTransaction.commit();
                break;
            case 2:
                rdGroup.check(R.id.rd_shop);
                pos = 2;
                fragment2 = new ShopCarFragment();
                fragment2.setType(3);
                mTransaction.replace(R.id.fragment_container, fragment2);
                mTransaction.commit();
                break;
            case 3:
                rdGroup.check(R.id.rd_mine);
                pos = 3;
                if(!isLogin()){
                    startActivity(new Intent(ShopMallActivity.this, LoginActivity.class));
                    return;
                }
                fragment3 = new Minefagment();
                mTransaction.replace(R.id.fragment_container, fragment3);
                mTransaction.commit();

                break;
            case 4:
                pos = 4;
                finish();
                break;

        }
    }

    private boolean isLogin() {
        boolean isLogin;
        String token = SpUtil.getString(this, "token", "");
        isLogin = !TextUtils.isEmpty(token) && !token.equals("");
        return isLogin;

    }
}




