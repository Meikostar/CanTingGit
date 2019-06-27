package com.zhongchuang.canting.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.hyphenate.util.LatLng;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.ui.EaseBaiduMapActivity;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

public class EaseChatRowLocation extends EaseChatRow {

    private TextView locationView;
    private EMLocationMessageBody locBody;

    public EaseChatRowLocation(Context context,int chatType, EMMessage message, int position, BaseAdapter adapter) {
        super(context, chatType,message, position, adapter);
    }

    @Override
    protected void onInflateView(int chatType) {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_location : R.layout.ease_row_sent_location, this);
    }
    protected RelativeLayout rl_bbg;
    protected TextView tv_reback;
    protected TextView tv_userid;
    public static final String EXETEND = "rb_extend";
    @Override
    protected void onFindViewById() {
        tv_reback = findViewById(R.id.tv_reback);
        tv_userid = findViewById(R.id.tv_userid);
        rl_bbg = findViewById(R.id.rl_bbg);
        locationView = findViewById(R.id.tv_location);
    }


    @Override
    protected void onSetUpView() {
        EMMessageBody body = message.getBody();
        String content = message.getStringAttribute(EXETEND, "");

        if(!TextUtil.isEmpty(content)){
            tv_userid.setVisibility(GONE);
        }
        if(body instanceof EMLocationMessageBody){
            locBody = (EMLocationMessageBody) body;
            locationView.setText(locBody.getAddress());

            // handle sending message
            if (message.direct() == EMMessage.Direct.SEND) {
                setMessageSendCallback();
                switch (message.status()) {
                    case CREATE:
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.GONE);
                        break;
                    case FAIL:
                        progressBar.setVisibility(View.GONE);
                        statusView.setVisibility(View.VISIBLE);
                        break;
                    case INPROGRESS:
                        progressBar.setVisibility(View.VISIBLE);
                        statusView.setVisibility(View.GONE);
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

            tv_reback.setVisibility(GONE);
            rl_bbg.setVisibility(VISIBLE);
        }else {
            tv_reback.setVisibility(VISIBLE);
            rl_bbg.setVisibility(GONE);

            tv_reback.setText(content);

        }

    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseBaiduMapActivity.class);
        intent.putExtra("latitude", locBody.getLatitude());
        intent.putExtra("longitude", locBody.getLongitude());
        intent.putExtra("address", locBody.getAddress());
        activity.startActivity(intent);
    }

    /*
	 * listener for map clicked
	 */
    protected class MapClickListener implements OnClickListener {

        LatLng location;
        String address;

        public MapClickListener(LatLng loc, String address) {
            location = loc;
            this.address = address;

        }

        @Override
        public void onClick(View v) {

        }
    }

}
