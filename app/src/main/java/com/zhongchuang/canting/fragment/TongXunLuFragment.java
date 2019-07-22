package com.zhongchuang.canting.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.zxing.client.android.activity.CaptureActivity;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.activity.chat.AddGroupActivity;
import com.zhongchuang.canting.activity.chat.ChatMenberActivity;
import com.zhongchuang.canting.activity.chat.ChatSetActivity;
import com.zhongchuang.canting.activity.chat.SeachFriendActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.adapter.ChatMenberAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.IMG;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.conference.ConferenceActivity;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.ui.AddFriendActivity;
import com.zhongchuang.canting.easeui.ui.EaseContactListFragment;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.FindFriendActivity;

import com.zhongchuang.canting.db.DemoDBManager;

import com.zhongchuang.canting.easeui.ui.GroupsActivity;
import com.zhongchuang.canting.easeui.widget.ContactItemView;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.StickyScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/11/22.
 */

@SuppressLint("ValidFragment")
public class TongXunLuFragment extends EaseContactListFragment implements View.OnClickListener,EaseContactListFragment.EaseContactListItemClickListener ,BaseContract.View {

    ImageView tongxunluSearch;
    ContactItemView group;
    ContactItemView add;
    StickyScrollView scrollView;
    GridView gridView;
    ContactItemView add_friend;
    ContactItemView ct_mine;
    ContactItemView conference_item;
    EditText tongxunluIpt;
    ImageView tongxunluBacbut;
    ImageView tongxunluFind;
    RelativeLayout rl_bg;

    private BasesPresenter presenter;
    private Context mContext;
    private PopupWindow mPopWindow;
    private List<FriendListBean> friends = new ArrayList<>();
    private Subscription mSubscription;
    private ChatMenberAdapter adapter;
    public TongXunLuFragment() {
    }

    public TongXunLuFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void initView() {
        super.initView();
        tongxunluSearch = super.getView().findViewById(R.id.tongxunlu_search);
        group = super.getView().findViewById(R.id.group_item);
        add = super.getView().findViewById(R.id.group_add);
        scrollView = super.getView().findViewById(R.id.scrollView);
        gridView = super.getView().findViewById(R.id.grid);
        add_friend = super.getView().findViewById(R.id.add_friend);
        ct_mine = super.getView().findViewById(R.id.ct_mine);
        conference_item = super.getView().findViewById(R.id.conference_item);//音视频会议
        tongxunluIpt = super.getView().findViewById(R.id.query);
        tongxunluBacbut = super.getView().findViewById(R.id.tongxunlu_bacbut);
        tongxunluFind = super.getView().findViewById(R.id.tongxunlu_find);
        rl_bg = super.getView().findViewById(R.id.rl_bg);
        adapter=new ChatMenberAdapter(getActivity());
        presenter=new BasesPresenter(this);
        add_friend.setFocusable(true);
        add_friend.setFocusableInTouchMode(true);
        add_friend.requestFocus();
        scrollView.setFocusable(false);
        tongxunluIpt.setVisibility(View.GONE);
        initListener();
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.FRIEND) {
                    getFriendList();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fridIntent = new Intent(getActivity(), SeachFriendActivity.class);

                startActivity(fridIntent);
            }
        });
        ct_mine.setValues(SpUtil.getName(getActivity())+"  (自己)",SpUtil.getAvar(getActivity()));
        conference_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ConferenceActivity.startConferenceCall(getActivity(), null);
            }
        });
    }


    private IMG guide_img = new IMG();
    private void iniGridView(final List<GAME> list) {

        int length = 69;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if(dm==null||getActivity()==null){
            return;
        }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 0, 0, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(10); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setDatas(list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.size() - 1 == position) {
                    Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ChatMenberActivity.class);
                    intent.putExtra("game", list.get(position));
                    startActivity(intent);
                }


            }
        });
    }
    @Override
    public void onResume() {
        if(presenter!=null){
            presenter.getChatGroupList();
        }
        super.onResume();
    }

    @Override
    protected void setUpView() {

//        getFriendList();
        setContactListItemClickListener(this);
        super.setUpView();

    }
   private Map<String, EaseUser> map = new HashMap<>();

    private void getFriendList() {

        presenter.getFrendList("");

    }
    public void delFriend(final String userId){


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("chatUserId", userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.delFriend(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse goodsSpeCate) {

                getFriendList();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //删除和某个user会话，如果需要保留聊天记录，传false
                        EMClient.getInstance().chatManager().deleteConversation(userId, true);

                    }
                }).start();
            }
            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }
    @Override
    public void refresh() {
//        super.refresh();
        getFriendList();
//        DemoDBManager.getInstance().saveContactList(contactList);
    }

    private void initListener() {
        tongxunluBacbut.setOnClickListener(this);
        tongxunluSearch.setOnClickListener(this);
        group.setOnClickListener(this);
        add.setOnClickListener(this);
        add_friend.setOnClickListener(this);
        ct_mine.setOnClickListener(this);
        tongxunluFind.setOnClickListener(this);

    }


//    @OnClick({R.id.tongxunlu_search, R.id.tongxunlu_ipt, R.id.tongxunlu_bacbut, R.id.tongxunlu_find})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.tongxunlu_search:
//                break;
//            case R.id.tongxunlu_ipt:
//                break;
//            case R.id.tongxunlu_bacbut:
//                getActivity().finish();
//                break;
//            case R.id.tongxunlu_find:
//                showPopupWindow();
//                break;
//
//        }
//    }

    //popowindow  使用
    private void showPopupWindow() {

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.tongxunlu_popuplayout, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        LinearLayout tongxunluButQunliao = contentView.findViewById(R.id.tongxunlu_but_qunliao);
        LinearLayout tongxunluButFindFrd = contentView.findViewById(R.id.tongxunlu_but_findFrd);
        LinearLayout tongxunluButSaoyisao = contentView.findViewById(R.id.tongxunlu_but_saoyisao);


        tongxunluButQunliao.setOnClickListener(TongXunLuFragment.this);
        tongxunluButFindFrd.setOnClickListener(TongXunLuFragment.this);
        tongxunluButSaoyisao.setOnClickListener(TongXunLuFragment.this);
        //tongxunluRecy.setOnClickListener(TongXunLuFragment.this);

        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);

        mPopWindow.showAsDropDown(tongxunluFind, 0, 60);
    }
    private View views=null;
    private TextView sure = null;
    private TextView cancel = null;
    private TextView title = null;
    private EditText reson = null;
    public void showPopwindow(String name, final String userid) {

            views = View.inflate(getActivity(), R.layout.del_friend, null);
            sure = views.findViewById(R.id.txt_sure);
            cancel = views.findViewById(R.id.txt_cancel);
            title = views.findViewById(R.id.tv_title);
            reson = views.findViewById(R.id.edit_reson);
            title.setText(getString(R.string.qdsc)+name+getString(R.string.hys));
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
                    delFriend(userid);

                    dialog.dismiss();
                }
            });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tongxunlu_recy:
                mPopWindow.dismiss();
                break;
            case R.id.tongxunlu_but_findFrd:
                mPopWindow.dismiss();
                Intent fridIntent = new Intent(getActivity(), FindFriendActivity.class);
                fridIntent.putExtra("data",gameinfo);
                startActivity(fridIntent);
                break;
            case R.id.tongxunlu_but_qunliao:
                mPopWindow.dismiss();
                Intent groupintent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(groupintent);
                break;
            case R.id.group_item:


                Intent intent = new Intent(getActivity(), GroupsActivity.class);
                startActivity(intent);

                break;
            case R.id.add_friend:

                Intent intents = new Intent(getActivity(), FindFriendActivity .class);
                intents.putExtra("data",gameinfo);
                startActivity(intents);
                break;
            case R.id.ct_mine:
                Intent intens = new Intent(getActivity(), NewPersonDetailActivity.class);
                intens.putExtra("id", SpUtil.getUserInfoId(getActivity()) + "");
                startActivity(intens);

                break;

            case R.id.tongxunlu_but_saoyisao:
                mPopWindow.dismiss();


                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CAMERA)) {

                        PermissionGen.with(getActivity())
                                .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                                .permissions(Manifest.permission.CAMERA)
                                .request();
                        // 显示给用户的解释
                    } else {
                        // No explanation needed, we can request the permission.
                        Intent intent1 = new Intent(getActivity(), CaptureActivity.class);

                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityForResult(intent1, 0);
                    }


                break;
            case R.id.tongxunlu_bacbut:
                getActivity().finish();
                break;
            case R.id.tongxunlu_find:


                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.CAMERA)) {

                    PermissionGen.with(getActivity())
                            .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                            .permissions(Manifest.permission.CAMERA)
                            .request();
                    // 显示给用户的解释
                } else {
                    // No explanation needed, we can request the permission.
                    Intent intent1 = new Intent(getActivity(), CaptureActivity.class);

                    intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivityForResult(intent1, 0);
                }

                break;
            case R.id.tongxunlu_search:
                break;
            case R.id.group_add:
                Intent intentsss = new Intent(getActivity(), ChatSetActivity .class);
                intentsss.putExtra("data",gameinfo);
                startActivity(intentsss);

                break;
        }
    }
    private GAME gameinfo;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameinfo = (GAME) getArguments().getSerializable("data");
        iniGridView(gameinfo.data);
    }
    @Override
    public void onListItemClicked(EaseUser user) {
        if (user != null) {
            Intent intentc = new Intent(getActivity(), ChatActivity.class);
            intentc.putExtra("userId",user.userid);
            startActivity(intentc);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
            if(mSubscription!=null){
                mSubscription.unsubscribe();
        }
    }
    @Override
    public void onLongListItemClicked(EaseUser user) {
        EaseUser user1 = map.get(user.userid);
        showPopwindow(user1.getNickname()==null?(user1.getUsername()==null?user1.userid:user1.getUsername()):user1.getNickname(),user.userid);
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        if(type==18){
            List<FriendListBean> hxFriends = (List<FriendListBean>) entity;
            if ((hxFriends!=null)&&(hxFriends.size()>0)) {
                LogUtil.d("好有个数=" + hxFriends.size());
                map.clear();
                friends.addAll(hxFriends);

                for (FriendListBean hx : hxFriends) {
                    String uid = hx.chat_user_id;

                    LogUtil.d("好友的ID=" + uid);
                    EaseUser easeUser = new EaseUser(uid);
                    easeUser.setAvatar(hx.head_image);
                    easeUser.userid = hx.chat_user_id;
                    easeUser.setNickname((TextUtil.isNotEmpty(hx.remark_name)&&(!hx.remark_name.equals("(NULL)")) )? hx.remark_name : hx.nickname);
                    map.put(uid, easeUser);
                }
                setContactsMap(map);
                getContactList();
                LogUtil.d("数据长度=" + contactList.size());
                DemoDBManager.getInstance().saveContactList(contactList);
                contactListLayout.refresh();
            }
        }else {
                GAME game= (GAME) entity;
                if(game!=null&&game.data!=null&&game.data.size()>0){
                    gameinfo=game;
                    if(getActivity()!=null){
                        iniGridView(gameinfo.data);
                    }

                }
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        getContactList();
        contactListLayout.refresh();
    }
}

