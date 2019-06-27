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
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.utils.EaseSmileUtils;
import com.zhongchuang.canting.easeui.widget.EaseImageView;
import com.zhongchuang.canting.utils.TextUtil;

public class EaseReBackRowText extends EaseChatRow {

    private TextView contentView;
    private TextView tv_ack;
    private TextView tvTime;
    private TextView tv_userid;
    private TextView tv_delivered;
    private ImageView img;
    private EaseImageView iv_userhead;
    private LinearLayout ll_bg;

    public EaseReBackRowText(Context context, int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context,chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {

              inflater.inflate(
                      R.layout.ease_row_sent_reback_message , this);



    }

    @Override
    protected void onFindViewById() {
        contentView = findViewById(R.id.tv_chatcontent);
        tv_ack = findViewById(R.id.tv_ack);
        tv_userid = findViewById(R.id.tv_userid);
        tv_delivered = findViewById(R.id.tv_delivered);
        img = findViewById(R.id.iv_img);
        iv_userhead = findViewById(R.id.iv_userhead);
        ll_bg = findViewById(R.id.ll_bg);
    }
    public static final String EXETEND = "rb_extend";
    @Override
    public void onSetUpView() {
        EMMessageBody body = message.getBody();
        String content = message.getStringAttribute(EXETEND, "");
        if(body instanceof EMTextMessageBody){
            if(TextUtil.isNotEmpty(content)){
                contentView.setText(content);
            }else {
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                String contents  = txtBody.getMessage();
//        if(contents.equals("*&@@&*")||contents.contains("&!&&!&")){
//            ll_bg.setVisibility(GONE);
//        }else {
//            ll_bg.setVisibility(VISIBLE);
                Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
                // 设置内容
                contentView.setText(span, BufferType.SPANNABLE);
            }

        }else {

            contentView.setText(content);
        }



        handleTextMessage();
        tv_ack.setVisibility(GONE);
        tv_userid.setVisibility(GONE);
        tv_delivered.setVisibility(GONE);
        iv_userhead.setVisibility(GONE);
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
