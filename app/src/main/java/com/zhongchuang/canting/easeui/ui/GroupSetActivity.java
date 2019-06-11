package com.zhongchuang.canting.easeui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.MineCodeActivity;
import com.zhongchuang.canting.activity.chat.EditorGroupActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.USER;
import com.zhongchuang.canting.easeui.adapter.GroupMemberAdapter;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.easeui.bean.GROUP_USER;
import com.zhongchuang.canting.easeui.bean.PLACE;
import com.zhongchuang.canting.easeui.bean.USER_AVATAR;
import com.zhongchuang.canting.easeui.widget.EaseSwitchButton;
import com.zhongchuang.canting.easeui.widget.GridViewInScroll;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by syj on 2016/12/5.
 * 群组设置页面
 */
public class GroupSetActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;
    @BindView(R.id.member_grid)
    GridViewInScroll member_grid;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.ll_nick)
    LinearLayout llNick;
    @BindView(R.id.bt_switch_news)
    EaseSwitchButton bt_switch_news;
    @BindView(R.id.news_set_module)
    RelativeLayout news_set_module;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.iv_arrows)
    ImageView ivArrows;


    private CHATMESSAGE chatmessage;
    private GroupMemberAdapter adapter;

    private String Group_name;
    private PLACE place;


    private String id;
    private String name;
    private boolean admin;
    //    private PopView_QrcodeShow popView_qrcodeShow;
//    private PopView_CancelOrSure popView_cancelOrSure;
    private boolean isFirst = true;
    private String toChatUsername;
    private List<GROUP_USER> groupMember;
    private boolean newDisplay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.em_activity_group_set);
        ButterKnife.bind(this);
        initView();
        setView();
    }

    private void initView() {

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        admin = getIntent().getBooleanExtra("admin", false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(id);
                    final int memberCount = group.getMemberCount();
                    final String groupName = group.getGroupName();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_title.setText("聊天信息"+"("+getMenber(memberCount+"")+")");
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        getGroupInfo(id);
        if(TextUtil.isNotEmpty(name)){
            tvName.setText(name);
        }
        if(TextUtil.isNotEmpty(SpUtil.getString(this,id+"@@",""))){
            tvNick.setText(SpUtil.getString(this,id+"@@",""));
        }else {
            if(TextUtil.isNotEmpty(SpUtil.getName(this))){
                tvNick.setText(SpUtil.getName(this));
            }
        }

           ivArrows.setVisibility(View.VISIBLE);
           llName.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(GroupSetActivity.this, EditorGroupActivity.class);
                   intent.putExtra("name", name);
                   intent.putExtra("id", id);
                   startActivityForResult(intent,15);
               }
           });

       llNick.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(GroupSetActivity.this, EditorGroupActivity.class);
               intent.putExtra("title", "1");
               intent.putExtra("name", tvNick.getText().toString());
               intent.putExtra("id", id);
               startActivityForResult(intent,18);
           }
       });
        bt_switch_news.closeSwitch();
//        popView_cancelOrSure = new PopView_CancelOrSure(this);
//        popView_qrcodeShow = new PopView_QrcodeShow(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_switch_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().unblockGroupMessage(id);//&#x9700;&#x5f02;&#x6b65;&#x5904;&#x7406;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_switch_news.closeSwitch();
                                        status = 0;
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().unblockGroupMessage(id);//需异步处理
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_switch_news.openSwitch();
                                        status = 1;
                                    }
                                });

                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.REFRESSH) {
                    getGroupInfo(id);
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

    private Subscription mSubscription;
    private int status;
    public String getMenber(String menber){
        String  name="";
        if(menber.length()==1){
            name="0000"+menber;
        }else if(menber.length()==2){
            name="000"+menber;
        }else if(menber.length()==3){
            name="00"+menber;
        }else if(menber.length()==4){
            name="0"+menber;
        }else {
            name=menber;
        }
        return name;
    }
    private void fillView() {
        place.setGroup_id(id);
        Group_name = name;
        if (place.getUsers() != null) {
            adapter = new GroupMemberAdapter(this, place.getUsers(), admin);
            adapter.setGroupid(place.getGroup_id());
            member_grid.setAdapter(adapter);
            if (place.getGroup_sys() == 1) {
                status = 1;
                bt_switch_news.openSwitch();
            } else {
                bt_switch_news.closeSwitch();
            }


            if (!TextUtils.isEmpty(place.getNickname())) {
//                group_nick.setText(place.getNickname());
            }
        }


    }

    private void setView() {
        llCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupSetActivity.this, MineCodeActivity.class);
                intent.putExtra("state", place.getGroup_id());
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admin) {
                    delGroup();
                } else {
                    delGroup();
//                    delFriendList(CanTingAppLication.userId);
                }
//                model.deleteGroup(id,GroupSetActivity.this);
            }
        });
        member_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == place.getUsers().size()) {
                    Intent intent = new Intent(GroupSetActivity.this, AddFriendActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("type", 1);
                    intent.putExtra("data", data);
                    intent.putExtra("ids", ids);
                    intent.putExtra("group_id", place.getGroup_id());
                    startActivityForResult(intent, 0);
                    return;

                }
                if (position == place.getUsers().size() + 1) {
                    Intent intent = new Intent(GroupSetActivity.this, AddFriendActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("group_id", place.getGroup_id());
                    intent.putExtra("place", place);
                    intent.putExtra("name", name);
                    intent.putExtra("ids", ids);
                    intent.putExtra("data", data);
                    startActivityForResult(intent, 1);

                    return;
                }

            }
        });


    }

    private USER_AVATAR data = new USER_AVATAR();
    List<USER_AVATAR> datas;

    public void getGroupInfo(final String groupId) {

        Map<String, String> map = new HashMap<>();
//        map.put("userInfoId", CanTingAppLication.userId);
        map.put("groupsId", groupId);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.getGroupInfo(map).enqueue(new BaseCallBack<USER>() {
            @Override
            public void onSuccess(USER group) {
                if (place == null) {
                    place = new PLACE();
                    datas = new ArrayList<USER_AVATAR>();
                } else {
                    datas.clear();
                }
                int i = 0;
                for (USER user : group.data) {
                    USER_AVATAR avatar = new USER_AVATAR();
                    if (!TextUtils.isEmpty(user.getNickname())) {
                        avatar.setName(user.getNickname());
                    }
                    if (!TextUtils.isEmpty(user.head_image)) {
                        avatar.setUser_avatar(user.head_image);
                    }
                    if (!TextUtils.isEmpty(user.user_group_name)) {
                        avatar.user_group_name=user.user_group_name;
                    }
                    if (!TextUtils.isEmpty(user.user_info_id)) {
                        avatar.user_info_id=user.user_info_id;
                    }

                    if (i == 0) {
                        ids = user.user_info_id;
                    } else {
                        ids = ids + "," + user.user_info_id;
                    }
                    i++;
                    datas.add(avatar);
                }


                data.data = datas;
                place.setUsers(datas);
//                ToastUtils.showNormalToast(goodsSpeCate.toString());
                fillView();
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
   private String ids;
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSubscription != null) {
            RxBus.getInstance().unSub(mSubscription);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode) {

                case 15:
                    String name = data.getStringExtra("name");
                    tvName.setText(name);
                    CanTingAppLication.GroupName=name;
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.GROUP_NAME,name));
                    break;
                case 18:
                    getGroupInfo(id);
                    String names = data.getStringExtra("name");
                    tvNick.setText(names);
                    break;

            }
        }

    }



    public void delGroup() {


        Map<String, String> map = new HashMap<>();
        map.put("groupId", id);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.deleteGroup(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse group) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.REFRESSHS, ""));
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FINISH, ""));
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
    public void onClick(View v) {

    }
}
