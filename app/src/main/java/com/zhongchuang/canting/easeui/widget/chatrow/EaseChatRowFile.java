package com.zhongchuang.canting.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.FileUtils;
import com.zhongchuang.canting.R;
import com.hyphenate.util.TextFormater;
import com.zhongchuang.canting.activity.chat.PlayVideoActivity;
import com.zhongchuang.canting.activity.chat.VideoActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.ui.EaseShowNormalFileActivity;
import com.zhongchuang.canting.utils.TextUtil;

import java.io.File;

public class EaseChatRowFile extends EaseChatRow {

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;

    protected EMCallBack sendfileCallBack;

    protected boolean isNotifyProcessed;
    private EMNormalFileMessageBody fileMessageBody;

    public EaseChatRowFile(Context context, int chatType,EMMessage message, int position, BaseAdapter adapter) {
        super(context, chatType,message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_file : R.layout.ease_row_sent_file, this);
    }

    @Override
    protected void onFindViewById() {
        fileNameView = findViewById(R.id.tv_file_name);
        fileSizeView = findViewById(R.id.tv_file_size);
        fileStateView = findViewById(R.id.tv_file_state);
        percentageView = findViewById(R.id.percentage);
    }


    @Override
    protected void onSetUpView() {
        fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
        fileNameView.setText(fileMessageBody.getFileName());
        fileSizeView.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            File file = new File(filePath);
            if (file.exists()) {
                fileStateView.setText(R.string.Have_downloaded);
            } else {
                fileStateView.setText(R.string.Did_not_download);
            }
            return;
        }

        // until here, to sending message
        handleSendMessage();
    }

    /**
     * handle sending message
     */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status()) {
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if(percentageView != null){
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress() + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        if(message!=null){
            EMVideoMessageBody     fileMessageBody = (EMVideoMessageBody) message.getBody();
            if(fileMessageBody!=null){
                String filePath = fileMessageBody.getLocalUrl();
                String url=fileMessageBody.getRemoteUrl();
                File file = new File(filePath);
                if(file!=null&&file.exists()){

                }else {
                    filePath=url;
                }
                CanTingAppLication.landType=2;

                context.startActivity(new Intent(context, PlayVideoActivity.class).putExtra("path", filePath));

//                if (file.exists()) {
//                    // open files if it exist
//                    FileUtils.openFile(file, (Activity) context);
//                } else {
//                    // download the file
//                    context.startActivity(new Intent(context, EaseShowNormalFileActivity.class).putExtra("msg", message));
//                }
//                if (message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat) {
//                    try {
//                        EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
//                    } catch (HyphenateException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
            }
        }


    }
}
