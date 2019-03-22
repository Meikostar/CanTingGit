/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhongchuang.canting.easeui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EMLog;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.model.styles.EaseMessageListItemStyle;
import com.zhongchuang.canting.easeui.utils.EaseCommonUtils;
import com.zhongchuang.canting.easeui.widget.ChatRowRedPacket;
import com.zhongchuang.canting.easeui.widget.ChatRowRedPacketAck;
import com.zhongchuang.canting.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRow;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowBigExpression;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowFile;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowImage;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowLocation;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowText;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowVideo;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseChatRowVoice;
import com.zhongchuang.canting.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

public class EaseMessageAdapter extends BaseAdapter{

	private final static String TAG = "msg";

	private Context context;

	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
	private static final int HANDLER_MESSAGE_SEEK_TO = 2;

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
	private static final int MESSAGE_TYPE_SENT_FILE = 10;
	private static final int MESSAGE_TYPE_RECV_FILE = 11;
	private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
	private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;
	private static final int MESSAGE_TYPE_SENT_CMD = 14;
	private static final int MESSAGE_TYPE_RECV_CMD= 15;
	private static final int MESSAGE_TYPE_RED_SEND= 16;
	private static final int MESSAGE_TYPE_RED_SENDS= 17;
	private static final int MESSAGE_TYPE_RED_GRAP= 18;
	private static final int MESSAGE_TYPE_RED_GRAPS= 19;


	public int itemTypeCount;

	// reference to conversation object in chatsdk
	private EMConversation conversation;
	EMMessage[] messages = null;

	private String toChatUsername;

	private MessageListItemClickListener itemClickListener;
	private EaseCustomChatRowProvider customRowProvider;

	private boolean showUserNick;
	private boolean showAvatar;
	private Drawable myBubbleBg;
	private Drawable otherBuddleBg;

	private ListView listView;
	private EaseMessageListItemStyle itemStyle;
    private int chatType;
    private List<EMMessage> mess=new ArrayList<>();
	public EaseMessageAdapter(Context context, String username, int chatType, ListView listView) {
		this.context = context;
		this.listView = listView;
		this.chatType = chatType;
		toChatUsername = username;
		this.conversation = EMClient.getInstance().chatManager().getConversation(username, EaseCommonUtils.getConversationType(chatType), true);
	}

	Handler handler = new Handler() {

		private void refreshList() {

			java.util.List<EMMessage> var = conversation.getAllMessages();
			mess.clear();
			for (EMMessage message : var) {
					EMTextMessageBody body = (EMTextMessageBody) message.getBody();
					String contents  = body.getMessage();
					if(contents.equals("*&@@&*")||contents.contains("&!&&!&")){
						RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.LIVE_SEND_FRESH,""));
						return;
					}else {
						mess.add(message);
					}

			}
			messages = mess.toArray(new EMMessage[var.size()]);
			conversation.markAllMessagesAsRead();
			notifyDataSetChanged();
		}

		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
				case HANDLER_MESSAGE_REFRESH_LIST:
					refreshList();
					break;
				case HANDLER_MESSAGE_SELECT_LAST:
					if (messages.length > 0) {
						listView.setSelection(messages.length - 1);
					}
					break;
				case HANDLER_MESSAGE_SEEK_TO:
					int position = message.arg1;
					listView.setSelection(position);
					break;
				default:
					break;
			}
		}
	};

	public void refresh() {
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}

	/**
	 * refresh and select the last
	 */
	public void refreshSelectLast() {
		final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
		handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
		handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
	}

	/**
	 * refresh and seek to the position
	 */
	public void refreshSeekTo(int position) {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
		msg.arg1 = position;
		handler.sendMessage(msg);
	}


	public EMMessage getItem(int position) {
		if (messages != null && position < messages.length) {
			return messages[position];
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * get count of messages
	 */
	public int getCount() {
		return messages == null ? 0 : messages.length;
	}

	/**
	 * get number of message type, here 14 = (EMMessage.Type) * 2
	 */
	public int getViewTypeCount() {
		if(customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0){
			return customRowProvider.getCustomChatRowTypeCount() + 14;
		}
		return 20;
	}


	/**
	 * get type of item
	 */
	public int getItemViewType(int position) {
		EMMessage message = getItem(position);
		if (message == null) {
			return -1;
		}

		if(customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0){
			return customRowProvider.getCustomChatRowType(message) + 13;
		}

		if (message.getType() == EMMessage.Type.TXT) {
			boolean red=message.getBooleanAttribute(EaseConstant.EXTRA_RED,false);
			String type=message.getStringAttribute(EaseConstant.EXTRA_RED_TYPE,null);
			if(red){
				if(type.equals("1")){
					return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RED_SEND : MESSAGE_TYPE_RED_SENDS;

				}else if(type.equals("2")){
					return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RED_GRAP : MESSAGE_TYPE_RED_GRAPS;
				}

			}else {
				if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
					return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
				}
				return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
			}

		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
		}
		if (message.getType() == EMMessage.Type.VIDEO) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
		}
		if (message.getType() == EMMessage.Type.FILE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
		}
		if (message.getType() == EMMessage.Type.CMD) {
			return message.direct() == EMMessage.Direct.RECEIVE ?  MESSAGE_TYPE_RECV_CMD: MESSAGE_TYPE_SENT_CMD;
		}
		return -1;// invalid
	}

	protected EaseChatRow createChatRow(Context context, EMMessage message, int position,int chatType) {
		EaseChatRow chatRow = null;
		if(customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null){
			return customRowProvider.getCustomChatRow(message, position, this);
		}
		boolean red=message.getBooleanAttribute(EaseConstant.EXTRA_RED,false);
		String type=message.getStringAttribute(EaseConstant.EXTRA_RED_TYPE,null);


			switch (message.getType()) {
				case TXT:
					  if(red){
						  if(type.equals("1")){
							  chatRow = new ChatRowRedPacket(context,chatType, message, position, this);

						  }else if(type.equals("2")){
							  chatRow = new ChatRowRedPacketAck(context,chatType, message, position, this);
						  }
					  }else {
						  if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
							  chatRow = new EaseChatRowBigExpression(context,chatType, message, position, this);
						  }else{
							  chatRow = new EaseChatRowText(context, chatType,message, position, this);
						  }
					  }

					break;
				case LOCATION:
					chatRow = new EaseChatRowLocation(context, chatType,message, position, this);
					break;
				case FILE:
					chatRow = new EaseChatRowFile(context,  chatType,message, position, this);
					break;
				case IMAGE:
					chatRow = new EaseChatRowImage(context,  chatType,message, position, this);
					break;
				case VOICE:
					chatRow = new EaseChatRowVoice(context, chatType, message, position, this);
					break;
				case VIDEO:
					chatRow = new EaseChatRowVideo(context, chatType, message, position, this);
					break;

				default:
					break;

		}


		return chatRow;
	}


	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		EMMessage message = getItem(position);
		if(convertView == null){
			convertView = createChatRow(context, message, position,chatType);
		}

		//refresh ui with messages
		((EaseChatRow)convertView).setUpView(message, position, itemClickListener, itemStyle);

		return convertView;
	}


	public void setItemStyle(EaseMessageListItemStyle itemStyle){
		this.itemStyle = itemStyle;
	}


	public void setItemClickListener(MessageListItemClickListener listener){
		itemClickListener = listener;
	}

	public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider){
		customRowProvider = rowProvider;
	}


	public boolean isShowUserNick() {
		return showUserNick;
	}


	public boolean isShowAvatar() {
		return showAvatar;
	}


	public Drawable getMyBubbleBg() {
		return myBubbleBg;
	}


	public Drawable getOtherBubbleBg() {
		return otherBuddleBg;
	}

}
