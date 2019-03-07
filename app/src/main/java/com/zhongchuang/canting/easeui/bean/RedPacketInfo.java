package com.zhongchuang.canting.easeui.bean;




import java.io.Serializable;

public class RedPacketInfo implements Serializable {
    public String send_jf = "";
    public String type = "";//1 表示发 2,表示已领取
    public int isSend =0;//表示是发还是抢  1
    public int isAll =0;//是否抢完  1
    public String grap_id = "";//领红包的id
    public String send_red_name = "";
    public String red_name = "";//领红包的人
    public String send_cout = "";
    public String send_name = "";//发红包的人
    public String leavMessage = "";
    public String sendType = "";//表示发群里还个人
    public String groupId = "";
    public String userInfoId = "";
    public String redEnvelopeId = "";//红包id
    public int number = 1;
    public int isgrab = 0;//1表示已抢

}

