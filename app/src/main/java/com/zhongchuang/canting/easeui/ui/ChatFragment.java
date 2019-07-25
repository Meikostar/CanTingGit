package com.zhongchuang.canting.easeui.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aliyun.common.utils.ToastUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EasyUtils;
import com.hyphenate.util.PathUtil;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.PersonMessageActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.activity.mine.PersonManActivity;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.GrapRed;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.GROUPS;
import com.zhongchuang.canting.easeui.bean.RedPacketInfo;
import com.zhongchuang.canting.easeui.bean.RobotUser;
import com.zhongchuang.canting.easeui.ui.red.GabRedDetailActivity;
import com.zhongchuang.canting.easeui.ui.red.SendRedActivity;
import com.zhongchuang.canting.easeui.ui.red.SendRedsActivity;
import com.zhongchuang.canting.easeui.ui.red.WaitRedDetailActivity;
import com.zhongchuang.canting.easeui.utils.EaseCommonUtils;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.red.CustomDialog;
import com.zhongchuang.canting.widget.red.OnRedPacketDialogClickListener;
import com.zhongchuang.canting.widget.red.RedPacketViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper, BaseContract.View {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RED = 99;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
    private static final int ITEM_RED_PACKET = 16;
    //end of red packet code

    /**
     * if it is chatBot
     */
    private boolean isRobot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private BannerAdaptes bannerAdapter;
    private BasesPresenter presenter;

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();

        presenter = new BasesPresenter(this);
        presenter.getBanners(2 + "");


        bannerAdapter = new BannerAdaptes(getActivity());
        bannerView.setAdapter(bannerAdapter);
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {
//                Banner banner= (Banner) o;
//                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
//                intent.putExtra("id", banner.product_sku_id);
//                intent.putExtra("type", 2);
//                startActivity(intent);
            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });
//        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);

        if (chatType == Constant.CHATTYPE_SINGLE) {
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_red, R.drawable.chat_red_input, ITEM_RED_PACKET, extendMenuItemClickListener);
        } else if (chatType == Constant.CHATTYPE_GROUP) {
            inputMenu.registerExtendMenuItem(R.string.attach_red, R.drawable.chat_red_input, ITEM_RED_PACKET, extendMenuItemClickListener);
        }

        //end of red packet code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
//                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
//                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
//                    startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case ITEM_RED_PACKET:
                    if (data != null) {
//                //积分红包
                        RedPacketInfo redPacketInfo = (RedPacketInfo) data.getSerializableExtra("data");
                        redPacketInfo.send_name = SpUtil.getName(getActivity());
                        redPacketInfo.isgrab = 0;
                        redPacketInfo.isSend = 1;
                        redPacketInfo.send_red_name = "1";

                        EMMessage message = EaseCommonUtils.createExpressionRedMessage(toChatUsername, redPacketInfo);
                        sendMessages(message);
                    }
                    break;

                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                       String   ids = data.getStringExtra("userid");

                        if(TextUtil.isNotEmpty(userIds)){
                            String[] split = ids.split(",");
                            if(split!=null&&split.length>0){
                                for(String id:split){
                                    if(!userIds.contains(id)){
                                        userIds=userIds+","+id;
                                    }
                                }
                            }
                        }else {
                            userIds=ids;
                        }

                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getActivity(), GroupSetActivity.class);
            intent.putExtra("id", group_id);
            intent.putExtra("name", TextUtil.isNotEmpty(CanTingAppLication.GroupName) ? CanTingAppLication.GroupName : group.getGroupName());
            intent.putExtra("admin", mChatmsg.admin);
            startActivityForResult(intent,
                    REQUEST_CODE_GROUP_DETAIL);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
//            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }


    public void onAvatarClick(String user_id) {
        if (TextUtil.isNotEmpty(user_id) && (!user_id.equals("null")) ) {
            Intent intent = new Intent(getActivity(), PersonManActivity.class);
            intent.putExtra("id", user_id + "");
            startActivity(intent);
        }

    }

    @Override
    public void onAvatarLongClick(EMMessage  username) {
        if(username!=null){
            String userName = username.getUserName();
            if(TextUtil.isNotEmpty(userIds)){
                if(userIds.contains(username.getFrom())){
                    userIds=userIds+","+username.getFrom();
                }
            }else {
                userIds=username.getFrom();
            }
            inputAtUsername(username.getFrom());
        }

    }

    private String user_id;
    private String send_name;

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        String s = message.toString();
        id = message.getStringAttribute(EaseConstant.EXTRA_REDENVELID, null);
        user_id = message.getStringAttribute(EaseConstant.EXTRA_RED_USERID, null);
        send_name = message.getStringAttribute(EaseConstant.EXTRA_SEND, null);
        if (TextUtil.isNotEmpty(id)) {
            presenter.getLuckInfo(id);

        }
        if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false) || message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {

            if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                startVoiceCall();
            } else {
                startVideoCall();
            }
        }

        return false;
    }

    private View mRedPacketDialogView;
    private CustomDialog mRedPacketDialog;
    private RedPacketViewHolder mRedPacketViewHolder;

    public void showRedPacketDialog(final RedInfo entity) {
        if (mRedPacketDialogView == null) {
            mRedPacketDialogView = View.inflate(getActivity(), R.layout.chat_red_open_dailog, null);
            mRedPacketViewHolder = new RedPacketViewHolder(getActivity(), mRedPacketDialogView);
            mRedPacketViewHolder.setData(entity);
            mRedPacketDialog = new CustomDialog(getActivity(), mRedPacketDialogView, R.style.custom_dialog);
            mRedPacketDialog.setCancelable(false);
        }

//        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                presenter.redGrab(id, entity.chatType + "");
            }
        });

        mRedPacketDialog.show();
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

        showPopwindow(message);
        // no message forward when in chat room
//        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
//                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
//                REQUEST_CODE_CONTEXT_MENU);
    }

    private View views = null;
    private TextView tv_ch = null;
    private TextView tv_fz = null;
    private TextView tv_delete = null;


    public void showPopwindow(final EMMessage message) {

        views = View.inflate(getActivity(), R.layout.message_choose, null);
        tv_ch = views.findViewById(R.id.tv_ch);
        tv_fz = views.findViewById(R.id.tv_fz);
        tv_delete = views.findViewById(R.id.tv_delete);
       if(message.direct() == EMMessage.Direct.RECEIVE ){
           tv_ch.setVisibility(View.GONE);
       }
      String  type = message.getStringAttribute(EaseConstant.EXTRA_RED_TYPE, null);
       if(TextUtil.isNotEmpty(type)&&type.equals("2")){
           tv_ch.setVisibility(View.GONE);
       }
        if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)||message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
            tv_ch.setVisibility(View.GONE);
        }
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        tv_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showToast(getActivity(), "开发中");
                setRebackMessage(message);
                dialog.dismiss();
            }
        });
        tv_fz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = message.getBody().toString();
                if(TextUtil.isNotEmpty(body)){
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(body);
                    ToastUtil.showToast(getActivity(), getString(R.string.fzcg));
                }
                dialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
                conversation.removeMessage(message.getMsgId());
                if (isMessageListInited) {
                    messageList.refreshSelectLast();
                }
                dialog.dismiss();
            }
        });


    }

    public static final String NAME = "hx_name";
    public static final String CONTENT = "re_content";
    public static final String MSID = "re_msid";
    public static final String UID = "hx_uid";
    public static final String FUID = "hx_fuid";
    public static final String AVATER = "hx_avater";
    public static final String EXETEND = "rb_extend";
    public static final String GNAME = "hx_gname";
    public static final String GNICK= "hx_gnick";
    public static final String GID = "hx_gid";
    public static final String GAVATER = "hx_gavater";
    public void setRebackMessage(EMMessage msg){

        msg.setAttribute(EaseConstant.EXTRA_RED,true);
        msg.setAttribute(EaseConstant.EXTRA_RED_TYPE,"4");
        msg.setAttribute(EXETEND,"你撤回了一条消息");

        EMTextMessageBody var4 = new EMTextMessageBody("你撤回了一条消息");
        msg.addBody(var4);
        DemoHelper.getInstance().upDateMessage(msg);
        final EMMessage message = EMMessage.createTxtSendMessage("@@@###!!", toChatUsername);
        message.setAttribute("em_robot_message", true);
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
            GROUPS groups = mChatmsg.getGroup();
            message.setAttribute(GAVATER, groups.getGavatar());
            message.setAttribute(GNAME, groups.getGroupname());
            message.setAttribute(GNICK, SpUtil.getNick(CanTingAppLication.getInstance()));
            message.setAttribute(GID, toChatUsername);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        message.setAttribute(CONTENT, "\""+SpUtil.getName(getActivity())+"\""+"撤回了消息");
        message.setAttribute(MSID, msg.getMsgId());
        message.setAttribute(FUID, message.getTo());
        message.setAttribute(EaseConstant.EXTRA_RED,true);
        message.setAttribute(EaseConstant.EXTRA_RED_TYPE,"4");
        //用户信息\
        String avar = SpUtil.getAvar(CanTingAppLication.getInstance());
        message.setAttribute(AVATER, avar);
        message.setAttribute(NAME,TextUtil.isEmpty( SpUtil.getName(CanTingAppLication.getInstance()))?SpUtil.getNick(CanTingAppLication.getInstance()): SpUtil.getName(CanTingAppLication.getInstance()));
        message.setAttribute(UID, SpUtil.getUserInfoId(CanTingAppLication.getInstance()));

        EMClient.getInstance().chatManager().sendMessage(message);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                if (conv == null) {
                    return;
                }
                conv.removeMessage(message.getMsgId());
                if (isMessageListInited) {
                    messageList.refresh();
                }

            }
        }).start();

    }

    /**
     * 撤回
     * 原理：
     * A用户发送消息。
     * A用户需要撤回某条消息，将消息id通过扩展消息发送到用户B。
     * B用户收到扩展消息，解析其中的messageid，从数据库删除对应消息。
     */
    private void recall() {

//        sendTextMessage();
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
// 如果是群聊，设置chattype，默认是单聊
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            cmdMsg.setChatType(EMMessage.ChatType.ChatRoom);
        }

        messageList.refreshSelectLast();
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {

            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            //red packet code : 进入发红包页面
            case ITEM_RED_PACKET:

                if (chatType == Constant.CHATTYPE_GROUP) {
                    Intent intents = new Intent(getActivity(), SendRedsActivity.class);
                    intents.putExtra("id", group_id);
                    startActivityForResult(intents, ITEM_RED_PACKET);
                } else {
                    Intent intents = new Intent(getActivity(), SendRedActivity.class);
                    startActivityForResult(intents, ITEM_RED_PACKET);
                }


                break;
            //end of red packet code
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    private void sendMessages(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        EMMessage msg = HxMessageUtils.exMsg(message, mChatmsg);
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
//            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", TextUtil.isNotEmpty(mChatmsg.getUserinfo().hx_fname)?mChatmsg.getUserinfo().hx_fname:toChatUsername)
//                    .putExtra("isComingCall", false));
            Intent intent = new Intent(getActivity(), VoiceCallActivity.class);
            intent.putExtra("username", toChatUsername);

            intent.putExtra(EaseConstant.EXTRA_CHATMSG, mChatmsg);
            intent.putExtra("isComingCall", false);
            startActivity(intent);
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(getActivity(), VideoCallActivity.class);
            intent.putExtra("username", toChatUsername);
            intent.putExtra(EaseConstant.EXTRA_CHATMSG, mChatmsg);
            intent.putExtra("isComingCall", false);
            startActivity(intent);

            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        CanTingAppLication.GroupName = "";
    }

    private String userId;
    private String id;
    private RedInfo info;

    @Override
    public <T> void toEntity(T entity, int type) {
        userId = SpUtil.getUserInfoId(getActivity());

        if (type == 69) {
            info = (RedInfo) entity;
            if (info == null) {
                return;
            }
            if (TextUtil.isNotEmpty(user_id) && userId.equals(user_id)) {
                if (chatType == EaseConstant.CHATTYPE_GROUP) {
                    info.chatType = 1;
                    if (info.grabRedenvelope == 1) {
                        Intent intent = new Intent(getActivity(), WaitRedDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    } else {
                        showRedPacketDialog(info);
                    }

                } else {
                    info.chatType = 2;
                    Intent intent = new Intent(getActivity(), WaitRedDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }

            } else {
                if (chatType == EaseConstant.CHATTYPE_GROUP) {
                    info.chatType = 1;
                } else {
                    info.chatType = 2;
                }
                if (info.grabRedenvelope == 1) {

                    Intent intent = new Intent(getActivity(), WaitRedDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("state", info);
                    intent.putExtra("type", info.chatType);
                    startActivity(intent);
                } else {
                    showRedPacketDialog(info);
                }

            }
        } else if (type == 19) {
            GrapRed red = (GrapRed) entity;
            if (red.content == 3) {
                mRedPacketViewHolder.setGones();
            } else if (red.content == 1) {
                mRedPacketDialog.dismiss();
                Intent intent = new Intent(getActivity(), WaitRedDetailActivity.class);
                intent.putExtra("id", id);
                if (info != null) {
                    intent.putExtra("type", info.chatType);
                }

                startActivity(intent);
                RedPacketInfo redPacketInfo = new RedPacketInfo();
                redPacketInfo.red_name = SpUtil.getName(getActivity());
                redPacketInfo.send_name = send_name;
                redPacketInfo.isgrab = 1;
                redPacketInfo.type = "2";
                redPacketInfo.redEnvelopeId = id;
                redPacketInfo.isAll = red.isAll;
                redPacketInfo.grap_id = SpUtil.getUserInfoId(getActivity());

                EMMessage message = EaseCommonUtils.createExpressionRedMessage(toChatUsername, redPacketInfo);
                sendMessages(message);

            }
        } else {
            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
