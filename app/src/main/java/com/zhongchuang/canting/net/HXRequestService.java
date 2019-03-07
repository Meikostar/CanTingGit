package com.zhongchuang.canting.net;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.HXFriendListBean;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.TOKEN;

import java.util.Map;


import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/11/29.
 */

public interface HXRequestService {


    //添加好友
    public static final String ADDFRIEND_URL = "web/chatDirectories/addFriends";

    //获取好友列表
    public static final String GETFRIENDList_URL = "web/chatDirectories/getChatDirectoriesList";





    @FormUrlEncoded
    @POST(ADDFRIEND_URL)
    Call<BaseResponse> addFriend(@FieldMap Map<String, String> opt);

    @GET("wap/qiNiu/getUpToken")
    Call<TOKEN> getUpToken();

    @FormUrlEncoded
    @POST("wap/direct/upRoomInfo")
    Call<BaseResponse> upRoomInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/direct/hostInfo")
    Call<Host> hostInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST(GETFRIENDList_URL)
    Call<HXFriendListBean> getFriendList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatGroup/getFrendList")
    Call<FriendListBean> getFrendList(@FieldMap Map<String, String> map);
}
