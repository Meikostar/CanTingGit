package com.zhongchuang.canting.easeui.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.utils.EaseSmileUtils;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    private TextView tvTime;
    private ImageView img;
    private LinearLayout ll_bg;

    public EaseChatRowText(Context context,int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context,chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
          if(chatType== Constant.CHATTYPE_CHATROOM){
              inflater.inflate(R.layout.chat_room_received , this);
          }else {
              inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                      R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
          }


    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
        img = findViewById(R.id.iv_img);
        ll_bg = findViewById(R.id.ll_bg);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        String contents  = txtBody.getMessage();
//        if(contents.equals("*&@@&*")||contents.contains("&!&&!&")){
//            ll_bg.setVisibility(GONE);
//        }else {
//            ll_bg.setVisibility(VISIBLE);
            Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
            // 设置内容
            contentView.setText(span, BufferType.SPANNABLE);
        String text = contentView.getText().toString();
        if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)||message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
            img.setVisibility(VISIBLE);
          if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){

             img.setImageDrawable(getResources().getDrawable(R.drawable.chat_voice));
          }else {
              if(message.direct() == EMMessage.Direct.SEND){
                  img.setImageDrawable(getResources().getDrawable(R.drawable.chat_videos));
              }else {
                  img.setImageDrawable(getResources().getDrawable(R.drawable.chat_video));
              }
          }
        }else {
            if(img!=null){
                img.setVisibility(GONE);
            }

        }

        handleTextMessage();
//        }

    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    if(chatType!=Constant.CHATTYPE_CHATROOM){
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.VISIBLE);
                    }

                    break;
                case SUCCESS:
                    if(chatType!=Constant.CHATTYPE_CHATROOM){
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.GONE);
                    }

                    break;
                case FAIL:
                    if(chatType!=Constant.CHATTYPE_CHATROOM){
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.VISIBLE);
                    }

                    break;
                case INPROGRESS:
                    if(chatType!=Constant.CHATTYPE_CHATROOM){
                        progressBar.setVisibility(View.VISIBLE);
                        statusView.setVisibility(View.GONE);
                    }

                    break;
                default:
                    break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        // TODO Auto-generated method stub

    }



}
