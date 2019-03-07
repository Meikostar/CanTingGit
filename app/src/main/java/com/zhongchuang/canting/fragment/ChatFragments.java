package com.zhongchuang.canting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.RechargeActivity;
import com.zhongchuang.canting.adapter.MemberAdapters;
import com.zhongchuang.canting.adapter.MessageAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.GIFTDATA;
import com.zhongchuang.canting.been.Gift;
import com.zhongchuang.canting.been.Member;
import com.zhongchuang.canting.been.Message;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleImageView;
import com.zhongchuang.canting.widget.FragmentGiftDialog;
import com.zhongchuang.canting.widget.GiftItemView;
import com.zhongchuang.canting.widget.HorizontialListView;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.popupwindow.PopView_Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

import rx.Subscription;
import rx.functions.Action1;
import tyrantgit.widget.HeartLayout;


/**
 * author：Administrator on 2016/12/26 09:35
 * description:文件说明
 * version:版本
 */
public class ChatFragments extends Fragment implements View.OnClickListener, View.OnLayoutChangeListener , BaseContract.View {

    private HorizontialListView listview;

    private GiftItemView giftView;
    private MemberAdapters mAdapter;
    private MessageAdapter messageAdapter;
    private ArrayList<Member> members;
    private ArrayList<Message> messages;
    private ArrayList<Gift> gifts;
    private HeartLayout heartLayout;
    private CircleImageView ivImg;
    private Random mRandom;
    private Timer mTimer = new Timer();
    private View sendView, menuView, topView;
    private EditText sendEditText;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat, null, false);
        initView(view);
        initData();

        return view;
    }

    private PopView_Comment popComment;
    private ImageView share;
    private TextView tv_care;
    private TextView tv_name;
    private TextView tv_count;
    private TextView textView;
    private FragmentGiftDialog dialog;

    private void initView(View view) {
        mRandom = new Random();
        listview = (HorizontialListView) view.findViewById(R.id.list);
        presenter = new BasesPresenter(this);
        mAdapter = new MemberAdapters(getActivity());
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                showDialog(mAdapter.datas.get(i));
            }
        });
        messageAdapter = new MessageAdapter(getActivity());
        giftView = (GiftItemView) view.findViewById(R.id.gift_item_first);
        tv_care = (TextView) view.findViewById(R.id.tv_care);
        ivImg =  view.findViewById(R.id.iv_img);
        tv_name =  view.findViewById(R.id.tv_name);
        tv_count =  view.findViewById(R.id.tv_count);
        textView =  view.findViewById(R.id.textView);
        heartLayout = (HeartLayout) view.findViewById(R.id.heart_layout);

        view.findViewById(R.id.send_message).setOnClickListener(this);
        view.findViewById(R.id.share).setOnClickListener(this);
        view.findViewById(R.id.gift).setOnClickListener(this);
        view.findViewById(R.id.close).setOnClickListener(this);
        view.findViewById(R.id.message).setOnClickListener(this);
        sendView = view.findViewById(R.id.layout_send_message);
        menuView = view.findViewById(R.id.layout_bottom_menu);

        topView = view.findViewById(R.id.layout_top);
        sendEditText = (EditText) view.findViewById(R.id.send_edit);
        //获取屏幕高度
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        rootView = view.findViewById(R.id.activity_main);
        rootView.addOnLayoutChangeListener(this);
        getDirIndexInfo();
        listGiftRoom();
        getBalance();
        initListener();
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
                if(bean.type==SubscriptionBean.NOTIFY_MONEY){
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
        tv_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (attention == 0) {
//                    presenter.focusTV(handlers.roomNumber,anchorid,1+"");
//                } else {
//                    presenter.focusTV(handlers.roomNumber,anchorid,2+"");
//                }

            }
        });

    }
    private BasesPresenter presenter;


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //这个地方已经产生了耦合，若还有其他的activity，这个地方就得修改

    }

    ;

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

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
        mTimer.cancel();
        mSubscription.unsubscribe();

    }
   private Gift gifs;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_message) {


        } else if (id == R.id.gift) {

            if (datas != null && datas.size() > 0) {
                FragmentGiftDialog.newInstance(datas).setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                    @Override
                    public void click(Gift gift) {
                        gifs=gift;
                        if(gift.gift_intege<=balance){
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


        } else if (id == R.id.close) {
            getActivity().finish();
        } else if (id == R.id.share) {
            ShareUtils.showMyShares(getActivity(), getString(R.string.jiguang), "http://www.gwlaser.tech");
        } else if (id == R.id.message) {
            heartLayout.addHeart(randomColor());
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
   private String  money;
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
                balance=sign1.data.userIntegral;
                avator=sign1.data.getHeadImage();
                nickname=sign1.data.getNickname();

                if(TextUtils.isEmpty(nickname)){
                    nickname=sign1.data.accountId;
                }
                money=sign1.data.totalAmount+"";
//                tv_care.setText("关注");
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
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
                    if(data.data!=null){
                        FragmentGiftDialog.newInstance(data.data).setOnGridViewClickListener(new FragmentGiftDialog.OnGridViewClickListener() {
                            @Override
                            public void click(Gift gift) {

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

    public void getDirIndexInfo() {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);


        netService api = HttpUtil.getInstance().create(netService.class);
        api.getDirIndexInfo(map).enqueue(new BaseCallBack<BEAN>() {
            @Override
            public void onSuccess(BEAN bean) {
                BEAN data=bean.data;
                ShareBean shareBean = new ShareBean();
                shareBean.img_=data.room_image;
                shareBean.content_=data.direct_see_name+getString(R.string.zzklgk);
                shareBean.content_=data.direct_see_name+getString(R.string.zzgszbklgkb);
                shareBean.title_=data.direct_see_name;
                shareBean.url_="http://119.23.212.8:8088/cloudOne/index.html";
                CanTingAppLication.shareBean=shareBean;
                Glide.with(getActivity()).load(StringUtil.changeUrl(data.room_image)).asBitmap().placeholder(R.drawable.default_head).into(ivImg);
                anchorid=data.user_info_id;
                if(!TextUtils.isEmpty(data.direct_see_name)){
                    tv_name.setText(data.direct_see_name);
                }
//                if(!TextUtils.isEmpty(data.dirRoomInfo.totalPeople)){
//                    tv_count.setText(data.dirRoomInfo.totalPeople);
//                }
//                count=data.giftcount;
                if(!TextUtils.isEmpty(data.fans_num)){
                    textView.setText(getString(R.string.fensis)+data.fans_num);
                }
                if(!TextUtils.isEmpty(data.type)){
                    if(data.type.equals("1")){
                        attention=1;
                        tv_care.setText(getString(R.string.qxgz));
                    }else if(data.type.equals("0")){
                        attention=0;
                        tv_care.setText(getString(R.string.gz));
                    }
                    textView.setText(getString(R.string.fensis)+data.fans_num);
                }

                if(data.threeuser!=null){
                    mAdapter.setDatas(data.threeuser);
                }

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    public void setNumber(String number){
        tv_count.setText(getString(R.string.zxrs)+number);
    }
    public void setFaus(String number){
        textView.setText(getString(R.string.fensis)+number);
    }
    private String anchorid;
    private String avator;
    private String nickname;
    public void sendGiftForGirl(String giftinfoid,String giftName, final int cout) {


        Map<String, String> map = new HashMap<>();
        map.put("userinfoid", CanTingAppLication.userId);
        map.put("giftinfoid", giftinfoid);
        map.put("giftName", giftName);
        map.put("anchorid",anchorid);
        map.put("giftnumber", ""+cout);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.sendGiftForGirl(map).enqueue(new BaseCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
//                if(TextUtils.isEmpty(nickname)){
//                    handlers.sendGift(TextUtils.isEmpty(avator)?gifs.gift_image:(avator+"@@@@@"+gifs.gift_image));
//                }else {
//                    handlers.sendGift(TextUtils.isEmpty(avator)?gifs.gift_image:(avator+"@@@@@"+gifs.gift_image+"@@@@@"+nickname));
//                }

                balance=Double.valueOf(bean.data);
                  count=count+1;
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
    private View views=null;
    private  MarkaBaseDialog dialogs;
    private  TextView sure = null;
    private TextView cancel = null;
    private  TextView title = null;
    private EditText reson = null;
    public void showPopwindow() {

        if(views==null){
            views = View.inflate(getActivity(), R.layout.del_friend, null);
            sure = (TextView) views.findViewById(R.id.txt_sure);
            cancel = (TextView) views.findViewById(R.id.txt_cancel);
            title = (TextView) views.findViewById(R.id.tv_title);
            reson = (EditText) views.findViewById(R.id.edit_reson);
            title.setText(R.string.jfyywqcz);
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
                    intent.putExtra("type",1);
                    intent.putExtra("bal",money);
                    startActivity(intent);
                    dialogs.dismiss();
                }
            });
        }else {
            title.setText(getString(R.string.jfyywqcz));
            dialogs.show();
        }

    }

    @Override
    public <T> void toEntity(T entity, int type) {
          if(type==1){
              ToastUtils.showNormalToast(getString(R.string.gzcg));
              attention=1;
              tv_care.setText(getString(R.string.qxgz));
          }else {
              attention=0;
              tv_care.setText(getString(R.string.gz));
          }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
