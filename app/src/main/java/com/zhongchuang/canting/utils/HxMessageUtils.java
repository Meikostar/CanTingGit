package com.zhongchuang.canting.utils;

import android.text.TextUtils;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.bean.GROUPS;
import com.zhongchuang.canting.easeui.bean.USER;
import com.zhongchuang.canting.easeui.db.UserInfoCacheSvc;
import com.zhongchuang.canting.install.Utils;


/**
 * Created by linquandong on 16/12/17.
 */
public class HxMessageUtils {

//    hx_avater 发送人的头像,\n
//    hx_name 姓名, hx_uid user_id
//    hx_gid 群组中需要群id hx_gname 群名
//    hx_gavater 群头像数组用,分开
//    , hx_fuid接受人的user_id
//    hx_fname hx_favater 同理
    //发送给谁
    public static final String AVATER = "hx_avater";
    public static final String NAME = "hx_name";
    public static final String UID = "hx_uid";


    public static final String GID = "hx_gid";
    public static final String ORDER_ID = "order_id";

    public static final String GNAME = "hx_gname";
    public static final String GNICK= "hx_gnick";

    public static final String GAVATER = "hx_gavater";
    public static final String FAVATER = "hx_favater";
    public static final String FUID = "hx_fuid";
    public static final String FNAME = "hx_fname";
    public static final String ID_TYPE = "id_type"; //0:表示是老师默认值，1：表示是陌生人


    public static EMMessage exMsg(EMMessage message, CHATMESSAGE chatmessage) {
        if (chatmessage == null) {
            return message;
        }
        try {

            //用户信息\
            String avar = SpUtil.getAvar(CanTingAppLication.getInstance());
            message.setAttribute(AVATER, avar);
            message.setAttribute(NAME,TextUtil.isEmpty( SpUtil.getName(CanTingAppLication.getInstance()))?SpUtil.getNick(CanTingAppLication.getInstance()): SpUtil.getName(CanTingAppLication.getInstance()));
            message.setAttribute(UID, SpUtil.getUserInfoId(CanTingAppLication.getInstance()));

            //用户注册所选择的身份
//            message.setAttribute(TYPEID, LoginUtils.getUserType());

            if (chatmessage.getUserinfo() != null) {
                message.setAttribute(FAVATER, chatmessage.getUserinfo().hx_favater);
                message.setAttribute(FNAME, chatmessage.getUserinfo().hx_fname);
                message.setAttribute(FUID, chatmessage.getUserinfo().hx_fuid);
                message.setAttribute(ORDER_ID, chatmessage.getUserinfo().order_id);
            }
//            message.setAttribute(ID_TYPE,chatmessage.getUserinfo().id_type);
            GROUPS groups = chatmessage.getGroup();
            if (groups != null) {

                message.setAttribute(GAVATER, groups.getGavatar());
                message.setAttribute(GNAME, groups.getGroupname());
                message.setAttribute(GNICK, SpUtil.getNick(CanTingAppLication.getInstance()));
                message.setAttribute(GID, groups.getGroup_id());
//                String nick = ShareDataManager.getInstance().getPara(MaKarApplication.getInstance(), groups.getGroup_id() + "");
//                if(TextUtil.isNotEmpty(nick)){
//                    message.setAttribute(NAME, nick);
//                }
                //群中设置的身份id
//                if (groups.getUser_groupinfo().getUser_type() == 0) {
                // message.setAttribute(TYPEID, groups.getUser_groupinfo().getUserinfo()
                //    .user_type);
//                } else {
//                }

//                //设置群昵称
//                if (!TextUtils.isEmpty(groups.getUser_groupinfo().getNickname())) {
//                    message.setAttribute(NAME, groups.getUser_groupinfo().getNickname());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }


    public static EMMessage getIdCardMsg(USER fuser, GROUPS groups, USER cradUser) {
        EMMessage msg = null;
        if (fuser != null) {
            msg = EMMessage.createTxtSendMessage("[名片]", fuser.easemob);
        } else if (groups != null) {
            msg = EMMessage.createTxtSendMessage("[名片]", groups.getEasemob_group_id());
            msg.setChatType(EMMessage.ChatType.GroupChat);
        }
        CHATMESSAGE ctmessage = new CHATMESSAGE();
        ctmessage.setGroup(groups);
        ctmessage.setUserinfo(fuser);
        HxMessageUtils.exMsg(msg, ctmessage);
        EMClient.getInstance().chatManager().sendMessage(msg);
        return msg;
    }

    /**
     * 当前的头像，在聊天页面中展示
     */
    public static String getMyAvater(EMMessage msg) {
        return msg.getStringAttribute(AVATER, "");
    }


    /**
     * 当前的名字，在聊天页面中展示
     */
    public static String getMyName(EMMessage msg) {
        boolean mySelf = isMySelf(msg);
        if (!mySelf) {
            USER user = UserInfoCacheSvc.getByChatUserName(msg.getFrom());
            if (user != null && !TextUtils.isEmpty(user.friend_nickname)) {
                return user.friend_nickname;
            }
        }
        return msg.getStringAttribute(NAME, "");
    }

    /**
     * 当前的id，在聊天页面中展示
     */
    public static String getMyUid(EMMessage msg) {
        return msg.getStringAttribute(UID, "");
    }




    /**================在群组聊天页面中展示的=========================*/
    /**
     * 当前群组头像，在聊天页面中展示，适用于群组聊天
     */
    public static String getGroupAvater(EMMessage msg) {
        return msg.getStringAttribute(GAVATER, "");
    }
    public static String getOrderId(EMMessage msg) {
        return msg.getStringAttribute(ORDER_ID, "");
    }
    /**
     * 当前群组名字，在聊天页面中展示，适用于群组聊天
     */
    public static String getGroupName(EMMessage msg) {
        return msg.getStringAttribute(GNAME, "");
    }

    public static String getNick(EMMessage msg) {
        return msg.getStringAttribute(GNICK, "");
    }
    /**================在会话列表中展示的=========================*/
    /**
     * 对方的名字，在会话列表中展示
     */
    public static String getFName(EMMessage msg) {
        if (isMySelf(msg)) {
            return msg.getStringAttribute(FNAME, "meiko");
        } else {
            return msg.getStringAttribute(NAME, "meiko");
        }
    }

    public static EMMessage updateName(EMMessage msg,String name){
        if (isMySelf(msg)) {
            msg.setAttribute(FNAME,name);

        } else {
            msg.setAttribute(NAME,name);
        }

        return msg;
    }
    /**
     * 对方的头像，在会话列表中展示
     */
    public static String getFAvater(EMMessage msg) {
        if (isMySelf(msg)) {
            return msg.getStringAttribute(FAVATER, "");
        } else {
            return msg.getStringAttribute(AVATER, "");
        }
    }

    /**
     * 对方的id，在会话列表中展示
     */
    public static String getFUid(EMMessage msg) {
        if (isMySelf(msg)) {
            return msg.getStringAttribute(FUID, "");
        } else {
            return msg.getStringAttribute(UID, "");
        }
    }

    /**
     * 获取群组信息
     * @param msg
     * @return
     */
    public static String getGroupId(EMMessage msg) {
        return msg.getStringAttribute(GID,"");
    }
    /**
     * 判断是否是自己发送的
     */
    public static boolean isMySelf(EMMessage msg) {
        String uid = msg.getStringAttribute(UID, "");
        return uid.equals(SpUtil.getUserInfoId(CanTingAppLication.getInstance()));
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String id, String username, TextView textView) {
        if (textView != null) {
            USER user = UserInfoCacheSvc.getById(id);
            if (user != null && !TextUtils.isEmpty(user.friend_nickname)) {
                textView.setText(user.friend_nickname);
            } else {
                textView.setText(username);
            }
        }
    }

    public static void sendCmdMessage(String toUsername, String action, int chatType, CHATMESSAGE
            chatmessage) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMsg = HxMessageUtils.exMsg(cmdMsg, chatmessage);
//        String toUsername = null;//发送给某个人
//        String action = null;//action可以自定义
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            //支持单聊和群聊，默认单聊，如果是群聊添加下面这行
            cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
//            toUsername = chatmessage.getGroup().getEasemob_group_id();
//            action = chatmessage.getGroup().getGid() + "," + HxMessageUtils
//                    .getMyUid
//                            (message) + "," + Constant
//                    .POSITIONGING_INVITATION;
        }
//        else {
//            action = "," + chatmessage.getUserinfo().user_id + "," + Constant
//                    .POSITIONGING_INVITATION;
//            toUsername = message.getFrom();//发送给某个人
//        }
//        cmdMsg.setReceipt(toUsername);
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    /***
     * 拼接邀请信息
     *
     * @param message
     * @param inviter
     * @return
     * @pachatmessage
     */
    public static EMMessage exInvitMsg(EMMessage message, String inviter, CHATMESSAGE chatmessage) {
        if (chatmessage == null) {
            return message;
        }
        try {
            for (int i = 0; i < chatmessage.getGroup_user().size(); i++) {
                USER user = chatmessage.getGroup_user().get(i).getUserinfo();
                if (user.easemob.equals(inviter)) {
                    //用户信息
                    message.setAttribute(AVATER, user.user_avatar);
                    message.setAttribute(NAME, user.user_name);
                    message.setAttribute(UID, user.user_id);
//                    message.setAttribute(LOCAL, LoginUtils.getisLocation());
                    //用户注册所选择的身份
//                    message.setAttribute(TYPEID, LoginUtils.getUserType());
                    break;
                }
            }

            USER user = chatmessage.getUserinfo();
            if (user != null) {
                message.setAttribute(FAVATER, user.user_avatar);
                message.setAttribute(FNAME, user.user_name);
                message.setAttribute(FUID, user.user_id);
            }
            GROUPS groups = chatmessage.getGroup();
            if (groups != null) {
                String gavater = Utils.getGroupImgStr(groups
                        .getGroup_img());
                message.setAttribute(GAVATER, gavater);
                message.setAttribute(GNAME, groups.getGroupname());
                //群中设置的身份id

                //设置群昵称
                if (!TextUtils.isEmpty(groups.getUser_groupinfo().getNickname())) {
                    message.setAttribute(NAME, groups.getUser_groupinfo().getNickname());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;

    }



}
