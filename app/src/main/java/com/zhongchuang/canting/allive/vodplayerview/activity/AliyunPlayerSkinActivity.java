package com.zhongchuang.canting.allive.vodplayerview.activity;


import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.live.MineVideoActivity;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.adapter.HandGitsAdapter;
import com.zhongchuang.canting.adapter.QFriendsAdapter;
import com.zhongchuang.canting.adapter.VideoLiveAdapter;
import com.zhongchuang.canting.allive.vodplayerview.constants.PlayParameter;
import com.zhongchuang.canting.allive.vodplayerview.utils.ScreenUtils;
import com.zhongchuang.canting.allive.vodplayerview.utils.VidStsUtil;
import com.zhongchuang.canting.allive.vodplayerview.utils.download.DownloadDBHelper;
import com.zhongchuang.canting.allive.vodplayerview.view.choice.AlivcShowMoreDialog;
import com.zhongchuang.canting.allive.vodplayerview.view.control.ControlLiveView;
import com.zhongchuang.canting.allive.vodplayerview.view.download.DownloadView;
import com.zhongchuang.canting.allive.vodplayerview.view.more.AliyunShowMoreValue;
import com.zhongchuang.canting.allive.vodplayerview.view.more.ShowMoreView;
import com.zhongchuang.canting.allive.vodplayerview.view.more.SpeedValue;
import com.zhongchuang.canting.allive.vodplayerview.view.tipsview.ErrorInfo;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunScreenMode;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView.OnPlayerViewClickListener;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView.PlayViewType;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.StatusBarUtil;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.CommetLikeBean;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.GIFTDATA;
import com.zhongchuang.canting.been.Gift;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.been.videobean;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.fragment.live.ChatFragments;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.presenter.OtherContract;
import com.zhongchuang.canting.presenter.OtherPresenter;
import com.zhongchuang.canting.utils.AdapterUtility;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.viewcallback.CareListener;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.FragmentGiftDialog;
import com.zhongchuang.canting.widget.GiftItemView;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RegularListView;
import com.zhongchuang.canting.widget.StickyScrollView;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;
import com.zhongchuang.canting.widget.popupwindow.PopView_CancelOrSure;
import com.zhongchuang.canting.widget.popupwindow.PopView_Comment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 播放器和播放列表界面
 */
public class AliyunPlayerSkinActivity extends AppCompatActivity implements OtherContract.View, BaseContract.View {

    private DownloadDBHelper dbHelper;
    private AliyunDownloadConfig config;
    private PlayerHandler playerHandler;
    private DownloadView dialogDownloadView;
    private AlivcShowMoreDialog showMoreDialog;
    private String url;
    private int type;
    private String room_info_id;
    private String id;
    private String name;


    public void setCareListener(CareListener listener) {
        this.listener = listener;
    }


    private CareListener listener;

    private boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }


    private AliyunScreenMode currentScreenMode = AliyunScreenMode.Small;

    private AliyunVodPlayerView mAliyunVodPlayerView = null;
    private TextView tvCare;
    private RelativeLayout flbg;




    private ErrorInfo currentError = ErrorInfo.Normal;
    /**
     * 开启设置界面的请求码
     */
    private static final int CODE_REQUEST_SETTING = 1000;
    /**
     * 设置界面返回的结果码, 100为vid类型, 200为url类型
     */
    private static final int CODE_RESULT_TYPE_VID = 100;
    private static final int CODE_RESULT_TYPE_URL = 200;
    private static String DEFAULT_URL = "rtmp://alive.chushenduojin.cn/zx/live_1234567?auth_key=1552975791-0-0-034983b047ea69b08db7f2cb52c08751";
    private static final String DEFAULT_VID = "6e783360c811449d8692b2117acc9212";
    /**
     * get StsToken stats
     */
    private boolean inRequest;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private GiftItemView giftView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isStrangePhone()) {
            //            setTheme(R.style.ActTheme);
        } else {
            setTheme(R.style.NoActionTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_player_layout_skin);


        url = getIntent().getStringExtra("url");
        type = getIntent().getIntExtra("type",0);
        name = getIntent().getStringExtra("name");
        room_info_id = getIntent().getStringExtra("room_info_id");
        id = getIntent().getStringExtra("id");
        presenter = new OtherPresenter(this);
        presenters = new BasesPresenter(this);
        presenter.getLiveUrl(id);
        presenters.friendInfo(username);
        dbHelper = DownloadDBHelper.getDownloadHelper(getApplicationContext(), 1);
        initAliyunPlayerView();
        inittab();

        if (TextUtil.isNotEmpty(url)) {
            selectPosition(1);
            if(url.contains("https:")||url.contains("http:")){
                DEFAULT_URL = url;

            }else {
                DEFAULT_URL = Constant.APP_FILE_NAME+url;

            }
            setPlaySource();

        }

    }
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private ChatFragments chatFragments;
    public interface GiftMessageListener{
        void click(int poistion);
    }
    private GiftMessageListener giftMessageListener;
    public void setGiftMessageListener(GiftMessageListener listener){
        giftMessageListener=listener;
    }
    private void addFragment() {
        mFragments = new ArrayList<>();
        chatFragments = new ChatFragments();
        chatFragments.setRoomId(room_id);
        chatFragments.setId(id);
        chatFragments.setRoomInfoId(room_info_id);
        mFragments.add(chatFragments);
        giftMessageListener= chatFragments;

    }
    private ArrayList<Gift> gifts;
    public void initViewPager(){

        addFragment();
        gifts=new ArrayList<>();
        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        stub_chat.setAdapter(mainViewPagerAdapter);
        stub_chat.setOffscreenPageLimit(1);//设置缓存view 的个数
        stub_chat.setScanScroll(false);
        chatFragments.setCareListener(new ChatFragments.CareListener() {
            @Override
            public void setData(BEAN data) {
                if(data==null){
                    return;
                }
               setHostData(data);

            }
            @Override
            public void setGift(Gift gift) {
                if(gift==null){
                    return;
                }
                if(mAliyunVodPlayerView.getScreenMode()==AliyunScreenMode.Full){
                    if (!gifts.contains(gift)) {
                        gifts.add(gift);
                        giftView.setGift(gift);
                    }
                    giftView.addNum(1);
                }


            }
            @Override
            public void change() {

            }
        });
    }
    private BEAN ben;
    private int status;
    public void setHostData(BEAN data){
        ben=data;
        if(TextUtil.isNotEmpty(data.type)){
            if(data.type.equals("1")){
                iv_care.setImageDrawable(getResources().getDrawable(R.drawable.live_gz));
                tv_care.setText("已订阅");
                flbg.setBackgroundColor(getResources().getColor(R.color.bluess));
            }else {
                tv_care.setText("订阅");
                iv_care.setImageDrawable(getResources().getDrawable(R.drawable.live_gzs));
                flbg.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
        String title="";
        if(TextUtil.isNotEmpty(data.live_address)){
            status=1;
            title=(TextUtil.isNotEmpty(ben.room_image)?ben.room_image:"#")+","+ (TextUtil.isNotEmpty(ben.user_info_nickname)?ben.user_info_nickname:"#")+","+ben.fans_num+","+ ben.type+","+(TextUtil.isNotEmpty(ben.live_address)?ben.live_address:"#")+","+(TextUtil.isNotEmpty(name)?name:"#");

            mAliyunVodPlayerView.updateTitleViews(title);


        }
//        if(TextUtil.isNotEmpty(data.live_address)&&status==0){
//            status=1;
//            if(TextUtil.isNotEmpty(name)){
//                mAliyunVodPlayerView.updateTitleViews(name+" ("+data.live_address+")");
//            }else {
//                mAliyunVodPlayerView.updateTitleViews( data.direct_see_name+" ("+data.live_address+")");
//            }
//
//        }
        ShareBean shareBean = new ShareBean();
        shareBean.img_ = data.room_image;
        shareBean.content_ = data.direct_see_name + getString(R.string.zzklgk);
//                shareBean.content_ = data.direct_see_name + getString(R.string.zzgszbklgkb);
        shareBean.title_ = data.direct_see_name;
        shareBean.url_ = Constant.APP_LIVE_DOWN;
        CanTingAppLication.shareBean = shareBean;
        tvFee.setText("粉丝数："+data.fans_num);

        if (!TextUtils.isEmpty(ben.user_info_nickname)) {
            tvName.setText(ben.user_info_nickname);
        }
        Glide.with(AliyunPlayerSkinActivity.this).load(ben.room_image).asBitmap().transform(new CircleTransform(AliyunPlayerSkinActivity.this)).placeholder(R.drawable.editor_ava).into(iv_imgs);

        tvCout.setText(ben.fans_num);

    }

    /**
     * init视频列表tab
     */

    private TextView tvChat;
    private TextView tv_care;
    private ImageView iv_care;
    private TextView tvZb;
    private TextView tvCout;
    private TextView tvRank;
    private ImageView ivChat;
    private ImageView ivZb;
    private ImageView iv_imgs;
    private ImageView ivRank;
    private ImageView iv_share;

    private TextView tvName;
    private TextView tvFee;
    private TextView tvChats;
    private TextView tvLiveContent;
    private TextView tvVideo;
    private TextView tvDetail;
    private StickyScrollView scrollView;
    private LinearLayout ll_zb;
    private LinearLayout ll_bgs;
    private LoadingPager loadingViewZb;
    private LoadingPager loadingView;

    private void inittab() {


        tvDetail = (TextView) findViewById(R.id.tv_detail);
        scrollView = (StickyScrollView) findViewById(R.id.scrooll);
        giftView = (GiftItemView) findViewById(R.id.gift_item_firsts);
        ll_zb = (LinearLayout) findViewById(R.id.ll_zb);
        ll_bgs = (LinearLayout) findViewById(R.id.ll_bgs);
        tvVideo = (TextView) findViewById(R.id.tv_video);
        tvChats = (TextView) findViewById(R.id.tv_chat);
        tvLiveContent = (TextView) findViewById(R.id.tv_live_content);
        tvFee = (TextView) findViewById(R.id.tv_fee);
        tvName = (TextView) findViewById(R.id.tv_namess);
        tvChat = (TextView) findViewById(R.id.tv_tab_chat);
        tv_care = (TextView) findViewById(R.id.tv_care);
        iv_care = (ImageView) findViewById(R.id.iv_care);
        tvZb = (TextView) findViewById(R.id.tv_tab_zb);
        tvCout = (TextView) findViewById(R.id.tv_cout);
        tvRank = (TextView) findViewById(R.id.tv_tab_rank);
        ivChat = (ImageView) findViewById(R.id.iv_chat);
        ivZb = (ImageView) findViewById(R.id.iv_zb);
        iv_imgs = (ImageView) findViewById(R.id.iv_imgs);
        ivRank = (ImageView) findViewById(R.id.iv_rank);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        stub_chat = (NoScrollViewPager) findViewById(R.id.viewpager_main);
        stub_zb = (ViewStub) findViewById(R.id.stub_zb);
        stub_hand = (ViewStub) findViewById(R.id.stub_hand);

        tvChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition(0);
            }
        });
        tvZb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition(1);
            }
        });
        tvRank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition(2);
            }
        });
        tvVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                tab_type = TAB_ONE;
                updateTabView();
            }
        });
        tvDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                tab_type = TAB_TWO;
                updateTabView();
            }
        });
        tvChats.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentc = new Intent(AliyunPlayerSkinActivity.this, ChatActivity.class);
                intentc.putExtra("userId", id);
                startActivity(intentc);
            }
        });
        tvName.setFocusable(true);
        tvName.setFocusableInTouchMode(true);
        scrollView.setFocusable(false);
        flbg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ben!=null&&TextUtil.isNotEmpty(ben.type)){
                    if(ben.type.equals("1")){
                        presenters.focusTV(room_info_id,id, "2");
                    }else if(ben.type.equals("0")){
                        presenters.focusTV(room_info_id,id, "1");
                    }
                }
//                flbg.setClickable(false);
            }
        });
        iv_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.showMyShares(AliyunPlayerSkinActivity.this, getString(R.string.jiguang), "http://www.gwlaser.tech");
            }
        });

    }


    /**
     * 根据tab点击事件更新UI
     *
     * @param id
     */
    private QFriendsAdapter qFriendsAdapter;
    private VideoLiveAdapter liveAdapter;
    private HandGitsAdapter handGitsAdapter;
    private FrameLayout container;
    private RegularListView relist_info;
    private ListView relist_infos;
    private LinearLayout ll_rbg;
    private RegularListView relist_hand;

    private List<Gift> dats;


    private void updateUI(int poistion) {


        stub_chat.setVisibility(View.GONE);
        if (view_stub_zb != null)
            stub_zb.setVisibility(View.GONE);
        if (view_stub_hand != null)
            stub_hand.setVisibility(View.GONE);
        switch (poistion) {
            case 1:
                ll_zb.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                stub_chat.setVisibility(View.VISIBLE);

                break;
            case 2:
                ll_zb.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);

                if (view_stub_zb == null) {

                    view_stub_zb = stub_zb.inflate();
                    relist_info = (RegularListView) findViewById(R.id.list_info);
                    relist_infos = (ListView) findViewById(R.id.list_infos);
                    ll_rbg = (LinearLayout) findViewById(R.id.ll_rbg);
                    loadingViewZb = (LoadingPager) findViewById(R.id.loadingViewzb);
                    qFriendsAdapter = new QFriendsAdapter(AliyunPlayerSkinActivity.this);
                    liveAdapter = new VideoLiveAdapter(AliyunPlayerSkinActivity.this);
                    updateTabView();
                    intListener();
                } else {
                    stub_zb.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                ll_zb.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                if (view_stub_hand == null) {
                    view_stub_hand = stub_hand.inflate();
                    relist_hand = (RegularListView) findViewById(R.id.list_rank);
                    loadingView = (LoadingPager) findViewById(R.id.loadingView);
                    handGitsAdapter = new HandGitsAdapter(AliyunPlayerSkinActivity.this);


                } else {
                    view_stub_hand.setVisibility(View.VISIBLE);
                }
                presenters.getRankingList(-1 + "", room_info_id, 2 + "", "", "", 2);
                break;
        }

    }


    /**
     * 朋友圈
     */


    public static void closeKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    private void sendNewGoodsComment(String commentStr) {
        String content = commentStr.trim();

        presenters.addFriendComment(qfd_id, to_id, userid, content);


    }

    private PopView_Comment popComment;
    private PopView_CancelOrSure popView_cancelOrSure;
    private PopView_CancelOrSure popView_cancelOrSures;

    public void intListener() {



        liveAdapter.setTakeListener(new VideoLiveAdapter.TakeawayListener() {
            @Override
            public void listener(int poistion) {
                if(TextUtil.isEmpty(dat.get(poistion).video_url)){
                    return;
                }
                if(dat.get(poistion).video_url.contains("http")){
                    PlayParameter.PLAY_PARAM_URL=dat.get(poistion).video_url;
                }else {
                    PlayParameter.PLAY_PARAM_URL=Constant.APP_FILE_NAME+dat.get(poistion).video_url;
                }
                String title="";
                if(TextUtil.isNotEmpty(dat.get(poistion).video_name)){
                    title=dat.get(poistion).video_name;
                }
                if(!dat.get(poistion).new_type.equals("0")){
                    CanTingAppLication.landType=6;
                    Intent intent = new Intent(AliyunPlayerSkinActivity.this, AliyunPlayerSkinActivity.class);
                    intent.putExtra("url",dat.get(poistion).video_url);
                    intent.putExtra("name",dat.get(poistion).video_name);
                    intent.putExtra("room_info_id",dat.get(poistion).room_info_id);
                    intent.putExtra("id",dat.get(poistion).user_info_id);
                    intent.putExtra("type",3);

                    startActivity(intent);
                    finish();
                }else {
                    if(dat.get(poistion).video_type==2){

                        CanTingAppLication.landType=6;
                        Intent intent = new Intent(AliyunPlayerSkinActivity.this, AliyunPlayerSkinActivity.class);
                        intent.putExtra("url",dat.get(poistion).video_url);
                        intent.putExtra("name",dat.get(poistion).video_name);
                        intent.putExtra("room_info_id",dat.get(poistion).room_info_id);
                        intent.putExtra("id",dat.get(poistion).user_info_id);
                        startActivity(intent);
                        finish();
//                    changePlayLocalSource( PlayParameter.PLAY_PARAM_URL, title);
                    }else if(dat.get(poistion).video_type==3){
                        CanTingAppLication.landType=6;
                        Intent intent = new Intent(AliyunPlayerSkinActivity.this, AliyunPlayerSkinActivity.class);
                        intent.putExtra("url",dat.get(poistion).video_url);
                        intent.putExtra("name",dat.get(poistion).video_name);
                        intent.putExtra("room_info_id",dat.get(poistion).room_info_id);
                        intent.putExtra("id",dat.get(poistion).user_info_id);
                        intent.putExtra("type",3);
                        startActivity(intent);
                        finish();

                    }else {
                        CanTingAppLication.landType=8;
                        Intent intent = new Intent(AliyunPlayerSkinActivity.this, AliyunPlayerSkinActivityMin.class);
                        intent.putExtra("url",dat.get(poistion).video_url);
                        intent.putExtra("name",dat.get(poistion).video_name);
                        intent.putExtra("room_info_id",dat.get(poistion).room_info_id);
                        intent.putExtra("id",dat.get(poistion).user_info_id);
                        startActivity(intent);
                        finish();

                    }
                }


            }
        });
        popView_cancelOrSure = new PopView_CancelOrSure(this);
        popView_cancelOrSure.setTitle(getString(R.string.qrscgtd));
        popView_cancelOrSure.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenters.delFriendCircle(qfd_id, sendId);
                }

            }
        });

        popView_cancelOrSures = new PopView_CancelOrSure(this);
        popView_cancelOrSures.setTitle(getString(R.string.qrscgpl));
        popView_cancelOrSures.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenters.delFriendComment(qfd_id, sendId, fromId, Id);
                }

            }
        });
        popComment = new PopView_Comment(this);
        popComment.setOnPop_CommentListenerr(new PopView_Comment.OnPop_CommentListener() {
            @Override
            public void chooseDeviceConfig(String commentStr) {
                sendNewGoodsComment(commentStr);
            }
        });
        qFriendsAdapter.setListener(new QFriendsAdapter.QFriendClickListener() {
            @Override
            public void clicks(int poistions, int commentPositions, QfriendBean infos) {
                commentPosition = commentPositions;
                qfd_id = infos.id;

                if (commentPositions == -1) {
                    commentPosition = -1;
                    to_id = userid;
                    toId = infos.user_info_id;
                    sendId = infos.user_info_id;
                    info = infos;
                    poistion = poistions;
                    popComment.showPopView("");
                } else if (commentPositions == -2) {
                    poistion = poistions;
                    isProse = false;
                    datas.clear();
                    if (infos.likeList != null && infos.likeList.size() > 0) {
                        for (CommetLikeBean commetLikeBean : infos.likeList) {
                            if (commetLikeBean.from_uid.equals(userid)) {
                                isProse = true;
                            } else {
                                datas.add(commetLikeBean);
                            }
                        }
                        if (!isProse) {
                            CommetLikeBean bean = new CommetLikeBean();
                            bean.from_nickname = username;
                            bean.from_uid = userid;
                            datas.add(bean);
                        }
                    }
                    list.get(poistions).likeList = datas;
                    presenters.changeFriendLike(qfd_id, isProse ? "2" : "1");
                } else if (commentPositions == -3) {
                    sendId = infos.user_info_id;
                    poistion = poistions;
                    popView_cancelOrSure.showPopView();
                } else {
                    to_id = infos.commentList.get(commentPosition).from_uid;
                    toId = infos.commentList.get(commentPosition).from_uid;
                    popComment.showPopView("");
                    if (TextUtil.isNotEmpty(infos.commentList.get(commentPosition).from_uname)) {
                        popComment.setTextHints(getString(R.string.hf) + infos.commentList.get(commentPosition).from_uname + ":");

                    } else {
                        popComment.setTextHints(getString(R.string.pl) + infos.commentList.get(commentPosition).from_uname + ":");
                    }

                    poistion = poistions;
                    info = infos;

                }
            }

            @Override
            public void click(int poistions, int commentPosition, QfriendBean infos) {
                qfd_id = infos.id;
                sendId = infos.user_info_id;
                Id = infos.commentList.get(commentPosition).id;
                fromId = infos.commentList.get(commentPosition).from_uid;

                poistion = poistions;
                popView_cancelOrSures.showPopView();
            }
        });
    }

    public static final int TAB_ONE = 4;
    public static final int TAB_TWO = 5;

    private int tab_type = TAB_ONE;

    /**
     * 更新二级TabView
     */
    private void updateTabView() {
        if (tab_type == TAB_ONE) {
            tvVideo.setBackground(getResources().getDrawable(R.drawable.alivc_choose));
            tvVideo.setTextColor(getResources().getColor(R.color.white));
            tvDetail.setTextColor(getResources().getColor(R.color.color9));
            tvDetail.setBackground(null);
            presenter.getVideoList(id);
            loadingViewZb.showPager(LoadingPager.STATE_LOADING);
        } else {
            tvDetail.setBackground(getResources().getDrawable(R.drawable.alivc_chooses));
            tvDetail.setTextColor(getResources().getColor(R.color.white));
            tvVideo.setTextColor(getResources().getColor(R.color.color9));
            tvVideo.setBackground(null);
            loadingViewZb.showPager(LoadingPager.STATE_LOADING);
            presenters.getFriendCirclesList(1, -1 + "", id);
        }


    }

    public void selectPosition(int position) {
        switch (position) {
            case 0:
                updateUI(1);
                tvChat.setActivated(true);
                tvZb.setActivated(false);
                tvRank.setActivated(false);
                ivChat.setActivated(true);
                ivZb.setActivated(false);
                ivRank.setActivated(false);
                break;
            case 1:
                updateUI(2);
                tvChat.setActivated(false);
                tvZb.setActivated(true);
                tvRank.setActivated(false);
                ivChat.setActivated(false);
                ivZb.setActivated(true);
                ivRank.setActivated(false);
                break;
            case 2:
                updateUI(3);
                tvChat.setActivated(false);
                tvZb.setActivated(false);
                tvRank.setActivated(true);
                ivChat.setActivated(false);
                ivZb.setActivated(false);
                ivRank.setActivated(true);
                break;
        }
    }

    private NoScrollViewPager stub_chat;//首页聊天
    private ViewStub stub_zb;//直播详情
    private ViewStub stub_hand;//主播排行
    private View view_stub_chat;//主播排行
    private View view_stub_zb;//主播排行
    private View view_stub_hand;//主播排行



    public void getDirIndexInfo() {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("roomInfoId", room_info_id);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.getDirIndexInfo(map).enqueue(new BaseCallBack<BEAN>() {
            @Override
            public void onSuccess(BEAN bean) {
                BEAN data = bean.data;
                if(data==null){
                    return;
                }
                setHostData(data);
                flbg.setClickable(true);


            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
    private Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            selectPosition(1);
            if(mAliyunVodPlayerView!=null){
                mAliyunVodPlayerView.setScandModel();
            }

            return false;
        }
    });

    private void initAliyunPlayerView() {
        mAliyunVodPlayerView = (AliyunVodPlayerView) findViewById(R.id.video_view);
        tvCare = (TextView) findViewById(R.id.tv_care);
        flbg = (RelativeLayout) findViewById(R.id.fl_bgs);

        //保持屏幕敞亮
        mAliyunVodPlayerView.setKeepScreenOn(true);
        PlayParameter.PLAY_PARAM_URL = DEFAULT_URL;
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 900 /*大小，MB*/);
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        //mAliyunVodPlayerView.setCirclePlay(true);
        mAliyunVodPlayerView.setAutoPlay(true);
        mAliyunVodPlayerView.changeQuality(IAliyunVodPlayer.QualityValue.QUALITY_2K);
       if(type!=0){
           mAliyunVodPlayerView.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

       }else {
           mAliyunVodPlayerView.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT);

       }
//        mAliyunVodPlayerView.setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mAliyunVodPlayerView.setOnPreparedListener(new MyPrepareListener(this));
        mAliyunVodPlayerView.setNetConnectedListener(new MyNetConnectedListener(this));
        mAliyunVodPlayerView.setOnCompletionListener(new MyCompletionListener(this));
        mAliyunVodPlayerView.setOnFirstFrameStartListener(new MyFrameInfoListener(this));
        mAliyunVodPlayerView.setOnChangeQualityListener(new MyChangeQualityListener(this));
        mAliyunVodPlayerView.setOnStoppedListener(new MyStoppedListener(this));
        mAliyunVodPlayerView.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
        mAliyunVodPlayerView.setOrientationChangeListener(new MyOrientationChangeListener(this));
        mAliyunVodPlayerView.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
        mAliyunVodPlayerView.setOnShowMoreClickListener(new MyShowMoreClickLisener(this));
        mAliyunVodPlayerView.enableNativeLog();
        mAliyunVodPlayerView.setGiftMessageListener(new AliyunVodPlayerView.GiftMessageListener() {
            @Override
            public void click(int poistion,int state) {
                if(poistion==1){
                    giftMessageListener.click(1);
                }else if(poistion==2) {
//                    if(type==1){
                        mAliyunVodPlayerView.setChangeModel();
                        selectPosition(0);
//                    }
//                    giftMessageListener.click(2);

                }else if(poistion==3) {
                    setPlaySource();

                }else if(poistion==4) {
                    playCare();

                }
            }
        });
        mAliyunVodPlayerView.setChangeListener(new AliyunVodPlayerView.ChangeListener() {
            @Override
            public void change(AliyunScreenMode mode) {
                if (mode == AliyunScreenMode.Small) {
                    iv_share.setVisibility(View.VISIBLE);
                }else {
                    iv_share.setVisibility(View.GONE);
                }
            }
        });

    }
    public void playCare(){
        if(ben!=null&&TextUtil.isNotEmpty(ben.type)){
            if(ben.type.equals("1")){
                presenters.focusTV(room_info_id,id, "2");
            }else if(ben.type.equals("0")){
                presenters.focusTV(room_info_id,id, "1");
            }
        }
    }
    private boolean isCare;

    /**
     * 请求sts
     */
    private void requestVidSts() {
        if (inRequest) {
            return;
        }
        inRequest = true;
        PlayParameter.PLAY_PARAM_VID = DEFAULT_VID;
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener(this));
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_SETTING) {
            switch (resultCode) {
                case CODE_RESULT_TYPE_VID:
                    setPlaySource();

                    break;
                case CODE_RESULT_TYPE_URL:
                    setPlaySource();

                    break;

                default:
                    break;
            }

        }
    }

    /**
     * 播放本地资源
     *
     * @param url
     * @param title
     */
    private void changePlayLocalSource(String url, String title) {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(url);
        alsb.setTitle(title);
        AliyunLocalSource localSource = alsb.build();
        mAliyunVodPlayerView.setLocalSource(localSource);
    }





    private OtherPresenter presenter;
    private BasesPresenter presenters;
    private String room_id;
    /**
     * 朋友圈
     */
    public static final String AVATER = "hx_avater";
    public static final String NAME = "hx_name";
    public static final String UID = "hx_uid";
    private boolean isProse;
    private List<CommetLikeBean> datas = new ArrayList<>();
    private String sendId;
    private String Id;
    private String toId;
    private String fromId;
    private String qfd_id;
    private String to_id;
    public static final String GID = "hx_gid";
    private int poistion;

    private int commentPosition;
    private String username = SpUtil.getNick(this);
    private String userid = SpUtil.getUserInfoId(this);
    private String myname;
    List<QfriendBean> list = new ArrayList<>();
    List<QfriendBean> lists;
    List<String> prase_list = new ArrayList<>();
    QfriendBean info;
    public static final String FUID = "hx_fuid";
    public static final String FNAME = "hx_fname";
    private List<QfriendBean> data = new ArrayList<>();
    private List<videobean> dat;
    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 19) {
            aliLive aliLive = (aliLive) entity;
            if (aliLive != null && aliLive.liveUrl != null) {

//                DEFAULT_URL = "rtmp://alive.chushenduojin.cn/zhixing/stream_urio1098110334129930240?auth_key=1553756190-0-0-da7b83d44eced17ea0b878c1d89aedbb";
                room_id = aliLive.liveUrl.chatrooms_id;
                if (TextUtil.isNotEmpty(DEFAULT_URL)&&TextUtil.isEmpty(url)) {
                    DEFAULT_URL = aliLive.liveUrl.rtmp_url;
                    setPlaySource();

                }
                initViewPager();
                if (TextUtil.isNotEmpty(url)) {
                    selectPosition(0);
                    handler.sendEmptyMessageDelayed(1,200);

                }else {
                    selectPosition(0);
                }

            }else {
                getDirIndexInfo();
            }
        } else if (type == 111) {
            dat = (List<videobean>) entity;
            if (dat != null && dat.size() > 0) {
                relist_infos.setVisibility(View.GONE);
                ll_rbg.setVisibility(View.GONE);
                relist_info.setVisibility(View.VISIBLE);
                relist_info.setFocusable(false);
                relist_infos.setFocusable(false);
                relist_info.setAdapter(liveAdapter);
                liveAdapter.setData(dat);
                loadingViewZb.showPager(LoadingPager.STATE_SUCCEED);
            } else {
                loadingViewZb.setContent("主播还没有录播视频");
                loadingViewZb.showPager(LoadingPager.STATE_EMPTY);
            }
        } else if (type == 79) {
            FriendInfo info= (FriendInfo) entity;
            if (!TextUtils.isEmpty(info.nickname)) {
                tvName.setText(info.nickname);
            }
            Glide.with(AliyunPlayerSkinActivity.this).load(info.head_image).asBitmap().transform(new CircleTransform(AliyunPlayerSkinActivity.this)).placeholder(R.drawable.editor_ava).into(iv_imgs);

        } else if (type == 3||type ==4) {
            getDirIndexInfo();
        }else if (type == 66) {//评论

            if (TextUtil.isNotEmpty(userid) && TextUtil.isNotEmpty(sendId)) {
                if (!toId.equals(sendId)) {
                    //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                    final EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", toId);
                    message.setAttribute("em_robot_message", true);
                    message.setAttribute(AVATER, SpUtil.getAvar(this));
                    message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                    message.setAttribute(FUID, sendId);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                            if (conv == null) {
                                return;
                            }
                            conv.removeMessage(message.getMsgId());
                        }
                    }).start();

                }
                final EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", sendId);
                message.setAttribute("em_robot_message", true);
                message.setAttribute(AVATER, SpUtil.getAvar(this));
                message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                message.setAttribute(FUID, sendId);
                EMClient.getInstance().chatManager().sendMessage(message);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                        if (conv == null) {
                            return;
                        }
                        conv.removeMessage(message.getMsgId());
                    }
                }).start();
            }
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    setWithOrHeigth();
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);
                    loadingViewZb.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }
            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard(tvChat);
        } else if (type == 77) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    setWithOrHeigth();
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);
                    loadingViewZb.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }


            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard(tvChat);
        } else if (type == 88) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                if (data != null) {
                    relist_infos.setVisibility(View.VISIBLE);
                    ll_rbg.setVisibility(View.VISIBLE);
                    relist_info.setVisibility(View.GONE);
                    setWithOrHeigth();
                    relist_info.setFocusable(false);
                    relist_infos.setFocusable(false);
                    relist_infos.setAdapter(qFriendsAdapter);
                    qFriendsAdapter.setDatas(data);
                    loadingViewZb.showPager(LoadingPager.STATE_SUCCEED);
                } else {
                    loadingView.showPager(LoadingPager.STATE_EMPTY);
                }


            }else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
            closeKeyBoard(tvChat);
        } else if (type == 99) {
            presenters.getFriendCirclesList(1, -1 + "", id);
        } else if (type == 2) {
            Hands data = (Hands) entity;
            if (data != null && data.data.giftList != null) {
                relist_hand.setFocusable(false);
                relist_hand.setAdapter(handGitsAdapter);
                handGitsAdapter.setDatas(data.data.giftList);
                AdapterUtility.setListViewHeightBasedOnChildren(relist_hand);
                loadingView.showPager(LoadingPager.STATE_SUCCEED);
            } else {

                loadingView.setContent(getString(R.string.zwsj));
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }
        } else if (type == 55 || type == 1) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            if (datas != null) {
                relist_infos.setVisibility(View.VISIBLE);
                ll_rbg.setVisibility(View.VISIBLE);
                relist_info.setVisibility(View.GONE);
                setWithOrHeigth();
                relist_info.setFocusable(false);
                relist_infos.setFocusable(false);
                relist_infos.setAdapter(qFriendsAdapter);
                list.addAll(datas);
                qFriendsAdapter.setDatas(datas);
                loadingViewZb.showPager(LoadingPager.STATE_SUCCEED);
            } else {
                loadingView.showPager(LoadingPager.STATE_EMPTY);
            }

//            onDataLoaded(TYPE_PULL_REFRESH, false, datas);
        }


    }
    public void setWithOrHeigth(){
        LinearLayout.LayoutParams params=( LinearLayout.LayoutParams)stub_zb.getLayoutParams();
        params.width= LinearLayout.LayoutParams.MATCH_PARENT;
        params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        stub_zb.setLayoutParams(params);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }


    private static class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyPrepareListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onPrepared() {
            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onPrepared();
            }
        }
    }

    private void onPrepared() {

        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.toast_prepare_success,
                Toast.LENGTH_SHORT).show();
    }

    private static class MyCompletionListener implements IAliyunVodPlayer.OnCompletionListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyCompletionListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onCompletion() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onCompletion();
            }
        }
    }

    private void onCompletion() {

        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.toast_play_compleion,
                Toast.LENGTH_SHORT).show();

        // 当前视频播放结束, 播放下一个视频
        onNext();
    }

    private void onNext() {
        if (currentError == ErrorInfo.UnConnectInternet) {
            // 此处需要判断网络和播放类型
            // 网络资源, 播放完自动波下一个, 无网状态提示ErrorTipsView
            // 本地资源, 播放完需要重播, 显示Replay, 此处不需要处理
            if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                mAliyunVodPlayerView.showErrorTipView(4014, -1, "当前网络不可用");
            }
            return;
        }


    }

    private static class MyFrameInfoListener implements IAliyunVodPlayer.OnFirstFrameStartListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyFrameInfoListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onFirstFrameStart() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onFirstFrameStart();
            }
        }
    }

    private void onFirstFrameStart() {


    }

    private class MyPlayViewClickListener implements OnPlayerViewClickListener {
        @Override
        public void onClick(AliyunScreenMode screenMode, PlayViewType viewType) {
            // 如果当前的Type是Download, 就显示Download对话框
            if (viewType == PlayViewType.Download) {

            }
        }
    }



    private Dialog downloadDialog = null;


    @Override
    protected void onStart() {
        super.onStart();
        StatusBarUtil.setTranslucentStatus(this, false);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color8), 0);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission Denied
                Toast.makeText(AliyunPlayerSkinActivity.this, "没有sd卡读写权限, 无法下载", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    List<AliyunDownloadMediaInfo> aliyunDownloadMediaInfoList;

    private void onDownloadPrepared(List<AliyunDownloadMediaInfo> infos) {
        aliyunDownloadMediaInfoList = new ArrayList<>();
        aliyunDownloadMediaInfoList.addAll(infos);
    }

    private static class MyChangeQualityListener implements IAliyunVodPlayer.OnChangeQualityListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyChangeQualityListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onChangeQualitySuccess(String finalQuality) {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualitySuccess(finalQuality);
            }
        }

        @Override
        public void onChangeQualityFail(int code, String msg) {
            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualityFail(code, msg);
            }
        }
    }

    private void onChangeQualitySuccess(String finalQuality) {
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(),
                getString(R.string.log_change_quality_success), Toast.LENGTH_SHORT).show();
    }

    void onChangeQualityFail(int code, String msg) {

        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(),
                getString(R.string.log_change_quality_fail), Toast.LENGTH_SHORT).show();
    }

    private static class MyStoppedListener implements IAliyunVodPlayer.OnStoppedListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyStoppedListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onStopped() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onStopped();
            }
        }
    }


    private void onStopped() {
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.log_play_stopped,
                Toast.LENGTH_SHORT).show();
    }

    private void setPlaySource() {
        if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
            alsb.setSource(DEFAULT_URL);

            Uri uri = Uri.parse(DEFAULT_URL);
            if (TextUtil.isNotEmpty(name)) {
                alsb.setTitle(name);
            }
            AliyunLocalSource localSource = alsb.build();
            mAliyunVodPlayerView.setLocalSource(localSource);

        } else if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            if (!inRequest) {
                AliyunVidSts vidSts = new AliyunVidSts();
                vidSts.setVid(PlayParameter.PLAY_PARAM_VID);
                vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
                vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
                vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);
                if (mAliyunVodPlayerView != null) {
                    mAliyunVodPlayerView.setVidSts(vidSts);
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }


    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }

                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                RelativeLayout.LayoutParams aliVcVideoViewLayoutParams = (RelativeLayout.LayoutParams) mAliyunVodPlayerView
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }

        if (playerHandler != null) {
            playerHandler.removeMessages(DOWNLOAD_ERROR);
            playerHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        updatePlayerViewMode();
    }

    private static final int DOWNLOAD_ERROR = 1;
    private static final String DOWNLOAD_ERROR_KEY = "error_key";

    private static class PlayerHandler extends Handler {
        //持有弱引用AliyunPlayerSkinActivity,GC回收时会被回收掉.
        private final WeakReference<AliyunPlayerSkinActivity> mActivty;

        public PlayerHandler(AliyunPlayerSkinActivity activity) {
            mActivty = new WeakReference<AliyunPlayerSkinActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AliyunPlayerSkinActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                switch (msg.what) {
                    case DOWNLOAD_ERROR:
                        Toast.makeText(mActivty.get(), msg.getData().getString(DOWNLOAD_ERROR_KEY), Toast.LENGTH_LONG)
                                .show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static class MyStsListener implements VidStsUtil.OnStsResultListener {

        private WeakReference<AliyunPlayerSkinActivity> weakctivity;

        public MyStsListener(AliyunPlayerSkinActivity act) {
            weakctivity = new WeakReference<AliyunPlayerSkinActivity>(act);
        }

        @Override
        public void onSuccess(String vid, String akid, String akSecret, String token) {
            AliyunPlayerSkinActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsSuccess(vid, akid, akSecret, token);
            }
        }

        @Override
        public void onFail() {
            AliyunPlayerSkinActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsFail();
            }
        }
    }

    private void onStsFail() {

        Toast.makeText(getApplicationContext(), R.string.request_vidsts_fail, Toast.LENGTH_LONG).show();
        inRequest = false;
        //finish();
    }

    private void onStsSuccess(String mVid, String akid, String akSecret, String token) {

        PlayParameter.PLAY_PARAM_VID = mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        inRequest = false;
        // 请求sts成功后, 加载播放资源,和视频列表
        setPlaySource();


    }

    private  class MyOrientationChangeListener implements AliyunVodPlayerView.OnOrientationChangeListener {

        private final WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyOrientationChangeListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void orientationChange(boolean from, AliyunScreenMode currentMode) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.hideDownloadDialog(from, currentMode);
            activity.hideShowMoreDialog(from, currentMode);
            if (currentMode == AliyunScreenMode.Small) {
                iv_share.setVisibility(View.VISIBLE);
            }else {
                iv_share.setVisibility(View.GONE);
            }
        }
    }

    private void hideShowMoreDialog(boolean from, AliyunScreenMode currentMode) {
        if (showMoreDialog != null) {
            if (currentMode == AliyunScreenMode.Small) {
                showMoreDialog.dismiss();
                currentScreenMode = currentMode;
            }
        }
    }

    private void hideDownloadDialog(boolean from, AliyunScreenMode currentMode) {

        if (downloadDialog != null) {
            if (currentScreenMode != currentMode) {
                downloadDialog.dismiss();
                currentScreenMode = currentMode;
            }
        }
    }

    /**
     * 判断是否有网络的监听
     */
    private class MyNetConnectedListener implements AliyunVodPlayerView.NetConnectedListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyNetConnectedListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onReNetConnected(isReconnect);
        }

        @Override
        public void onNetUnConnected() {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onNetUnConnected();
        }
    }

    private void onNetUnConnected() {
        currentError = ErrorInfo.UnConnectInternet;

    }

    private void onReNetConnected(boolean isReconnect) {
        if (isReconnect) {
            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
                int unCompleteDownload = 0;
                for (AliyunDownloadMediaInfo info : aliyunDownloadMediaInfoList) {
                    //downloadManager.startDownloadMedia(info);
                    if (info.getStatus() == AliyunDownloadMediaInfo.Status.Stop) {

                        unCompleteDownload++;
                    }
            }

                if (unCompleteDownload > 0) {
                    Toast.makeText(this, "网络恢复, 请手动开启下载任务...", Toast.LENGTH_SHORT).show();
                }
            }
            VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener(this));
        }
    }

    private static class MyOnUrlTimeExpiredListener implements IAliyunVodPlayer.OnUrlTimeExpiredListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyOnUrlTimeExpiredListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<AliyunPlayerSkinActivity>(activity);
        }

        @Override
        public void onUrlTimeExpired(String s, String s1) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onUrlTimeExpired(s, s1);
        }
    }

    private void onUrlTimeExpired(String oldVid, String oldQuality) {
        //requestVidSts();
        AliyunVidSts vidSts = VidStsUtil.getVidSts(oldVid);
        PlayParameter.PLAY_PARAM_VID = vidSts.getVid();
        PlayParameter.PLAY_PARAM_AK_SECRE = vidSts.getAkSceret();
        PlayParameter.PLAY_PARAM_AK_ID = vidSts.getAcId();
        PlayParameter.PLAY_PARAM_SCU_TOKEN = vidSts.getSecurityToken();

    }

    private  class MyShowMoreClickLisener implements ControlLiveView.OnShowMoreClickListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        MyShowMoreClickLisener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void showMore() {
            ShareUtils.showMyShares(AliyunPlayerSkinActivity.this, getString(R.string.jiguang), "http://www.gwlaser.tech");
//            AliyunPlayerSkinActivity activity = weakReference.get();
//            activity.showMore(activity);
        }
    }

    private void showMore(final AliyunPlayerSkinActivity activity) {
        showMoreDialog = new AlivcShowMoreDialog(activity);
        AliyunShowMoreValue moreValue = new AliyunShowMoreValue();
        moreValue.setSpeed(mAliyunVodPlayerView.getCurrentSpeed());
        moreValue.setVolume(mAliyunVodPlayerView.getCurrentVolume());
        moreValue.setScreenBrightness(mAliyunVodPlayerView.getCurrentScreenBrigtness());

        ShowMoreView showMoreView = new ShowMoreView(activity, moreValue);
        showMoreDialog.setContentView(showMoreView);
        showMoreDialog.show();
        showMoreView.setOnDownloadButtonClickListener(new ShowMoreView.OnDownloadButtonClickListener() {
            @Override
            public void onDownloadClick() {
                // 点击下载
                showMoreDialog.dismiss();
                if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                    Toast.makeText(activity, "Url类型不支持下载", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        showMoreView.setOnScreenCastButtonClickListener(new ShowMoreView.OnScreenCastButtonClickListener() {
            @Override
            public void onScreenCastClick() {
                Toast.makeText(AliyunPlayerSkinActivity.this, "功能开发中, 敬请期待...", Toast.LENGTH_SHORT).show();
            }
        });

        showMoreView.setOnBarrageButtonClickListener(new ShowMoreView.OnBarrageButtonClickListener() {
            @Override
            public void onBarrageClick() {
                Toast.makeText(AliyunPlayerSkinActivity.this, "功能开发中, 敬请期待...", Toast.LENGTH_SHORT).show();
            }
        });

        showMoreView.setOnSpeedCheckedChangedListener(new ShowMoreView.OnSpeedCheckedChangedListener() {
            @Override
            public void onSpeedChanged(RadioGroup group, int checkedId) {
                // 点击速度切换
                if (checkedId == R.id.rb_speed_normal) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.One);
                } else if (checkedId == R.id.rb_speed_onequartern) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.OneQuartern);
                } else if (checkedId == R.id.rb_speed_onehalf) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.OneHalf);
                } else if (checkedId == R.id.rb_speed_twice) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.Twice);
                }

            }
        });

        // 亮度seek
        showMoreView.setOnLightSeekChangeListener(new ShowMoreView.OnLightSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                mAliyunVodPlayerView.setCurrentScreenBrigtness(progress);
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });

        showMoreView.setOnVoiceSeekChangeListener(new ShowMoreView.OnVoiceSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                mAliyunVodPlayerView.setCurrentVolume(progress);
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });

    }
}
