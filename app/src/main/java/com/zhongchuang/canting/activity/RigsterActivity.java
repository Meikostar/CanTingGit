package com.zhongchuang.canting.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.base.BaseTitle_Activity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.PhoneCheck;
import com.zhongchuang.canting.widget.ClearEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RigsterActivity extends BaseTitle_Activity {


    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.login_yanzhen_phone)
    EditText phoneEt;
    @BindView(R.id.login_yanzhen_send)
    TextView sendCode;
    @BindView(R.id.login_yanzhen_yanzhenma)
    ClearEditText codeEt;
    @BindView(R.id.login_pass)
    ClearEditText loginPass;
    @BindView(R.id.login_pass_again)
    ClearEditText login_pass_again;
    @BindView(R.id.login_yanzhen_login)
    TextView loginYanzhenLogin;
    @BindView(R.id.txt_choose)
    TextView txt_choose;
    private TimeCount timeCount;
    @Override
    public View addContentView() {
        return getLayoutInflater().inflate(R.layout.activity_rigster, null);
    }

    @Override
    public boolean isTitleShow() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        timeCount = new TimeCount(60000, 1000);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        txt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RigsterActivity.this, CityPickerActivity.class),0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timeCount!=null){
            timeCount.cancel();
        }

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
                sendCode.setText(millisUntilFinished / 1000 + "s重新获取");
            }

        }

        @Override
        public void onFinish() {
            if(sendCode!=null){
                sendCode.setText(getString(R.string.cxhq));
                sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
                sendCode.setEnabled(true);
            }

        }
    }
    @OnClick({R.id.login_yanzhen_send, R.id.login_yanzhen_yanzhenma, R.id.login_yanzhen_login})
    public void onViewClicked(View view) {
        String phoneNumber = phoneEt.getText().toString().trim();
        switch (view.getId()) {
            case R.id.login_yanzhen_send:

                if ((!TextUtils.isEmpty(phoneNumber)) && PhoneCheck.judgePhoneNums(phoneNumber)) {
                    getCode( (CanTingAppLication.code.equals("86")?"":CanTingAppLication.code)+phoneNumber);
                } else {
                    ToastUtils.showNormalToast(getString(R.string.sjhbnwk));
                }

            case R.id.login_yanzhen_yanzhenma:
                //String checkCode = loginYanzhenYanzhenma.getText().toString().trim();

                break;
            case R.id.login_yanzhen_login:

                String check = codeEt.getText().toString().trim();
                String pwd = loginPass.getText().toString().trim();
                String surePwd = login_pass_again.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showNormalToast(getString(R.string.qsrxmm));
                    return;
                }
                if (TextUtils.isEmpty(surePwd)) {
                    ToastUtils.showNormalToast(getString(R.string.qqrxmm));
                    return;
                }
                if (!pwd.equals(surePwd)) {
                    ToastUtils.showNormalToast(getString(R.string.lcmmbyz));
                    return;
                }
                if ((!TextUtils.isEmpty(phoneNumber)) && PhoneCheck.judgePhoneNums(phoneNumber)) {

                } else {
                    ToastUtils.showNormalToast(getString(R.string.sjhbnwk));
                    return;
                }

                if (TextUtils.isEmpty(check)) {
                    ToastUtils.showNormalToast(getString(R.string.yzmbnwk));
                    return;
                }

                mDialog = new ProgressDialog(this);
                mDialog.setMessage(getString(R.string.zhz));
                mDialog.show();
                changePassWord(check,phoneNumber,pwd);
                break;


        }
    }
   private ProgressDialog mDialog;

    private void initData() {

    }


    private void loginHx(final CodeCheckBean.DataBean db) {
    }

    public void getCode(String mobile) {


        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", mobile);
        map.put("smsType",  2+"");
        map.put("type", CanTingAppLication.code.equals("86")?1+"":2+"");

        netService api = HttpUtil.getInstance().create(netService.class);
        api.getCall(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse bean) {
                timeCount.start();
                sendCode.setBackground(getResources().getDrawable(R.drawable.hui_blue_rectangle));
                sendCode.setEnabled(false);
              ToastUtils.showNormalToast(getString(R.string.fscg));
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                sendCode.setBackground(getResources().getDrawable(R.drawable.login_selector));
                sendCode.setEnabled(true);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    public void changePassWord(String code, String phone,String password) {
        Map<String, String> map = new HashMap<>();
        map.put("mobileNumber", phone);
        map.put("code",  code);
        map.put("pwd", password);
        map.put("password", password);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.updatePwd(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse bean) {
                ToastUtils.showNormalToast(getString(R.string.xgcg));
                if(mDialog!=null){
                    mDialog.dismiss();
                }

                finish();

            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                if(mDialog!=null){
                    mDialog.dismiss();
                }
                ToastUtils.showNormalToast(t);
            }
        });
    }
}