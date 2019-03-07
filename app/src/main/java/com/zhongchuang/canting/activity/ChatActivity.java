package com.zhongchuang.canting.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hyphenate.util.EasyUtils;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.ui.ChatFragment;
import com.zhongchuang.canting.easeui.ui.EaseChatFragment;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.PersonInfoPresenter;
import com.zhongchuang.canting.presenter.impl.PersonInfoPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StatusBarUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.viewcallback.GetUserInfoViewCallback;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * chat activity，EaseChatFragment
 */
public class ChatActivity extends FragmentActivity implements BaseContract.View {
    public static ChatActivity activityInstance;
    @BindView(R.id.bg_img)
    ImageView bg_img;
    @BindView(R.id.container)
    FrameLayout container;
    private EaseChatFragment chatFragment;
    int chatType;
    String toChatUsername;
    String group_id;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        StatusBarUtils.setWindowStatusBarColor(this, getResources().getColor(R.color.wordColor));
        setContentView(R.layout.activity_chat);
        presenter = new BasesPresenter(this); ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
                PermissionGen.with(this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.REQUEST_INSTALL_PACKAGES,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();

        }
        int choosType= SpUtil.getInt(this,"choosTyp",0);
        if(choosType==0){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg0));
        }else if (choosType==1){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg1));
        }else if (choosType==2){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg2));
        }else if (choosType==3){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg3));
        }else if (choosType==4){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg4));
        }else if (choosType==5){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg5));
        }else if (choosType==6){
            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg6));
        }

        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        group_id = getIntent().getExtras().getString("group_id");
        CHATMESSAGE chatmessage = (CHATMESSAGE) getIntent().getSerializableExtra(EaseConstant.EXTRA_CHATMSG);
        if (chatmessage != null) {
            go2Chat(chatmessage);
        } else {

            Map<String, String> map = new HashMap<>();
             username = getIntent().getStringExtra("userId");
            if(TextUtil.isEmpty(username)){
                finish();
            }
            if (username.equals("ifun")) {
                return;
            }
            if (TextUtils.isEmpty(username)) {
                username= SpUtil.getUserInfoId(this);
            }

            presenter.friendInfo(username);
        }
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.FINISH) {
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

    }
    private String username;
    private BasesPresenter presenter;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        if (chatFragment != null) {
            chatFragment.onBackPressed();

        }

        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    private void go2Chat(CHATMESSAGE chatmessage) {
        chatFragment = new ChatFragment();
        getIntent().putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
        getIntent().putExtra("group_id", group_id);
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }



    @Override
    public <T> void toEntity(T entity, int type) {
        FriendInfo info= (FriendInfo) entity;
        info.friendsId=username;
        CHATMESSAGE chatmessage = CHATMESSAGE.fromLogin(info);
        go2Chat(chatmessage);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
