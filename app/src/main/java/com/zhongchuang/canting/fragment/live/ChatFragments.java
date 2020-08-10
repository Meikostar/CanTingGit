package com.zhongchuang.canting.fragment.live;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.RechargeActivity;
import com.zhongchuang.canting.activity.live.LiveChatRoomFragment;
import com.zhongchuang.canting.adapter.MemberAdapters;
import com.zhongchuang.canting.adapter.MessageAdapter;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.zhongchuang.canting.allive.vodplayerview.activity.AliyunPlayerSkinActivityMin;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.GIFTDATA;
import com.zhongchuang.canting.been.Gift;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.been.Member;
import com.zhongchuang.canting.been.Message;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.viewcallback.CareListener;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.FragmentGiftDialog;
import com.zhongchuang.canting.widget.GiftItemView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.popupwindow.PopView_Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;
import tyrantgit.widget.HeartLayout;


/**
 * author：Administrator on 2016/12/26 09:35
 * description:文件说明
 * version:版本
 */
public class ChatFragments extends Fragment implements View.OnClickListener, View.OnLayoutChangeListener, BaseContract.View,AliyunPlayerSkinActivity.GiftMessageListener,AliyunPlayerSkinActivityMin.GiftMessageListener  {


    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.gift_item_first)
    GiftItemView giftItemFirst;
    @BindView(R.id.heart_layout)
    HeartLayout heartLayout;
    @BindView(R.id.gift)
    ImageView gift;
    @BindView(R.id.send_message)
    TextView sendMessage;
    @BindView(R.id.message)
    ImageView message;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.close)
    ImageView close;
    @BindView(R.id.layout_bottom_menu)
    LinearLayout layoutBottomMenu;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    Unbinder unbinder;
    private GiftItemView giftView;
    private MemberAdapters mAdapter;
    private MessageAdapter messageAdapter;
    private ArrayList<Member> members;
    private ArrayList<Message> messages;
    private ArrayList<Gift> gifts;


    private Random mRandom;
    private Timer mTimer = new Timer();
    private View sendView, menuView;
    private EditText sendEditText;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private View rootView;
    private String room_id;
    private String room_info_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        initData();


        return view;
    }

    private PopView_Comment popComment;


    public void setRoomId(String room_id) {
        this.room_id = room_id;
    }
    public void setRoomInfoId(String room_info_id) {
        this.room_info_id = room_info_id;
    }
    private FragmentGiftDialog dialog;

    public void setCareListener(CareListener listener) {
        this.listener = listener;
    }

    @Override
    public void click(int poistion) {
        if(poistion==1){
            if (datas != null && datas.size() > 0) {
                FragmentGiftDialog.newInstance(datas).setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                    @Override
                    public void click(Gift gift) {
                        gifs = gift;
                        listener.setGift(gifs);
                        if(gift.gift_intege<=CanTingAppLication.Chargeintegral){
                            sendGiftForGirl(gift.gift_info_id,gift.gift_name,1);
                        }else {
                            showPopwindow();
                        }

                    }
                }).show(getChildFragmentManager(), "dialog");

            } else {
                type = 1;
                listGiftRoom();
            }
        }else {
            popComment.showPopView("");
        }
    }

    public interface  CareListener{
        void setData(BEAN data);
        void setGift(Gift data);
        void change();
    }
    private CareListener listener;
    private CountDownTimer countDownTimer;

    private void initView(View view) {
        mRandom = new Random();
        presenter = new BasesPresenter(this);
        mAdapter = new MemberAdapters(getActivity());
        messageAdapter = new MessageAdapter(getActivity());
        giftView = view.findViewById(R.id.gift_item_first);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.change();
                popComment.showPopView("");
            }
        });
        share.setOnClickListener(this);
        gift.setClickable(true);
        gift.setFocusable(true);
        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datas != null && datas.size() > 0) {
                    FragmentGiftDialog.newInstance(datas).setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                        @Override
                        public void click(Gift gift) {
                            gifs = gift;

                        if(gift.gift_intege<=CanTingAppLication.Chargeintegral){
                            sendGiftForGirl(gift.gift_info_id,gift.gift_name,1);
                        }else {
                            showPopwindow();
                        }

                        }
                    }).show(getChildFragmentManager(), "dialog");

                } else {
                    type = 1;
                    listGiftRoom();
                }
            }
        });

        message.setOnClickListener(this);
        sendView = view.findViewById(R.id.layout_send_message);
        menuView = view.findViewById(R.id.layout_bottom_menu);


        //获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        rootView = view.findViewById(R.id.activity_main);
        rootView.addOnLayoutChangeListener(this);
        getDirIndexInfo(id);
        countDownTimer = new CountDownTimer((60 * 60 * 24 * 1000 * 365), 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getDirIndexInfo("");
            }
            @Override
            public void onFinish() {

            }
        }.start();
        listGiftRoom();
        getBalance();
        initListener();
        go2Chat();
        popComment = new PopView_Comment(getActivity());
        popComment.setOnPop_CommentListenerr(new PopView_Comment.OnPop_CommentListener() {
            @Override
            public void chooseDeviceConfig(String commentStr) {
                chatFragment.sendMessages(commentStr);
            }
        });
    }

    private void showDialog(BEAN m) {
//        FragmentDialog.newInstance(m.name, m.sig, "确定", "取消", -1, false, new FragmentDialog.OnClickBottomListener() {
//            @Override
//            public void onPositiveClick() {
//
//            }
//
//            @Override
//            public void onNegtiveClick() {
//
//            }
//        }).show(getChildFragmentManager(), "dialog");

    }

    private Subscription mSubscription;

    public void initListener() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.NOTIFY_MONEY) {
                    getBalance();
//                    BEAN data= (BEAN) bean.content;
//                    money=data.totalAmount;
//                    balance=Double.valueOf(data.totalGlod);
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

    private BasesPresenter presenter;


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //这个地方已经产生了耦合，若还有其他的activity，这个地方就得修改

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(presenter!=null){
            presenter.getUserIntegral();
        }

    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    /**
     * 添加一些数据
     */
    private void initData() {

        gifts = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }

        mTimer.cancel();
        mSubscription.unsubscribe();

    }

    private Gift gifs;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_message) {


        } else if (id == R.id.gift) {


        } else if (id == R.id.close) {
            getActivity().finish();
        } else if (id == R.id.share) {
            ShareUtils.showMyShares(getActivity(), getString(R.string.jiguang), "http://www.gwlaser.tech");
        } else if (id == R.id.message) {


            heartLayout.addHeart(randomColor());
            sendMessage();
        }

    }


    public static void sendMessage(){
        if(chatFragment!=null){
            chatFragment.sendMessages("*&@@&*");
        }

    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            menuView.setVisibility(View.GONE);
//            Toast.makeText(MainActivity.getActivity(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            menuView.setVisibility(View.VISIBLE);
//            Toast.makeText(MainActivity.getActivity(), "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
        }
    }

    private int type;


    private int attention;
    /**
     * 关注
     */
    private List<Gift> datas;

    private double balance;
    private String money;

    /**
     * 余额
     */
    public void getBalance() {
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getUserInfo(map).enqueue(new BaseCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo sign1) {
//                ToastUtils.showNormalToast("关注成功");
                balance = sign1.data.userIntegral;
                avator = sign1.data.getHeadImage();
                nickname = sign1.data.getNickname();

                if (TextUtils.isEmpty(nickname)) {
                    nickname = sign1.data.accountId;
                }
                money = sign1.data.totalAmount + "";
//                tv_care.setText("关注");
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    private static LiveChatRoomFragment chatFragment;

    private void go2Chat() {
        FriendInfo info = new FriendInfo();
        info.friendsId = room_id;
        CHATMESSAGE chatmessage = CHATMESSAGE.fromLogin(info);
        chatFragment = new LiveChatRoomFragment();
        getActivity().getIntent().putExtra(EaseConstant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
        getActivity().getIntent().putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
        getActivity().getIntent().putExtra("group_id", room_id);
        //pass parameters to chat fragment
        chatFragment.setArguments(getActivity().getIntent().getExtras());

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    /**
     * 余额
     */
    public void listGiftRoom() {
        Map<String, String> map = new HashMap<>();

        netService api = HttpUtil.getInstance().create(netService.class);
        api.listGiftRoom(map).enqueue(new BaseCallBack<GIFTDATA>() {
            @Override
            public void onSuccess(GIFTDATA data) {
//                ToastUtils.showNormalToast("关注成功");
                if (type == 0) {
                    datas = data.data;
                } else {
                    type = 0;
                    if (data.data != null) {
                        FragmentGiftDialog.newInstance(data.data).setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                            @Override
                            public void click(Gift gift) {
                                listener.setGift(gifs);
                                if (!gifts.contains(gift)) {
                                    gifts.add(gift);
                                    giftView.setGift(gift);
                                }
                                giftView.addNum(1);
                            }
                        }).show(getChildFragmentManager(), "dialog");
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

    private long count;

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public void getDirIndexInfo(String id) {


        Map<String, String> map = new HashMap<>();
        if(TextUtil.isNotEmpty(id)){
            map.put("userInfoId", id);
        }else {
            map.put("userInfoId", CanTingAppLication.userId);
        }

        map.put("roomInfoId", room_info_id);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getDirIndexInfo(map).enqueue(new BaseCallBack<BEAN>() {
            @Override
            public void onSuccess(BEAN bean) {
                if(getActivity()!=null){
                    BEAN data = bean.data;
                    ShareBean shareBean = new ShareBean();
                    shareBean.img_ = data.room_image;
                    shareBean.content_ = data.direct_see_name + getString(R.string.zzklgk);
//                shareBean.content_ = data.direct_see_name + getString(R.string.zzgszbklgkb);
                    shareBean.title_ = data.direct_see_name;
                    shareBean.url_ = com.zhongchuang.canting.db.Constant.APP_LIVE_DOWN;
                    CanTingAppLication.shareBean = shareBean;

                    anchorid = data.user_info_id;
                    listener.setData(data);
//                if(!TextUtils.isEmpty(data.dirRoomInfo.totalPeople)){
//                    tv_count.setText(data.dirRoomInfo.totalPeople);
//                }
//                count=data.giftcount;


                    if (data.threeuser != null) {
                        mAdapter.setDatas(data.threeuser);
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


    private String anchorid;
    private String avator;
    private String nickname;

    public void sendGiftForGirl(String giftinfoid, String giftName, final int cout) {


        Map<String, String> map = new HashMap<>();
        map.put("userinfoid", CanTingAppLication.userId);
        map.put("giftinfoid", giftinfoid);
        map.put("roominfoid",  room_info_id);
        map.put("giftName", giftName);
        map.put("anchorid", anchorid);
        map.put("giftnumber", "" + cout);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.sendGiftForGirl(map).enqueue(new BaseCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                if(TextUtils.isEmpty(nickname)){
                    chatFragment.sendMessages(gifs.gift_image + "&!&&!&" + "游客");
                }else {
                    chatFragment.sendMessages(gifs.gift_image + "&!&&!&" + nickname);
                }
                presenter.getUserIntegral();
//                balance = Double.valueOf(bean.data);
                count = count + 1;
//                    textView.setText("礼物数量: "+count);

                if (!gifts.contains(gifs)) {
                    gifts.add(gifs);
                    giftView.setGift(gifs);
                }
                giftView.addNum(1);

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    private View views = null;
    private MarkaBaseDialog dialogs;
    private TextView sure = null;
    private TextView cancel = null;
    private TextView title = null;
    private EditText reson = null;

    public void showPopwindow() {

        if (views == null) {
            views = View.inflate(getActivity(), R.layout.del_friend, null);
            sure = views.findViewById(R.id.txt_sure);
            cancel = views.findViewById(R.id.txt_cancel);
            title = views.findViewById(R.id.tv_title);
            reson = views.findViewById(R.id.edit_reson);
            title.setText("打赏给主播的兑换值必须是充值兑换值！你的充值兑换值已用完，快去充值吧!");
            sure.setText(R.string.mscz);

            dialogs = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
            dialogs.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogs.dismiss();
                }
            });
            final EditText finalReson = reson;
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("bal", money);
                    startActivity(intent);
                    dialogs.dismiss();
                }
            });
        } else {
            title.setText(getString(R.string.jfyywqcz));
            dialogs.show();
        }

    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 1) {
            ToastUtils.showNormalToast(getString(R.string.gzcg));
            attention = 1;
        } else if(type==19) {
            Ingegebean bean = (Ingegebean) entity;
            if(bean==null){
                return;
            }
            if (TextUtil.isNotEmpty(bean.money_buy_integral)) {
                CanTingAppLication.totalintegral =  CanTingAppLication.totalintegral +Double.valueOf(bean.money_buy_integral);
                CanTingAppLication.Chargeintegral = Double.valueOf(bean.money_buy_integral);
            }
            if (TextUtil.isNotEmpty(bean.chat_integral)) {
                CanTingAppLication.totalintegral =  CanTingAppLication.totalintegral +Double.valueOf(bean.chat_integral);
            }
            if (TextUtil.isNotEmpty(bean.jewel_integral)) {
                CanTingAppLication.totalintegral =  CanTingAppLication.totalintegral +Double.valueOf(bean.jewel_integral);
            }
            if (TextUtil.isNotEmpty(bean.direct_integral)) {
                CanTingAppLication.totalintegral =  CanTingAppLication.totalintegral +Double.valueOf(bean.direct_integral);
            }


        }else {
            attention = 0;
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
