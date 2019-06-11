package com.zhongchuang.canting.activity.chat;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.adapter.ChatSetAdapter;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.easeui.bean.CHATMESSAGE;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.ListPopupWindow;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddFriendActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.et_content)
    ClearEditText etContent;
    @BindView(R.id.et_nick)
    ClearEditText etNick;
    @BindView(R.id.tv_add)
    Button tvAdd;
    @BindView(R.id.tv_group)
    TextView tvGroup;
    @BindView(R.id.ll_choose)
    LinearLayout llChoose;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    private BasesPresenter presenter;
    private ChatSetAdapter adapter;
    private GAME game;
    private FriendSearchBean.DataBean dataBean;
    private ListPopupWindow popupWindow;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_add_friend);
        game = (GAME) getIntent().getSerializableExtra("data");
        dataBean = (FriendSearchBean.DataBean) getIntent().getSerializableExtra("datas");
        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(this);
        presenter = new BasesPresenter(this);
        presenter.friendInfo(dataBean.getRingLetterName());

    }

    public void init() {
        if (game != null && game.data != null && game.data.size() > 0) {
            for (GAME game : game.data) {
                if (game.chatGroupName.equals(getString(R.string.msr))) {
                    game.chatGroupName = getString(R.string.shxz);
                    groupName = getString(R.string.shxz);
                    groupoId = game.id;
                }
            }

            if (TextUtil.isEmpty(groupName)) {
                groupName = getString(R.string.shxz);
                groupoId = game.data.get(0).id;
            }

            tvGroup.setText(groupName);
            popupWindow = new ListPopupWindow(this, game.data);
            popupWindow.setSureListener(new ListPopupWindow.ClickListener() {
                @Override
                public void clickListener(GAME menu, int poistion) {
                    groupName = menu.chatGroupName;
                    groupoId = menu.id;
                    tvGroup.setText(menu.chatGroupName);
                }
            });
        }
    }

    @Override
    public void bindEvents() {
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFriendActivity.this, AddGroupActivity.class);
                startActivity(intent);
            }
        });


        llChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.showAsDropDown(navigationBar);
                }

            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFriendRequest(dataBean.getNickname(), dataBean.getRingLetterName());


            }
        });
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {
                Intent intent = new Intent(AddFriendActivity.this, NewPersonDetailActivity.class);
                intent.putExtra("id", dataBean.getRingLetterName() + "");
                startActivity(intent);
            }

            @Override
            public void navigationimg() {

            }
        });
    }

    private String groupoId;
    private String groupName;
    public static final String FAVATER = "hx_favater";
    public static final String FUID = "hx_fuid";
    public static final String FNAME = "hx_fname";
    private void addFriendRequest(final String nickName, final String hxNameId) {



//发送消息

        showProgress(getString(R.string.tjz));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getString(this, "userInfoId", ""));
        map.put("token", SpUtil.getString(this, "token", ""));
        map.put("chatUserId", hxNameId);
        map.put("groupId", groupoId);
        map.put("groupName", groupName);

        Call<BaseResponse> call = api.addFriend(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse bs = response.body();
                if (TextUtil.isNotEmpty(etContent.getText().toString())) {
                    //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                    EMMessage message = EMMessage.createTxtSendMessage(etContent.getText().toString(), hxNameId);
                    if(info!=null){
                        message.setAttribute(FAVATER, info.nickname);
                        message.setAttribute(FNAME, etContent.getText().toString());
                        message.setAttribute(FUID, hxNameId);
                    }else {
                        message.setAttribute(FNAME, nickName);
                    }
                    EMMessage msg = HxMessageUtils.exMsg(message, new CHATMESSAGE());
                    //send message
                    EMClient.getInstance().chatManager().sendMessage(message);
                }else {
                    EMMessage message = EMMessage.createTxtSendMessage(getString(R.string.wmyshy), hxNameId);
                    if(info!=null){
                        message.setAttribute(FAVATER, info.head_image);
                        message.setAttribute(FNAME,info.nickname );
                        message.setAttribute(FUID, hxNameId);
                    }else {
                        message.setAttribute(FNAME, nickName);
                    }

                    EMMessage msg = HxMessageUtils.exMsg(message, new CHATMESSAGE());
                    //send message
                    EMClient.getInstance().chatManager().sendMessage(message);
                }
                if (bs.status == 301) {
                    presenter.addRemark( dataBean.getRingLetterName(),etNick.getText().toString(),"","");


                } else {
                    Toast.makeText(AddFriendActivity.this, bs.message, Toast.LENGTH_SHORT).show();
                    dimessProgress();
                }



            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LogUtil.d(t.toString());
                Toast.makeText(AddFriendActivity.this, R.string.tjhysb, Toast.LENGTH_SHORT).show();
                dimessProgress();
            }
        });
    }

    @Override
    public void onResume() {
        if (presenter != null) {
            presenter.getChatGroupList();
        }
        super.onResume();
    }

    @Override
    public void initData() {

    }


    public void setData() {

    }

    private FriendInfo info;
    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 22) {
            game = (GAME) entity;
            init();
        }else if (type == 999) {
            Toast.makeText(AddFriendActivity.this, R.string.hyfscg, Toast.LENGTH_SHORT).show();
            RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FRIEND, ""));
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
            dimessProgress();
        } else if (type == 79) {
            info = (FriendInfo) entity;

        } else {
            List<GAME> list = (List<GAME>) entity;
            adapter.setDatas(list);
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        if(TextUtil.isNotEmpty(msg)){
            if(msg.equals("该用户已属于您的好友")){
                finish();
            }
        }
        showToasts(msg);
    }


}
