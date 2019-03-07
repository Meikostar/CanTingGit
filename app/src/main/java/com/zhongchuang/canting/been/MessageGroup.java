package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class MessageGroup extends BaseResponse {
//        "create_by": "admin",  //系统默认的
//                "create_time": 1536201871000,
//                "chat_group_name": "家人",  //分组名称
//                "id": 1,                 //分组id
//                "sort_id": 1
    public List<MessageGroup> data;
    public String create_by;
    public long create_time;
    public String chat_group_name;
    public String id;
    public String sort_id;




}
