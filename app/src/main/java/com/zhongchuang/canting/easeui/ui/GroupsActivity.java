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
package com.zhongchuang.canting.easeui.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.adapter.GroupsListAdapter;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.bean.GROUP;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;

public class GroupsActivity extends BaseActivity {
	public static final String TAG = "GroupsActivity";
	private ListView groupListView;
	private ImageView back;
	private ImageView add;


	protected List<GROUP> grouplist = new ArrayList<>();
	private GroupsListAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;

	private SwipeRefreshLayout swipeRefreshLayout;
    private Subscription mSubscription;
    private int poistion;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			swipeRefreshLayout.setRefreshing(false);
			switch (msg.what) {
				case 0:
					refresh();
					break;
				case 1:
					Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);
		initViews();
		bindEvents();

	}

	public void initViews() {

		getGroupList( );
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		groupListView = findViewById(R.id.list);
		back = findViewById(R.id.tongxunlu_bacbut);
		add = findViewById(R.id.iv_add);
		//show group list

//		model=new FriendModel();
//		model.getGroupList(this);获取群列表

		groupAdapter = new GroupsListAdapter(this,grouplist);
		groupListView.setAdapter(groupAdapter);

		swipeRefreshLayout = findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);
		//pull down to refresh
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run(){
						try {
							EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
							handler.sendEmptyMessage(0);
						} catch (HyphenateException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(1);
						}
					}
				}.start();
			}
		});

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		groupListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int positions, long id) {
				poistion=positions;
				GROUP item = groupAdapter.getItem(positions);
				showDailog(item.getGroup_id());
				return true;
			}
		});
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				GROUP item = groupAdapter.getItem(position);
				CanTingAppLication.GroupName=item.groupname;
				CanTingAppLication.headimage=item.headimage;
					Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
					// it is group chat
					intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
					intent.putExtra(EaseConstant.EXTRA_USER_ID, item.getGroup_hx());
					intent.putExtra("group_id", item.groupid+"");
					CHATMESSAGE chatmessage = CHATMESSAGE.fromGroup(item);
					intent.putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
					startActivityForResult(intent, 0);




			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupsActivity.this, AddFriendActivity.class);
				startActivity(intent);
			}
		});
	}
    public void showDailog(final int id){
		View views = View.inflate(GroupsActivity.this, R.layout.base_dailog_view, null);
		TextView sure = views.findViewById(R.id.txt_sure);
		TextView cancel = views.findViewById(R.id.txt_cancel);
		TextView title = views.findViewById(R.id.txt_title);
		title.setText(getString(R.string.quedingshanchugaiqz));

		final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(GroupsActivity.this).setMessageView(views).create();
		dialog.show();
		sure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//                 model.deleteGroup(id+"",GroupsActivity.this);

				dialog.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	public void bindEvents() {
		mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
			@Override
			public void call(SubscriptionBean.RxBusSendBean bean) {
				if (bean == null) return;
				if(bean.type== SubscriptionBean.REFRESSHS){
					getGroupList( );
				}

			}
		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		RxBus.getInstance().addSubscription(mSubscription);
	}
	private boolean isFisrt;
	private ProgressDialog mDialog;
	public void getGroupList( ){
        if(!isFisrt){
			mDialog = new ProgressDialog(this);
			mDialog.setMessage( getString(R.string.jzz));
			mDialog.show();
			isFisrt=true;
		}

		Map<String, String> map = new HashMap<>();
		map.put("userInfoId", CanTingAppLication.userId);

		netService api = HttpUtil.getInstance().create(netService.class);
		api.getGroupList(map).enqueue(new BaseCallBack<GROUP>() {
			@Override
			public void onSuccess(GROUP group) {
				mDialog.dismiss();
				groupAdapter.setData(group.data);
				for(GROUP groups:group.data){
					SpUtil.putInt(GroupsActivity.this,groups.groupid,groups.admin?1:0);
				}

			}

			@Override
			public void onOtherErr(int code, String t) {
				super.onOtherErr(code, t);
				mDialog.dismiss();
				ToastUtils.showNormalToast(t);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		refresh();
		super.onResume();
	}

	private void refresh(){
		getGroupList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
		if (mSubscription != null) {
			RxBus.getInstance().unSub(mSubscription);
		}
	}

	public void back(View view) {
		finish();
	}

//	@Override
//	public void net4thirdGetMailListSucces(List<USER> userList) {
//
//	}
//
//	@Override
//	public void getGroupListSucces(List<GROUP> phoneContacts) {
//		grouplist.clear();
//		grouplist.addAll(phoneContacts);
//		groupAdapter.setData(grouplist);
//
//	}
//
//	@Override
//	public void getPhoneListSucces(List<PhoneContact> phoneContacts) {
//
//	}

}
