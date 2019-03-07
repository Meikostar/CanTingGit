package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9.
 */

public class CommetLikeBean extends BaseResponse {
    public CommetLikeBean data;
    public String id;
    public String orderCode;
    public int by_user_id;
    public List<GoodsComentBean> list;
    public String comment;// (string, optional): 评论内容 ,

    public String create_time_;// (string, optional): 评论时间 ,
    public String english_name;// (string, optional): 评论时间 ,

    public int goods_ ;//(integer, optional): 商品id ,

    public int id_ ;//(integer, optional): 主键 ,
    public int to_user_id ;//(integer, optional): 主键 ,
    public int dynamic_comment_time ;//(integer, optional): 主键 ,
    public int dynamic_id ;//(integer, optional): 主键 ,
    public int u_e_id ;//(integer, optional): 主键 ,
    public long re_add_time ;//(integer, optional): 主键 ,

    public int u_d_c_id ;//(integer, optional): 主键 ,

    public String user_avatar;// (string, optional): 用户昵称 ,

    public int reply_complaint_id;// (integer, optional): 父id ,

    public int user_id ;//(integer, optional): 用户id

    public String by_user_name ;//(string, optional): 被回复昵称
    public String name ;//(string, optional): 被回复昵称
    public String from_uname ;//(string, optional): 被回复昵称
    public String from_nickname ;//(string, optional): 被回复昵称
    public String user_dynamic_comment ;//(string, optional): 被回复昵称
    public String from_uid ;//(string, optional): 被回复昵称
    public String topic_id ;//(string, optional): 被回复昵称
    public String to_uid ;//(string, optional): 被回复昵称
    public String content ;//(string, optional): 被回复昵称
    public String to_uname ;//(string, optional): 被回复昵称
    public String from_uimage ;//(string, optional): 被回复昵称



}
