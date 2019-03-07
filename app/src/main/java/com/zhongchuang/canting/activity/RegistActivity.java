package com.zhongchuang.canting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.fragment.Regiet_PhoneFragment;
import com.zhongchuang.canting.fragment.Regist_ChecknumFragment;
import com.zhongchuang.canting.fragment.Regist_PasswordFragment;
import com.zhongchuang.canting.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/10/29.
 */

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.regist_inp_phnum)
    TextView registInpPhnum;
    @BindView(R.id.regist_inp_chknum)
    TextView registInpChknum;
    @BindView(R.id.regist_inp_phpass)
    TextView registInpPhpass;
    @BindView(R.id.regist_fragm)
    FrameLayout registFragm;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private int index = 0;

    private FragmentTransaction regTransition;
    private Regiet_PhoneFragment mPhone_regietFragment;
    private Regist_ChecknumFragment mRegist_ChecknumFragment;

    private Regist_PasswordFragment mRegist_PasswordFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist);

        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        switch (index){
//            case 0:
//                break;
            case 1:
                repalceCheckNumFrag();
                break;
            case 2:
                repalcePassWordFrag();
                break;
        }
    }

    private void initView() {
        index = 0;
        regTransition = getSupportFragmentManager().beginTransaction();

        removeAllFragment(regTransition);
        mPhone_regietFragment = new Regiet_PhoneFragment();
        regTransition.replace(R.id.regist_fragm, mPhone_regietFragment);
        regTransition.commit();
        registInpPhnum.setTextColor(this.getResources().getColor(R.color.wordColor));

    }


    public void repalceCheckNumFrag() {
        regTransition = getSupportFragmentManager().beginTransaction();
        mRegist_ChecknumFragment = new Regist_ChecknumFragment();
        regTransition.replace(R.id.regist_fragm, mRegist_ChecknumFragment);
        regTransition.commit();
        index = 1;
        registInpPhnum.setTextColor(this.getResources().getColor(R.color.color6));
        registInpChknum.setTextColor(this.getResources().getColor(R.color.wordColor));

    }

    public void repalcePassWordFrag() {
        regTransition = getSupportFragmentManager().beginTransaction();

        mRegist_PasswordFragment = new Regist_PasswordFragment();
        regTransition.replace(R.id.regist_fragm, mRegist_PasswordFragment);
        regTransition.commit();
        index = 2;
        registInpChknum.setTextColor(this.getResources().getColor(R.color.color6));
        registInpPhpass.setTextColor(this.getResources().getColor(R.color.wordColor));

    }


    private void removeAllFragment(FragmentTransaction mTransaction) {
        if (mPhone_regietFragment != null) {
            mTransaction.remove(mPhone_regietFragment);
        }
        if (mRegist_ChecknumFragment != null) {
            mTransaction.remove(mRegist_ChecknumFragment);
        }
        if (mRegist_PasswordFragment != null) {
            mTransaction.remove(mRegist_PasswordFragment);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("注册调用销毁");
    }

    public void gotoZhuyeFrag() {

        finish();
}

    @Override
    protected void onStop() {
        super.onStop();
//        this.finish();
    }
}


