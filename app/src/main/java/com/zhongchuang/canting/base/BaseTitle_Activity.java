package com.zhongchuang.canting.base;

import android.content.Context;
import android.os.Bundle;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.MActivityManager;
import com.zhongchuang.canting.event.BaseEvent;
import com.zhongchuang.canting.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by fl on 2017/4/17.
 */

public abstract class BaseTitle_Activity extends BaseActivity {

    protected TitleBar titleBar;
    protected FrameLayout rootLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basetitle_activity);
        initContent();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new BaseEvent());
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent baseEvent){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        closeKeyBoard();
    }

    private void initContent() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        if (!isTitleShow()){
            titleBar.setVisibility(View.GONE);
        }
        rootLayout = (FrameLayout) findViewById(R.id.contain_content);
        rootLayout.addView(addContentView());
    }
    /**
     * 关闭键盘
     */
    public void closeKeyBoard() {
        try {
            if(this.getCurrentFocus() == null){
                return;
            }
            IBinder iBinder = this.getCurrentFocus().getWindowToken();
            if (iBinder == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void initTile(String titleMsg) {
        titleBar.setBackgroundColor(getResources().getColor(R.color.coofeColor));
        titleBar.setLeftImageResource(R.mipmap.tab_back_btn);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.setActionTextColor(getResources().getColor(R.color.white));
        titleBar.setTitle(titleMsg);
        titleBar.setTitleColor(getResources().getColor(R.color.white));
        titleBar.setDividerHeight(0);
//        titleBar.addAction(new TitleBar.TextAction("注册") {
//            @Override
//            public void performAction(View view) {
//
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MActivityManager.getInstance().addACT(this);
    }

    public abstract View addContentView();
    public abstract boolean isTitleShow();
}
