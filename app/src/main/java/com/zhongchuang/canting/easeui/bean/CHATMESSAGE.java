package com.zhongchuang.canting.easeui.bean;


import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.utils.Utils;
import com.zhongchuang.canting.utils.HxMessageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CHATMESSAGE implements Serializable {

    private USER userinfo;
    public boolean admin;
    private GROUPS group;
    private List<GROUP_USER> group_user;

    public GROUPS getGroup() {
        return group;
    }

    public void setGroup(GROUPS group) {
        this.group = group;
    }

    public List<GROUP_USER> getGroup_user() {
        return group_user;
    }

    public void setGroup_user(List<GROUP_USER> group_user) {
        this.group_user = group_user;
    }

    public USER getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(USER userinfo) {
        this.userinfo = userinfo;
    }

    public static CHATMESSAGE fromLogin(UserInfo loginuser) {
        CHATMESSAGE chatmessage = new CHATMESSAGE();
        chatmessage.userinfo = new USER();
        chatmessage.userinfo.hx_favater = loginuser.getHeadImage();

        if(!TextUtils.isEmpty(loginuser.getNickname())){
            chatmessage.userinfo.hx_fname = loginuser.getNickname();
        }

        chatmessage.userinfo.hx_fuid = loginuser.getAccountId()+"";
        return chatmessage;
    }

    public static CHATMESSAGE fromLogin(FriendInfo loginuser) {
        CHATMESSAGE chatmessage = new CHATMESSAGE();
        chatmessage.userinfo = new USER();
        chatmessage.userinfo.hx_favater = loginuser.head_image;


        if(!TextUtils.isEmpty(loginuser.nickname)) {
            chatmessage.userinfo.hx_fname = loginuser.nickname;
        }
        if(!TextUtils.isEmpty(loginuser.remark_name)){
            chatmessage.userinfo.hx_fname = loginuser.remark_name;
        }

        chatmessage.userinfo.hx_fuid = loginuser.friendsId+"";
        return chatmessage;
    }

    public static CHATMESSAGE fromConversation(EMConversation conversation) {
        EMMessage msg = conversation.getLastMessage();
        if(msg == null){
            return null;
        }
        CHATMESSAGE chatmessage = new CHATMESSAGE();
        if(conversation.getType() == EMConversation.EMConversationType.GroupChat){
            chatmessage.group = new GROUPS();
            chatmessage.group.setGroup_id(HxMessageUtils.getGroupId(msg));
            chatmessage.group.setGroup_img_str(HxMessageUtils.getGroupAvater(msg));
            chatmessage.group.setGroupname(HxMessageUtils.getGroupName(msg));
            chatmessage.group.setNickname(HxMessageUtils.getNick(msg));

        }else{
            chatmessage.userinfo = new USER();
            chatmessage.userinfo.hx_favater = HxMessageUtils.getFAvater(msg);
            chatmessage.userinfo.hx_fname = HxMessageUtils.getFName(msg);
            chatmessage.userinfo.hx_fuid = HxMessageUtils.getFUid(msg);
        }

        return chatmessage;
    }

    public static CHATMESSAGE fromGroup(GROUP item) {
        if(item == null){
            return null;
        }
        CHATMESSAGE chatmessage = new CHATMESSAGE();
        chatmessage.group = new GROUPS();
        chatmessage.admin = item.admin;
        chatmessage.group.setGroup_id(item.groupid+"");
        chatmessage.group.setGroup_img_str(Utils.getGroupImgStr((ArrayList<String>) item.headimage));
        chatmessage.group.setGroupname(item.groupname);
        return chatmessage;
    }
    public static CHATMESSAGE fromGroup(EMGroup item) {
        if(item == null){
            return null;
        }
        CHATMESSAGE chatmessage = new CHATMESSAGE();
        chatmessage.group = new GROUPS();
        chatmessage.group.setGroup_id(item.getGroupId()+"");
        chatmessage.group.setGroup_img_str(Utils.getGroupImgStr((ArrayList<String>) item.getMuteList()));
        chatmessage.group.setGroupname(item.getGroupName());
        return chatmessage;
    }
}
