package com.zhongchuang.canting.activity.mall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.BuildConfig;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.AboubtProductActivity;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.pay.ChangePayActivity;
import com.zhongchuang.canting.activity.pay.PaySettingActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseAllActivity;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.utils.DataCleanManager;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscription;
import rx.functions.Action1;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 设置
 */
public class SettingActivity extends BaseAllActivity {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_moren)
    LinearLayout llMoren;
    @BindView(R.id.ll_clear)
    LinearLayout llClear;
    @BindView(R.id.tv_clear)
    TextView tvClear;
    @BindView(R.id.switchs)
    Switch switchs;
    @BindView(R.id.ll_change)
    LinearLayout llChange;
    @BindView(R.id.ll_pays)
    LinearLayout llPays;
    private Subscription mSubscription;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        switchs.setChecked(SpUtil.isAdmin("open"));
        tvPhone.setText(SpUtil.getMobileNumber(SettingActivity.this));
        tvClear.setText(DataCleanManager.getExternalCacheSize(SettingActivity.this));
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

    private String userInfoId;

    private void exitApp() {
        String olderToken = SpUtil.getString(this, "token", "");//token值
        String code = SpUtil.getString(this, "code", "");
        if (TextUtils.isEmpty(olderToken)) {
            return;
        } else {
            userInfoId = SpUtil.getString(SettingActivity.this, "userInfoId", "");
            if (userInfoId != null) {
                SpUtil.remove(SettingActivity.this, "userInfoId");//token值
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FINISH, ""));
                                finish();
                            }
                        });
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
    public void bindEvents() {

        llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.putExtra("type", 6);
                startActivity(intent);
//                exitApp();
            }
        });
        switchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpUtil.putInt(SettingActivity.this, "open", 1);
                    switchs.setChecked(true);
                } else {
                    switchs.setChecked(false);
                    SpUtil.putInt(SettingActivity.this, "open", 0);
                }
            }
        });

        llPays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CanTingAppLication.isSetting){
                    Intent intent = new Intent(SettingActivity.this, ChangePayActivity.class);

                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SettingActivity.this, PaySettingActivity.class);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }

            }
        });
        llPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AboubtProductActivity.class));
            }
        });
        llMoren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AddressListActivity.class));
            }
        });
        llClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(getString(R.string.zzqlhc));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.deleteFolderFile(getExternalCacheDir().getPath(), true);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (DataCleanManager.getExternalCacheSize(SettingActivity.this).equals("0.0Byte")) {

                                    ToastUtils.showNormalToast(getString(R.string.yqc));
                                } else {
                                    ToastUtils.showNormalToast(getString(R.string.qc) + "(" + DataCleanManager.getExternalCacheSize(SettingActivity.this) + ")");
                                    tvClear.setText(DataCleanManager.getExternalCacheSize(SettingActivity.this));

                                }
                                dimessProgress();
                            }
                        });
                    }
                }).start();

                DataCleanManager.clearAllCache(SettingActivity.this);
                try {
                    String size = DataCleanManager.getTotalCacheSize(SettingActivity.this);
                    tvClear.setText(size);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void initData() {

    }


    private void processAppVersion(String version, final String url) {


        String newVersion = version;

        String oldVersion = StringUtil.getVersion(this);//"0.17"

        try {
            if (newVersion.compareTo(oldVersion) > 0) {
                View view = View.inflate(this, R.layout.base_dailog_view, null);
                TextView sure = (TextView) view.findViewById(R.id.txt_sure);
                TextView cancel = (TextView) view.findViewById(R.id.txt_cancel);
                TextView title = (TextView) view.findViewById(R.id.txt_title);
                title.setText(getString(R.string.check_versions));
                cancel.setText(R.string.yihougx);
                sure.setText(R.string.wozhidol);
                final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(view).create();
                dialog.show();
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new DownloadApk(url)).start();
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                View view = View.inflate(this, R.layout.base_dailog_view, null);
                TextView sure = (TextView) view.findViewById(R.id.txt_sure);
                TextView cancel = (TextView) view.findViewById(R.id.txt_cancel);
                TextView title = (TextView) view.findViewById(R.id.txt_title);
                title.setText(getString(R.string.check_version));
                cancel.setVisibility(View.GONE);
                sure.setText(R.string.wozhidol);
                final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(view).create();
                dialog.show();
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
//                float newV = Float.parseFloat(newVersion);
//
//                float oldV = Float.parseFloat(oldVersion);
//
//                if (newV > oldV) {
//                    hasNew=true;
//                }
//                else {
//                    hasNew=false;
//                }
        } catch (Exception e) {
//            if (!StringUtil.equals(newVersion, oldVersion)) {
//                hasNew=true;
//            }else {
//                hasNew=false;
//            }
        }
    }



    public class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        InputStream is;
        FileOutputStream fos;
        private Context context;

        public DownloadApk(String url) {
            this.url = url;
        }

        private String url;

        /**
         * 下载完成,提示用户安装
         */
        private void installApk(File file) {

            //调用系统安装程序
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri photoURI = FileProvider.getUriForFile(LiveHomeActivity.this, LiveHomeActivity.this.getApplicationContext().getPackageName() + ".provider", file);
//            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
//            LiveHomeActivity.this.startActivityForResult(intent, 0);
//
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
            startActivityForResult(intent, 0);
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
                    //设置最大值
                    //保存到sd卡
                    String apkName = url.substring(url.lastIndexOf("/") + 1, url.length());
                    File apkFile = new File(Environment.getExternalStorageDirectory(), apkName);
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
                            //设置进度

                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    //下载完成,提示用户安装
                    installApk(apkFile);
                }
            } catch (IOException e) {
                return;
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


}
