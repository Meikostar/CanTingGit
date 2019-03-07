package com.zhongchuang.canting.been;

import java.util.List;

/***
 * 功能描述:商品评论
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/
public class GoodsComentBean {

//    public String name;
//
//    public String id;
//
//    public boolean isReplay;
//
//    public String toReplyName;
//
//    public String content;

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
    public int by_user_id ;//(integer, optional): 被回复id

    public String by_user_name ;//(string, optional): 被回复昵称
    public String name ;//(string, optional): 被回复昵称
    public String user_name ;//(string, optional): 被回复昵称
    public String user_dynamic_comment ;//(string, optional): 被回复昵称

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreate_time_() {
        return create_time_;
    }

    public void setCreate_time_(String create_time_) {
        this.create_time_ = create_time_;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public int getGoods_() {
        return goods_;
    }

    public void setGoods_(int goods_) {
        this.goods_ = goods_;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }

    public int getDynamic_comment_time() {
        return dynamic_comment_time;
    }

    public void setDynamic_comment_time(int dynamic_comment_time) {
        this.dynamic_comment_time = dynamic_comment_time;
    }

    public int getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(int dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public int getU_d_c_id() {
        return u_d_c_id;
    }

    public void setU_d_c_id(int u_d_c_id) {
        this.u_d_c_id = u_d_c_id;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public int getReply_complaint_id() {
        return reply_complaint_id;
    }

    public void setReply_complaint_id(int reply_complaint_id) {
        this.reply_complaint_id = reply_complaint_id;
    }

    public int getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(int by_user_id) {
        this.by_user_id = by_user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBy_user_name() {
        return by_user_name;
    }

    public void setBy_user_name(String by_user_name) {
        this.by_user_name = by_user_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_dynamic_comment() {
        return user_dynamic_comment;
    }

    public void setUser_dynamic_comment(String user_dynamic_comment) {
        this.user_dynamic_comment = user_dynamic_comment;
    }

    public List<GoodsComentBean> getList() {
        return list;
    }

    public void setList(List<GoodsComentBean> list) {
        this.list = list;
    }

    public int getU_e_id() {
        return u_e_id;
    }

    public void setU_e_id(int u_e_id) {
        this.u_e_id = u_e_id;
    }

    public long getRe_add_time() {
        return re_add_time;
    }

    public void setRe_add_time(long re_add_time) {
        this.re_add_time = re_add_time;
    }
}
