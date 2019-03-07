package com.zhongchuang.canting.base;

import android.content.Intent;
import android.os.Bundle;

import com.zhongchuang.canting.activity.LoginActivity;
import com.zhongchuang.canting.app.CanTingAppLication;


/**
 * 基类页面,可判断是否登录,并进行登录跳转和登录后返回
 *
 * Created by Administrator on 2015/8/14 0014.
 */
public abstract class BaseLoginActivity extends BaseActivity {

    private boolean isCreated = false;





    @Deprecated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果用户未登陆,则跳转到登陆页面
        if(!CanTingAppLication.getInstance().isLogin()){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ClassName", this.getClass().getName());
            //附加数据
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                intent.putExtras(bundle);
            }
            startActivity(intent);
            finish();

        }else{

            _onCreate(savedInstanceState);

            isCreated = true;
        }
    }

    @Deprecated
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isCreated){
            _onDestroy();
        }

    }



    protected abstract void _onCreate(Bundle savedInstanceState);

    protected abstract void _onDestroy();

}
