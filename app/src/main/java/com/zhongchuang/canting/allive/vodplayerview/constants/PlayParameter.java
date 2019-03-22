package com.zhongchuang.canting.allive.vodplayerview.constants;

/**
 * 播放参数, 包含:
 *  vid, vidSts, akId, akSecre, scuToken
 */
public class PlayParameter {


    /**
     * type, 用于区分播放类型, 默认为vidsts播放
     * vidsts: vid类型
     * localSource: url类型
     *
     */
    public static  String PLAY_PARAM_TYPE = "localSource";

    /**
     * vid, 初始为: 6e783360c811449d8692b2117acc9212
     */
    public static  String PLAY_PARAM_VID = "6e783360c811449d8692b2117acc9212";

    /**
     * akId
     */
    public static  String PLAY_PARAM_AK_ID = "";

    /**
     * akSecre
     */
    public static  String PLAY_PARAM_AK_SECRE = "";

    /**
     * scuToken
     */
    public static  String PLAY_PARAM_SCU_TOKEN = "";

    /**
     * url类型的播放地址, 初始为:http://player.alicdn.com/video/aliyunmedia.mp4
     */
    public static String PLAY_PARAM_URL = "rtmp://alive.chushenduojin.cn/zx/live_1234567?auth_key=1552975791-0-0-034983b047ea69b08db7f2cb52c08751";

}
