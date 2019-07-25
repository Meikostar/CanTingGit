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

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.USER;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.bean.GROUPS;
import com.zhongchuang.canting.easeui.bean.PLACE;
import com.zhongchuang.canting.easeui.bean.USER_AVATAR;
import com.zhongchuang.canting.easeui.ui.ChatFragment;
import com.zhongchuang.canting.easeui.ui.EaseChatFragment;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
//        if(choosType==0){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg0));
//        }else if (choosType==1){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg1));
//        }else if (choosType==2){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg2));
//        }else if (choosType==3){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg3));
//        }else if (choosType==4){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg4));
//        }else if (choosType==5){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg5));
//        }else if (choosType==6){
//            bg_img.setImageDrawable(getResources().getDrawable(R.drawable.bg6));
//        }

        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        group_id = getIntent().getExtras().getString("group_id");
        CHATMESSAGE chatmessage = (CHATMESSAGE) getIntent().getSerializableExtra(EaseConstant.EXTRA_CHATMSG);
        if (chatmessage != null) {
            if(chatType==EaseConstant.CHATTYPE_GROUP){

                GROUPS group = chatmessage.getGroup();
                if(group!=null&&TextUtil.isNotEmpty(group.getGroup_id())){
                    getGroupInfo(group.getGroup_id());
                    go2Chat(chatmessage);
                }else {
                    presenter.getGroupInfo(toChatUsername);
                    getGroupInfo(toChatUsername);
                }
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
//获取此会话的所有消息
//                List<EMMessage> messages = conversation.getAllMessages();
//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                if(conversation!=null){
                    EMMessage lastMessage = conversation.getLastMessage();
                    String content="";
                    try {
                        content = lastMessage.getStringAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    if(TextUtil.isNotEmpty(content)){
                        lastMessage.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,"");
                    }
                }


            }else {
                go2Chat(chatmessage);
            }


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
    public void getGroupInfo(final String groupId) {
        if(CanTingAppLication.easeDatas==null){
            CanTingAppLication.easeDatas=new HashMap<>();
        }else {
            CanTingAppLication.easeDatas.clear();
        }
        Map<String, String> map = new HashMap<>();
//        map.put("userInfoId", CanTingAppLication.userId);
        map.put("groupsId", groupId);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.getGroupInfo(map).enqueue(new BaseCallBack<USER>() {
            @Override
            public void onSuccess(USER group) {


                for (USER user : group.data) {
                    USER_AVATAR avatar = new USER_AVATAR();
                    if (!TextUtils.isEmpty(user.getNickname())) {
                        avatar.setName(user.getNickname());
                    }
                    if (!TextUtils.isEmpty(user.head_image)) {
                        avatar.setUser_avatar(user.head_image);
                    }
                    if (!TextUtils.isEmpty(user.user_group_name)) {
                        avatar.user_group_name = user.user_group_name;
                    }
                    if (!TextUtils.isEmpty(user.user_info_id)) {
                        avatar.user_info_id = user.user_info_id;
                    }
                    if(CanTingAppLication.easeDatas!=null){
                        CanTingAppLication.easeDatas.put(user.user_info_id,user.getNickname()+","+user.head_image);
                    }

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
            Intent intent = new Intent(this, HomeActivitys.class);
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
        if(type==55){
            FriendInfo info= (FriendInfo) entity;
            info.friendsId=username;
            CHATMESSAGE chatmessage = CHATMESSAGE.transGroup(info);
            go2Chat(chatmessage);
        }else {
            FriendInfo info= (FriendInfo) entity;
            info.friendsId=username;
            CHATMESSAGE chatmessage = CHATMESSAGE.fromLogin(info);
            go2Chat(chatmessage);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
