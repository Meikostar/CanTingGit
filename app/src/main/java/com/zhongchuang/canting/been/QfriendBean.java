package com.zhongchuang.canting.been;




import java.io.Serializable;
import java.util.List;

/***
 * 功能描述:今日新款Item
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/

public class QfriendBean extends BaseResponse{
//    list": [
//    {
//        "dynamic_id": "1",
//            "dynamic": "动态内容",
//            "user_id": "10",
//            "dynamic_time": "1493106826",
//            "praise_num": "0",
//            "browse_num": "0",
//            "comment_num": "1",
//            "user_avatar": "345432",
//            "english_name": "ww",
//            "name": "王五",
//            "img": [
//        "https://www.baidu.com/img/bd_logo1.png",
//                "http://www.eqc.com/Upload/App/Order/createOrder/2017-05-08/59105fd862e29.jpeg",
//                "http://www.eqc.com/Upload/App/Order/createOrder/2017-05-08/59105fd862e29.jpeg",
//                "http://www.eqc.com/Upload/App/Order/createOrder/2017-05-08/59105fd862e29.jpeg",
//                "http://www.eqc.com/Upload/App/Order/createOrder/2017-05-08/59105fd862e29.jpeg"
//        ],
//        "comment": [
//        {
//            "u_d_c_id": "1",
//                "dynamic_id": "1",
//                "by_user_id": "1",
//                "user_dynamic_comment": "评论动态",
//                "dynamic_comment_time": "1493107182",
//                "to_user_id": "0",
//                "user_name": "",
//                "user_avatar": "http://120.76.129.214/eqc/Uploads/app//2017-05-17/591ba8161ed44.jpg",
//                "english_name": "ghfjdsk",
//                "name": "erewsdf"
//        }
//        ]
//    }
//    ],
//            "count": "1"

    public final static int TYPE_IMAGE = 0;
    public int type;
    public int order_id;
    public int dynamic_id;
    public int user_id;
    public int praise_num;
    public int browse_num;
    public int comment_num;
    public int many_day;
    public int u_d_c_id	;
    public int count;
    public int to_user_id;
    public int is_praise;
    public int order_type;
    public int by_user_id;
    public long create_time;
    public List<CommetLikeBean> commentList;
    public List<QfriendBean> data;
    public List<CommetLikeBean> likeList;
    public long dynamic_time;
    public long praise_time;

    public long dynamic_comment_time;

    public boolean isChoose;
    public boolean is_report;
    public String order_sn;
    public String id;
    public QfriendBean friendCircles;
    public String topic_id;
    public String user_info_id;
    public String post_info;
    public String address;
    public String english_name;
    public String name;
    public String post_image;

    public String user_dynamic_comment;
    public String head_image;
    public String user_name;//chalufei
    public String subsidies;//buzhu

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMany_day() {
        return many_day;
    }

    public void setMany_day(int many_day) {
        this.many_day = many_day;
    }

    public static int getTypeImage() {
        return TYPE_IMAGE;
    }

    public int getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(int is_praise) {
        this.is_praise = is_praise;
    }

    public long getPraise_time() {
        return praise_time;
    }

    public void setPraise_time(long praise_time) {
        this.praise_time = praise_time;
    }

    public List<CommetLikeBean> getPraise_name() {
        return likeList;
    }

    public void setPraise_name(List<CommetLikeBean> praise_name) {
        this.likeList = praise_name;
    }


    public int getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(int dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPraise_num() {
        return praise_num;
    }

    public void setPraise_num(int praise_num) {
        this.praise_num = praise_num;
    }

    public int getBrowse_num() {
        return browse_num;
    }

    public void setBrowse_num(int browse_num) {
        this.browse_num = browse_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getU_d_c_id() {
        return u_d_c_id;
    }

    public void setU_d_c_id(int u_d_c_id) {
        this.u_d_c_id = u_d_c_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
    }


    public int getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(int by_user_id) {
        this.by_user_id = by_user_id;
    }

    public long getDynamic_time() {
        return dynamic_time;
    }

    public void setDynamic_time(long dynamic_time) {
        this.dynamic_time = dynamic_time;
    }

    public long getDynamic_comment_time() {
        return dynamic_comment_time;
    }

    public void setDynamic_comment_time(long dynamic_comment_time) {
        this.dynamic_comment_time = dynamic_comment_time;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean is_report() {
        return is_report;
    }

    public void setIs_report(boolean is_report) {
        this.is_report = is_report;
    }

    public String getDynamic() {
        return post_info;
    }

    public void setDynamic(String dynamic) {
        this.post_info = dynamic;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }



    public String getImg() {
        return post_image;
    }

    public void setImg(String img) {
        this.post_image = img;
    }

    public String getUser_dynamic_comment() {
        return user_dynamic_comment;
    }

    public void setUser_dynamic_comment(String user_dynamic_comment) {
        this.user_dynamic_comment = user_dynamic_comment;
    }

    public String getUser_avatar() {
        return head_image;
    }

    public void setUser_avatar(String user_avatar) {
        this.head_image = user_avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSubsidies() {
        return subsidies;
    }

    public void setSubsidies(String subsidies) {
        this.subsidies = subsidies;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public long getService_start_date() {
        return create_time;
    }

    public void setService_start_date(long service_start_date) {
        this.create_time = service_start_date;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
