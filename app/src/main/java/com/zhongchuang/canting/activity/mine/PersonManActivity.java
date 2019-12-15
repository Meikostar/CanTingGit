package com.zhongchuang.canting.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.chat.AddFriendActivity;
import com.zhongchuang.canting.activity.chat.QfriendActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseLoginActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendInfo;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.easeui.DemoHelper;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.HxMessageUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.CircleTransform;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NavigationBar;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/14.
 */

public class PersonManActivity extends BaseLoginActivity implements BaseContract.View {


    @BindView(R.id.navigationBar)
    NavigationBar navigationBar;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_bei)
    TextView tvBei;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_group)
    TextView tvGroup;
    @BindView(R.id.ll_choose)
    LinearLayout llChoose;
    @BindView(R.id.tv_fir)
    TextView tvFir;
    @BindView(R.id.img_3)
    ImageView img3;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.imgs)
    ImageView imgs;
    @BindView(R.id.ll_qf)
    RelativeLayout llQf;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.tv_del)
    TextView tvDel;
    private Gson mGson;
    private UserInfo userInfo = CanTingAppLication.getInstance().getUserInfo();
    private String id;

    private int type = 0;
    private BasesPresenter presenter;

    @Override
    protected void _onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_man_detail);

        type = getIntent().getIntExtra("type", 0);

        ButterKnife.bind(this);
        navigationBar.setNavigationBarListener(new NavigationBar.NavigationBarListener() {
            @Override
            public void navigationLeft() {
                finish();
            }

            @Override
            public void navigationRight() {

            }

            @Override
            public void navigationimg() {

            }
        });
        id = getIntent().getStringExtra("id");
        presenter = new BasesPresenter(this);
        presenter.friendInfo(id);
        presenter.getCircleImage(id);
        initView();
        setEvents();
    }

    @Override
    protected void _onDestroy() {

    }

    private void initView() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(presenter!=null){
            presenter.friendInfo(id);


        }
    }

    private void setEvents() {

        llQf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonManActivity.this, QfriendActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendSearchBean.DataBean dataBean = new FriendSearchBean.DataBean();
                dataBean.setNickname(info.nickname);
                dataBean.setRingLetterName(id);
                Intent intent = new Intent(PersonManActivity.this, AddFriendActivity.class);
                intent.putExtra("data", HomeActivitys.messageGroup);
                intent.putExtra("datas", dataBean);
                startActivityForResult(intent, 5);
            }
        });
       ivImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(PersonManActivity.this, NewPersonDetailActivity.class);
               intent.putExtra("id", id + "");
               startActivity(intent);
           }
       });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow(TextUtil.isNotEmpty(info.remark_name) ? info.remark_name : info.nickname, id);
            }
        });
        llChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info == null) {
                    return;
                }

                if (info.isFriend == 1) {
                    Intent intent = new Intent(PersonManActivity.this, EditorInfoActivity.class);
                    intent.putExtra("info", info);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    ToastUtils.showNormalToast(getString(R.string.nhbshy));

                }


            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == 0) {
                this.finish();
            } else {

            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private View views = null;
    private TextView sure = null;
    private TextView cancel = null;
    private TextView title = null;
    private EditText reson = null;

    public void showPopwindow(String name, final String userid) {

        views = View.inflate(this, R.layout.del_friend, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.tv_title);
        reson = views.findViewById(R.id.edit_reson);
        title.setText(getString(R.string.qesc) + name + getString(R.string.hys));
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
                delFriend(userid);

                dialog.dismiss();
            }
        });


    }

    public void delFriend(final String userId) {


        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("chatUserId", userId);
        netService api = HttpUtil.getInstance().create(netService.class);
        api.delFriend(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse goodsSpeCate) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //删除和某个user会话，如果需要保留聊天记录，传false
                        EMClient.getInstance().chatManager().deleteConversation(userId, true);
                        RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SEND_DEL,""));
                        finish();
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

    private FriendInfo info;

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 79) {
            info = (FriendInfo) entity;
            if (info == null)
                return;
            Glide.with(this).load(StringUtil.changeUrl(info.head_image)).asBitmap().transform(new CircleTransform(this)).placeholder(R.drawable.editor_ava).into(ivImg);
            if (TextUtil.isNotEmpty(info.remark_name)) {
                tvBei.setText(info.remark_name);
                tvFir.setText(info.remark_name+getString(R.string.dllq));
                DemoHelper.getInstance().upDateName(info.remark_name,id);
            } else {
                tvBei.setText(info.nickname);
                tvFir.setText(info.nickname+getString(R.string.dllq));
            }
            if (TextUtil.isNotEmpty(info.nickname)) {
                tvName.setText(info.nickname);
            }
            if (TextUtil.isNotEmpty(info.remark_phone)) {
                tvPhone.setText(info.remark_phone);
            }
            if (TextUtil.isNotEmpty(info.remark_message)) {
                tvInfo.setText(info.remark_message);
            }
            if (info.isFriend == 1) {
                tvSend.setVisibility(View.INVISIBLE);
                tvDel.setVisibility(View.VISIBLE);
            } else {
                tvSend.setVisibility(View.VISIBLE);
                tvDel.setVisibility(View.GONE);
            }
            info.friendsId = id;
        } else {
            List<String> data = (List<String>) entity;
            if (data != null && data.size() > 0) {
                if (data.size() == 1) {
                    img3.setVisibility(View.INVISIBLE);
                    img2.setVisibility(View.INVISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                } else if (data.size() == 2) {
                    img3.setVisibility(View.INVISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(1)).asBitmap().placeholder(R.drawable.moren).into(img2);
                } else if (data.size() == 3) {
                    img3.setVisibility(View.VISIBLE);
                    img2.setVisibility(View.VISIBLE);
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(0)).asBitmap().placeholder(R.drawable.moren).into(img1);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(1)).asBitmap().placeholder(R.drawable.moren).into(img2);
                    Glide.with(this).load(QiniuUtils.baseurl + data.get(2)).asBitmap().placeholder(R.drawable.moren).into(img3);
                }
            }
        }

    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }



}
