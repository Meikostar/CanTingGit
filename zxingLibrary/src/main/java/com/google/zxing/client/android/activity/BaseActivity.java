
package com.google.zxing.client.android.activity;

import android.os.Bundle;

import com.google.zxing.client.android.utils.LogUtil;

//import lxm.meiko.zxinglibrary.zxing.utils.LogUtil;


/**
 * Activity管理类
 *
 * @author 咖枯
 * @version 1.0 2015
 */
public class BaseActivity extends SwipeBackActivity {

    /**
     * Log tag ：BaseActivity
     */
    private static final String LOG_TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LogUtil.i(LOG_TAG, getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟session的统计

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
