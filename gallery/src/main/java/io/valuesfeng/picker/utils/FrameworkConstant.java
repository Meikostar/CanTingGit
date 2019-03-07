package io.valuesfeng.picker.utils;

import android.os.Environment;

/**
 * Created by linquandong on 16/8/3.
 */
public class FrameworkConstant {
    /**
     * 存放发送图片的目录
     */
    public static String PICTURE_PATH = Environment.getExternalStorageDirectory() + "/ifun/image/";
    /**
     * 存放拍摄照片的目录
     */
    public static String PICTURE_APP_PATH = Environment.getExternalStorageDirectory() + "/ifun/image/";///marvoto/baby_image/

    /**
     * 存放截图图片的目录
     */
    public static String CROPPICTURE_PATH = Environment
            .getExternalStorageDirectory() + "/ifun/image/crop";

    /**
     * 存放视频目录
     */
    public static String VIDEO_PATH = Environment
            .getExternalStorageDirectory() + "/ifun/videp/";


    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_CAMERA = 1;// 拍照修改头像
    public static final int REQUESTCODE_LOCATION = 2;// 本地相册修改头像
    public static final int REQUESTCODE_CROP = 3;// 系统裁剪头像
    public static final int REQUESTCODE_VIDEO = 4;// 录制视频




}
