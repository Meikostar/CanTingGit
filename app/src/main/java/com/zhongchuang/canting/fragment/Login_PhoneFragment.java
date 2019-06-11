package com.zhongchuang.canting.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.CityPickerActivity;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.activity.RegistActivity;
import com.zhongchuang.canting.activity.RigsterActivity;
import com.zhongchuang.canting.activity.HomeActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseFragment;
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
import com.zhongchuang.canting.utils.PhoneCheck;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.RegisterViewCallback;
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

public class Login_PhoneFragment extends BaseFragment implements RegisterViewCallback {


    @BindView(R.id.login_yanzhen_phone)
    ClearEditText phoneEt;
    @BindView(R.id.login_yanzhen_send)
    TextView sendCode;
    @BindView(R.id.login_yanzhen_yanzhenma)
    ClearEditText codeEt;
    @BindView(R.id.login_yanzhen_login)
    TextView loginBtn;

    @BindView(R.id.login_yanzhen_question)
    TextView loginYanzhenQuestion;
    @BindView(R.id.login_yanzhen_weixin)
    ImageView loginYanzhenWeixin;
    @BindView(R.id.login_yanzhen_qq)
    ImageView loginYanzhenQq;
    @BindView(R.id.login_yanzhen_weibo)
    ImageView loginYanzhenWeibo;
    Unbinder unbinder;
    @BindView(R.id.tv_registered)
    TextView tvRegistered;
    @BindView(R.id.txt_choose)
    TextView txt_choose;

    private RegisterPresenter presenter;

    private TimeCount timeCount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_phone, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new RegisterPresenterImpl(this);
        //计时器
        timeCount = new TimeCount(60000, 1000);

        initView();

        return view;
    }
    private int type;
    public void setType(int type){
        this.type=type;
    }
    private void initView() {

        txt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),0);
            }
        });
        tvRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logintent = new Intent(getActivity(),RegistActivity.class);
                startActivity(logintent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        txt_choose.setText("+"+CanTingAppLication.code+">");
    }

    //计时器
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(sendCode!=null){
                sendCode.setEnabled(false);
                sendCode.setText(millisUntilFinished / 1000 + getString(R.string.cshq));
            }

        }

        @Override
        public void onFinish() {
            if(sendCode!=null){
                sendCode.setText(R.string.cxhq);
                sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
                sendCode.setEnabled(true);
            }

        }
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        unbinder.unbind();
     timeCount.cancel();

    }
    private ProgressDialog mDialog;
    private void mHuanXingLogin(final UserInfo db) {

        EMClient.getInstance().login(db.getRingLetterName(), db.getRingLetterPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {

                ToastUtils.showNormalToast(getString(R.string.dlcg));
                SpUtil.putString(getActivity(), "mobileNumber", (CanTingAppLication.code.equals("86")?"":CanTingAppLication.code)+phoneEt.getText().toString());//手机号

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
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                if(type==6){
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FINISH,""));

                }
                CanTingAppLication.getInstance().setUserInfo(db);
                Intent gotoZhuYe = new Intent(getActivity(), HomeActivitys.class);
                startActivity(gotoZhuYe);
                getActivity().finish();
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.SIGN, ""));

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                //Toast.makeText(getActivity(),"登录聊天服务器失败",Toast.LENGTH_SHORT).show();
                Log.d("main+1", getString(R.string.dlltfwqsb) + code + "  " + message.toString());
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                //logOut();
            }
        });

    }

    public void login(){
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getString(R.string.dlzqsh));
        mDialog.show();
       netService api = HttpUtil.getInstance().create(netService.class);
       api.getCodeLogin("5",(CanTingAppLication.code.equals("86")?"":CanTingAppLication.code)+ phoneEt.getText().toString(), codeEt.getText().toString()).enqueue(new BaseCallBack<UserLoginBean>() {
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
    @OnClick({R.id.login_yanzhen_send, R.id.login_yanzhen_yanzhenma, R.id.login_yanzhen_login,  R.id.login_yanzhen_question, R.id.login_yanzhen_weixin, R.id.login_yanzhen_qq, R.id.login_yanzhen_weibo})
    public void onViewClicked(View view) {
        String phoneNumber = phoneEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.login_yanzhen_send:

                if ((!TextUtils.isEmpty(phoneNumber)) && PhoneCheck.judgePhoneNums(phoneNumber)) {

                    presenter.getYzm("3", (CanTingAppLication.code.equals("86")?"": CanTingAppLication.code)+phoneNumber,CanTingAppLication.code);
                } else {
                    ToastUtils.showNormalToast(getString(R.string.sjhbnwk));
                }

            case R.id.login_yanzhen_yanzhenma:
                //String checkCode = loginYanzhenYanzhenma.getText().toString().trim();

                break;
            case R.id.login_yanzhen_login:

                String check = codeEt.getText().toString().trim();
                if (!TextUtils.isEmpty(check)) {
//                    showPress();
//                    Map<String, String> map = new HashMap<>();
//                    map.put("mobileNumber", phoneNumber);
//                    map.put("code", check);
//                    presenter.checkCode(map);
                    if(type==6){
                        logOut();
                    }else{
                        login();
                    }

                } else {
                    ToastUtils.showNormalToast(getString(R.string.yzmbnwk));
                }
                break;

            case R.id.login_yanzhen_question:
                startActivity(new Intent(getActivity(), RigsterActivity.class));
                break;
            case R.id.login_yanzhen_weixin:
                break;
            case R.id.login_yanzhen_qq:
                break;
            case R.id.login_yanzhen_weibo:
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
                        login();
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

    private void initData() {

    }

    @Override
    public void onResultSuccess(UserLoginBean userLoginBean) {

    }

    @Override
    public void onFail(int code, String msg) {
        sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
        sendCode.setEnabled(true);
        ToastUtils.showNormalToast(msg);
        hidePress();
    }

    @Override
    public void getYzm(BaseResponse response) {
        timeCount.start();
        sendCode.setBackground(getResources().getDrawable(R.drawable.hui_blue_rectangle));
        sendCode.setEnabled(false);
        ToastUtils.showNormalToast(getString(R.string.yzmfscg));
    }

    @Override
    public void checkCode(CodeCheckBean codeCheckBean) {
        loginHx(codeCheckBean.getData());
//        CodeCheckBean.DataBean  db = codeCheckBean.getData();
    }

    @Override
    public void setPassWordSuccess(BaseResponse baseResponse) {

    }

    private void loginHx(final CodeCheckBean.DataBean db) {
        EMClient.getInstance().login(db.getRingLetterName(), db.getRingLetterPwd(), new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                //Toast.makeText(getActivity(),"登录聊天服务器成功",Toast.LENGTH_SHORT).show();
                Log.d("main+1", getString(R.string.dlltfwqcg));

                SpUtil.putString(getActivity(), "userInfoId", db.getUserInfoId());
                SpUtil.putString(getActivity(), "token", db.getToken());
                SpUtil.putString(getActivity(), "ringLetterName", db.getRingLetterName());
                SpUtil.putString(getActivity(), "ringLetterPwd", db.getRingLetterPwd());

                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.gotoZhuyeFrag();
                Toast.makeText(getActivity(), R.string.dlcgs, Toast.LENGTH_SHORT).show();
                hidePress();

//                RegistActivity registActivity= (RegistActivity) getActivity();
//                registActivity.gotoZhuyeFrag();


            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                hidePress();
                Log.d("main+1", getString(R.string.dlltfwqsb) + code + "  " + message.toString());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}




