package com.zhongchuang.canting.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.BuildConfig;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.DownLoadProgressbar;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownActivity extends BaseActivity1 implements BaseContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.zhuye_saoyisao)
    ImageView zhuyeSaoyisao;
    @BindView(R.id.bannerView)
    BannerView bannerView;
    @BindView(R.id.person_name)
    TextView personName;
    @BindView(R.id.person_code)
    TextView personCode;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.progress)
    DownLoadProgressbar mProgress;
    private BasesPresenter presenter;
    private BannerAdapters bannerAdapter;
    private String url;
    private String data;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_down);
        ButterKnife.bind(this);
        presenter = new BasesPresenter(this);
        bannerAdapter = new BannerAdapters(this);
        bannerView.setAdapter(bannerAdapter);
        presenter.getHomeBanners("1");
        url = getIntent().getStringExtra("url");
        data = getIntent().getStringExtra("data");
        new Thread(new DownloadApk(url)).start();
    }

   private long max;
   private long current;

    //循环模拟下载过程
    public void start() {
        if (current <= max) {
             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     mProgress.setMaxValue(max);
                     mProgress.setCurrentValue(current);
                 }
             });

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
//            builder.setContentTitle("下载完成")
//                    .setContentText("点击安装")
//                    .setAutoCancel(true);//设置通知被点击一次是否自动取消
//
//
//            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
//            notification = builder.setContentIntent(pi).build();
//            notificationManager.notify(1, notification);

            DownActivity.this.startActivityForResult(intent, 0);
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
                    max=contentLength;
                    getSize(contentLength);
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
                            notify += len;
                            if(notify==1024*512){
                                notify=0;
                                current=progress;

                                start();
                            }
                            //设置进度
//                            builder.setProgress(100, (int) ((progress / (contentLength * 1.0)) * 100), false);
//                            builder.setContentText("下载进度:" + (int) ((progress / (contentLength * 1.0)) * 100) + "%");
//                            notification = builder.build();
//                            notificationManager.notify(1, notification);


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
    private long notify;
    DecimalFormat df = new DecimalFormat("#0.00");
    private String fileSizeString;
    private String tagSize;

    public void getSize(long fileS) {
        if (fileS < 1024 * 1024 * 10) {
            fileSizeString = df.format((double) fileS / 1048576);
            tagSize = "MB";
        } else if (fileS < 1024 * 1024 * 100) {
            df = new DecimalFormat("#0.0");
            fileSizeString = df.format((double) fileS / 1048576);
            tagSize = "MB";
        } else if (fileS < 1024 * 1024 * 1000) {
            df = new DecimalFormat("#000");
            fileSizeString = df.format((double) fileS / 1048576);
            tagSize = "MB";
        }
        tvSize.setText(fileSizeString + "  " + tagSize);
    }


    @Override
    public void bindEvents() {

    }

    @Override
    public void initData() {

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        Home home = (Home) entity;
        bannerAdapter.setData(home.banner);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
