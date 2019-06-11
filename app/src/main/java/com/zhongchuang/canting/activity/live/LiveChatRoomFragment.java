package com.zhongchuang.canting.activity.live;

import android.app.Activity;
import android.content.ClipData;
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

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.PathUtil;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.PersonManActivity;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.GrapRed;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.RedPacketInfo;
import com.zhongchuang.canting.easeui.bean.RobotUser;
import com.zhongchuang.canting.easeui.ui.ContextMenuActivity;
import com.zhongchuang.canting.easeui.ui.EaseChatFragment;
import com.zhongchuang.canting.easeui.ui.GroupSetActivity;
import com.zhongchuang.canting.easeui.ui.ImageGridActivity;
import com.zhongchuang.canting.easeui.ui.PickAtUserActivity;
import com.zhongchuang.canting.easeui.ui.VideoCallActivity;
import com.zhongchuang.canting.easeui.ui.VoiceCallActivity;
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
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.red.CustomDialog;
import com.zhongchuang.canting.widget.red.OnRedPacketDialogClickListener;
import com.zhongchuang.canting.widget.red.RedPacketViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LiveChatRoomFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper, BaseContract.View {


    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;



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

        super.setUpView();

//        presenter = new BasesPresenter(this);
//        presenter.getBanners(2 + "");
//        bannerAdapter = new BannerAdaptes(getActivity());
//        bannerView.setAdapter(bannerAdapter);
        // set click listener
        bannerView.setVisibility(View.GONE);
        inputMenu.setVisibility(View.GONE);
        titleBar.setVisibility(View.GONE);
//        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
//            @Override
//            public void onPageClick(int position, Object o) {
////                Banner banner= (Banner) o;
////                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
////                intent.putExtra("id", banner.product_sku_id);
////                intent.putExtra("type", 2);
////                startActivity(intent);
//            }
//
//            @Override
//            public void onPageDown() {
//
//            }
//
//            @Override
//            public void onPageUp() {
//
//            }
//        });
//        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());


    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();


        //end of red packet code
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

    }


    public void onAvatarClick(String user_id) {


    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }

    private String user_id;
    private String send_name;

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {

        return false;
    }



    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

//        showPopwindow(message);
        // no message forward when in chat room
//        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
//                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
//                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {

        //keep exist extend menu
        return false;
    }
   private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    public void sendMessages(String content) {


      final   EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
            message.setChatType(EMMessage.ChatType.ChatRoom);
        EMMessage msg = HxMessageUtils.exMsg(message, mChatmsg);
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        if(content.equals("*&@@&*")||content.contains("&!&&!&")){
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                        conv.removeMessage(message.getMsgId());
                    }
                }
            });

            return;
        }else {
            if (isMessageListInited) {
                messageList.refreshSelectLast();
            }
        }


        //refresh ui

    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
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

            Banner banner = (Banner) entity;
            bannerAdapter.setData(banner.data);


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
