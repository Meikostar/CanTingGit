package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SIGN;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.LiaoTianFragment;
import com.zhongchuang.canting.fragment.MineFragment;
import com.zhongchuang.canting.fragment.live.ZhiBoFragment;
import com.zhongchuang.canting.hud.ToastUtils;


import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseAllActivity implements View.OnClickListener {


    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rd_menu_liaotian)
    RadioButton rdMenuLiaotian;
    @BindView(R.id.rd_menu_zhibo)
    RadioButton rdMenuZhibo;
    @BindView(R.id.rd_menu_zhuye)
    RadioButton rdMenuZhuye;
    @BindView(R.id.rd_menu_meishi)
    RadioButton rdMenuMeishi;
    @BindView(R.id.rd_menu_game)
    RadioButton rdMenuGame;
    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    private LiaoTianFragment mLiaoTianFragment;
    private ZhiBoFragment mZhiBoFragment;
    private MineFragment mMallFragment;

    private MineFragment mYouXiFragment;
    protected FragmentTransaction mTransaction;

    private int pos = 0;
   private Subscription mSubscription;
//    private ProfileInfoHelper infoHelper;
    private int type;


    @Override
    public void initViews() {
        setContentView(R.layout.activity_mains);
        ButterKnife.bind(this);

        type=getIntent().getIntExtra("type",2);

        mTransaction = getSupportFragmentManager().beginTransaction();
        rdGroup.check(R.id.rd_menu_zhuye);
        mMallFragment = new MineFragment();
        mTransaction.replace(R.id.fragment_container, mMallFragment);
        mTransaction.commit();
//        infoHelper = new ProfileInfoHelper(this);
//        infoHelper.getMyProfile();
        pos = 2;

        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if(bean.type==SubscriptionBean.LIVECLOSE){
                    logoutLive();
                }else if(bean.type==SubscriptionBean.CHANGE){
                    String data= (String) bean.content;
                    if (TextUtils.isEmpty(data)) {
                        toMainPage(1);
                    }else {
                        toMainPage(3);
                    }

                }else if(bean.type==SubscriptionBean.SIGN){

                    getSign();
                }else if(bean.type==SubscriptionBean.FINISH){
                     finish();

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
//        setEvents();
        setSelect(type);
    }

    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    private void setEvents() {
//
//        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//
//                //设置主布局随菜单滑动而滑动
//                int drawerViewWidth = drawerView.getWidth();
//
//                //设置控件最先出现的位置
//                double padingLeft = drawerViewWidth * (1 - 0.618) * (1 - slideOffset);
//                menu_layout.setPadding((int) padingLeft, 0, 0, 0);
//                main_layout.setTranslationX(drawerViewWidth * slideOffset);
//
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                setLoginMessage();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
    }
    public void logoutLive(){


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.logoutDirect(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse anchor) {

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
//        setLoginMessage();
    }
    public void getSign(){

        String sign = SpUtil.getSign(this);
        if(type!=1){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.generation(map).enqueue(new BaseCallBack<SIGN>() {
            @Override
            public void onSuccess(SIGN sign1) {
                CanTingAppLication.signStr=sign1.data;

                login();
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
    // 登录
    private void login(){
        String strAccount =CanTingAppLication.userId;
        String strPwd = CanTingAppLication.signStr;

        if (TextUtils.isEmpty(strAccount) || TextUtils.isEmpty(strPwd)){
//            DlgMgr.showMsg(this, R.string.msg_input_empty);
            return;
        }

    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }

    public void setSelect(int type){
        if(type==1){
            toMainPage(1);
            rdMenuMeishi.setVisibility(View.VISIBLE);
            rdMenuZhibo.setVisibility(View.VISIBLE);
            rdMenuLiaotian.setVisibility(View.GONE);
            rdMenuGame.setVisibility(View.GONE);
            rdMenuZhuye.setVisibility(View.GONE);
        }else if(type==2){
            rdMenuMeishi.setVisibility(View.VISIBLE);
            rdMenuZhibo.setVisibility(View.GONE);
            rdMenuLiaotian.setVisibility(View.GONE);
            rdMenuGame.setVisibility(View.GONE);
            rdMenuZhuye.setVisibility(View.VISIBLE);
            toMainPage(2);
        }else if(type==3){
            rdMenuMeishi.setVisibility(View.VISIBLE);
            rdMenuZhibo.setVisibility(View.GONE);
            rdMenuLiaotian.setVisibility(View.GONE);
            rdMenuGame.setVisibility(View.VISIBLE);
            rdMenuZhuye.setVisibility(View.GONE);
            toMainPage(4);
        }
    }
    @OnClick({R.id.rd_menu_liaotian, R.id.rd_menu_zhibo, R.id.rd_menu_zhuye, R.id.rd_menu_meishi, R.id.rd_menu_game})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.rd_menu_liaotian:
                toMainPage(0);
                break;
            case R.id.rd_menu_zhibo:
                toMainPage(1);
                break;
            case R.id.rd_menu_zhuye:
                toMainPage(2);
                break;
            case R.id.rd_menu_meishi:
                toMainPage(3);
                break;
            case R.id.rd_menu_game:
                toMainPage(4);
                break;

        }
    }

    private void removeAllFragment(FragmentTransaction mTransaction) {
        if (mLiaoTianFragment != null) {
            mTransaction.remove(mLiaoTianFragment);
        }
        if (mZhiBoFragment != null) {
            mTransaction.remove(mZhiBoFragment);
        }
        if (mMallFragment != null) {
            mTransaction.remove(mMallFragment);
        }

        if (mYouXiFragment != null) {
            mTransaction.remove(mYouXiFragment);
        }

    }

    private long clickTime = 0;

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            switch (pos) {
//                case 2:
//                    if (System.currentTimeMillis() - clickTime > 2500) {
//                        clickTime = System.currentTimeMillis();
//                        ToastUtils.showNormalToast("再按一次退出");
//                        return true;
//                    } else {
//                        this.finish();
//                        System.exit(0);
//                    }
//                    break;
//                default:
//                    rdGroup.check(R.id.rd_menu_zhuye);
//                    pos = 2;
//                    mTransaction = getSupportFragmentManager().beginTransaction();
//                    removeAllFragment(mTransaction);
//                    mMallFragment = new MallFragment();
//                    mTransaction.replace(R.id.fragment_container, mMallFragment);
//                    mTransaction.commit();
//                    return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    public void openDraweLayout() {
        String token = SpUtil.getString(this, "token", "");
        if (token == null || token.equals("")) {
            Intent gotoLogin = new Intent(this, LoginActivity.class);
            startActivity(gotoLogin);

        } else {
//            drawerLayout.openDrawer(Gravity.LEFT);
        }
//    perIntent = new Intent(getActivity(), PersonActivity.class);
//    startActivity(perIntent);


    }

    public void toMainPage(int p) {

        mTransaction = getSupportFragmentManager().beginTransaction();
        removeAllFragment(mTransaction);
        switch (p) {
            case 0:
                rdGroup.check(R.id.rd_menu_liaotian);
                pos = 0;
                if(!StringUtil.isLogin(MainActivity.this)){
                    return;
                }
                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                intent.putExtra("status",1);
                startActivity(intent);
                break;
            case 1:
                rdGroup.check(R.id.rd_menu_zhibo);
                pos = 1;
                mZhiBoFragment = new ZhiBoFragment(this);
                mTransaction.replace(R.id.fragment_container, mZhiBoFragment);
                mTransaction.commit();
                break;
            case 2:
                rdGroup.check(R.id.rd_menu_zhuye);
                pos = 2;
                mMallFragment = new MineFragment();
                mTransaction.replace(R.id.fragment_container, mMallFragment);
                mTransaction.commit();
                break;
            case 3:
             finish();
                break;
            case 4:
                rdGroup.check(R.id.rd_menu_game);
                pos = 4;
                mYouXiFragment = new MineFragment();
                mTransaction.replace(R.id.fragment_container, mYouXiFragment);
                mTransaction.commit();
                break;

        }
    }

//    @Override
//    public void updateProfileInfo(TIMUserProfile profile) {
//        if (null != profile) {
//            MySelfInfo.getInstance().setAvatar(profile.getFaceUrl());
//            MySelfInfo.getInstance().setSign(profile.getSelfSignature());
//            if (!TextUtils.isEmpty(profile.getNickName())) {
//                MySelfInfo.getInstance().setNickName(profile.getNickName());
//            } else {
//                MySelfInfo.getInstance().setNickName(profile.getIdentifier());
//            }
//        }
//    }
//
//    @Override
//    public void updateUserInfo(int requestCode, List<TIMUserProfile> profiles) {
//
//    }
}




