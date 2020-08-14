package com.zhongchuang.canting.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.CityPickerActivity;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.RegistActivity;
import com.zhongchuang.canting.activity.RigsterActivity;
import com.zhongchuang.canting.activity.mall.SettingActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseViewCallBack;
import com.zhongchuang.canting.presenter.LoginPresenter;
import com.zhongchuang.canting.presenter.impl.LoginPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * Created by Administrator on 2017/10/27.
 */

@SuppressLint("ValidFragment")
public class Login_PassedwordFragment extends Fragment implements BaseViewCallBack<UserLoginBean> {

    @BindView(R.id.login_phone_tet)
    ClearEditText loginPhoneTet;
    @BindView(R.id.login_password_phone)
    LinearLayout loginPasswordPhone;
    @BindView(R.id.login_password_tex)
    ClearEditText loginPasswordTex;
    @BindView(R.id.login_password_password)
    LinearLayout loginPasswordPassword;
    @BindView(R.id.login_password_login)
    TextView loginPasswordLogin;
    @BindView(R.id.tv_found)
    TextView tv_found;
    @BindView(R.id.login_password_weiixn)
    ImageView loginPasswordWeiixn;
    @BindView(R.id.login_password_qq)
    ImageView loginPasswordQq;
    @BindView(R.id.login_password_weibo)
    ImageView loginPasswordWeibo;
    Unbinder unbinder;
    @BindView(R.id.tv_registered)
    TextView tvRegistered;
    @BindView(R.id.txt_choose)
    TextView txt_choose;

    //    private String intPhone;
//    private String intPass;
    private ProgressDialog mDialog;


//    private int code;
//    private Context mContext;

    private LoginPresenter loginRegisterPresenter;

    public Login_PassedwordFragment(Context mContext) {
//        this.mContext = mContext;
    }

    public Login_PassedwordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_password, container, false);
        loginRegisterPresenter = new LoginPresenterImpl(this);
        unbinder = ButterKnife.bind(this, view);
        tvRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logintent = new Intent(getActivity(),RegistActivity.class);
                startActivity(logintent);
            }
        });
        txt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),0);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_choose.setText("+"+CanTingAppLication.code+">");
    }
    private int type;
    public void setType(int type){
        this.type=type;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_found, R.id.login_phone_tet, R.id.login_password_tex, R.id.login_password_login, R.id.login_password_weiixn, R.id.login_password_qq, R.id.login_password_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_phone_tet:
                break;
            case R.id.tv_found:
                startActivity(new Intent(getActivity(), RigsterActivity.class));
                break;

            case R.id.login_password_tex:

                break;
            case R.id.login_password_login:

                String intPhone = loginPhoneTet.getText().toString().trim();
                String intPass = loginPasswordTex.getText().toString().trim();

//               String  phone = SpUtil.getString(getActivity(), "mobileNumber", "");
//               String  passWord = SpUtil.getString(getActivity(), "password", "");

                if (TextUtils.isEmpty(intPhone) || TextUtils.isEmpty(intPass)) {
                    Toast.makeText(getActivity(), R.string.yhhmmcw, Toast.LENGTH_SHORT).show();
                } else {
                    if(type==6){
                        logOut();
                    }else{
                        loginRegisterPresenter.login("1",(CanTingAppLication.code.equals("86")?"":CanTingAppLication.code) +intPhone, intPass);

                    }


                }
                break;

            case R.id.login_password_weiixn:
                break;
            case R.id.login_password_qq:
                break;
            case R.id.login_password_weibo:
                break;
        }
    }

    private void logOut() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
//                        tvLogin.setText("登录");
                        String intPhone = loginPhoneTet.getText().toString().trim();
                        String intPass = loginPasswordTex.getText().toString().trim();
                        loginRegisterPresenter.login("1",(CanTingAppLication.code.equals("86")?"":CanTingAppLication.code) +intPhone, intPass);

                        Log.d(TAG, "main+12: " + getString(R.string.dccg));
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        }).start();

    }
    private UserInfo userInfo;

    private void initDta(UserInfo db) {

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.dlzqsh));
        mDialog.show();
        userInfo = db;
        mHuanXingLogin(db);
    }


    private void mHuanXingLogin(final UserInfo db) {

        EMClient.getInstance().login(db.getRingLetterName(), db.getRingLetterPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {

                ToastUtils.showNormalToast(getString(R.string.dlcg));
                SpUtil.putString(getActivity(), "mobileNumber", loginPhoneTet.getText().toString());//手机号
                SpUtil.putString(getActivity(), "idCardFrontImg", db.idCardFrontImg);//手机号
                SpUtil.putString(getActivity(), "password", loginPasswordTex.getText().toString());//密码
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
                Log.d("main+1", getString(R.string.dlltfwqcg));
                mDialog.dismiss();
                CanTingAppLication.getInstance().setUserInfo(db);
                if(type==6){
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FINISH,""));

                }
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.gotoZhuyeFrag();
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SIGN, ""));

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                //Toast.makeText(getActivity(),"登录聊天服务器失败",Toast.LENGTH_SHORT).show();
                Log.d("main+1", getString(R.string.dlltfwqsb) + code + "  " + message.toString());

                if (code == 200) {
                    EMClient.getInstance().logout(true, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            mHuanXingLogin(userInfo);
                        }

                        @Override
                        public void onError(int i, String s) {

                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onResultSuccess(UserLoginBean loginMess) {

        initDta(loginMess.getData());


    }
    @Override
    public void onFail(int code, String msg) {

        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showNormalToast(msg);
        }

    }
}
