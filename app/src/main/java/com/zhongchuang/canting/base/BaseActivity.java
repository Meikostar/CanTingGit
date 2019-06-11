package com.zhongchuang.canting.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;


import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.MActivityManager;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.hud.HudHelper;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.Locale;


/**
 *   设置状态栏颜色的基类
 * Created by Administrator on 2016/12/17 0017.
 */

public class BaseActivity extends AppCompatActivity {

    private HudHelper helper;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStautsBar();

        String langueType = SpUtil.getLangueType(this);
//        String languageEnv = getLanguageEnv();
        if(TextUtil.isNotEmpty(CanTingAppLication.LangueType)){
            if(!CanTingAppLication.LangueType.equals(langueType)){
                updateActivity(langueType);
            }
        }else {
            updateActivity(langueType);

        }
    }

    /**
     * 刷新语言
     */
    public void updateActivity(String sta) {
        SpUtil.putString(this,"LangueType",sta);
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        startActivity((new Intent(this, HomeActivitys.class)));

    }



    private String getLanguageEnv() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String country = l.getCountry().toLowerCase();
        if ("zh".equals(language)) {
            if ("cn".equals(country)) {
                language = "zh-CN";
            } else if ("tw".equals(country)) {
                language = "zh-TW";
            }
        } else if ("pt".equals(language)) {
            if ("br".equals(country)) {
                language = "pt-BR";
            } else if ("pt".equals(country)) {
                language = "pt-PT";
            }
        }
        return language;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    protected void setStautsBar(){
//        StatusBarUtil.setColor(this,getResources().getColor(R.color.theme_color));

    }





    @Override
    protected void onResume() {
        super.onResume();
        MActivityManager.getInstance().addACT(this);

    }


    public void showPress(){
        if (helper==null){
            helper = HudHelper.getInstance();
        }
        helper.showPress(this);
    }

    public void showPress(String label){
        if (helper==null){
            helper = HudHelper.getInstance();
        }

        helper.showPress(this,label);
    }

    public void hidePress(){
        if (helper!=null){
            helper.hide();
        }
    }


}
