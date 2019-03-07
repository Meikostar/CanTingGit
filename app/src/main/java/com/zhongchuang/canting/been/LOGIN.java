package com.zhongchuang.canting.been;


import android.text.TextUtils;

/**
 * Created by mykar on 17/4/24.
 */
public class LOGIN {
//    "user_id":"34",
//            "prefix":"+86",
//            "account":"18739355172",
//            "add_time":"1496305982",
//            "status":"0",
//            "is_show":"0",
//            "session_salt":"1496634137",
//            "user_class":"1",
//            "freeze_time":"0",
//            "currency":"1",
//            "rate":"1.00",
//            "is_state":"0",
//            "accept_num":"0",
//            "integral":"0.00",
//            "score":"0",
//            "srore_num":"0",
//            "hx_username":"1873935517234",
//            "pai_time":"0",
//            "order_num":"0",
//            "es_num":"0"
//    "role":"5",//角色，1验货员，2验货主管，3下单员，4管理员，5独立验货员
//            "name":"\u6e23\u6e23",//名称
//            "english_name":"zhazha",//英文名
//            "user_avatar":"http:\/\/120.76.129.214\/eqc\/Uploads\/app\/img\/2017-05-31\/592e66a9dc6c4.jpg",//头像
//            "qr_code":"http:\/\/www.eqc.com\/app\/user\/code?id=26",//二维码
//            "department":"",//部门
//            "duties":""//职务
//}"accept_num":"0",//接单数
    private String prefix;
    private String account;
    private String name;
    private String english_name;
    private String session_salt;
    private String hx_username;
    private String user_avatar;
    private String qr_code;
    private String department;
    private String duties;
    private String remarks;

    private int user_id;
    private long freeze_time;
    private long add_time;
    private long pai_time;
    private int status;
    private int is_state;
    private int accept_num;
    private int order_num;
    private int score;
    private int role;
    private int srore_num;
    private int es_num;
    private double rate;
    private double integral;
    private int is_show;
    private int currency;
    private int user_class;
    private int invite_code;
    public int name_status;
    public int company_status;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        if(!TextUtils.isEmpty(english_name)){
            return english_name;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getSession_salt() {
        return session_salt;
    }

    public void setSession_salt(String session_salt) {
        this.session_salt = session_salt;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getFreeze_time() {
        return freeze_time;
    }

    public void setFreeze_time(long freeze_time) {
        this.freeze_time = freeze_time;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getUser_class() {
        return user_class;
    }

    public void setUser_class(int user_class) {
        this.user_class = user_class;
    }

    public int getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(int invite_code) {
        this.invite_code = invite_code;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public long getPai_time() {
        return pai_time;
    }

    public void setPai_time(long pai_time) {
        this.pai_time = pai_time;
    }

    public int getIs_state() {
        return is_state;
    }

    public void setIs_state(int is_state) {
        this.is_state = is_state;
    }

    public int getAccept_num() {
        return accept_num;
    }

    public void setAccept_num(int accept_num) {
        this.accept_num = accept_num;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSrore_num() {
        return srore_num;
    }

    public void setSrore_num(int srore_num) {
        this.srore_num = srore_num;
    }

    public int getEs_num() {
        return es_num;
    }

    public void setEs_num(int es_num) {
        this.es_num = es_num;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
