/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhongchuang.canting.utils;

import android.os.Environment;

import java.io.File;

/**

 */
public class Constants {
    public static final String BD_EXIT_APP = "bd_sxb_exit";

    public static final String USER_INFO = "user_info";

    public static final String USER_ID = "user_id";

    public static final String USER_SIG = "user_sig";

    public static final String USER_TOKEN = "user_token";

    public static final String USER_NICK = "user_nick";

    public static final String USER_SIGN = "user_sign";

    public static final String USER_AVATAR = "user_avatar";

    public static final String USER_ROOM_NUM = "user_room_num";

    public static final String LIVE_ANIMATOR = "live_animator";
    public static final String LOG_LEVEL = "log_level";
    public static final String BEAUTY_TYPE = "beauty_type";
    public static final String VIDEO_QULITY = "video_qulity";

    public static final int LOCATION_PERMISSION_REQ_CODE = 1;
    public static final int TEXT_TYPE = 0;
    public static final int SDK_APPID = 1400027849;
    public static final String IMAGE_SAVE_PATH = Environment.getExternalStorageDirectory()+ File.separator +"CanTing/download/images/";
    public static final String TEMPIMAGE_SAVE_PATH = Environment.getExternalStorageDirectory()+ File.separator +"CanTing/download/tempimage/";
    public static final String APK_SAVE_PATH = Environment.getExternalStorageDirectory()+ File.separator +"CanTing/download/apk/";

    public static final int AVIMCMD_MULTI = 0x800;             // 多人互动消息类型

    public static final int AVIMCMD_MUlTI_HOST_INVITE = AVIMCMD_MULTI + 1;         // 邀请互动,
    public static final int AVIMCMD_MULTI_CANCEL_INTERACT = AVIMCMD_MUlTI_HOST_INVITE + 1;       // 断开互动，
    public static final int AVIMCMD_MUlTI_JOIN = AVIMCMD_MULTI_CANCEL_INTERACT + 1;       // 同意互动，
    public static final int AVIMCMD_MUlTI_REFUSE = AVIMCMD_MUlTI_JOIN + 1;      // 拒绝互动，

    public static final int AVIMCMD_MULTI_HOST_ENABLEINTERACTMIC = AVIMCMD_MUlTI_REFUSE + 1;  // 主播打开互动者Mic，
    public static final int AVIMCMD_MULTI_HOST_DISABLEINTERACTMIC = AVIMCMD_MULTI_HOST_ENABLEINTERACTMIC + 1;// 主播关闭互动者Mic，
    public static final int AVIMCMD_MULTI_HOST_ENABLEINTERACTCAMERA = AVIMCMD_MULTI_HOST_DISABLEINTERACTMIC + 1; // 主播打开互动者Camera，
    public static final int AVIMCMD_MULTI_HOST_DISABLEINTERACTCAMERA = AVIMCMD_MULTI_HOST_ENABLEINTERACTCAMERA + 1; // 主播打开互动者Camera
    public static final int AVIMCMD_MULTI_HOST_CANCELINVITE = AVIMCMD_MULTI_HOST_DISABLEINTERACTCAMERA + 1; //主播让某个互动者下麦
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_CAMERA = AVIMCMD_MULTI_HOST_CANCELINVITE + 1; //主播控制某个上麦成员摄像头
    public static final int AVIMCMD_MULTI_HOST_CONTROLL_MIC = AVIMCMD_MULTI_HOST_CONTROLL_CAMERA + 1; //主播控制某个上麦成员MIC
    public static final int AVIMCMD_MULTI_HOST_SWITCH_CAMERA = AVIMCMD_MULTI_HOST_CONTROLL_MIC+1; ////主播切换某个上麦成员MIC

    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息

    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件

    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 用户加入直播,
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 用户退出直播,
    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息,
    public static final int AVIMCMD_HOST_LEAVE = AVIMCMD_PRAISE + 1;         // 主播离开,
    public static final int AVIMCMD_HOST_BACK = AVIMCMD_HOST_LEAVE + 1;      // 主播回来,


    public static final String HOST_ROLE = "LiveMaster";
    public static final String VIDEO_MEMBER_ROLE = "LiveGuest";
    public static final String NORMAL_MEMBER_ROLE = "Guest";

    public static final String HD_ROLE = "HD";
    public static final String SD_ROLE = "SD";
    public static final String LD_ROLE = "LD";
    public static final String HD_GUEST_ROLE = "HDGuest";
    public static final String SD_GUEST_ROLE = "SDGuest";
    public static final String LD_GUEST_ROLE = "LDGuest";

    public static final String SD_GUEST = "Guest";
    public static final String LD_GUEST = "Guest2";


}
