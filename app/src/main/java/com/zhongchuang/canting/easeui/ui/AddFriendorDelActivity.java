package com.zhongchuang.canting.easeui.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.been.HXFriend;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.Constant;
import com.zhongchuang.canting.easeui.EaseConstant;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.bean.USER;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class AddFriendorDelActivity extends BaseActivity {



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

    private addFriendFragment gategoryFragment;
    private List<USER> userList;
    private String groupName;
    private int id;


    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private List<Fragment> mFragments;
    private Subscription mSubscription;
    private HXFriend place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_home);
        ButterKnife.bind(this);
        initViews();
        bindEvents();

    }


    public void initViews() {

        viewpagerMain.setScanScroll(false);
        groupName=getIntent().getStringExtra("name");
        place= (HXFriend) getIntent().getSerializableExtra("place");
        id = getIntent().getIntExtra("id", 0);

        if(place!=null){

            tv_sure.setText( getString(R.string.yc));
        }else {
            tv_sure.setText( getString(R.string.add));
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
    }

    private void addFragment() {
        mFragments = new ArrayList<>();
        gategoryFragment = new addFriendFragment();
        if(place!=null){

          gategoryFragment.setInfo(place);
        }
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
                for(EaseUser user: contacts){
                    if(user.isChoose){
                        list.add(user.userid);
                    }
                }
                list.add(SpUtil.getUserInfoId(AddFriendorDelActivity.this));
                showPopwindow();


//                gategoryFragment.addMenber();
            }
        });

    }
    public List<String> list=new ArrayList<>();
    public List<USER> getUserList() {
        return userList;
    }


    public void initData() {

    }
    public void creatGroup( final String groupNames){

       final String[] allMembers = list.toArray(new String[list.size()]);

        new Thread(){
            public void run() {
                EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
//                option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

                try {
                    EMGroup group = EMClient.getInstance().groupManager().createGroup(groupNames, "", allMembers, "", option);
                    if(!TextUtils.isEmpty(group.getGroupId())){
                        Intent intent = new Intent(AddFriendorDelActivity.this, ChatActivity.class);
                        // it is group chat
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, group.getGroupName());
                        intent.putExtra("group_id", group.getGroupId()+"");
                        CHATMESSAGE chatmessage = CHATMESSAGE.fromGroup(group);
                        intent.putExtra(EaseConstant.EXTRA_CHATMSG, chatmessage);
                        startActivityForResult(intent, 0);
                        finish();
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.tv_title);
        reson = views.findViewById(R.id.edit_reson);

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

                if(!TextUtils.isEmpty(finalReson.getText().toString().trim())){
                 creatGroup(finalReson.getText().toString().trim());
                }else {

                }
                dialog.dismiss();
            }
        });
    }
}
