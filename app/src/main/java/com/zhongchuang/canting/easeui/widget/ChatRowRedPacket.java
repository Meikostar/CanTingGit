package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRow;
import com.zhongchuang.canting.utils.TextUtil;

public class ChatRowRedPacket extends EaseChatRow {

    private TextView mTvGreeting;
    private TextView tv_money_state;
    private TextView mTvSponsorName;
    private TextView mTvPacketType;
    private RelativeLayout bubble;
    protected RelativeLayout rl_bbg;
    protected TextView tv_userid;
    protected TextView tv_reback;
    public ChatRowRedPacket(Context context,int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context,chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        if (message.getBooleanAttribute(EaseConstant.EXTRA_RED, false)) {
            inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                    R.layout.em_row_received_red_packet : R.layout.em_row_sent_red_packet, this);
        }
    }

    @Override
    protected void onFindViewById() {
        mTvGreeting = findViewById(R.id.tv_money_greeting);
        tv_money_state = findViewById(R.id.tv_money_state);
        mTvSponsorName = findViewById(R.id.tv_sponsor_name);
        mTvPacketType = findViewById(R.id.tv_packet_type);
        bubble = findViewById(R.id.bubble);
        tv_userid = findViewById(R.id.tv_userid);
        tv_reback = findViewById(R.id.tv_reback);
        rl_bbg = findViewById(R.id.rl_bbg);
    }

    public static final String EXETEND = "rb_extend";
    @Override
    protected void onSetUpView() {
        String sponsorName = message.getStringAttribute(EaseConstant.EXTRA_SEND_RED_NAME, "");
        String redName=message.getStringAttribute(EaseConstant.EXTRA_RED_NAME,null);
        int isgrap=message.getIntAttribute(EaseConstant.EXTRA_RED_IS_GRAP,0);
        String content = message.getStringAttribute(EXETEND, "");
        if(TextUtil.isEmpty(content)){
            if(message.direct() == EMMessage.Direct.RECEIVE ){
                if(isgrap==1){
                    bubble.setBackground(context.getResources().getDrawable(R.drawable.em_red_packet_chatfrom_bgs));
                    tv_money_state.setText("红包已领取");
                }else {
                    tv_money_state.setText("查看红包");
                    bubble.setBackground(context.getResources().getDrawable(R.drawable.em_red_packet_chatfrom_bg));
                }

            }else {
                if(isgrap==1){
                    tv_money_state.setText("红包已领取");
                    bubble.setBackground(context.getResources().getDrawable(R.drawable.em_red_packet_chatto_bgs));
                }else {
                    tv_money_state.setText("查看红包");
                    bubble.setBackground(context.getResources().getDrawable(R.drawable.em_red_packet_chatto_bg));
                }

            }
            tv_reback.setVisibility(GONE);
            rl_bbg.setVisibility(VISIBLE);
            mTvGreeting.setText(redName);
        }else {
            tv_reback.setVisibility(VISIBLE);
            rl_bbg.setVisibility(GONE);
            tv_reback.setText(content);
            tv_userid.setVisibility(GONE);
        }

//        mTvSponsorName.setText(sponsorName);
//        String packetType = message.getStringAttribute(RPConstant.MESSAGE_ATTR_RED_PACKET_TYPE, "");
//        if (!TextUtils.isEmpty(packetType) && TextUtils.equals(packetType, RPConstant.RED_PACKET_TYPE_GROUP_EXCLUSIVE)) {
//            mTvPacketType.setVisibility(VISIBLE);
////            mTvPacketType.setText(R.string.exclusive_red_packet);
//        } else {
//            mTvPacketType.setVisibility(GONE);
//        }
        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    // 发送消息
                    break;
                case SUCCESS: // 发送成功
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL: // 发送失败
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case INPROGRESS: // 发送中
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
    }

}
