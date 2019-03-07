package com.zhongchuang.canting.been;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FoodShopBean  extends BaseResponse implements Serializable {

    public List<FoodShopBean> shopList;
    public List<FoodShopBean> shopImg;
    public String food_rec_image_url;
    public String shop_info_id;
    public String shop_address;
    public String shop_name;
    public String shopImageUrl;

    public String activityId;
    public String shopInfoId;
    public String activityName;
    public String activityImage;
    public String link;
    public String serialNumber;
    public String createTime;
    public String modifyTime;
    public String createBy;
    public double begtime;
    public double endtime;
    public String isEnabled;

    public FoodShopBean data;
    public FoodShopBean cateActivity;
//"cateActivity": {
//        "activityId": "1",  //活动表id
//                "shopInfoId": "1",  //商店信息id
//                "activityName": "1",  //活动名称
//                "activityImage": "1",  //活动图片
//                "link": "1",		//活动链接
//                "serialNumber": 5,  //序号
//                "createTime": 1511774940000, 	//创建时间
//                "modifyTime": 1512984544000,	//修改时间
//                "createBy": "1",
//                "begtime": 1511861353000,  //开始时间
//                "endtime": 1512984555000,  //结束时间
//                "isEnabled": 0  //是否删除
//    },

}
