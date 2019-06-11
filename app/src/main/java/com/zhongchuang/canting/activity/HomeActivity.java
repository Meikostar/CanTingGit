package com.zhongchuang.canting.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.vod.common.utils.StorageUtils;
import com.google.gson.Gson;
import com.google.zxing.client.android.activity.CaptureActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.zhongchuang.canting.BuildConfig;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.ChatSplashActivity;
import com.zhongchuang.canting.activity.mall.ShopCompsiteMallActivity;
import com.zhongchuang.canting.activity.mall.ShopMallActivity;
import com.zhongchuang.canting.activity.shop.AppStoreActivity;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.Basedapter1;
import com.zhongchuang.canting.allive.AliveSplashActivity;
import com.zhongchuang.canting.allive.editor.editor.EditorActivity;
import com.zhongchuang.canting.allive.recorder.CameraDemo;
import com.zhongchuang.canting.allive.recorder.util.Common;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.ProvinceModel;
import com.zhongchuang.canting.been.SIGN;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.Version;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionFail;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.BadgeUtil;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.ShareUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.DownLoadProgressbar;
import com.zhongchuang.canting.widget.MCheckBox;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollGridView;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.SharePopWindow;
import com.zhongchuang.canting.widget.banner.BannerView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class HomeActivity extends BaseTitle_Activity implements BaseContract.View {


    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.grid_content)
    NoScrollGridView girdView;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_go)
    TextView tv_go;
    @BindView(R.id.tv_gos)
    TextView tv_gos;
    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.progress)
    DownLoadProgressbar mProgress;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.ll_bgs)
    LinearLayout llBgs;

    private Basedapter1 homedapter;
    private View view;
    private BasesPresenter presenter;
    private BannerAdapters bannerAdapter;

    @Override
    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.home_activity, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        PermissionGen.with(HomeActivity.this)
                .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .request();
        showPress();
        presenter.getDirRoomClassify();
        presenter.verifyPassword("");
        bannerAdapter = new BannerAdapters(this);
        bannerView.setAdapter(bannerAdapter);
        presenter.getHomeBanners("1");
        if (!TextUtils.isEmpty(CanTingAppLication.userId)) {
            presenter.getChatGroupList();
        }
        initView();

    }
    @PermissionFail(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardFailed() {

    }
    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSdcardSuccess() {
        File file = new File(StorageUtils.getCacheDirectory(CanTingAppLication.getInstance()).getAbsolutePath() + File.separator + "live.zip");
        if (file != null && file.length() < 31755920) {
            CanTingAppLication.isComplete=false;
            new Thread(new HomeActivity.DownloadApk("http://119.23.212.8:8080/live.zip", 2)).start();
        } else if (file == null || !file.exists()) {
            CanTingAppLication.isComplete=false;
            new Thread(new HomeActivity.DownloadApk("http://119.23.212.8:8080/live.zip", 2)).start();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Common.unZip();
                }
            }).start();

            CanTingAppLication.isComplete=true;
        }

    }
    private View views = null;
    private String langueType = "zh";
    private MCheckBox ivType1;
    private MCheckBox ivType2;
    private MCheckBox ivType3;
    private MCheckBox ivType4;
    private MCheckBox ivType5;
    private MCheckBox ivType6;
    private MCheckBox ivType7;
    private LinearLayout ll_langue1;
    private LinearLayout ll_langue2;
    private LinearLayout ll_langue3;
    private LinearLayout ll_langue4;
    private LinearLayout ll_langue5;
    private LinearLayout ll_langue6;
    private LinearLayout ll_langue7;

    public void showPopwindow() {

        views = View.inflate(this, R.layout.langue_item_choose, null);


        ivType1 = views.findViewById(R.id.iv_type1);
        ivType2 = views.findViewById(R.id.iv_type2);
        ivType3 = views.findViewById(R.id.iv_type3);
        ivType4 = views.findViewById(R.id.iv_type4);
        ivType5 = views.findViewById(R.id.iv_type5);
        ivType6 = views.findViewById(R.id.iv_type6);
        ivType7 = views.findViewById(R.id.iv_type7);

        ll_langue1 = views.findViewById(R.id.ll_langue1);
        ll_langue2 = views.findViewById(R.id.ll_langue2);
        ll_langue3 = views.findViewById(R.id.ll_langue3);
        ll_langue4 = views.findViewById(R.id.ll_langue4);
        ll_langue5 = views.findViewById(R.id.ll_langue5);
        ll_langue6 = views.findViewById(R.id.ll_langue6);
        ll_langue7 = views.findViewById(R.id.ll_langue7);


        dialogs = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        selectType(SpUtil.getLangueType(this));
        dialogs.show();

        ll_langue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("zh-rCN");
                updateActivity(langueType);
            }
        });
        ll_langue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("en");
                updateActivity(langueType);
            }
        });
        ll_langue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("fan");
                updateActivity(langueType);
            }
        });
        ll_langue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("ja");
                updateActivity(langueType);
            }
        });
        ll_langue5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("ko");
                updateActivity(langueType);
            }
        });
        ll_langue6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("ms");
                updateActivity(langueType);
            }
        });
        ll_langue7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType("ru");
                updateActivity(langueType);
            }
        });


    }

    /**
     * 刷新语言
     */
    public void updateActivity(String sta) {
        SpUtil.putString(this, "LangueType", sta);
        if (isLogin) {
            presenter.setLanguge(getLangue(sta));
        }
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        startActivity((new Intent(HomeActivity.this, HomeActivity.class)));


    }

    public String getLangue(String lan) {
        String langue = "";
        if (lan.equals("zh-rCN")) {
            langue = "zh";
        } else if (lan.equals("fan")) {
            langue = "tw";
        } else {
            langue = lan;
        }
        return langue;
    }

    private MarkaBaseDialog dialogs;

    public void selectType(String type) {

        if (type.equals("zh-rCN")) {
            langueType = "zh-rCN";
            ivType1.setChecked(true);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
            ivType5.setChecked(false);
            ivType6.setChecked(false);
            ivType7.setChecked(false);
        } else if (type.equals("en")) {
            langueType = "en";
            ivType1.setChecked(false);
            ivType2.setChecked(true);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
            ivType5.setChecked(false);
            ivType6.setChecked(false);
            ivType7.setChecked(false);
        } else if (type.equals("fan")) {
            langueType = "fan";
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(true);
            ivType4.setChecked(false);
            ivType5.setChecked(false);
            ivType6.setChecked(false);
            ivType7.setChecked(false);
        } else if (type.equals("ja")) {
            langueType = "ja";
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(true);
            ivType5.setChecked(false);
            ivType6.setChecked(false);
            ivType7.setChecked(false);
        } else if (type.equals("ko")) {
            langueType = "ko";
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
            ivType5.setChecked(true);
            ivType6.setChecked(false);
            ivType7.setChecked(false);
        } else if (type.equals("ms")) {
            langueType = "ms";
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
            ivType5.setChecked(false);
            ivType6.setChecked(true);
            ivType7.setChecked(false);
        } else if (type.equals("ru")) {
            langueType = "ru";
            ivType1.setChecked(false);
            ivType2.setChecked(false);
            ivType3.setChecked(false);
            ivType4.setChecked(false);
            ivType5.setChecked(false);
            ivType6.setChecked(false);
            ivType7.setChecked(true);
        }
        dialogs.dismiss();
        CanTingAppLication.LangueType = langueType;

    }


    private int[] homeimg1 = {};


    public boolean isLogin;

    private int cont;

    public void setData(int cout) {
        String[] indepent1 = {getString(R.string.qyzg), getString(R.string.cjzg), getString(R.string.szds), getString(R.string.zb),
                getString(R.string.ll), getString(R.string.grzx), getString(R.string.yy), getString(R.string.appfx), getString(R.string.gsgj)};
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

        homedapter.setData(datas);
        homedapter.notifyDataSetChanged();

    }

    private List<HOMES> datas = new ArrayList<>();

    private void setLoginMessage() {
        String phone = SpUtil.getString(this, "mobileNumber", "");
        String token = SpUtil.getString(this, "token", "");
        String avar = SpUtil.getString(this, "avar", "");
        if (TextUtils.isEmpty(token) || token.equals("") || TextUtils.isEmpty(token) || token.equals("")) {
            if (isStar) {
                llBg.setVisibility(View.GONE);
            } else {
                llBg.setVisibility(View.VISIBLE);
            }

            tvLogin.setText(getString(R.string.dl));
            isLogin = false;
        } else {
            llBg.setVisibility(View.GONE);
            tvLogin.setText(R.string.zx);
            isLogin = true;
        }


    }


    private Subscription mSubscription;
    private CountDownTimer countDownTimer1;
    private int states;


    private void initView() {
        EMClient.getInstance().chatManager().addMessageListener(msgListener);//收到消息弹框
        if (CanTingAppLication.data == null) {

            Observable.create(new Observable.OnSubscribe<JSONObject>() {

                @Override
                public void call(Subscriber<? super JSONObject> subscriber) {
                    JSONObject json = TextUtil.getJson("phone.json", HomeActivity.this);
                    subscriber.onNext(json);
                    subscriber.onCompleted();//结束异步任务

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject json) {
                            JSONObject dataJson = json.optJSONObject("data");
//                            PREFIX  data = new Gson().fromJson(dataJson.toString(), PREFIX.class);

                        }
                    });
        }
        if (CanTingAppLication.province == null) {
            Observable.create(new Observable.OnSubscribe<JSONObject>() {

                @Override
                public void call(Subscriber<? super JSONObject> subscriber) {
                    JSONObject json = TextUtil.getJson("city.json", HomeActivity.this);
                    subscriber.onNext(json);
                    subscriber.onCompleted();//结束异步任务

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject json) {
                            JSONObject dataJson = json.optJSONObject("data");
                            CanTingAppLication.province = new Gson().fromJson(dataJson.toString(), ProvinceModel.class);

                        }
                    });

        }
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.SIGN) {
                    if (states == 0) {

                    }
                    if (countDownTimer1 == null) {
                        states = 1;
                        countDownTimer1 = new CountDownTimer(5000, 5000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                states = 0;
                                countDownTimer1.cancel();
                            }
                        }.start();
                    }

                } else if (bean.type == SubscriptionBean.OUTLOGIN) {
                    exitApp();

                } else if (bean.type == SubscriptionBean.LOGIN_FINISH) {
                    exitApp();

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);

        setLoginMessage();

        presenter.getVersionAndUrl();

        homedapter = new Basedapter1(this);


        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //商城
                        Intent intentsss = new Intent(HomeActivity.this, ShopCompsiteMallActivity.class);
                        intentsss.putExtra("type", 1);
                        startActivity(intentsss);

                        break;
                    case 1://乐聊

                        Intent intent = new Intent(HomeActivity.this, ShopMallActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 2://乐聊
                        Intent intentss = new Intent(HomeActivity.this, ShopMallActivity.class);
                        intentss.putExtra("type", 2);
                        startActivity(intentss);


                        break;
                    case 3://直播

                        if (isLogin) {
                            Intent intent2 = new Intent(HomeActivity.this, ChatSplashActivity.class);
                            if (messageGroup == null) {
                                return;
                            }
                            intent2.putExtra("data", data);
                            intent2.putExtra("type", 1);
                            startActivity(intent2);
                        } else {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                        break;
                    case 4://我的

                        if (isLogin) {
                            Intent intents = new Intent(HomeActivity.this, ChatSplashActivity.class);
                            if (messageGroup == null) {
                                return;
                            }
                            intents.putExtra("data", messageGroup);
                            startActivity(intents);
                        } else {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }
                        break;
                    case 5://同城
                        if (isLogin) {
                            Intent intent3 = new Intent(HomeActivity.this, MainActivity.class);
                            intent3.putExtra("type", 3);
                            startActivity(intent3);
                        } else {
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }


                        break;
                    case 6: //应用
                        Intent intent4 = new Intent(HomeActivity.this, AppStoreActivity.class);
                        intent4.putExtra("type", 1);
                        startActivity(intent4);

                        break;
                    case 7: //应用
                        ShareUtils.showMyShare(HomeActivity.this, getString(R.string.gs), "http://www.gwlaser.tech");

                        break;
                    case 8: //应用
//                        Toast toast = Toast.makeText(HomeActivity.this, "开发中敬请期待", Toast.LENGTH_SHORT);
//                        toast.show();
                        showPopwindow();
                        break;
                }

            }
        });

        girdView.setAdapter(homedapter);
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    exitApp();
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

            }
        });
    }

    private SharePopWindow shopBuyWindow;
    private int state = 1;

    private MarkaBaseDialog dialog;

    public void showPopwindow(final String url) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.down_dialog, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);
        if (TextUtil.isNotEmpty(description)) {
            title.setText(description);

        } else {
            title.setText(R.string.yxdbbo);

        }
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
//                initNotification();
                llBgs.setVisibility(View.VISIBLE);
                if (!isLogin) {
                    llBg.setVisibility(View.GONE);
                }
                isStar = true;
                new Thread(new DownloadApk(url, 1)).start();
                dialog.dismiss();

            }
        });
    }







    private String userInfoId;

    private void exitApp() {
        String olderToken = SpUtil.getString(this, "token", "");//token值
        String code = SpUtil.getString(this, "code", "");
        if (TextUtils.isEmpty(olderToken)) {
            return;
        } else {
            userInfoId = SpUtil.getString(HomeActivity.this, "userInfoId", "");
            if (userInfoId != null) {
                SpUtil.remove(HomeActivity.this, "userInfoId");//token值
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
            state = 0;
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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

    private int cout;
    public boolean isShow = true;
    public boolean isStar = false;

    @Override
    public void onResume() {
        super.onResume();
        isShow = true;
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
        setData(cout);
        showCount(cout);


        if (presenter != null) {
            presenter.getDirRoomClassify();
            presenter.verifyPassword("");
            if (!TextUtils.isEmpty(CanTingAppLication.userId)) {
                presenter.getChatGroupList();
                presenter.hostInfo();
            }

        }

        if (state == 0) {

        }
        setLoginMessage();
        if (!TextUtils.isEmpty(CanTingAppLication.userId)) {
            state = 1;
        }
    }

    public void showCount(int allCount) {
//        madMode(99);
//        BadgeUtil.setBadgeCount( this,allCount, R.drawable.red_point);


    }

    /**
     * 获取所有App的包名和启动类名
     *
     * @param count count
     */
    private void madMode(int count) {

        BadgeUtil.setBadgeOfMadMode(getApplicationContext(), count, "com.zhongchuang.canting", "com.zhongchuang.canting.activity.AliveSplashActivity");

    }

    @Override
    public void onStop() {
        super.onStop();
        hidePress();
        isShow = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isShow = false;
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public boolean isTitleShow() {
        return false;
    }


    private List<ZhiBo_GuanZhongBean.DataBean> cooks = new ArrayList<>();


    public void onResultSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {

    }

    private GAME data;
    public static GAME messageGroup;
    private List<GAME> dat = new ArrayList<>();

    @Override
    public <T> void toEntity(T entity, int type) {
        hidePress();
        if (type == 12) {
            data = new GAME();
            GAME gas = new GAME();
            gas.directTypeName = "录播视频";
            gas.id = "-1";
            dat.clear();
            dat.add(gas);
            GAME ga = new GAME();
            ga.directTypeName = "热门";
            ga.id = "0";
            dat.add(ga);
            List<GAME> games = (List<GAME>) entity;
            for (GAME game : games) {
                dat.add(game);
            }
            data.data = dat;
        } else if (type == 14) {
            Version data = (Version) entity;
            String oldVersion = StringUtil.getVersion(CanTingAppLication.getInstance());//"0.17"
            description = data.description;
            if (TextUtil.isNotEmpty(data.name)) {
                CanTingAppLication.url = data.name;
            } else {
                CanTingAppLication.url = "http://ychc.9913seo.com";
            }
            if (data.version.compareTo(oldVersion) > 0) {
                showPopwindow(data.url);

            }
        } else if (type == 22) {
            messageGroup = (GAME) entity;

        } else if (type == 66) {
            Home home = (Home) entity;
            banners = home.banner;
            bannerAdapter.setData(banners);

        } else if (type == 989) {

        } else if (type == 111) {

        } else {
            Host data = (Host) entity;
            if (data != null && TextUtil.isNotEmpty(data.is_direct)) {

                if (data.is_direct.equals("1")) {

                    SpUtil.putString(HomeActivity.this, "isAnchor", 1 + "");
                } else {
                    if (TextUtil.isNotEmpty(data.userIntegral)) {
//                        CanTingAppLication.totalintegral = Double.valueOf(data.userIntegral);
                    } else {
                        if (TextUtil.isNotEmpty(data.user_integral)) {
//                            CanTingAppLication.totalintegral = Double.valueOf(data.user_integral);
                        }
                    }
                    SpUtil.putString(HomeActivity.this, "isAnchor", 0 + "");
                }
            }
        }

    }

    private String description;
    List<Banner> banners;

    @Override
    public void toNextStep(int type) {

    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                // in background, do not refresh UI, notify it in notification bar
                String name = HxMessageUtils.getFName(message);
                if (name.contains("!@#$$#@!")) {
                    return;
                }
            }
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
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.MESSAGENOTIFIS, cout));
            setData(cout);
//            showCount(cout);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    @Override
    public void showTomast(String msg) {
        hidePress();
    }

    private long max;
    private long current;

    //循环模拟下载过程
    public void start() {
        if (isShow) {
            if (current <= max) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mProgress.setCurrentValue(current);
                    }
                });

            }
        }


    }

    /**
     * Created by mykar on 17/10/25.
     */
    public class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        InputStream is;
        FileOutputStream fos;
        private Context context;

        public DownloadApk(String url, int type) {
            this.url = url;
            this.type = type;
        }

        private String url;
        private int type = 2;//1 下载apk  2 下载 live文件

        /**
         * 下载完成,提示用户安装
         */
        private void installApk(File file) {
            isStar = false;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isLogin) {
                        llBg.setVisibility(View.VISIBLE);
                    }
                    llBgs.setVisibility(View.GONE);
                }
            });


            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }


            HomeActivity.this.startActivityForResult(intent, 0);
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().get().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {

                    //获取内容总长度
                    long contentLength = response.body().contentLength();
                    max = contentLength;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgress.setMaxValue(max);
                        }
                    });

                    String apkName = null;
                    File apkFile = null;
                    //设置最大值
                    //保存到sd卡
                    if (type == 1) {
                        apkName = url.substring(url.lastIndexOf("/") + 1, url.length());
                        apkFile = new File(Environment.getExternalStorageDirectory(), apkName);
                    } else {
                        apkFile = new File(StorageUtils.getCacheDirectory(HomeActivity.this).getAbsolutePath() + File.separator, "live.zip");

                    }
                    fos = new FileOutputStream(apkFile);
                    //获得输入流
                    is = response.body().byteStream();
                    //定义缓冲区大小
                    byte[] bys = new byte[1024];
                    int progress = 0;
                    int len = -1;
                    while ((len = is.read(bys)) != -1) {
                        try {
                            Thread.sleep(1);
                            fos.write(bys, 0, len);
                            fos.flush();
                            progress += len;
                            notify += len;
                            if (notify / 1024 == 512) {
                                notify = 0;
                                current = progress;
                                start();
                            }


                        } catch (InterruptedException e) {
                            return;
                        }
                    }


                    //下载完成,提示用户安装
                    if (type == 1) {
                        installApk(apkFile);
                    } else {
                        String SD_DIR = StorageUtils.getCacheDirectory(HomeActivity.this).getAbsolutePath() + File.separator;
                        upZipFile(new File(apkFile.getAbsolutePath()), SD_DIR);
                        Common.unZip();
                        CanTingAppLication.isComplete=true;
                        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.DOWN_COMPELTE,""));
                    }

                }
            } catch (IOException e) {
                return;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭io流
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
            }

        }
    }

    /**
     * 解压缩
     * 将zipFile文件解压到folderPath目录下.
     *
     * @param zipFile    zip文件
     * @param folderPath 解压到的地址
     * @throws IOException
     */
    private void upZipFile(File zipFile, String folderPath) throws IOException {
        ZipFile zfile = new ZipFile(zipFile);
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            if (ze.isDirectory()) {
                Log.d(TAG, "ze.getName() = " + ze.getName());
                String dirstr = folderPath + ze.getName();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                Log.d(TAG, "str = " + dirstr);
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            Log.d(TAG, "ze.getName() = " + ze.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
            int readLen = 0;
            while ((readLen = is.read(buf, 0, 1024)) != -1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
    }

    private long notify = 0;
//   downurl1.add("http://119.23.212.8:8080/live/aliyun_svideo_animation_filter.zip");
//        downurl1.add("http://119.23.212.8:8080/live/aliyun_svideo_caption.zip");
//        downurl1.add("http://119.23.212.8:8080/live/aliyun_svideo_filter.zip");
//        downurl1.add("http://119.23.212.8:8080/live/aliyun_svideo_mv.zip");
//        downurl1.add("http://119.23.212.8:8080/live/aliyun_svideo_overlay.zip");
//        downurl1.add("http://119.23.212.8:8080/live/tail.zip");
//        downurl2.add("http://119.23.212.8:8080/live/filter.zip");
//        downurl2.add("http://119.23.212.8:8080/live/maohuzi.zip");
//        downurl2.add("http://119.23.212.8:8080/livemodel.zip");
//        downurl2.add("http://119.23.212.8:8080/live/mp3.zip");
//        downurl3.add("http://119.23.212.8:8080/live/encrypt.zip");
//
//        new Thread(new HomeActivity.DownloadApk("http://119.23.212.8:8080/live.zip",2)).start();
//}
//    private List<String> downurl1=new ArrayList<>();
//    private List<String> downurl2=new ArrayList<>();
//    private List<String> downurl3=new ArrayList<>();

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                try {
                    substr = new String(substr.getBytes("8859_1"), "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ret = new File(ret, substr);

            }
            Log.d(TAG, "1ret = " + ret);
            if (!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length - 1];
            try {
                substr = new String(substr.getBytes("8859_1"), "GB2312");
                Log.d(TAG, "substr = " + substr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ret = new File(ret, substr);
            Log.d(TAG, "2ret = " + ret);
            return ret;
        }
        return ret;
    }

}

