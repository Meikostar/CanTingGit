package com.zhongchuang.canting.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;

import com.aliyun.common.httpfinal.QupaiHttpFinal;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import com.zhongchuang.canting.allive.downloader.DownloaderManager;
import com.zhongchuang.canting.been.Contury;
import com.zhongchuang.canting.been.INTEGRAL;
import com.zhongchuang.canting.been.ProvinceModel;
import com.zhongchuang.canting.been.ShareBean;
import com.zhongchuang.canting.db.Constant;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.EaseUI;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.utils.EaseCommonUtils;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.db.DemoDBManager;
import com.zhongchuang.canting.db.UserDao;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.ThirdShareManager;
import com.zhongchuang.canting.utils.location.LocationUtil;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.valuesfeng.picker.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import io.valuesfeng.picker.universalimageloader.core.ImageLoader;
import io.valuesfeng.picker.universalimageloader.core.ImageLoaderConfiguration;
import io.valuesfeng.picker.universalimageloader.core.assist.QueueProcessingType;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/19.
 */

public class CanTingAppLication extends Application {


    private static CanTingAppLication instance;
    public static ShareBean shareBean;
    public static ShareBean productBean;
    public static ShareBean appbean;
    public static String mobileNumber;
    public static String invitation_code;
    public static String video_path;
    public static String city_id;
    public static String city_ids;
    public static String city_name;
    public static INTEGRAL integral;
    public Handler mHandler = new Handler();
    public static Map<String ,String > list=new Hashtable<>();
    public static Map<String ,String > easeDatas;


    // 上下文菜单
    private static Context mContext;

    public static double totalintegral=0;
    public static double Chargeintegral=0;
    private UserInfo userInfo;
    public static  String userId="";
    public static  int choosType=0;
    public static  int landType=0;
    public static  String state="";
    public static  String sk="LTAICEWfthnWiWww";
    public static  String stcry="xivcP4sGTIH7qz5TZjFMFe9Dyo3sz0";
    public static  String url="http://ychc.9913seo.com";
    public static  String code="86";
    public static  String CompanyType= Constant.CompanyType;
    public static  String GroupName="";
    public static  List<String> headimage;
    public static  boolean isSetting=false;
    public static  boolean isLogin;
    public static  boolean isPay;
    public static  boolean isComplete;
    public static  String signStr="";
    // 记录是否已经初始化
    private boolean isInit = false;
    public static String LangueType="";
//    private OkHttpClient mOkHttpClient;
    public static ProvinceModel province;
    public static Contury data;
   public LocationUtil locationUtil;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new ConnectivityChangedReceiver(), filter);

        ToastUtils.init(this) ;
        LocationUtil.initUtil(this);
        locationUtil = LocationUtil.shareInstance();
        locationUtil.startLocation();
        ThirdShareManager.getInstance().init(this);
        String userInfoId = SpUtil.getUserInfoId(this);
        //初始化环信
        DemoHelper.getInstance().init(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        if(!TextUtils.isEmpty(userInfoId)){
            userId=userInfoId;
        }
        loadLibs();
        QupaiHttpFinal.getInstance().initOkHttpFinal();
        com.aliyun.vod.common.httpfinal.QupaiHttpFinal.getInstance().initOkHttpFinal();
        //Logger.setDebug(true);
        initDownLoader();
        initImageLoader(this);
        initHttp();
        initDB();
        // 初始化环信SDK
        initEasemob();
        if(CanTingAppLication.data==null){

            Observable.create(new Observable.OnSubscribe<JSONObject>() {

                @Override
                public void call(Subscriber<? super JSONObject> subscriber) {
                    JSONObject json = TextUtil.getJson("phone.json",instance);
                    subscriber.onNext(json);
                    subscriber.onCompleted();//结束异步任务

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JSONObject>() {
                        @Override
                        public void call(JSONObject json) {

                            data = new Gson().fromJson(json.toString(), Contury.class);

                        }
                    });
        }
    }
    private void loadLibs(){
        System.loadLibrary("live-openh264");
        System.loadLibrary("QuCore-ThirdParty");
        System.loadLibrary("QuCore");
        System.loadLibrary("FaceAREngine");
        System.loadLibrary("AliFaceAREngine");
    }
    public static  void  goLogin(Context context){
        Intent gotoLogin=new Intent(context, LoginActivity.class);
        context.startActivity(gotoLogin);
    }
    private void initDownLoader() {
        DownloaderManager.getInstance().init(this);
    }

    class ConnectivityChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
    private void initHttp() {
        // 构建 OkHttpClient 时,将 OkHttpClient.Builder() 传入 with() 方法,进行初始化配置
//        mOkHttpClient = ProgressManager.getInstance().with(new OkHttpClient.Builder())
//                .build();
        HttpUtil.getInstance().init(this, netService.TOM_BASE_URL,true);
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
//          ImageLoaderConfiguration.createDefault(this);
        // method.
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.memoryCacheSize(cacheSize);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 10 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.memoryCache(new WeakMemoryCache()).threadPoolSize(1);
        config.memoryCacheExtraOptions(480, 800);
        config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }
//    public OkHttpClient getOkHttpClient() {
//        return mOkHttpClient;
//    }

    /**
     *
     */
    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        /**
         * SDK初始化的一些配置
         * 关于 EMOptions 可以参考官方的 API 文档
         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
         */
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("guaju");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，TODO 这个暂时有bug，上层收不到发送回执
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        //options.setRequireServerAck(true);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);
        //初始化EaseUI

        EaseUI.getInstance().init(this, options);

        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });


        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, options);
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(false);

        // 设置初始化已经完成
        isInit = true;



    }
    /**
     * 判断应用是否是在后台
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }
    private void initDB(){
        DemoDBManager.getInstance();
    }

    private EaseUser getUserInfo(String username){
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
//        if(username.equals(EMClient.getInstance().getCurrentUser()))
//
//            return getCurrentUserInfo();
        LogUtil.d("查询name="+username);
        user = getContactList().get(username);

//        LogUtil.d("查询nick="+user.getNickname());
        // if user is not in your contacts, set inital letter for him/her
        if(user == null){
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

//    public synchronized EaseUser getCurrentUserInfo() {
//
//            String username = EMClient.getInstance().getCurrentUser();
//        EaseUser currentUser = new EaseUser(username);
//            String nick = SpUtil.getString(this,"");
//            currentUser.setNick((nick != null) ? nick : username);
//            currentUser.setAvatar(getCurrentUserAvatar());
//
//        return currentUser;
//    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        UserDao dao = new UserDao(this);
        Map<String, EaseUser> contactList = dao.getContactList();

        // return a empty non-null object to avoid app crash
        if(contactList == null){
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }
    /**
     * 获取所有App的包名和启动类名
     *
     * @param count count
     */
    public static void madMode(int count,String activityName,String con) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);


//            BadgeUtil.setBadgeOfMadMode(mContext.getApplicationContext(), count, "com.zhongchuang.canting", activityName);

    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    public static CanTingAppLication getInstance() {
        return instance;
    }

    public boolean isLogin() {
        String token = SpUtil.getString(this, "token", "");
//        return !TextUtils.isEmpty(token);
        return true;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
