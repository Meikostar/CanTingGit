package com.zhongchuang.canting.presenter;


import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Params;

import java.util.List;

public class OtherContract {
    public interface View extends BaseView {

        //        <T> void toList(List<T> list, int type, int... refreshType);
        <T> void toEntity(T entity, int type);

        void toNextStep(int type);

        void showTomast(String msg);
    }

    public interface Presenter {

        void getVideoList(String id);
        /**
         * 设置支付密码接口
         * @param paymentPassword
         * @param confirmPassword
         */
        void setPaymentPassword(String paymentPassword, String confirmPassword);
        /**
         * 修改支付密码接口
         * @param oldPassword
         * @param paymentPassword
         * @param confirmPassword
         */
        void alterPaymentPassword(String oldPassword, String paymentPassword,String confirmPassword);
        /**
         * 验证支付密码接口
         * @param paymentPassword
         */
        void verifyPassword(String paymentPassword);

        /**
         * 忘记密码验证验证码
         * @param mobileNumber
         */
        void payCheckCode(String mobileNumber,String code);

        /**
         * 发红包接口接口
         * @param integralCount
         * @param number
         * @param type
         * @param groupId
         * @param leavMessage
         * @param sendType
         */
        void sendRed(String integralCount,String number,String type,String groupId,String leavMessage,String sendType);

        /**
         * 查看领取详情接口
         * @param redEnvelopeId
         */
        void getLuckInfo(String redEnvelopeId);

        /**
         * 阿里获取直播推流接口
         */
        void getPushUrl();

        /**
         * 阿里获取拉流接口
         */
        void getLiveUrl(String id);
        /**
         * 增加直播录制转点播配置接口
         */
        void addLiveRecordVod();

        /**
         * 获取token接口
         */
        void getLiveToken();

        /**
         * 获取token接口
         */
        void uploadVideo(String coverImage,String videoName,String videoUrl,int type,String liveThirdId);
        /**
         * 设置推流断流回调地址接口
         */
        void setLiveNotifyUrl(int type);

        /**
         * 视频类型
         */
        void updateType(String id,String type);

        /**
         * 获取二级分类
         */
        void getSecondLists(String liveFirstId);

        /**
         * 获取一级分类
         */
        void getFirstCategoryList();


        /**
         * 获取三级分类
         */
        void getThirdList(String liveFirstId);


        /**
         * .录制视频首页接口
         */
        void getDefaultVideoAndCategory(String id);

        /**
         *根据三级类别id获取直播列表接口
         */
        void getDirectListByThirdid(String liveThirdId);


        /**
         *根据三级类别id获取视频列表接口
         */
        void getVideoListByThirdid(String liveThirdId);

        /**
         *根直播列表首页接口
         */
        void getDefaultLiveAndCategory(String id);

        /**
         *24.不分类别获取热门直播列表接口

         */
        void getHotDirect();



        /**
         *22.视频搜索接口

         */


        void searchVideoByNameOrCategory(String videoName,String liveFirstId,String livesecondId	,String liveThirdId );


        /**
         *25.直播搜索接口


         */
        void searchDirectByNameOrCategory(String videoName,String liveFirstId,String livesecondId	,String liveThirdId );


        void getLiveCategory();
    }
}
