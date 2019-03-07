package com.zhongchuang.canting.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.PersonMessageActivity;
import com.zhongchuang.canting.activity.RegistActivity;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.presenter.impl.RegisterPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.RegisterViewCallback;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

;

/**
 * Created by Administrator on 2017/10/29.
 */

public class Regist_PasswordFragment extends Fragment implements RegisterViewCallback {


    @BindView(R.id.login_pass)
    ClearEditText loginPass;
    @BindView(R.id.regist_pas_check)
    TextView registPasCheck;
    @BindView(R.id.regist_ps_xieyi_yes)
    ImageView registPsXieyiYes;
    @BindView(R.id.regisit2_yanzhen_weixin)
    ImageView regisit2YanzhenWeixin;
    @BindView(R.id.regisit2_yanzhen_qq)
    ImageView regisit2YanzhenQq;
    @BindView(R.id.regisit2_yanzhen_weibo)
    ImageView regisit2YanzhenWeibo;
    private Unbinder unbinder;


    private ProgressDialog mDialog;
    private ProgressDialog mDialogs;


    private RegisterPresenter presenter;


    public Regist_PasswordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regist_password, container, false);

        unbinder = ButterKnife.bind(this, view);
        presenter = new RegisterPresenterImpl(this);

        //String passST = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showPopwinds(final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(getActivity(), R.layout.base_dailog_view, null);
        sure = (TextView) views.findViewById(R.id.txt_sure);
        cancel = (TextView) views.findViewById(R.id.txt_cancel);
        title = (TextView) views.findViewById(R.id.txt_title);

        title.setText(R.string.qdqxgdd);
        final MarkaBaseDialog dialog = BaseDailogManager.getInstance().getBuilder(getActivity()).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
    }
    @OnClick({R.id.regist_pas_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regist_pas_check:
                String passWord = loginPass.getText().toString().trim();
                if (TextUtils.isEmpty(passWord)) {
                    ToastUtils.showNormalToast(getString(R.string.mmbnwk));
                } else {
                    register();
                }
                break;

        }
    }


    private void register() {
        Map<String, String> map = new HashMap();

        String token = SpUtil.getString(getActivity(), "token", "");
        String userInfoId = SpUtil.getString(getActivity(), "userInfoId", "");
        //code = SpUtil.getString(getActivity(),"code","");

        map.put("token", token);
        map.put("userInfoId", userInfoId);
        map.put("password", loginPass.getText().toString().trim());
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("登录中，请稍后...");
        mDialog.show();
        presenter.register(map);
    }

    private void initDta(String hunXinName, String hunXinPawd) {


        mHuanXingLogin(hunXinName, hunXinPawd);
    }


    private void mHuanXingLogin(String hunXinName, String hunXinPawd) {

        EMClient.getInstance().login(hunXinName, hunXinPawd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //Toast.makeText(getActivity(),"登录聊天服务器成功",Toast.LENGTH_SHORT).show();
                Log.d("main+1", "登录聊天服务器成功！");
                UserInfo userInfo = new UserInfo();
                userInfo.setUserInfoId(SpUtil.getString(getActivity(), "userInfoId", ""));
                userInfo.setToken(SpUtil.getString(getActivity(), "token", ""));
                userInfo.setRingLetterName(SpUtil.getString(getActivity(), "ringLetterName", ""));
                userInfo.setRingLetterPwd(SpUtil.getString(getActivity(), "ringLetterPwd", ""));
                CanTingAppLication.getInstance().setUserInfo(userInfo);

                mDialog.dismiss();
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SIGN, ""));
                Intent registToZhuFrag=new Intent(getActivity(),PersonMessageActivity.class);
                registToZhuFrag.putExtra("type",1);
                startActivity(registToZhuFrag);
                getActivity().finish();


            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

                if (mDialog != null) {
                    mDialog.dismiss();
                }
                Log.d("main+1", "登录聊天服务器失败！" + code + "  " + message.toString());
            }
        });


    }

    @Override
    public void onResultSuccess(UserLoginBean userLoginBean) {

    }

    @Override
    public void onFail(int code, String msg) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void getYzm(BaseResponse response) {

    }

    @Override
    public void checkCode(CodeCheckBean codeCheckBean) {

    }

    @Override
    public void setPassWordSuccess(BaseResponse baseResponse) {
        //环信登录
        login();
    }

    private void mHuanXingLogin(final UserInfo db) {

        EMClient.getInstance().login(db.getRingLetterName(), db.getRingLetterPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {

                ToastUtils.showNormalToast(getString(R.string.dlcg));
                SpUtil.putString(getActivity(), "mobileNumber",(CanTingAppLication.code.equals("86")?"": CanTingAppLication.code)+ CanTingAppLication.mobileNumber);//手机号
                SpUtil.putString(getActivity(), "password", loginPass.getText().toString());//密码
                SpUtil.putString(getActivity(), "token", db.getToken());//token值
                CanTingAppLication.userId = db.getUserInfoId();
                CanTingAppLication.signStr = db.signStr;
                CanTingAppLication.isLogin = true;
                SpUtil.putString(getActivity(), "userInfoId", db.getUserInfoId());//userId值
                SpUtil.putString(getActivity(), "ringLetterName", db.getRingLetterName());//环信登录名
                SpUtil.putString(getActivity(), "ringLetterPwd", db.getRingLetterPwd());//环信登录密码
                SpUtil.putString(getActivity(), "name", db.getNickname());//环信登录密码
                SpUtil.putString(getActivity(), "ava", db.getHeadImage());//环信登录密码

                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main+1", "登录聊天服务器成功！");
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                CanTingAppLication.getInstance().setUserInfo(db);

                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SIGN, ""));
                Intent registToZhuFrag=new Intent(getActivity(),PersonMessageActivity.class);
                registToZhuFrag.putExtra("type",1);
                startActivity(registToZhuFrag);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                //Toast.makeText(getActivity(),"登录聊天服务器失败",Toast.LENGTH_SHORT).show();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                Log.d("main+1", "登录聊天服务器失败！" + code + "  " + message.toString());
                Intent registToZhuFrag=new Intent(getActivity(),PersonMessageActivity.class);
                registToZhuFrag.putExtra("type",1);
                startActivity(registToZhuFrag);
            }
        });

    }

    public void login() {


        netService api = HttpUtil.getInstance().create(netService.class);
        api.getLoginMess("1", (CanTingAppLication.code.equals("86")?"": CanTingAppLication.code)+CanTingAppLication.mobileNumber, loginPass.getText().toString()).enqueue(new BaseCallBack<UserLoginBean>() {
            @Override
            public void onSuccess(UserLoginBean db) {
                try {
                    mHuanXingLogin(db.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                ToastUtils.showNormalToast(t);
            }
        });
    }
}
