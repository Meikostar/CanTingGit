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
        void getLiveUrl();
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
        void uploadVideo(String coverImage,String videoName,String videoUrl);
    }
}
