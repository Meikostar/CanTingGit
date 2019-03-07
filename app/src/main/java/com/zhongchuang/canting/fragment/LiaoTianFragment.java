package com.zhongchuang.canting.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.TongXunLuActivity;
import com.zhongchuang.canting.listener.MyConnectionListener;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.List;

import butterknife.Unbinder;
import rx.Subscription;

/**
 * Created by Administrator on 2017/10/25.
 */

@SuppressLint("ValidFragment")
public class LiaoTianFragment extends EaseConversationListFragment implements View.OnClickListener,EaseConversationListFragment.EaseConversationListItemClickListener,EaseConversationListFragment.EaseConversationListLongItemClickListener{

    FrameLayout tongxunluPic;
    EditText liaotianSearch;
    private Context mContext;
    private Unbinder unbinder;

    public LiaoTianFragment() {}

    public LiaoTianFragment(Context mContext) {
        this.mContext = mContext;
    }

    public LiaoTianFragment(Context mContext, List<GAME> games,String types) {
        this.mContext = mContext;
        type = types;
        if(games!=null&&games.size()>0){
            map.clear();
            for(GAME id : games){
                map.put(id.chat_user_id,id.chat_user_id);
            }
        }

    }
    @Override
    protected void initView() {
        super.initView();
        EMClient.getInstance().chatManager().addMessageListener(msgListener);//收到消息弹框

        tongxunluPic = super.getView().findViewById(R.id.tongxunlu_pic);
        liaotianSearch = super.getView().findViewById(R.id.query);
        initListenner();
        initSDKListenner();
    }
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.MESSAGENOTIFI,""));

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    private void initListenner() {
        tongxunluPic.setOnClickListener(this);
        liaotianSearch.setOnClickListener(this);
        setConversationListItemClickListener(this);
        setConversationListLongItemClickListener(this);
    }


    private void initSDKListenner() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        //实现ConnectionListener接口
        class MyConnectionListener implements EMConnectionListener {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(final int error) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (error == EMError.USER_REMOVED) {
                            // 显示帐号已经被移除
                        } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号在其他设备登录
                        } else {
                            if (NetUtils.hasNetwork(getActivity())) {
                                //连接不到聊天服务器
                            } else {
                                //当前网络不可用，请检查网络设置
                            }
                        }
                    }
                });
            }
        }
    }


    @Override
    public void onDestroyView() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroyView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tongxunlu_pic:
                Intent mIntent=new Intent(getActivity(), TongXunLuActivity.class);
                startActivity(mIntent);
                break;
            case R.id.liaotian_search:
                break;
            case R.id.liaotian_recy:
                break;
        }
    }

    @Override
    public void onListItemClicked(EMConversation conversations) {
        EMConversation conversation = conversations;
        String username = conversation.conversationId();
        if (username.equals(EMClient.getInstance().getCurrentUser()))
            Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
        else {
            // start chat acitivity
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            if (conversation.isGroup()) {
                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                    // it's group chat
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                } else {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                }
            }
            CHATMESSAGE chatmessage = CHATMESSAGE.fromConversation(conversation);
            if (chatmessage != null) {
                intent.putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
            }
            // it's single chat
            intent.putExtra(Constant.EXTRA_USER_ID, username);
            startActivity(intent);
        }
    }

    @Override
    public void onListLongItemClicked(EMConversation conversations) {
        EMConversation conversation = conversations;
        String username = conversation.conversationId();
        //删除和某个user会话，如果需要保留聊天记录，传false
       showPopwindow(username);

    }
    private View views=null;
    private TextView sure = null;
    private TextView cancel = null;
    private TextView title = null;
    private EditText reson = null;
    public void showPopwindow(final String id) {

        views = View.inflate(getActivity(), R.layout.del_friend, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.tv_title);
        reson = (EditText) views.findViewById(R.id.edit_reson);
        title.setText(R.string.schjqkjl);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final EditText finalReson = reson;
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean b = EMClient.getInstance().chatManager().deleteConversation(id, true);
                if(b){
                    setUpView();
                }
                dialog.dismiss();
            }
        });


    }
}
