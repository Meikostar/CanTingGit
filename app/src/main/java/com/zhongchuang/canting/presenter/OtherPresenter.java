package com.zhongchuang.canting.presenter;


        import com.zhongchuang.canting.app.CanTingAppLication;
        import com.zhongchuang.canting.been.BaseResponse;
        import com.zhongchuang.canting.been.LiveItemBean;
        import com.zhongchuang.canting.been.LiveTypeBean;
        import com.zhongchuang.canting.been.RedInfo;
        import com.zhongchuang.canting.been.VideoData;
        import com.zhongchuang.canting.been.VideoMoreData;
        import com.zhongchuang.canting.been.aliLive;
        import com.zhongchuang.canting.been.videobean;
        import com.zhongchuang.canting.fragment.mall.LiveMineFragments;
        import com.zhongchuang.canting.net.BaseCallBack;
        import com.zhongchuang.canting.net.HttpUtil;
        import com.zhongchuang.canting.net.netService;
        import com.zhongchuang.canting.utils.SpUtil;
        import com.zhongchuang.canting.utils.TextUtil;
        import com.zhongchuang.canting.utils.location.LocationUtil;

        import java.util.HashMap;
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
    public void getDefaultVideoAndCategory(String id) {

        Map<String, String> map = new HashMap<>();
        map.put("liveFirstId", id);
        map.put("companyType", CanTingAppLication.CompanyType);
        map.put("userInfoId",  TextUtil.isEmpty(SpUtil.getUserInfoId(CanTingAppLication.getInstance())) ? "" : SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        api.getDefaultVideoAndCategory(map).enqueue(new BaseCallBack<VideoData>() {
            @Override
            public void onSuccess(VideoData userLoginBean) {

                mView.toEntity(userLoginBean.data, 2);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
//                callBack.onFail(code,t);
            }
        });
    }
    @Override
    public void getLiveCategory() {


        Map<String, String> params = new TreeMap<>();

        params.put("userInfoId", TextUtil.isEmpty(SpUtil.getUserInfoId(CanTingAppLication.getInstance())) ? "" : SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        params.put("companyType", CanTingAppLication.CompanyType);

        api.getLiveCategory(params).enqueue(new BaseCallBack<LiveTypeBean>() {

            @Override
            public void onSuccess(LiveTypeBean userLoginBean) {
                LiveMineFragments.datas=userLoginBean.data;
                mView.toEntity(userLoginBean.data, 998);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getHotDirect() {

        Map<String, String> map = new HashMap<>();

        map.put("userInfoId",  TextUtil.isEmpty(SpUtil.getUserInfoId(CanTingAppLication.getInstance())) ? "" : SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("companyType", CanTingAppLication.CompanyType);

        api.getHotDirect(map).enqueue(new BaseCallBack<VideoData>() {
            @Override
            public void onSuccess(VideoData userLoginBean) {

                mView.toEntity(userLoginBean.data.videoList, 2);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
//                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void getDefaultLiveAndCategory(String id) {

        Map<String, String> map = new HashMap<>();
        map.put("liveFirstId", id);
        map.put("companyType", CanTingAppLication.CompanyType);
        map.put("userInfoId",  TextUtil.isEmpty(SpUtil.getUserInfoId(CanTingAppLication.getInstance())) ? "" : SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        api.getDefaultLiveAndCategory(map).enqueue(new BaseCallBack<VideoData>() {
            @Override
            public void onSuccess(VideoData userLoginBean) {

                mView.toEntity(userLoginBean.data, 2);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
//                callBack.onFail(code,t);
            }
        });
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
    public void getSecondLists(String liveFirstId) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("liveFirstId", liveFirstId);

        api.getSecondLists(map).enqueue(new BaseCallBack<LiveItemBean>() {

            @Override
            public void onSuccess(LiveItemBean userLoginBean) {
                mView.toEntity(userLoginBean.data, 2);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getFirstCategoryList() {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("companyType", CanTingAppLication.CompanyType);

        api.getFirstCategoryList(map).enqueue(new BaseCallBack<LiveItemBean>() {

            @Override
            public void onSuccess(LiveItemBean userLoginBean) {
                mView.toEntity(userLoginBean.data, 1);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getDirectListByThirdid(String liveThirdId) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("liveThirdId",liveThirdId);


        api.getDirectListByThirdid(map).enqueue(new BaseCallBack<VideoMoreData>() {

            @Override
            public void onSuccess(VideoMoreData userLoginBean) {
                mView.toEntity(userLoginBean.data, 1);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getVideoListByThirdid(String liveThirdId) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("liveThirdId",liveThirdId);
        map.put("companyType", CanTingAppLication.CompanyType);

        api.getVideoListByThirdid(map).enqueue(new BaseCallBack<VideoMoreData>() {

            @Override
            public void onSuccess(VideoMoreData userLoginBean) {
                mView.toEntity(userLoginBean.data, 1);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void searchVideoByNameOrCategory(String videoName,String liveFirstId,String livesecondId	,String liveThirdId ) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        if(TextUtil.isNotEmpty(videoName)){
            map.put("videoName",videoName);
        }  if(TextUtil.isNotEmpty(liveFirstId)){
            map.put("liveFirstId",liveFirstId);
        }  if(TextUtil.isNotEmpty(livesecondId)){
            map.put("livesecondId",livesecondId);
        }  if(TextUtil.isNotEmpty(liveThirdId)){
            map.put("liveThirdId",liveThirdId);
        }

        map.put("companyType", CanTingAppLication.CompanyType);




        api.searchVideoByNameOrCategory(map).enqueue(new BaseCallBack<VideoMoreData>() {

            @Override
            public void onSuccess(VideoMoreData userLoginBean) {
                mView.toEntity(userLoginBean.data, 1);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void searchDirectByNameOrCategory(String directSeeName,String liveFirstId,String livesecondId	,String liveThirdId ) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        if(TextUtil.isNotEmpty(directSeeName)){
            map.put("directSeeName",directSeeName);
        }  if(TextUtil.isNotEmpty(liveFirstId)){
            map.put("liveFirstId",liveFirstId);
        }  if(TextUtil.isNotEmpty(livesecondId)){
            map.put("livesecondId",livesecondId);
        }  if(TextUtil.isNotEmpty(liveThirdId)){
            map.put("liveThirdId",liveThirdId);
        }



        map.put("companyType", CanTingAppLication.CompanyType);


        api.searchDirectByNameOrCategory(map).enqueue(new BaseCallBack<VideoMoreData>() {

            @Override
            public void onSuccess(VideoMoreData userLoginBean) {
                mView.toEntity(userLoginBean.data, 1);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    @Override
    public void getThirdList(String liveFirstId) {

        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("secondCategoryId", liveFirstId);
        map.put("companyType", CanTingAppLication.CompanyType);
        api.getThirdList(map).enqueue(new BaseCallBack<LiveItemBean>() {

            @Override
            public void onSuccess(LiveItemBean userLoginBean) {
                mView.toEntity(userLoginBean.data, 3);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    public void updateType(String id,String type) {


        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);

        api.updateType(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse userLoginBean) {
                mView.toEntity(userLoginBean, 5);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    public void setLiveNotifyUrl(int type) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("type", type+"");
        if(TextUtil.isNotEmpty(LocationUtil.city)){
            map.put("liveAddress", LocationUtil.city);
        }

        api.setLiveNotifyUrl(map).enqueue(new BaseCallBack<aliLive>() {

            @Override
            public void onSuccess(aliLive userLoginBean) {
                mView.toEntity(userLoginBean.data, 7);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getVideoList(String id) {
        Map<String, String> params = new TreeMap<>();
        params.put("pageNum", "1");
        params.put("pageSize", "2000");

        params.put("userInfoId", id);

        api.getVideoList(params).enqueue(new BaseCallBack<videobean>() {

            @Override
            public void onSuccess(videobean userLoginBean) {
                mView.toEntity(userLoginBean.data, 111);
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
    public void getPushUrl() {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("companyType", CanTingAppLication.CompanyType);
        api.getPushUrl(map).enqueue(new BaseCallBack<aliLive>() {

            @Override
            public void onSuccess(aliLive userLoginBean) {
                mView.toEntity(userLoginBean.data, 19);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    @Override
    public void getLiveUrl(String id) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("anchorUserId", id);

        api.getLiveUrl(map).enqueue(new BaseCallBack<aliLive>() {

            @Override
            public void onSuccess(aliLive userLoginBean) {
                mView.toEntity(userLoginBean.data, 19);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }
    @Override
    public void getLiveToken() {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));

        api.getLiveToken(map).enqueue(new BaseCallBack<aliLive>() {

            @Override
            public void onSuccess(aliLive userLoginBean) {
                mView.toEntity(userLoginBean.data, 19);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }


    @Override
    public void uploadVideo(String coverImage,String videoName,String videoUrl,int type,String liveThirdId) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("coverImage", coverImage);
        map.put("videoName", videoName);
        map.put("videoUrl", videoUrl);
        map.put("liveThirdId", liveThirdId);
        map.put("type", type+"");
        map.put("companyType", CanTingAppLication.CompanyType);
        api.uploadVideo(map).enqueue(new BaseCallBack<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse BaseResponse) {
                mView.toEntity(BaseResponse, 12);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                mView.showTomast(t);
            }
        });
    }

    @Override
    public void addLiveRecordVod() {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
        map.put("companyType", CanTingAppLication.CompanyType);
        api.addLiveRecordVod(map).enqueue(new BaseCallBack<aliLive>() {

            @Override
            public void onSuccess(aliLive userLoginBean) {
                mView.toEntity(userLoginBean.data, 19);
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
        map.put("companyType", CanTingAppLication.CompanyType);
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
