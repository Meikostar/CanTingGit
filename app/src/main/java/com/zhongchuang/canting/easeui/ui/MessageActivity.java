package com.zhongchuang.canting.easeui.ui;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.activity.CaptureActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.chat.AddFriendActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.fragment.TongXunLuFragment;
import com.zhongchuang.canting.fragment.message.MessageChatFragment;
import com.zhongchuang.canting.fragment.message.QFriendCircleFragment;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StatusBarUtils;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.functions.Action1;

public class MessageActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.rd_menu_message)
    RadioButton rd_menu_message;
    @BindView(R.id.rd_menu_contact)
    RadioButton rd_menu_contact;
    @BindView(R.id.rd_menu_mine)
    RadioButton rd_menu_mine;

    @BindView(R.id.rd_group)
    RadioGroup rdGroup;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.rd_menu_meishi)
    RadioButton rdMenuMeishi;
    @BindView(R.id.txt_unread)
    TextView txtUnread;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.rd_menu_qfriend)
    RadioButton rdMenuQfriend;

    private TongXunLuFragment tongXunLuFragment;
    private MessageMineFragment mZhuYeFragment;
    private MessageChatFragment liaoTianFragment;
    private QFriendCircleFragment qFriendCircleFragment;

    protected FragmentTransaction mTransaction;

    private int pos = 0;
    private int cout = 0;
    private Subscription mSubscription;
    public static GAME game;

    //    private ProfileInfoHelper infoHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        game = (GAME) getIntent().getSerializableExtra("data");
        StatusBarUtils.setWindowStatusBarColor(this, getResources().getColor(R.color.wordColor));

        cout = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                EMConversation emConversation = EMClient.getInstance().chatManager().getConversation(conversation.conversationId());
                if (emConversation != null) {
                    int unreadMsgCount = emConversation.getUnreadMsgCount();
                    cout = cout + unreadMsgCount;
                }

            }
        }
        if (cout == 0) {
            txtUnread.setVisibility(View.GONE);
            llBg.setVisibility(View.GONE);
        } else {
            txtUnread.setVisibility(View.VISIBLE);
            llBg.setVisibility(View.VISIBLE);
            txtUnread.setText(cout + "");
        }
        mTransaction = getSupportFragmentManager().beginTransaction();
        rdGroup.check(R.id.rd_menu_zhuye);
        liaoTianFragment = new MessageChatFragment();
        liaoTianFragment.setArguments(getIntent().getExtras());
        mTransaction.replace(R.id.fragment_container, liaoTianFragment);
        mTransaction.commit();
        rdGroup.check(R.id.rd_menu_message);
        pos = 0;

        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.OPEN) {
                    PermissionGen.with(MessageActivity.this)
                            .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                            .permissions(Manifest.permission.CAMERA)
                            .request();
                } else if (bean.type == SubscriptionBean.MESSAGENOTIFIS) {
                    int cout = (int) bean.content;

                    toMainPage(0);
                    if (cout == 0) {
                        txtUnread.setVisibility(View.GONE);
                        llBg.setVisibility(View.GONE);
                    } else {
                        txtUnread.setVisibility(View.VISIBLE);
                        llBg.setVisibility(View.VISIBLE);
                        txtUnread.setText(cout + "");
                    }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();

    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSuccess() {
        Intent intent = new Intent(MessageActivity.this, CaptureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 0);

    }

    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestFail() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        cout = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                EMConversation emConversation = EMClient.getInstance().chatManager().getConversation(conversation.conversationId());
                if (emConversation != null) {
                    int unreadMsgCount = emConversation.getUnreadMsgCount();
                    cout = cout + unreadMsgCount;
                }

            }
        }
        if (txtUnread == null) {
            return;
        }
        if (cout == 0) {
            txtUnread.setVisibility(View.GONE);
            llBg.setVisibility(View.GONE);
        } else {
            txtUnread.setVisibility(View.VISIBLE);
            llBg.setVisibility(View.VISIBLE);
            txtUnread.setText(cout + "");
        }

    }

    public void getReadCout() {
        toMainPage(0);
        cout = 0;
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            if (conversation.getAllMessages().size() != 0) {
                EMConversation emConversation = EMClient.getInstance().chatManager().getConversation(conversation.conversationId());
                if (emConversation != null) {
                    int unreadMsgCount = emConversation.getUnreadMsgCount();
                    cout = cout + unreadMsgCount;
                }

            }
        }
        if (txtUnread == null) {
            return;
        }
        if (cout == 0) {
            txtUnread.setVisibility(View.GONE);
            llBg.setVisibility(View.GONE);
        } else {
            txtUnread.setVisibility(View.VISIBLE);
            llBg.setVisibility(View.VISIBLE);
            txtUnread.setText(cout + "");
        }
    }

    @Override
    public void onClick(View v) {
        onViewClicked(v);
    }

    @OnClick({R.id.rd_menu_message, R.id.rd_menu_contact, R.id.rd_menu_mine, R.id.rd_menu_meishi, R.id.rd_menu_qfriend})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.rd_menu_message:
                toMainPage(0);
                break;
            case R.id.rd_menu_contact:
                toMainPage(1);
                break;
            case R.id.rd_menu_qfriend:
                toMainPage(2);
                break;
            case R.id.rd_menu_mine:
                toMainPage(3);
                break;
            case R.id.rd_menu_meishi:
                finish();
                break;


        }
    }

    private void removeAllFragment(FragmentTransaction mTransaction) {
        if (tongXunLuFragment != null) {
            mTransaction.remove(tongXunLuFragment);
        }
        if (liaoTianFragment != null) {
            mTransaction.remove(liaoTianFragment);
        }
        if (mZhuYeFragment != null) {
            mTransaction.remove(mZhuYeFragment);
        }

        if (qFriendCircleFragment != null) {
            mTransaction.remove(qFriendCircleFragment);
        }


    }


    public void toMainPage(int p) {

        mTransaction = getSupportFragmentManager().beginTransaction();
        removeAllFragment(mTransaction);
        switch (p) {
            case 0:
                rdGroup.check(R.id.rd_menu_message);
                pos = 0;
                liaoTianFragment = new MessageChatFragment(this);
                liaoTianFragment.setArguments(getIntent().getExtras());
                mTransaction.replace(R.id.fragment_container, liaoTianFragment);
                mTransaction.commit();
                break;
            case 1:
                rdGroup.check(R.id.rd_menu_contact);
                pos = 1;
                tongXunLuFragment = new TongXunLuFragment(this);
                tongXunLuFragment.setArguments(getIntent().getExtras());
                mTransaction.replace(R.id.fragment_container, tongXunLuFragment);
                mTransaction.commit();
                break;
            case 2:
                rdGroup.check(R.id.rd_menu_qfriend);
                pos = 2;
                qFriendCircleFragment = new QFriendCircleFragment();
                mTransaction.replace(R.id.fragment_container, qFriendCircleFragment);
                mTransaction.commit();
                break;
            case 3:
                rdGroup.check(R.id.rd_menu_mine);
                pos = 3;
                mZhuYeFragment = new MessageMineFragment();
                mTransaction.replace(R.id.fragment_container, mZhuYeFragment);
                mTransaction.commit();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            if (TextUtils.isEmpty(result)) {
                return;
            }
            String[] userids = result.split(",");
            if (result.contains("@@!!##$$%%")) {
                if (userids == null || userids.length != 3) {
                    return;
                }
                showPopwindows(userids[0], userids[1]);
            } else {
                if (userids == null || userids.length != 3) {
                    return;
                }
                FriendSearchBean.DataBean dataBean = new FriendSearchBean.DataBean();
                dataBean.setNickname(userids[0]);
                dataBean.setRingLetterName(userids[1]);
                Intent intent = new Intent(MessageActivity.this, AddFriendActivity.class);
                game.data.remove(0);
                intent.putExtra("data", game);
                intent.putExtra("datas", dataBean);
                startActivityForResult(intent, 2);
//                showPopwindow(, userids[1]);
            }


        }
    }

    private MarkaBaseDialog dialog;

    public void showPopwindows(final String name, final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(getString(R.string.jrs) + name + getString(R.string.q));
        dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFriendList(id, name);
                dialog.dismiss();

            }
        });
    }

    public void addFriendList(String id, final String groupsName) {

        Map<String, String> map = new HashMap<>();
//        map.put("userInfoId", CanTingAppLication.userId);
        map.put("addusers", SpUtil.getUserInfoId(MessageActivity.this));
        map.put("groupId", id);
        map.put("groupsName", groupsName);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.addFriendList(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse group) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.REFRESSH, ""));
                ToastUtils.showNormalToast(getString(R.string.nyjs) + groupsName + getString(R.string.qcy));
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    private void addFriendRequest(final String nickName, String hxNameId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(this));
        map.put("token", SpUtil.getString(this, "token", ""));
        map.put("chatUserId", hxNameId);

        Call<BaseResponse> call = api.addFriend(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse bs = response.body();

                Toast.makeText(MessageActivity.this, getString(R.string.hyqqfscg) + nickName + getString(R.string.hy), Toast.LENGTH_SHORT).show();
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FRIEND, ""));


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LogUtil.d(t.toString());
                Toast.makeText(MessageActivity.this, R.string.tjhysb, Toast.LENGTH_SHORT).show();
            }
        });
    }

}




