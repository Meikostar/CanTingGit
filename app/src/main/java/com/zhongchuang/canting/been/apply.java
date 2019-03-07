package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class apply extends BaseResponse{
//        appTypeName": "每日推荐",
//                "appList": [
//        {
//                "application_image_url": "http://pa7efx2i6.bkt.clouddn.com/1532941575930fangtianxia.png",
//                "create_time": 1532941754000,
//                "application_image_type": "每日推荐",
//                "application_image_name": "房天下",
//                "id": 6
//        }

        public List<apply> data;
        public List<apply> appList;

        public String appTypeName;
        public String application_address;
        public String application_image_url;
        public String category_img;
        public String application_image_type;
        public String application_image_name;
        public String id;
        public long create_time;

}
