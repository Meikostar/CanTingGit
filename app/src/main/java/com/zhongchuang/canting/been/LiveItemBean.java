package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */

public class LiveItemBean extends BaseResponse{
//        appTypeName": "每日推荐",
//                "appList": [
//        {
//                "application_image_url": "http://pa7efx2i6.bkt.clouddn.com/1532941575930fangtianxia.png",
//                "create_time": 1532941754000,
//                "application_image_type": "每日推荐",
//                "application_image_name": "房天下",
//                "id": 6
//        }


        public List<LiveItemBean> data;


        public String category_image;
        public String category_name;
        public String stort_id;
        public String id;
        public String live_first_category_id;
        public String sec_category_name;

        public long create_time;

}
