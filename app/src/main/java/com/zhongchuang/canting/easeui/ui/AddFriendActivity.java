package com.zhongchuang.canting.easeui.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseBean;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.bean.PLACE;
import com.zhongchuang.canting.easeui.bean.USER;
import com.zhongchuang.canting.easeui.bean.USER_AVATAR;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class AddFriendActivity extends BaseActivity implements BaseContract.View {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private int type = 0;
    public static final String TAB_ONE = "tab_one";
    public static final String TAB_TWO = "tab_two";
    private BasesPresenter presenter;
    private addFriendFragment gategoryFragment;
    private List<USER> userList;
    private String groupName;
    private String ids;
    private String url;
    private String id;
    private String group_id;
    private String group_ids;
    private int order_id;

    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private Subscription mSubscription;
    private PLACE place;
    private USER_AVATAR user_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_home);
        ButterKnife.bind(this);
        initViews();
        bindEvents();

    }


    public void initViews() {
        presenter = new BasesPresenter(this);
        viewpagerMain.setScanScroll(false);
        groupName = getIntent().getStringExtra("name");
        ids = getIntent().getStringExtra("ids");
        url = getIntent().getStringExtra("url");
        user_avatar = (USER_AVATAR) getIntent().getSerializableExtra("data");
        id = getIntent().getStringExtra("id");
        group_id = getIntent().getStringExtra("group_id");
        group_ids = getIntent().getStringExtra("group_ids");
        order_id = getIntent().getIntExtra("order", 0);
        type = getIntent().getIntExtra("type", 0);
        if (order_id != 0) {
            tv_sure.setVisibility(View.GONE);
            tv_title.setText(R.string.wdhy);
        }

        addFragment();

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setCurrentItem(0);
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type != 0) {

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        if (type == 1) {
            tv_title.setText( getString(R.string.tjhy));
        } else if (type == 2) {
            tv_title.setText(R.string.ychy);
        }
    }

    private void addFragment() {
        mFragments = new ArrayList<>();
        gategoryFragment = new addFriendFragment();

        gategoryFragment.setType(ids, type);
        mFragments.add(gategoryFragment);

    }

    public void bindEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                bnbHome.setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EaseUser> contacts = gategoryFragment.contactListLayout.getContacts();
                list.clear();
                int i = 0;
                chatUserId="";
                for (EaseUser user : contacts) {
                    if (user.isChoose) {
                        if (i == 0) {
                            chatUserId = user.userid;
                        } else {
                            chatUserId = chatUserId + "," + user.userid;
                        }
                        list.add(user.userid);
                        i++;
                    }

                }
//
                if(TextUtil.isEmpty(chatUserId)){
                    ToastUtils.showNormalToast(getString(R.string.qxzcy));
                    return;
                }
                if (type == 1) {
                    if (TextUtil.isNotEmpty(group_ids)) {

                        presenter.upUserGroup(chatUserId, groupName, group_ids, 1 + "");
                    } else {
                        addFriendList(chatUserId);
                    }

                } else if (type == 2) {
                    if (TextUtil.isNotEmpty(group_ids)) {

                        presenter.upUserGroup(chatUserId, groupName, group_ids, 0 + "");
                    } else {
                        delFriendList(chatUserId);
                    }

                } else if (type == 0) {
                    showPopwindow();
                }


//                gategoryFragment.addMenber();
            }
        });

    }

     private String chatUserId;
    public List<String> list = new ArrayList<>();

    public List<USER> getUserList() {
        return userList;
    }


    public void initData() {

    }

    public void creatGroup(final String groupNames) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("chatUserId", chatUserId);
        map.put("groupsName", groupNames);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.createGroups(map).enqueue(new BaseCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean group) {
//                ToastUtils.showNormalToast(goodsSpeCate.toString());
                if (!TextUtils.isEmpty(group.data)) {
                    Intent intent = new Intent(AddFriendActivity.this, GroupsActivity.class);
                    startActivity(intent);
                    finish();
//                    Intent intent = new Intent(AddFriendActivity.this, ChatActivity.class);
//                    // it is group chat
//                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
//                    intent.putExtra(EaseConstant.EXTRA_USER_ID, groupNames);
//                    intent.putExtra("group_id", group.data+"");
////                    CHATMESSAGE chatmessage = CHATMESSAGE.fromGroup(group);
////                    intent.putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
//                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            RxBus.getInstance().unSub(mSubscription);
        }
    }

    public void showPopwindow() {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.write_group_name, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.tv_title);
        reson = (EditText) views.findViewById(R.id.edit_reson);

        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
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

                if (!TextUtils.isEmpty(finalReson.getText().toString().trim())) {
                    creatGroup(finalReson.getText().toString().trim());
                } else {

                }
                dialog.dismiss();
            }
        });
    }

    public void addFriendList(String addusers) {


        Map<String, String> map = new HashMap<>();
//        map.put("userInfoId", CanTingAppLication.userId);
        map.put("addusers", addusers);
        map.put("groupId", group_id);
        map.put("groupsName", groupName);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.addFriendList(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse group) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.REFRESSH, ""));
                finish();
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    public void delFriendList(String menbers) {


        Map<String, String> map = new HashMap<>();
        map.put("groupId", group_id);
        map.put("menbers", menbers);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.delFriendList(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse group) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.REFRESSH, ""));
                finish();

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        ToastUtils.showNormalToast(getString(R.string.czcg));
        Intent intent = new Intent();
        intent.putExtra("name", "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}
