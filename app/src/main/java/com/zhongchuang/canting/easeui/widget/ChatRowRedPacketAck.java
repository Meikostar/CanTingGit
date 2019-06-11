package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRow;


public class ChatRowRedPacketAck extends EaseChatRow {

    private TextView mTvMessage;

    public ChatRowRedPacketAck(Context context,int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context,chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        if (message.getBooleanAttribute(EaseConstant.EXTRA_RED, false)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.em_row_red_packet_ack_message : R.layout.em_row_red_packet_ack_message, this);
        }
    }

    @Override
    protected void onFindViewById() {
        mTvMessage = findViewById(R.id.ease_tv_money_msg);
    }

    @Override
    protected void onSetUpView() {
        String currentUser = EMClient.getInstance().getCurrentUser();
        String fromUser = message.getStringAttribute(EaseConstant.EXTRA_SEND, "");//红包发送者
        String toUser = message.getStringAttribute(EaseConstant.EXTRA_NAME, "");//红包接收者
        String sendId = message.getStringAttribute(EaseConstant.EXTRA_USER_ID, "");//发红包人的id
        String senderId = message.getStringAttribute(EaseConstant.EXTRA_GRAPID, "");//抢红包id
        String type=message.getStringAttribute(EaseConstant.EXTRA_RED_TYPE,null);
        int isAll=message.getIntAttribute(EaseConstant.EXTRA_RED_IS_ALL,0);

        if (!(message.direct() == EMMessage.Direct.SEND)) {
            if (message.getChatType().equals(EMMessage.ChatType.GroupChat)) {

                if (!sendId.equals(currentUser)) {
                    if(type.equals("1")){
                        String str1="";
                        String str2="";
                        str1=toUser+"领取了"+fromUser+"的";
                        str2="，"+fromUser+"的红包已被领完。";
                        String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>"+str2;
                        mTvMessage.setText( Html.fromHtml(content));

                    }else {
                        String str1="";
                        str1=toUser+"领取了"+fromUser+"的";
                        String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>";
                        mTvMessage.setText( Html.fromHtml(content));

                    }
                } else {
                    if(type.equals("1")){
                        String str1="";
                        String str2="";
                        str1=toUser+"领取了你的";
                        str2="，你的红包已被领完。";
                        String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>"+str2;
                        mTvMessage.setText( Html.fromHtml(content));
                    }else {
                        String str1="";
                        str1=toUser+"领取了你的";
                        String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>";
                        mTvMessage.setText( Html.fromHtml(content));

                    }

                }
            } else {

                String str1="";
                str1=toUser+"领取了你的";
                String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>";
                mTvMessage.setText( Html.fromHtml(content));
            }
        } else {
            if(type.equals("1")){
                    String str1="";
                    String str2="";
                        str1="你领取了"+fromUser;
                        str2=","+fromUser+"的红包已被领完。";
                        String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>"+str2;
                        mTvMessage.setText( Html.fromHtml(content));


            }else {
                    String str1="";
                    String str2="";
                    str1="你领取了"+fromUser;
                    String content=str1+"<font color=\"#F9A33C\">" + "红包" + "</font>";
                    mTvMessage.setText( Html.fromHtml(content));

            }

        }
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onBubbleClick() {
    }

}
