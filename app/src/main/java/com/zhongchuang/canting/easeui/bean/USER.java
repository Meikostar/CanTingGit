package com.zhongchuang.canting.easeui.bean;


import com.zhongchuang.canting.install.Utils;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by linquandong on 15/8/7.
 */
public class USER implements Serializable {

    public static final int APPLYING = 1;
    public static final int APPLYSUC = 2;
    public static final int APPLYFAIL = 3;

    //public USER userinfo;
    public String imagePath;
    /**
     * uid : 11849
     * nickname :
     * actual_name :
     * id_card :
     * id_card_img :
     * id_card_status : 0
     * old_card :
     * openid :
     * mobile : 13580129695
     * gender : 男
     * avatar :
     * password : 4c3e01a9191235920da4057c50ec0ecf
     * email :
     * integral : 0
     * integral_history : null
     * fanli : 100000
     * fanli_history : null
     * province :
     * city :
     * address :
     * reg_time : 0
     * session_salt :
     * height : 0
     * weight : 0
     * age : 0
     * skin :
     * integral_sum : 0
     * devicetoken : 0
     * os : 0
     * unread_commentsum : 0
     * unread_replysum : 0
     * is_z_user : 0
     * is_deleted : 0
     * employees_id : 0
     * department :
     * employees :
     * group : 0
     * parent_id : 0
     * age_shop : 1
     * province_id : 0
     * city_id : 0
     * user_sn :
     * sn_prefix :
     * power : 4
     * region :
     * login_time : 0
     * source : 0
     * card_amount : 0
     * card_quantity : 0
     * undo_remarks :
     * remark :
     * managed_accounts : null
     * is_vip : null
     * securities_integral : 0
     * member_integral : 0
     * seed : tugp
     * power_name : null
     * parent_name : 冯程
     * city_name : null
     * province_name : null
     * order_goods1 : 2
     * order_goods2 : 0
     * order_goods3 : 0
     * order_goods4 : 0
     * bank_count : 1
     * bond_count : 0
     */
    public String hx_fuid ;
    public String hx_favater ;
    public String hx_fname ;
    public int uid;
    public String nickname;
    public int id_card_status; //0:未处理
    public String mobile;
    public String account;
    public String avatar;
    public String password;
    public double integral;//积分
    public String city;
    public String address;
    public int os;
    public int group;
    public int is_friend;
    public String filter;
    public int source;
    public int user_class;
    public int is_state;

    public int is_vip;
    public int order_id;
    public String name;
    public String remarks;
    public String english_name;
    public String city_name;
    public String province_name;
    public String hx_username;


    public int user_id;
    public String user_avatar;
    public String user_name;
    public String easemob;
    public int user_sex;
    public int user_type;
    public int province_id;
    public int city_id;
    public int global_positioning;
    public String province;
    public int is_request;
    public int requests_status;
    public boolean isChoose;
    public int status;
    public String friend_nickname;
    public String license_plate;
    public String self_numbering;

    //验证身分或者是获取params
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
//        if (imagePath != null && !imagePath.equals("")) {
//            File file = new File(imagePath);
//            params.put("avatar", file);
//        }
//        params.put("actual_name", actual_name);
//        params.put("id_card", id_card);// 1.头像 2.身份证
//        params.put("province", province);
//        params.put("gender", gender);
//        params.put("id_card_status", id_card_status);
        if (imagePath != null && !imagePath.equals("")) {
            File file = new File(imagePath);
            params.put("avatar", file);
        }
        params.put("name", name);
        params.put("sex", user_sex);
        params.put("global_positioning", global_positioning);
        params.put("province_id", province_id);
        params.put("city_id", city_id);
        params.put("province", province);
        params.put("city", city);
        params.put("user_type", user_type);
        params.put("license_plate", license_plate);
        params.put("self_numbering", self_numbering);
        return params;
    }


    public String getPalce() {
        String palce = province_name + city_name;
        return palce.equals("") ? "未知地" : palce;
    }

    public String getCardStatus() {
        //0未提交审核，1审核中，2审核成功，3审核失败
        String str = "未提交审核";
        switch (id_card_status) {
            case APPLYING:
                str = "审核中";
                break;
            case APPLYSUC:
                str = "审核成功";
                break;
            case APPLYFAIL:
                str = "审核失败";
                break;
        }
        return str;
    }

    public String getName() {
        if (nickname != null && !nickname.equals("")) {
            return nickname;
        }
        return "未设置账户名(" + mobile + ")";

    }

    public String getVipDes() {
        if (is_vip == 0) {
            return "会员";
        } else {
            return "VIP";
        }
    }

    protected String initialLetter;


    public String getInitialLetter() {
        if (initialLetter == null) {
            Utils.setUserInitialLetter(this);
        }
        return filter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    /**
     * 重写 equals方法
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof USER) {
            USER otherUser = (USER) object;
            return this.user_id == otherUser.user_id;
        }
        return false;
    }
}
