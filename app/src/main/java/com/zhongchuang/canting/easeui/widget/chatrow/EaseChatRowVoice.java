package com.zhongchuang.canting.easeui.widget.chatrow;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.zhongchuang.canting.R;
import com.hyphenate.util.EMLog;
import com.zhongchuang.canting.easeui.utils.EaseUserUtils;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.TextUtil;

public class EaseChatRowVoice extends EaseChatRowFile {

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;

    public EaseChatRowVoice(Context context,int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context,chatType, message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }
    protected RelativeLayout rl_bbg;
    protected TextView tv_reback;
    @Override
    protected void onFindViewById() {
        voiceImageView = findViewById(R.id.iv_voice);
        tv_reback = findViewById(R.id.tv_reback);
        tv_userid = findViewById(R.id.tv_userid);
        rl_bbg = findViewById(R.id.rl_bbg);
        voiceLengthView = findViewById(R.id.tv_length);
        readStatusView = findViewById(R.id.iv_unread_voice);
    }
    protected TextView tv_userid;
    public static final String EXETEND = "rb_extend";
    @Override
    protected void onSetUpView() {

        EMMessageBody body = message.getBody();
        String content = message.getStringAttribute(EXETEND, "");
        if(!TextUtil.isEmpty(content)){
            tv_userid.setVisibility(GONE);
        }else {
            if(message.getChatType()== EMMessage.ChatType.GroupChat){
                tv_userid.setVisibility(VISIBLE);
            }else {
                tv_userid.setVisibility(GONE);
            }
        }
        if(body instanceof EMVoiceMessageBody){
            EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
            int len = voiceBody.getLength();
            if(len>0){
                voiceLengthView.setText(voiceBody.getLength() + "\"");
                voiceLengthView.setVisibility(View.VISIBLE);
            }else{
                voiceLengthView.setVisibility(View.INVISIBLE);
            }


            EaseUserUtils.setUserNick(HxMessageUtils.getFName(message), tv_userid);
            if (EaseChatRowVoicePlayClickListener.playMsgId != null
                    && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
                AnimationDrawable voiceAnimation;
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    voiceImageView.setImageResource(R.anim.voice_from_icon);
                } else {
                    voiceImageView.setImageResource(R.anim.voice_to_icon);
                }
                voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
                voiceAnimation.start();
            } else {
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
                } else {
                    voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);
                }
            }

            if (message.direct() == EMMessage.Direct.RECEIVE) {
                if (message.isListened()) {
                    // hide the unread icon
                    readStatusView.setVisibility(View.INVISIBLE);
                } else {
                    readStatusView.setVisibility(View.VISIBLE);
                }
                EMLog.d(TAG, "it is receive msg");
                if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                        voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                    progressBar.setVisibility(View.VISIBLE);
                    setMessageReceiveCallback();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);

                }
                return;
            }
            tv_reback.setVisibility(GONE);
            rl_bbg.setVisibility(VISIBLE);
        }else {
            tv_reback.setVisibility(VISIBLE);
            rl_bbg.setVisibility(GONE);

            tv_reback.setText(content);
        }

        // until here, handle sending voice message
        handleSendMessage();
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStatusView, adapter, activity).onClick(bubbleLayout);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
            EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }

}
