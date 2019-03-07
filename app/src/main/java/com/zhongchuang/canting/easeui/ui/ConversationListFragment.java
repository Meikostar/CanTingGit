package com.zhongchuang.canting.easeui.ui;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.util.NetUtils;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.db.InviteMessgeDao;
import com.zhongchuang.canting.easeui.model.EaseAtMessageHelper;


/**
 * 聊天列表 主要处理了长按点击事件
 */
public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation item = conversationListView.getItem(position);

                   go2hxChat(item);


            }
        });
        //red packet code : 红包回执消息在会话列表最后一条消息的展示


        //end of red packet code
    }


    /**
     * 环信聊天界面
     * @param emConversation
     */
    private void go2hxChat(EMConversation emConversation) {
        EMConversation conversation = emConversation;
        String username = conversation.conversationId();
        if (username.equals(EMClient.getInstance().getCurrentUser()))
            Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
        else {
            // start chat acitivity
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            if (conversation.isGroup()) {
                if (conversation.getType() == EMConversationType.ChatRoom) {
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
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation eqcmsg = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);


        EMConversation tobeDeleteCons = eqcmsg;
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

}
