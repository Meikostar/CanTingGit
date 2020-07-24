package com.zhongchuang.canting.been;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class Smgapply extends BaseResponse {
    //        appTypeName": "每日推荐",
//                "appList": [
//        {
//                "application_image_url": "http://pa7efx2i6.bkt.clouddn.com/1532941575930fangtianxia.png",
//                "create_time": 1532941754000,
//                "application_image_type": "每日推荐",
//                "application_image_name": "房天下",
//                "id": 6
//        }
    public List<Smgapply> data;
    public List<Smgapply> list;
    public List<Smgapply> apps;

    public String title;
    public String img;
    public String type;
    public String image_url;
    public String application_image_url;
    public String category_img;
    public String url;
    public String application_image_type;
    public String application_image_name;
    public String id;

}
