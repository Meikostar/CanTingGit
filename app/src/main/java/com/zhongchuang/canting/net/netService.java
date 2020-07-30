package com.zhongchuang.canting.net;

import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.been.AppInfo;
import com.zhongchuang.canting.been.BEAN;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.BaseBe;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.BaseBean1;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.BusinssBean;
import com.zhongchuang.canting.been.CancelParam;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.been.Cares;
import com.zhongchuang.canting.been.Catage;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.been.Codes;
import com.zhongchuang.canting.been.Favor;
import com.zhongchuang.canting.been.FoodOrderBean;
import com.zhongchuang.canting.been.FoodShopBean;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.GIFTDATA;
import com.zhongchuang.canting.been.GameBean;
import com.zhongchuang.canting.been.GameDownBean;
import com.zhongchuang.canting.been.GoodsDetailBean;
import com.zhongchuang.canting.been.GoodsSpeCate;
import com.zhongchuang.canting.been.GrapRed;
import com.zhongchuang.canting.been.GrapRedDetail;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.been.Home;
import com.zhongchuang.canting.been.Host;
import com.zhongchuang.canting.been.INTEGRAL;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.Ingegebean;
import com.zhongchuang.canting.been.LIVEBEAN;
import com.zhongchuang.canting.been.LiveItemBean;
import com.zhongchuang.canting.been.LiveTypeBean;
import com.zhongchuang.canting.been.MainPageBean;
import com.zhongchuang.canting.been.MessageGroup;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.OrderType;
import com.zhongchuang.canting.been.Param;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.ProductBuy;
import com.zhongchuang.canting.been.ProductDel;
import com.zhongchuang.canting.been.Profit;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.been.SIGN;
import com.zhongchuang.canting.been.ShopBean;
import com.zhongchuang.canting.been.ShopCarBean;
import com.zhongchuang.canting.been.ShopChildBean;
import com.zhongchuang.canting.been.ShopFirstBean;
import com.zhongchuang.canting.been.ShopHeaderBean;
import com.zhongchuang.canting.been.ShopTypeBean;
import com.zhongchuang.canting.been.Smgapply;
import com.zhongchuang.canting.been.SureOrder;
import com.zhongchuang.canting.been.TOKEN;
import com.zhongchuang.canting.been.USER;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.been.UserInfoBean;
import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.been.TencentSing;
import com.zhongchuang.canting.been.Version;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.been.VideoDatas;
import com.zhongchuang.canting.been.VideoMoreData;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.been.ZhiBo_ZhuboBean;
import com.zhongchuang.canting.been.ZhuBo_Live_Start;
import com.zhongchuang.canting.been.ZhuBo_Live_Stop;
import com.zhongchuang.canting.been.aliLive;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.been.pay.WpParam;
import com.zhongchuang.canting.been.pay.alipay;
import com.zhongchuang.canting.been.videobean;
import com.zhongchuang.canting.easeui.bean.GROUP;


import java.util.List;
import java.util.Map;

import internal.org.apache.http.entity.mime.content.ContentBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/11/11.
 */

public interface netService {

    //    public static final String LOCAL_BASE_URL = "http://192.168.50.88:8080/cck/";
//    public static final String LOCAL_BASE_URL = "http://120.78.196.53:8780/ifun/";
//    public static final String LOCAL_BASE_URL = "http://111.230.248.224:8780/ifun/";
//    public static final String LOCAL_BASE_URL = "http://47.74.189.52:8080/ifun/";
//        public static final String LOCAL_BASE_URL = "http://119.23.235.1:8080/ifun/";
//        public static final String LOCAL_BASE_URL = "http://120.77.222.116:8080/ifun/";


//    public static final String LOCAL_BASE_URL = "http://192.168.50.193:8780/ifun/";
//    public static final String LOCAL_BASE_URL = "http://192.168.50.79:8780/ifun/";
//    public static final String LOCAL_BASE_URL = "http://192.168.50.28:8080/ifun/";
    //    public static final String LOCAL_BASE_URL = "http://192.168.50.193:8080/ifun/";


    //public static final String TOM_BASE_URL = "http://120.77.34.77:8080/cck/";

    //public static final String TOM_BASE_URL = "http://16535j5e29.51mypc.cn/cck/";

//    public static final String TOM_BASE_URL = "http://120.78.196.53:8080/cck/";

    //    public static final String TOM_BASE_URL = "http://192.168.50.88:8080/cck/";
//    public static final String TOM_BASE_URL = "http://120.78.196.53:8780/ifun/";
//    public static final String TOM_BASE_URL = "http://111.230.248.224:8780/ifun/";
//    public static final String TOM_BASE_URL = "http://47.74.189.52:8080/ifun/";
//      public static final String TOM_BASE_URL = "http://119.23.235.1:8080/ifun/";
//    public static final String TOM_BASE_URL = "http://120.77.222.116:8080/ifun/";



//    public static final String TOM_BASE_URL = "http://120.78.148.31:8080/ifun/";//替换数字时代服务器
//    public static final String BASE_URL = "http://120.78.148.31:8080";//替换数字时代服务器

//    public static final String TOM_BASE_URL = "https://ifun.xjxlsy.cn/portal/";//替换数字时代服务器
//    public static final String BASE_URL = "https://ifun.xjxlsy.cn/portal";//替换数字时代服务器


//    public static final String TOM_BASE_URL = "http://120.78.148.31:8080/ifun/";//替换数字时代服务器
//    public static final String BASE_URL = "http://120.78.148.31:8080";//替换数字时代服务器
    public static final String TOM_BASE_URL = "https://ifun.xjxlsy.cn/portal/";//替换数字时代服务器
    public static final String BASE_URL = "https://ifun.xjxlsy.cn/portal/";//替换数字时代服务器
//

//    public static final String TOM_BASE_URL = "http://119.23.212.8:8080/ifun/";
//    public static final String TOM_BASE_URL = "http://119.23.212.8:8080/ifun/";
//    public static final String TOM_BASE_URL = "http://47.107.249.69:8080/ifun/";

//    public static final String TOM_BASE_URL = "http://192.168.3.39:8080/ifun/";

//    public static final String TOM_BASE_URL = "http://192.168.50.28:8080/ifun/";

//    public static final String TOM_BASE_URL = "http://119.23.235.1:8080/ifun/";


//    public static final String TOM_BASE_URL = "http://119.23.235.1:8089/ifun/";
//    public static final String TOM_BASE_URL = "http://119.23.235.1:8080/ifun/";

//     public static final String TOM_BASE_URL = "http://192.168.0.101:8080/ifun/";

//    public static final String TOM_BASE_URL = "http://192.168.50.193:8780/ifun/";
//    public static final String TOM_BASE_URL = "http://192.168.50.79:8780/ifun/";
//    public static final String TOM_BASE_URL = "http://192.168.50.28:8080/ifun/";
//    public static final String TOM_BASE_URL = "http://192.168.50.193:8080/ifun/";


    //0
    //图片上传
    String UPDATE_PHOTO_URL = "common/upload/images";


    //1
    //号码判断
    String REGIST_CHECK_URL = "wap/sysSmsLog/send";


    //2
    //CODE 验证
    String CODE_CHECK_URL = "wap/register/checkCode";


    //手机号码、密码提交
    String REGISTPHPW_URL = "wap/userInfo/savePwd";

    //用户登录
    String LOGIN_URL = "wap/login";
    String CODE_LOGIN = "wap/userLogin/login";


    //主播开播
    String LIVEZHUBO_URL = "web/userInfo/isDirectSeed";


    //观众端收看
    String LIVEGUANZHONG_URL = "wap/dirRoomInfo/getDirRoomList";

    //主播上线通知   废弃
    String ONLIVE_URL = "web/dirRoomInfo/setOnline";

    //主播下线通知   废弃
    String STOPLIVE_URL = "web/dirRoomInfo/setOffline";

    //即时通讯签名,暂缓后续处理
    String USERSING_URL = "web/sign/generation";

    //添加好友
    String FRIEND_URL = "web/chatDirectories/getChatUserList";


    //游戏列表地址
    String GAME_URL = "wap/gameList/getGameInfoList";

    //通讯录
    String DIREC_URL = "web/chatDirectories/getChatDirectoriesList";


    //游戏下载与开启地址
    String GAME_DOWN_URL = "wap/gameList/getGameInfo";

    //修改个人信息
    String CHANAGE_PERSONINFO_URL = "wap/userInfo/update";

    //主页banner获取
    String MAIN_BANNER_URL = "wap/sysHomeInfo/list";


    //--------------------------------------------------------------------------------------------------------------------
    //商城模块

    //商城首页分类
    String SHOP_TYPE_URL = "wap/goodsCla/getGoodClass";
    //推荐列表头部
    String SHOP_HEADER_URL = "wap/goodsInfo/getGoodInfo";
    //推荐列表获取
    String CREOMMOND_LIST_URL = "wap/goodsInfo/getGoodInfoIndex";
    //分类所属列表获取
    String SHOP_LIST_URL = "wap/goodsInfo/getGoodsListByType";
    //商品详情
    String GOODS_DETAIL_URL = "wap/goodsInfo/getInfoByGoodsId";
    //购物车列表
    String SHOPCAR_LIST_URL = "wap/goodsCart/getGoodsCartByUser";
    //添加倒购物车
    String SHOPCAR_ADD_URL = "wap/goodsCart/addGoodsCart";
    //删除倒购物车
    String SHOPCAR_DELETE_URL = "wap/goodsCart/delGoodsCartByKey";
    //地址列表
    String ADDRESS_LIST_URL = "wap/goodsUserAd/getAddress";
    //新郑地址
    String ADDRESS_ADD_URL = "wap/goodsUserAd/addAdress";
    //修改地址
    String ADDRESS_EDIT_URL = "wap/goodsUserAd/updateAdress";
    //删除地址
    String ADDRESS_DELETE_URL = "wap/goodsUserAd/delUserAd";
    //设置默认地址
    String ADDRESS_DEFAULT_URL = "wap/goodsUserAd/updateStateAdress";

    //购物车提交订单 确定订单支付
    String SUBMIT_ORDER_URL = "goods/goodsOrder/buyFromCartGoodsOrder";

    //立即购买
    String SUBMIT_NOW_GOODS_ORDER_URL = "goods/goodsOrder/buyNowGoodsOrder";

    //13 返回商品规格信息
    String GOODS_SPE_CATE_URL = "goods/goodsSpeCate/getGoodsCate";

    //14 微信支付
    String GOODS_WX_PAY = "WechatPay/getWeiPayOrder";


    //@Headers("Accept-Encoding : application/json;charset=UTF-8")
//    @FormUrlEncoded
    @GET(MAIN_BANNER_URL)
    Call<MainPageBean> getBannerList(@Query("app") String v);

    @GET("wap/directType/getDirRoomClassify")
    Call<GAME> getDirRoomClassify();

    //1
    @FormUrlEncoded
    @POST(REGIST_CHECK_URL)
    Call<BaseResponse> getCall(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/dirRoomInfo/getDirectRoomList")
    Call<ZhiBo_GuanZhongBean> getDirectRoomList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/liveCategory/getDefaultVideoAndCategory")
    Call<VideoData> getDefaultVideoAndCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/video/getRecomdVideoList")
    Call<VideoDatas> getRecomdVideoList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/directCategory/getDirectAndCategory")
    Call<VideoData> getDefaultLiveAndCategory(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/video/getLatestVideoList")
    Call<ZhiBo_GuanZhongBean> getLatestVideoList(@FieldMap Map<String, String> map);

    //2
    @FormUrlEncoded
    @POST(CODE_CHECK_URL)
    Call<CodeCheckBean> getCode(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(REGISTPHPW_URL)
    Call<BaseResponse> getPassWord(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userLogin/login")
    Call<UserLoginBean> getLoginMess(@Field("loginType") String type, @Field("loginIdentifier") String phone, @Field("loginCredential") String password, @Field("companyType") String companyType);

    @FormUrlEncoded
    @POST(CODE_LOGIN)
    Call<UserLoginBean> getCodeLogin(@Field("loginType") String type, @Field("loginIdentifier") String phone, @Field("loginCredential") String password, @Field("companyType") String companyType);


    @FormUrlEncoded
    @POST(LIVEZHUBO_URL)
    Call<ZhiBo_ZhuboBean> getLiverMess(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(LIVEGUANZHONG_URL)
    Call<ZhiBo_GuanZhongBean> getLiverRoom(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ONLIVE_URL)
    Call<ZhuBo_Live_Start> getLiverStart(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(STOPLIVE_URL)
    Call<ZhuBo_Live_Stop> getLiverStop(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(USERSING_URL)
    Call<TencentSing> getUserSing(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(FRIEND_URL)
    Call<FriendSearchBean> getFriendMessage(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GAME_URL)
    Call<GameBean> getClearGameMess(@FieldMap Map<String, Integer> map);

    @FormUrlEncoded
    @POST(DIREC_URL)
    Call<FriendListBean> getChatDirectoriesList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GAME_DOWN_URL)
    Call<GameDownBean> getGameDown(@FieldMap Map<String, String> map);



    @FormUrlEncoded
    @POST(CHANAGE_PERSONINFO_URL)
    Call<BaseResponse> changePersonInfo(@FieldMap Map<String, String> map);


    //-------------------------------------------商城模块-----------------------------------------------
    @FormUrlEncoded
    @POST(SHOP_TYPE_URL)
    Call<ShopTypeBean> getShopType(@Field("app") String app);

    @FormUrlEncoded
    @POST(SHOP_HEADER_URL)
    Call<ShopHeaderBean> getShopFirstHeaderData(@Field("app") String str);

    @FormUrlEncoded
    @POST(CREOMMOND_LIST_URL)
    Call<ShopFirstBean> getShopFirstListData(@Field("app") String str);

    @FormUrlEncoded
    @POST(SHOP_LIST_URL)
    Call<ShopChildBean> getShopListData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GOODS_DETAIL_URL)
    Call<GoodsDetailBean> getGoodsDetail(@Field("goodsId") String goodId);

    @FormUrlEncoded
    @POST(SHOPCAR_LIST_URL)
    Call<ShopCarBean> getShopCarList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(SHOPCAR_ADD_URL)
    Call<BaseResponse> addShopCar(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(SHOPCAR_DELETE_URL)
    Call<BaseResponse> deleteShopCar(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ADDRESS_LIST_URL)
    Call<AddressBase> getAddressList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ADDRESS_ADD_URL)
    Call<BaseResponse> addAddress(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST(ADDRESS_EDIT_URL)
    Call<BaseResponse> editAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ADDRESS_DELETE_URL)
    Call<BaseResponse> deleteAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(ADDRESS_DEFAULT_URL)
    Call<BaseResponse> defaultAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(SUBMIT_ORDER_URL)
    Call<WEIXINREQ> submitOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(SUBMIT_NOW_GOODS_ORDER_URL)
    Call<SureOrder> submitNowGoodsOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GOODS_SPE_CATE_URL)
    Call<GoodsSpeCate> getGoosSpeCate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GOODS_WX_PAY)
    Call<WEIXINREQ> getpayInfo(@FieldMap Map<String, String> map);

    //-------------------------------------------美食-----------------------------------------------
    @FormUrlEncoded
    @POST("wap/foodIndex/getIndexInfo")
    Call<FoodShopBean> getFoodInfo(@FieldMap Map<String, String> map);

    //关注商店
    @FormUrlEncoded
    @POST("wap/cateShop/collectionShop")
    Call<BaseResponse> cateShop(@FieldMap Map<String, String> map);

    //商店详细
    @FormUrlEncoded
    @POST("wap/cateShop/getShopInfo")
    Call<BusinssBean> getShopInfo(@FieldMap Map<String, String> map);

    //8.	立即评价
    @FormUrlEncoded
    @POST("wap/CateOrder/commentNow")
    Call<FoodShopBean> getCommentNow(@FieldMap Map<String, String> map);

    //商品评价列表
    @FormUrlEncoded
    @POST("wap/cateShop/getShopComment")
    Call<BusinssBean> getCommentList(@FieldMap Map<String, String> map);

    //查询商店菜品
    @FormUrlEncoded
    @POST("wap/cateShop/getCateGreensListByMsg")
    Call<BaseResponse> getShopFood(@FieldMap Map<String, String> map);

    //我的所有订单
    @FormUrlEncoded
    @POST("wap/CateOrder/getUserOrderList")
    Call<FoodOrderBean> getAllOrder(@FieldMap Map<String, String> map);

    //再来一单
    @FormUrlEncoded
    @POST("wap/CateOrder/buyAgain")
    Call<BaseBean> getBuyAgain(@FieldMap Map<String, String> map);

    //美食到购物车
    @FormUrlEncoded
    @POST("wap/CateOrder/addCateCart")
    Call<BaseResponse> addCateCart(@FieldMap Map<String, String> map);

    //购物车中删除
    @FormUrlEncoded
    @POST("wap/CateOrder/deleteCateCart")
    Call<BaseResponse> deleteCateCart(@FieldMap Map<String, String> map);

    //生成订单并调用支付
    @FormUrlEncoded
    @POST("wap/CateOrder/creatFoodOrder")
    Call<WEIXINREQ> creatFoodOrer(@FieldMap Map<String, String> map);

    //删除订单
    @FormUrlEncoded
    @POST("wap/CateOrder/delFoodsOrder")
    Call<FoodShopBean> delFooodOrder(@FieldMap Map<String, String> map);

    //12.	删除订单
    @FormUrlEncoded
    @POST("wap/CateOrder/cancelFoodsOrder")
    Call<FoodShopBean> cancelFoodOrder(@FieldMap Map<String, String> map);


    //上传图片
    @FormUrlEncoded
    @POST("common/upload/image")
    Call<BaseBean1> upPhotos(@FieldMap Map<String, ContentBody> map);

    //13.	获取用户信息
    @FormUrlEncoded
    @POST("web/ListOfFriends/deleteFriends")
    Call<BaseResponse> delFriend(@FieldMap Map<String, String> map);

    //13.	获取用户信息
    @FormUrlEncoded
    @POST("wap/userInfo/getUserInfo")
    Call<UserInfo> getUserInfo(@FieldMap Map<String, String> map);


    //13.	获取用户信息
    @FormUrlEncoded
    @POST("wap/chatGroupsDirectories/createGroups")
    Call<BaseBean> createGroups(@FieldMap Map<String, String> map);

    //13.	获取用户信息
    @FormUrlEncoded
    @POST("web/chatDirectories/upUserGroup")
    Call<BaseResponse> upUserGroup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(GOODS_WX_PAY)
    Call<WEIXINREQ> getPayCharge(@FieldMap Map<String, String> map);

    //13.	群列表
    @FormUrlEncoded
    @POST("wap/chatGroupsListDirectories/listGroupsMenber")
    Call<GROUP> getGroupList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/chatGroupsMenberDirectories/selectGroupsMenber")
    Call<USER> getGroupInfo(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/chatGroupsAddUserDirectories/addUserGroupsMenber")
    Call<BaseResponse> addFriendList(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/chatGroupsRemoveDirectories/removeGroupsMenber")
    Call<BaseResponse> delFriendList(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/chatGroupsDeleteGroupsDirectories/deleteGroupsMenber")
    Call<BaseResponse> deleteGroup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/deposit")
    Call<BaseResponse> deposit(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/userIntegral/updateIntegral")
    Call<BaseResponse> updateIntegral(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/getExchandeTail")
    Call<INTEGRALIST> getExchandeTail(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/userInfo/recharge")
    Call<WEIXINREQ> recharge(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/dirRoomInfo/upRoomState")
    Call<BaseResponse> upRoomState(@FieldMap Map<String, String> map);

    //
    @FormUrlEncoded
    @POST("wap/sign/generation")
    Call<SIGN> generation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/userRoomAtt/addAttention")
    Call<BaseResponse> addAttention(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/userRoomAtt/cancelAttention")
    Call<BaseResponse> cancelAttention(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userInfo/isDirectSeed")
    Call<LIVEBEAN> isDirectSeed(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/dirRoomGiftListInfo/listGiftRoom")
    Call<GIFTDATA> listGiftRoom(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/userInfo/logoutDirect")
    Call<BaseResponse> logoutDirect(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/dirRoomInfo/getDirIndexInfo")
    Call<BEAN> getDirIndexInfo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/dirRoomSendGiftInfo/sendGiftForGirl")
    Call<BaseBean> sendGiftForGirl(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userInfo/exchangeGlod")
    Call<BEAN> exchangeGlod(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/getIntegral")
    Call<BEAN> getIntegral(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userInfo/rechargeGlod")
    Call<WEIXINREQ> exchangeWx(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userAuths/updatePwd")
    Call<BaseResponse> updatePwd(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/userInfo/checkIsExist")
    Call<BaseResponse> checkIsExist(@FieldMap Map<String, String> map);

    /**
     * ------------------------------------------j积分----------------------------------------
     */
    @FormUrlEncoded
    @POST("wap/integral/getIntegralGoods")
    Call<INTEGRALIST> getIntegralGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/chatDetail/getChatIntegralDetail")
    Call<INTEGRALIST> getChatIntegralDetail(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/addIntegralGoods")
    Call<BaseResponse> addIntegralGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/integral/getIntegralPlatform")
    Call<INTEGRAL> getIntegralPlatform(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/getUserFromMobile")
    Call<BEAN> getUserFromMobile(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/getUserIntegralGoods")
    Call<INTEGRALIST> getUserIntegralGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/integralAttorn")
    Call<BaseResponse> integralAttorn(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/chatDetail/addChatDetail")
    Call<BaseResponse> addChatDetail(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/getUserIntegralLog")
    Call<INTEGRALIST> getUserIntegralLog(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/integralDetails")
    Call<INTEGRALIST> integralDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/profit/getProfitList")
    Call<Profit> getProfitList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/deleteIntegralGoods")
    Call<BaseResponse> deleteIntegralGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/integralToGlod")
    Call<BaseResponse> integralToGlod(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/rechargeIntegral")
    Call<WEIXINREQ> addOrderUserRechargeIntegral(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("web/integral/buyIntegralGoods")
    Call<WEIXINREQ> buyIntegralGoods(@FieldMap Map<String, String> map);

    /**
     * 商城接口
     */
    @FormUrlEncoded
    @POST("wap/homePage/getCategoryList")
    Call<Home> getBanner(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/Internationa/setLanguge")
    Call<BaseResponse> setLanguge(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/homePage/getProductList")
    Call<Product> getProductList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/homePage/getActivityProductList")
    Call<Product> getActivityProductList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/adminSearch/getProductBySecondName")
    Call<Product> getProductBySecondName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroup/groupSort")
    Call<BaseResponse> groupSort(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/adminSearch/seaContentList")
    Call<Home> seaContentList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/adminSearch/clearSearch")
    Call<BaseResponse> clearSearch(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/adminSearch/getAllCateList")
    Call<Catage> getAllCateList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/adminSearch/getSecondList")
    Call<Catage> getSecondList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/homePage/getProDetail")
    Call<ProductDel> getProDetail(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/homePage/getProudctSku")
    Call<ProductBuy> getProudctSku(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/homePage/getProParameter")
    Call<Param> getProParameter(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/shopCart/addToCart")
    Call<BaseResponse> addToCart(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/shopCart/getMyShopCart")
    Call<Product> getMyShopCart(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/shopCart/deletProduct")
    Call<BaseResponse> deletProduct(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userAddress/getUserAddress")
    Call<AddressBase> getUserAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userAddress/alterDefaultAddress")
    Call<BaseResponse> alterDefaultAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userAddress/addUserAddress")
    Call<BaseResponse> addUserAddress(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/shopFavorite/addShop")
    Call<BaseResponse> addShop(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userAddress/deleteAdress")
    Call<BaseResponse> deleteAdress(@FieldMap Map<String, String> map);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("wap/order/accountMoney")
    Call<OrderData> accountMoney(@Body OrderParam body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("http://api.huifengshengwu.cn/api/user/shoppingGive")
    Call<BaseResponse> shoppingGive(@Body WpParam body);



    @GET("http://api.huifengshengwu.cn/api/user/getPoints")
    Call<BaseResponse> getPoints(@Query("phone") String phone);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("http://api.huifengshengwu.cn/api/user/shoppingCut")
    Call<BaseResponse> shoppingCut(@Body WpParam body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("wap/order/submitOrder")
    Call<WEIXINREQ> submitOrder(@Body OrderParam body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("wap/order/submitOrder")
    Call<BaseBean> submitOrderPal(@Body OrderParam body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("wap/order/submitOrder")
    Call<alipay> submitOrders(@Body OrderParam body);

    @FormUrlEncoded
    @POST("wap/homePage/getShopById")
    Call<ShopBean> getShopById(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/orderManage/userInfo")
    Call<OrderType> userInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userLogin/appPay")
    Call<alipay> appPay(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userLogin/appPay")
    Call<WEIXINREQ> appPays(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/alipayIntegral")
    Call<alipay> rechargeInteger(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/alipayIntegral")
    Call<WEIXINREQ> rechargeIntegers(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/userIntegral/alipayIntegral")
    Call<BaseBean> rechargeIntegerss(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/orderManage/orderList")
    Call<OrderData> favoriteList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/orderManage/orderDetails")
    Call<OrderData> orderDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/shopFavorite/favoriteList")
    Call<Favor> shoFavorite(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/orderManage/receiptGoods")
    Call<BaseResponse> receiptGoods(@FieldMap Map<String, String> map);


//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("wap/orderManage/cancelOrder")
//    Call<BaseResponse> cancelOrder(@Body CancelParam body);

    @FormUrlEncoded
    @POST("wap/orderManage/deleteOrder")
    Call<BaseResponse> deleteOrder(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/orderManage/cancelOrder")
    Call<BaseResponse> cancelOrder(@FieldMap Map<String, String> map);

    @GET("wap/app/getVersionAndUrl")
    Call<Version> getVersionAndUrl();

    @FormUrlEncoded
    @POST("wap/application/appList")
    Call<apply> appList(@FieldMap Map<String, String> map);


    @GET("https://bbsc.2aa6.com/api/data/apps")
    Call<List<Smgapply>> appSmgList();

    @FormUrlEncoded
    @POST("wap/chatGroup/getFrendList")
    Call<FriendListBean> getFrendList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroup/addGroup")
    Call<BaseResponse> addGroup(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroupsDirectories/updateGroupsName")
    Call<BaseResponse> updateGroupsName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroupsDirectories/updateOwnerName")
    Call<BaseResponse> updateOwnerName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroup/alterGroupName")
    Call<BaseResponse> alterGroupName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroup/alterGroupImage")
    Call<BaseResponse> alterGroupImage(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/chatGroup/deleteGroup")
    Call<BaseResponse> deleteGroups(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("wap/submitPaypal/success")
    Call<BaseResponse> success(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/submitPaypal/integerSuccess")
    Call<BaseResponse> integerSuccess(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wap/direct/hostInfo")
    Call<Host> hostInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/focus/focusList")
    Call<Care> focusList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/focus/upFavoriteType")
    Call<BaseResponse> upFavoriteType(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/focus/anchorsList")
    Call<Cares> anchorsList(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/dirRoomInfo/focusTV")
    Call<BaseResponse> focusTV(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/focus/getHostdirHostList")
    Call<Hand> getHostdirHostList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/focus/getRankingList")
    Call<Hands> getRankingList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/chatDetail/getChatDetailListForTime")
    Call<Hand> getChatDetailListForTime(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/chatDetail/getChatDetailForTimeSearch")
    Call<Hand> getChatDetailForTimeSearch(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/direct/upRoomInfo")
    Call<BaseResponse> upRoomInfo(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/chatGroup/getChatGroupList")
    Call<GAME> getChatGroupList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatBanner/banner")
    Call<Banner> getBanners(@FieldMap Map<String, String> opt);

    @GET("wap/qiNiu/getUpToken")
    Call<TOKEN> getUpToken();


    @FormUrlEncoded
    @POST("wap/dirRoomInfo/getDirectRoomInfo")
    Call<Hands> getDirectRoomInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/app/getCode")
    Call<Codes> getCodes(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/app/uploadCode")
    Call<BaseResponse> uploadCode(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/focus/getGiftDetailedList")
    Call<Hands> getGiftDetailedList(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/userIntegral/getUserIntegral")
    Call<Ingegebean> getUserIntegral(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/userIntegral/updateJewelIntegral")
    Call<Ingegebean> updateJewelIntegral(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/userIntegral/recordIntegralDetails")
    Call<INTEGRALIST> recordIntegralDetails(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("web/friendCircles/getFriendCirclesList")
    Call<QfriendBean> getFriendCirclesList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/information/getUserInformation")
    Call<UserInfoBean> getUserInformation(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/information/saveInformation")
    Call<BaseResponse> saveInformation(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/getFriendCirclesDetail")
    Call<QfriendBean> getFriendById(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/addInfo")
    Call<BaseResponse> addInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/addFriendComment")
    Call<QfriendBean> addFriendComment(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/changeFriendLike")
    Call<QfriendBean> changeFriendLike(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/delFriendComment")
    Call<QfriendBean> delFriendComment(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/delFriendCircle")
    Call<BaseResponse> delFriendCircle(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("web/friendCircles/getCircleImage")
    Call<BaseBe> getCircleImage(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatFriend/friendInfo")
    Call<FriendInfo> friendInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatGroupsListDirectories/selectGroupsInfo")
    Call<FriendInfo> selectGroupsInfo(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/chatRemark/addRemark")
    Call<BaseResponse> addRemark(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/password/setPaymentPassword")
    Call<BaseResponse> setPaymentPassword(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/password/alterPaymentPassword")
    Call<BaseResponse> alterPaymentPassword(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/password/verifyPassword")
    Call<BaseResponse> verifyPassword(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/password/payCheckCode")
    Call<BaseResponse> payCheckCode(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/redEnvelope/send")
    Call<RedInfo> sendRed(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/redDetail/getLuckInfo")
    Call<RedInfo> getLuckInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/redDetail/getDetails")
    Call<GrapRedDetail> getDetails(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/redEnvelope/grab")
    Call<GrapRed> redGrab(@FieldMap Map<String, String> opt);

    /**
     * 阿里直播相关接口
     */

    @FormUrlEncoded
    @POST("wap/course/pushUrl")
    Call<aliLive> getPushUrl(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveRecord/addConfig")
    Call<aliLive> addConfig(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveRecord/setLiveNotifyUrl")
    Call<aliLive> setLiveNotifyUrl(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/liveRecord/updateType")
    Call<BaseResponse> updateType(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/directCategory/updateCategory")
    Call<BaseResponse> updateCategory(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/liveRecordVod/addLiveRecordVod")
    Call<aliLive> addLiveRecordVod(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/course/liveUrl")
    Call<aliLive> getLiveUrl(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/course/getLiveToken")
    Call<aliLive> getLiveToken(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/video/uploadVideo")
    Call<BaseResponse> uploadVideo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/video/getVideoList")
    Call<videobean> getVideoList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/video/updateVideoType")
    Call<BaseResponse> updateVideoType(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/video/deleteVideo")
    Call<BaseResponse> delVideo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveRecord/deleteConfig")
    Call<BaseResponse> deleteConfig(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatrooms/delete")
    Call<BaseResponse> deleteRoom(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/chatrooms/create")
    Call<aliLive> create(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/chatrooms/getChatRoomInfo")
    Call<aliLive> getChatRoomInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveCategory/getFirstCategoryList")
    Call<LiveItemBean> getFirstCategoryList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/directCategory/getDirectListByThirdid")
    Call<VideoMoreData> getDirectListByThirdid(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveCategory/getVideoListByThirdid")
    Call<VideoMoreData> getVideoListByThirdid(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveCategory/searchVideoByNameOrCategory")
    Call<VideoMoreData> searchVideoByNameOrCategory(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/directCategory/searchDirectByNameOrCategory")
    Call<VideoMoreData> searchDirectByNameOrCategory(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/directCategory/getHotDirect")
    Call<VideoData> getHotDirect(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/sysHomeInfo/getAppInfo")
    Call<AppInfo> getAppInfo(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/liveCategory/getSecondList")
    Call<LiveItemBean> getSecondLists(@FieldMap Map<String, String> opt);


    @FormUrlEncoded
    @POST("wap/liveCategory/getThirdList")
    Call<LiveItemBean> getThirdList(@FieldMap Map<String, String> opt);

    @FormUrlEncoded
    @POST("wap/directCategory/getLiveCategory")
    Call<LiveTypeBean> getLiveCategory(@FieldMap Map<String, String> opt);


}
