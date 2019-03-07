package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class BusinssBean extends BaseResponse implements Serializable {
// "isAttention": 0, //是否登录
//         "shopInfo": {
//        "shopInfoId": "cesh945612034093875200", //商店信息id
//                "userInfoId": null,   //用户信息id
//                "shopName": "商店一号",  //商店名称
//                "shopAddress": "广东省深圳市",  //商店地址
//                "shopTelNumber": "1342869000",  //商店电话号码
//                "distributionDec": "很慢",  //配送服务说明
//                "distributionTime": "2018",  //配送时间
//                "activityContent": null,  //活动内容
//                "shopLng": 40.1,
//                        "shopLat": 40.8,
//                        "isEnabled": false,  //是否启用
//                        "createTime": 1514286452000,
//                        "createTime": 1514286452000,
//                        "createBy": "admin",
//                        "cuisineName": null,  //菜系名称
//                        "shopCusSetId": null,  //商店菜系id
//                        "foodRecImageUrl": null, //食品档案图片地址
//                        "cuisineInfoId": null, //菜系信息id
//                        "foodRecId": null,  //食品档案id
//                        "enabled": false  //是否启用

    public BusinssBean shopInfo;
    public List<BusinssBean> cateCartlist;
    public List<BusinssBean> cateSgCatList;
    public List<CateCaBean> categreenList;
    public List<CommentBean> commentList;
    public String shopInfoId;
    public String userInfoId;
    public String shopName;
    public String shopAddress;
    public String shopTelNumber;
    public String shopCartId;
    public String distributionTime;
    public String shopLng;
    public String shopLat;
    public String isEnabled;
    public String createTime;
    public String createBy;
    public String greensInfoId;
    public String cuisineName;
    public String shopCusSetId;
    public String foodRecImageUrl;
    public String cuisineInfoId;
    public String foodRecId;
    public String shop_cart_id;
    public String shop_gre_set_id;
    public String user_info_id;
    public String categoryName;
    public String greCateId;

    public int taste;
    public int packing;
    public int distri;
    public boolean enabled;
    public boolean isChoose;

    public BusinssBean data;
    public int isAttention;



}
