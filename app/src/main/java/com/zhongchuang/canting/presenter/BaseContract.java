package com.zhongchuang.canting.presenter;


import com.zhongchuang.canting.been.CancelParam;
import com.zhongchuang.canting.been.OrderParam;
import com.zhongchuang.canting.been.Params;

import java.util.List;

public class BaseContract {
    public interface View extends BaseView {

        //        <T> void toList(List<T> list, int type, int... refreshType);
        <T> void toEntity(T entity, int type);

        void toNextStep(int type);

        void showTomast(String msg);
    }

    public interface Presenter {
        /**
         * 3.红包流水接口
         * @param pageNum
         * @param type
         * @param state
         */

        void getDetails(String pageNum	,String startime,String type,final  int state);


        void redGrab(String redEnvelopeId	,String sendType);

        /**
         * 查看领取详情接口
         * @param redEnvelopeId
         */
        void getLuckInfo(String redEnvelopeId);
        /**
         * 获取轮播图片
         *
         * @param type
         */
        void getHomeBanner(String type);
        /**
         * 获取轮播图片
         *
         * @param type
         */
        void getHomeBanners(String type);

        /**
         * 获取商品列表
         *
         * @param type
         */
        void getProductList(int type, String pageNum, String pageSize, String searchInfo, String proSite, String searchType, String sortType);

        /**
         * 获取商品列表S
         *
         * @param type
         */
        void getProductBySecondName(final int type, String pageNum, String pageSize, String secondCategoryName, String proSite);

        /**
         * 获取热搜和历史搜索
         *
         * @param
         */
        void seaContentList();

        /**
         * 清空用户历史搜索列表
         *
         * @param
         */
        void clearSearch();

        /**
         * 一级二级列表-接口
         *
         * @param
         */
        void getAllCateList();

        /**
         * 一级分类获取二级分类列表
         *
         * @param
         */
        void getSecondList(String id);


        /**
         * 获取商品详情-接口
         *
         * @param
         */
        void getProDetail(String id);

        /**
         * 击加入购物车按钮获取产品的sku-接口
         *
         * @param
         */
        void getProudctSku(String productPlatformId);

        /**
         * 获取产品的参数
         *
         * @param
         */
        void getProParameter(String productSkuId);

        /**
         * 添加商品进购物车确定按钮-接口
         *
         * @param
         */
        void addToCart(String shopId, String productPlatformId, String number, String proSite, String productSkuId);


        /**
         * 获取我的购物车-接口
         *
         * @param
         */
        void getMyShopCart(final int type, String pageNum, String pageSize, String proSite);


        /**
         * 删除购物车商品-接口
         *
         * @param
         */
        void deletProduct(String productSkuId);

        /**
         * 获取用户收货地址-接口
         *
         * @param
         */
        void getUserAddress(String type);

        /**
         * 获取用户收货地址-接口
         *
         * @param
         */
        void alterDefaultAddress(String addressId);

        /**
         * 新增用户收货地址-接口
         *
         * @param
         */

        void addUserAddress(String addressId, String shippingName, String linKNumber, String shippingAddress, String detailedAddress, String isDefault);

        /**
         * 收藏店铺-接口
         *
         * @param
         */
        void addShop(String shopId, String type);


        /**
         * 用户删除收货地址-接口
         *
         * @param
         */
        void deleteAdress(String addressId, String addressType);

        /**
         * 用户删除收货地址-接口
         *
         * @param
         */
        void accountMoney(OrderParam proList);


        void submitOrder(OrderParam proList);


        /**
         * 商家店铺-接口
         *
         * @param
         */
        void getShopById(String mId);

        /**
         * 获取用户的订单列表各个状态的数量和用户账户积分代币余额-接口
         *
         * @param
         */
        void userInfo();

        /**
         * 获取订单列表-接口
         *
         * @param
         */
        void favoriteList(String pageNum, String pageSize, String orderType);

        /**
         * 获取订单详情-接口
         *
         * @param
         */
        void orderDetails(String orderId);

        /**
         * 获取我的收藏列表-接口
         *
         * @param
         */
        void shoFavorite();

        /**
         * 确认收货-接口
         *
         * @param
         */
        void receiptGoods(String orderType, String orderId);

        /**
         * 删除订单-接口
         *
         * @param
         */
        void deleteOrder(String orderId);

        /**
         * 删除订单-接口
         *
         * @param
         */
        void appList();

        /**
         * 获取直播分类
         *
         * @param
         */
        void getDirRoomClassify();


        void hostInfo( );

        void focusList(String roomInfoId,String favoriteType );

        void upFavoriteType(String roomInfoId,String favoriteType,String anchorsId  );

        void anchorsList( );

        void focusTV(String roomInfoId,String names,String type );

        void getHostdirHostList();

        void getRankingList(String pageNum,String roomInfoId,String type,String startDate,String endDate,final int loadtype );

        void upRoomInfo(String directSeeName,String leaveMassege,String roomImage,String directOverview );

        void getDirectRoomInfo();

        void getGiftDetailedList(String pageNum, final int loadtype);

        void cancelOrder(List<Params> cancelList);

        void getVersionAndUrl();

        void submitOrders(OrderParam proList);

        void submitOrderpal(OrderParam proList);
        void rechargeInteger(String integralNumber,String payType);
        void rechargeIntegers(String integralNumber,String payType);
        void rechargeIntegerss(String integralNumber,String payType);
        void success(String paymentId,String orderCode) ;
        void integerSuccess(String  orderId);
        void getChatGroupList() ;
        void getFrendList(String groupId);
        void addGroup(String chatGroupName,String img);

        void groupSort(String sortType	,String id);
        void getBanners(String bannerSite);
        void deleteGroups(String id);
        void alterGroupName(String chatGroupName,String id,String alterGroupName);
        void updateOwnerName(String userGroupName,String groupsId);
        void updateGroupsName(String groupsName,String groupsId);
        void addChatDetail(String friendId,String chatType,String stringStartTime,String stringEndTime);
        void getChatDetailListForTime( String chatType, String timeType);
        void getChatDetailForTimeSearch( String chatType, String stringStartTime,String stringEndTime);
        void upUserGroup(String chatUserId, String groupName, String groupId,String type);
        void alterGroupImage(String id,String groupBackImage);
        void deposit(String number,String type);
        void getUserIntegral();
        void recordIntegralDetails(final int type, String pageNum,  String integralSite, String types, String stringStartTime, String stringEndTime);
        void integralDetails(final int type, String pageNum,  String integralSite, String types, String stringStartTime, String stringEndTime,String pageSize);
        void getFriendCirclesList(final int type, String pageNum,  String Id);
        void getFriendData(final int type, String pageNum,  String Id);

        void  addInfo(String postInfo, String postImage);
        void  changeFriendLike(String topicId, String type );
        void  addFriendComment(String topicId, String toUid,String fromUid,String content );

        void  delFriendComment(String topicId, String sendId,String fromUid,String id );

        void delFriendCircle(String topicId, String sendId);
        void getCircleImage(String id);
        void getFriendById( String Id);
        void friendInfo(String friendsId);
        void uploadCode(String type,String codeUrl) ;
        void getCodes(final String type) ;
        void addRemark(String friendsId, String remarkName,String remarkPhone, String remarkMessage);
        void getActivityProductList(final int type, String pageNum, String pageSize, String searchInfo, String proSite, String productType);
        void verifyPassword(String paymentPassword);

        void setLanguge(String languge);

        /**
         * 增加直播录制转点播配置接口
         */
        void addLiveRecordVod();
        /**
         * 增加直播录制配置接口
         */
        void addConfig(String coverImage	,String videoName);

        /**
         * 获取视频列表接口
         */
        void getVideoList(final int type, String pageNum);

        /**
         * 5.删除直播录制配置接口
         */
        void deleteConfig();
        /**
         *设置推流断流回调地址接口
         */
        void setLiveNotifyUrl();
        /**
         *1.创建聊天室
         */
        void create();
        /**
         *1.获取首页最新的视频列表接口
         */
        void getLatestVideoList( String pageNum, final int type);
    }
}
