package com.zhongchuang.canting.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.RegistActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.presenter.impl.RegisterPresenterImpl;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.RegisterViewCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/10/29.
 */

public class Regist_ChecknumFragment extends Fragment implements RegisterViewCallback {


    @BindView(R.id.regist_check_phoneyanzhen)
    EditText registCheckPhoneyanzhen;
    @BindView(R.id.regist_check_num)
    TextView registCheckNum;
    @BindView(R.id.regist_ps_xieyi_yes)
    ImageView registPsXieyiYes;
    @BindView(R.id.regisit_yanzhen_weixin)
    ImageView regisitYanzhenWeixin;
    @BindView(R.id.regisit_yanzhen_qq)
    ImageView regisitYanzhenQq;
    @BindView(R.id.regisit_yanzhen_weibo)
    ImageView regisitYanzhenWeibo;
    Unbinder unbinder;

    public String checkNum;
    private String mobileNumber;
    private String userInfoId;
    private String token;
    private String ringLetterName;
    private String ringLetterPwd;

    private RegisterPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.regist_checknumber, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new RegisterPresenterImpl(this);
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();


    }


    @OnClick({R.id.regist_check_num, R.id.regist_ps_xieyi_yes, R.id.regisit_yanzhen_weixin, R.id.regisit_yanzhen_qq, R.id.regisit_yanzhen_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regist_check_num:
                checkNum = registCheckPhoneyanzhen.getText().toString().trim();
                // SMSSDK.submitVerificationCode("86", Regiet_PhoneFragment.mobileNumber, registCheckPhoneyanzhen.getText().toString());
                if (TextUtils.isEmpty(checkNum) || checkNum.equals("")) {
                    Toast.makeText(getActivity(), R.string.yzmbnwk, Toast.LENGTH_SHORT).show();
                }
//                CodeNet();
                mobileNumber = SpUtil.getString(getActivity(), "mobileNumber", "");
                Map<String, String> map = new HashMap<>();
                map.put("mobileNumber", (CanTingAppLication.code.equals("86")?"": CanTingAppLication.code)+mobileNumber);
                map.put("code", checkNum);
                presenter.checkCode(map);
                mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage(getString(R.string.jyz));
                mDialog.show();
                break;
            case R.id.regist_ps_xieyi_yes:
                break;
            case R.id.regisit_yanzhen_weixin:
                break;
            case R.id.regisit_yanzhen_qq:
                break;
            case R.id.regisit_yanzhen_weibo:
                break;
        }

    }
     private ProgressDialog mDialog;
    private void initPassWordFragment() {
        RegistActivity parentActivity = (RegistActivity) getActivity();
        parentActivity.repalcePassWordFrag();
        if(mDialog!=null){
            mDialog.dismiss();
        }
    }

    @Override
    public void onResultSuccess(UserLoginBean userLoginBean) {

    }

    @Override
    public void onFail(int code, String msg) {
        if(mDialog!=null){
            mDialog.dismiss();
        }
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getYzm(BaseResponse response) {

    }

    @Override
    public void checkCode(CodeCheckBean mCodeCheckBean) {
        userInfoId = mCodeCheckBean.getData().getUserInfoId();
        token = mCodeCheckBean.getData().getToken();
        ringLetterName = mCodeCheckBean.getData().getRingLetterName();
        ringLetterPwd = mCodeCheckBean.getData().getRingLetterPwd();

        SpUtil.putString(getActivity(), "userInfoId", userInfoId);
        SpUtil.putString(getActivity(), "token", token);
        SpUtil.putString(getActivity(), "ringLetterName", ringLetterName);
        SpUtil.putString(getActivity(), "ringLetterPwd", ringLetterPwd);
        SpUtil.putString(getActivity(), "code", checkNum);

        initPassWordFragment();
    }

    @Override
    public void setPassWordSuccess(BaseResponse baseResponse) {

    }


}
