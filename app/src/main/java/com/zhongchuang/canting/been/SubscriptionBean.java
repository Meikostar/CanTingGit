package com.zhongchuang.canting.been;

/***
 * 功能描述:RxBus消息订阅
 * 作者:qiujialiu
 * 时间:2016/12/15
 * 版本:1.0
 ***/

public class SubscriptionBean {

   public static int FINISH=111;

   public static int REFRESSH=112;
   public static int REFRESSHS=113;
   public static int CHANGE=114;
   public static int LIVECLOSE=115;
   public static int OPEN=116;
   public static int CHAEGE_SUCCESS=117;
   public static int NOTIFY_MONEY=118;
   public static int SIGN=119;
   public static int REFRESSHIMG=120;
   public static int FRIEND=121;
   public static int SEAECH=122;
   public static int LOGIN_FINISH=123;
   public static int CAR_CHANGE=124;
   public static int CAR_CHANGEA=125;
   public static int CONTENT=126;
    public static int MESSAGECONT=128;
    public static int MESSAGENOTIFI=129;
    public static int MESSAGENOTIFIS=1290;
    public static int OUTLOGIN=1291;
    public static int QFRIED_FRESH=1292;
    public static int QFRIED_SEND_FRESH=1293;
    public static int FRIEND_RESH=1294;
    public static int SEND_DEL=1295;
    public static int PAYSET_FIN=1296;
    /**
     * 重新登入
     */

    public static <T>RxBusSendBean createSendBean(int type,T t){
        RxBusSendBean<T> busSendBean = new RxBusSendBean();
        busSendBean.type = type;
        busSendBean.content = t;
        return busSendBean;
    }

    public static class RxBusSendBean<T>{
        public int type;
        public T content;
    }

}
