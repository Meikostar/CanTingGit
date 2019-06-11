package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.base.LazyFragment;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.fragment.live.ZhiBoFragment;
import com.zhongchuang.canting.fragment.mall.LiveMineFragment;
import com.zhongchuang.canting.fragment.mall.LiveMineFragments;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class LiveActivity extends BaseAllActivity implements View.OnClickListener , OtherContract.View{


    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.rd_mall)
    RadioButton rdMall;
    @BindView(R.id.rd_mine)
    RadioButton rdMine;
    @BindView(R.id.rd_home)
    RadioButton rdHome;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    private ZhiBoFragment fragment;


    private LazyFragment fragment3;

    protected FragmentTransaction mTransaction;
    private OtherPresenter presenter;
    private int pos = 1;
    private Subscription mSubscription;
    private int type = 1;

    private GAME data;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);
        data = (GAME) getIntent().getSerializableExtra("data");
        presenter=new OtherPresenter(this);
        mTransaction = getSupportFragmentManager().beginTransaction();
        rdGroup.check(R.id.rd_menu_zhuye);
        fragment = new ZhiBoFragment();
        if (data == null) {
            data = new GAME();
        }
        getIntent().putExtra("data", data);

        //pass parameters to chat fragment
        fragment.setArguments(getIntent().getExtras());
        mTransaction.replace(R.id.fragment_container, fragment);
        mTransaction.commit();



//        setEvents();
        setSelect(1);
    }

    @Override
    public void bindEvents() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.LOGIN_FINISH) {
                    exitApp();
                    Intent intent = new Intent(LiveActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("status", 1);
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


    @Override
    public void initData() {
        setLoginMessage();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }


    protected void onResume() {
        super.onResume();
    }


    private void exitApp() {
        String olderToken = SpUtil.getString(this, "token", "");//token值
        String code = SpUtil.getString(this, "code", "");
        if (TextUtils.isEmpty(olderToken)) {
            return;
        } else {
            userInfoId = SpUtil.getString(LiveActivity.this, "userInfoId", "");
            if (userInfoId != null) {
                SpUtil.remove(LiveActivity.this, "userInfoId");//token值
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
            startActivity(new Intent(LiveActivity.this, LoginActivity.class));
            logOut();

        }
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

    public boolean isLogin;

    private void setLoginMessage() {
        String token = SpUtil.getString(this, "token", "");

        isLogin = !TextUtils.isEmpty(token) && !token.equals("") && !TextUtils.isEmpty(token) && !token.equals("");


    }

    @OnClick({R.id.rd_mall, R.id.rd_mine, R.id.rd_home})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.rd_mall:
                toMainPage(0);
                break;

            case R.id.rd_mine:
                if (isLogin) {
                    toMainPage(3);
                } else {
                    showToasts(getString(R.string.nhwdl));
                }

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
                fragment = new ZhiBoFragment();
                getIntent().putExtra("data", data);

                //pass parameters to chat fragment
                fragment.setArguments(getIntent().getExtras());
                mTransaction.replace(R.id.fragment_container, fragment);
                mTransaction.commit();
                break;

            case 3:
                rdGroup.check(R.id.rd_mine);
                pos = 3;
                String anchor = SpUtil.isAnchor(LiveActivity.this);

                if (TextUtil.isNotEmpty(anchor) && anchor.equals("0")) {
                    fragment3 = new LiveMineFragment();
                } else {
                    presenter.getPushUrl();
                    fragment3 = new LiveMineFragments();
                }

                mTransaction.replace(R.id.fragment_container, fragment3);
                mTransaction.commit();

                break;
            case 4:
                pos = 4;
                finish();
                break;

        }
    }


    @Override
    public <T> void toEntity(T entity, int type) {
        aliLive aliLive= (aliLive) entity;
        if(aliLive!=null&&TextUtil.isNotEmpty(aliLive.pushurl)){
            SpUtil.putString(this,"live_url",aliLive.pushurl);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

         showToasts(msg);
    }
}




