package com.zhongchuang.canting.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.mob.MobSDK;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.widget.share.ShareModel;
import com.zhongchuang.canting.widget.share.SharePopupWindow;


import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by linquandong on 2017/6/22.
 */

public class ShareUtils {
    public static String staticurl = "http://laser.yunchedian.com/index.php/app/";//生产环境

    public final static String DOWNLOAD = staticurl + "news/down";//网页分享
    public final static String SHARE_LOGO = "http://yyrtv.mykar.com/108-108.png";//分享的Logo
    public static void share(Context mContext){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("激光");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("激光");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(mContext);
    }
    public static void showMyShare(Activity activity, String text, String url) {
        MobSDK.init(activity.getApplicationContext());
        SharePopupWindow share = new SharePopupWindow(activity);
        share.setPlatformActionListener(new PlatformActionListener() {
            //分享成功
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("sharesdk","分享成功"+i);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("sharesdk","onError"+i);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("sharesdk","onCancel"+i);
            }
        });
        ShareModel model = new ShareModel();
        model.setImageUrl(SHARE_LOGO);
        model.setText(text);
        if (TextUtils.isEmpty(url)) {
            model.setUrl(DOWNLOAD);
        } else {
            model.setUrl(url);
        }
        model.setTitle(activity.getString(R.string.yqlb));
        share.initShareParams(model);
        share.showShareWindow();
        share.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public static void showMyShares(Activity activity, String text, String url) {
        MobSDK.init(activity.getApplicationContext());
        SharePopupWindow share = new SharePopupWindow(activity);
        share.setType(1);
        share.setPlatformActionListener(new PlatformActionListener() {
            //分享成功
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("sharesdk","分享成功"+i);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("sharesdk","onError"+i);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("sharesdk","onCancel"+i);
            }
        });
        ShareModel model = new ShareModel();
        model.setImageUrl(SHARE_LOGO);
        model.setText(text);
        if (TextUtils.isEmpty(url)) {
            model.setUrl(DOWNLOAD);
        } else {
            model.setUrl(url);
        }
        model.setTitle("一起来吧");
        share.initShareParams(model);
        share.showShareWindow();
        share.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
