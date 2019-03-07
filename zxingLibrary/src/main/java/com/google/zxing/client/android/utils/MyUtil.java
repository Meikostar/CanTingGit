/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.google.zxing.client.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author 咖枯
 * @version 1.0 2015/07
 */
public class MyUtil {

    /**
     * Log tag ：MyUtil
     */
    private static final String LOG_TAG = "MyUtil";



    /**
     * 去掉文件扩展名
     *
     * @param fileName 文件名
     * @return 没有扩展名的文件名
     */
    public static String removeEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < fileName.length())) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    public static void setStatusBarTranslucent(ViewGroup vg, Activity activity) {
        // 如果版本在4.4以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏高度
            int height = getStatusBarHeight(activity);
            if (height <= 0) {
                return;
            }
            // 设置距离顶部状态栏垂直距离
            vg.setPadding(0, height, 0, 0);
            // 状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏透明
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void setStatusBarTranslucent(Activity activity) {
        // 如果版本在4.4以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            height = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }



    /**
     * 时间补零
     *
     * @param time 需要补零的时间
     * @return 补零后的时间
     */
    public static String addZero(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }

        return String.valueOf(time);
    }

    /**
     * 振动单次100毫秒
     *
     * @param context context
     */
    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    /**
     * 检查当前网络是否可用
     *
     * @param context context
     * @return 是否连接到网络
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {

                    return true;
                }
            }
        }
        return false;
    }


    private static long mLastClickTime = 0;             // 按钮最后一次点击时间
    private static final int SPACE_TIME = 500;          // 空闲时间

    /**
     * 是否连续点击按钮多次
     *
     * @return 是否快速多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = SystemClock.elapsedRealtime();
        if (time - mLastClickTime <= SPACE_TIME) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }







    public static File getFileDirectory(Context context, String path) {
        File file = null;
        if (isHasSDCard()) {
            file = new File(Environment.getExternalStorageDirectory(), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    /**
     * Returns specified directory(/mnt/sdcard/Android/data/<application package>/files/...).
     * directory will be created on SD card by defined path if card
     * is mounted. Else - Android defines files directory on device's
     * files（/data/data/<application package>/files） system.
     *
     * @param context context
     * @param path    file  path (e.g.: "/music/a.mp3", "/pictures/a.jpg")
     * @return File {@link File directory}
     */
    public static File getExternalFileDirectory(Context context, String path) {
        File file = null;
        if (isHasSDCard()) {
            file = new File(context.getExternalFilesDir(null), path);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    file = null;
                }
            }
        }
        if (file == null) {
            // 使用内部缓存[MediaStore.EXTRA_OUTPUT ("output")]是无法正确写入裁切后的图片的。
            // 系统是用全局的ContentResolver来做这个过程的文件io操作，app内部的存储被忽略。（猜测）
            file = new File(context.getFilesDir(), path);
        }
        return file;
    }

    public static boolean isHasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Returns directory absolutePath.
     *
     * @param context context
     * @param path    file path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jpg")
     * @return /mnt/sdcard/Android/data/<application package>/files/....
     */
    public static String getFilePath(Context context, String path) {
        return getExternalFileDirectory(context, path).getAbsolutePath();
    }

    /**
     * set intent options
     *
     * @param context  context
     * @param uri      image path uri
     * @param filePath save path (e.g.: "/AppDir/a.mp3", "/AppDir/files/images/a.jpg")
     * @param type     0，截取壁纸/拍照；1，截取Logo
     * @return Intent
     */
    public static Intent getCropImageOptions(Context context, Uri uri, String filePath, int type) {
        int width;
        int height;
        // 截取壁纸/拍照
        if (type == 0) {
            width = context.getResources().getDisplayMetrics().widthPixels;
            height = context.getResources().getDisplayMetrics().heightPixels;
        } else { // 截取logo
            width = height = dip2px(context, 30);
        }

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        // 保存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getExternalFileDirectory
                (context, filePath)));
        // 是否去除面部检测
        intent.putExtra("noFaceDetection", true);
        // 是否保留比例
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 裁剪区的宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        // 是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        return intent;
    }

    /**
     * 网址验证
     *
     * @param url 需要验证的内容
     */
    public static boolean checkWebSite(String url) {
        //http://www.163.com
//        String format = "^(http)\\://(\\w+\\.\\w+\\.\\w+|\\w+\\.\\w+)";
        //TODO: 正则表达式理解
        String format = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        return startCheck(format, url);
    }

    /**
     * 网址路径（无协议部分）验证
     *
     * @param url 需要验证的路径
     */
    public static boolean checkWebSitePath(String url) {
        String format = "[\\w\\-_]+(\\.[\\w\\-_]+)+" +
                "([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        return startCheck(format, url);
    }

    /**
     * 匹配正则表达式
     *
     * @param format 匹配格式
     * @param str    匹配内容
     * @return 是否匹配成功
     */
    private static boolean startCheck(String format, String str) {
        boolean tem;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(str);

        tem = matcher.matches();
        return tem;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
