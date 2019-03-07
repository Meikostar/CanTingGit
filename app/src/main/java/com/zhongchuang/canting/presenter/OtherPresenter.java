package com.zhongchuang.canting.presenter;


        import android.widget.TextView;

        import com.zhongchuang.canting.app.CanTingAppLication;
        import com.zhongchuang.canting.been.AddressBase;
        import com.zhongchuang.canting.been.Banner;
        import com.zhongchuang.canting.been.BaseBe;
        import com.zhongchuang.canting.been.BaseBean;
        import com.zhongchuang.canting.been.BaseResponse;
        import com.zhongchuang.canting.been.CancelParam;
        import com.zhongchuang.canting.been.Care;
        import com.zhongchuang.canting.been.Cares;
        import com.zhongchuang.canting.been.Catage;
        import com.zhongchuang.canting.been.Codes;
        import com.zhongchuang.canting.been.Favor;
        import com.zhongchuang.canting.been.FriendInfo;
        import com.zhongchuang.canting.been.FriendListBean;
        import com.zhongchuang.canting.been.GAME;
        import com.zhongchuang.canting.been.Hand;
        import com.zhongchuang.canting.been.Hands;
        import com.zhongchuang.canting.been.Home;
        import com.zhongchuang.canting.been.Host;
        import com.zhongchuang.canting.been.INTEGRALIST;
        import com.zhongchuang.canting.been.Ingegebean;
        import com.zhongchuang.canting.been.OrderData;
        import com.zhongchuang.canting.been.OrderParam;
        import com.zhongchuang.canting.been.OrderType;
        import com.zhongchuang.canting.been.Param;
        import com.zhongchuang.canting.been.Params;
        import com.zhongchuang.canting.been.Product;
        import com.zhongchuang.canting.been.ProductBuy;
        import com.zhongchuang.canting.been.ProductDel;
        import com.zhongchuang.canting.been.QfriendBean;
        import com.zhongchuang.canting.been.RedInfo;
        import com.zhongchuang.canting.been.ShopBean;
        import com.zhongchuang.canting.been.Version;
        import com.zhongchuang.canting.been.WEIXINREQ;
        import com.zhongchuang.canting.been.apply;
        import com.zhongchuang.canting.been.pay.alipay;
        import com.zhongchuang.canting.net.BaseCallBack;
        import com.zhongchuang.canting.net.HttpUtil;
        import com.zhongchuang.canting.net.netService;
        import com.zhongchuang.canting.utils.SpUtil;
        import com.zhongchuang.canting.utils.TextUtil;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.TreeMap;

        import rx.Subscription;


public class OtherPresenter implements OtherContract.Presenter {
    private Subscription subscription;

    private OtherContract.View mView;


    protected netService api;

    public OtherPresenter(OtherContract.View view) {
        mView = view;

        api = HttpUtil.getInstance().create(netService.class);
    }
    @Override
    public void alterPaymentPassword(String oldPassword, String paymentPassword,String confirmPassword) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("oldPassword", oldPassword);
        map.put("paymentPassword", paymentPassword);
        map.put("confirmPassword", confirmPassword);


        api.alterPaymentPassword(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse userLoginBean) {
                mView.toEntity(userLoginBean, 13);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    @Override
    public void setPaymentPassword(String paymentPassword, String confirmPassword) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("paymentPassword", paymentPassword);
        map.put("confirmPassword", confirmPassword);


        api.setPaymentPassword(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse userLoginBean) {
                mView.toEntity(userLoginBean, 11);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void verifyPassword(String paymentPassword) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        if(TextUtil.isNotEmpty(paymentPassword)){
            map.put("paymentPassword", paymentPassword);
            map.put("type", "1");
        }else {
            map.put("type", "2");
        }



        api.verifyPassword(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse userLoginBean) {
                mView.toEntity(userLoginBean, 999);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }


    @Override
    public void payCheckCode(String mobileNumber,String code) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("mobileNumber", mobileNumber);
        map.put("code", code);

        api.payCheckCode(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse userLoginBean) {
                mView.toEntity(userLoginBean, 999);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }


    @Override
    public void sendRed(String integralCount,String number,String type,String groupId,String leavMessage,String sendType) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("integralCount", integralCount);
        map.put("number", number);
        map.put("type", type);
        map.put("groupId", groupId);
        map.put("leavMessage", leavMessage);
        map.put("sendType", sendType);


        api.sendRed(map).enqueue(new BaseCallBack<RedInfo>() {

            @Override
            public void onSuccess(RedInfo userLoginBean) {
                mView.toEntity(userLoginBean.data, 59);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    @Override
    public void getLuckInfo(String redEnvelopeId) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));

        map.put("redEnvelopeId", redEnvelopeId);


        api.getLuckInfo(map).enqueue(new BaseCallBack<RedInfo>() {

            @Override
            public void onSuccess(RedInfo userLoginBean) {
                mView.toEntity(userLoginBean.data, 59);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
}
