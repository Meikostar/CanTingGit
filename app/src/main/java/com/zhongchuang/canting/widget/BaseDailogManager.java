package com.zhongchuang.canting.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.zhongchuang.canting.R;;


/***
 * 功能描述:Dialog实例管理
 * 作者:chenwei
 * 时间:2016/11/28
 * 版本:1.0
 ***/

public class BaseDailogManager {
    public static final int LEFT_BUTTON= MarkaBaseDialog.Builder.LEFT_BUTTON;
    public static final int RIGHT_BUTTON=MarkaBaseDialog.Builder.RIGHT_BUTTON;
    private MarkaBaseDialog mDialog;
    private static BaseDailogManager instance;

    private BaseDailogManager(){}

    public static synchronized BaseDailogManager getInstance(){
        if (instance==null){
            instance = new BaseDailogManager();
        }
        return instance;
    }

    public Builder getBuilder(Activity context){
        return new Builder(context);
    }

    public class Builder extends MarkaBaseDialog.Builder{
        public Builder(Context context) {
            super(context);
        }

        @Override
        public MarkaBaseDialog create() {
            if (mDialog!=null && mDialog.isShowing()){
                mDialog.dismiss();
            }
            mDialog = super.create();
            return mDialog;
        }

        @Override
        public MarkaBaseDialog.Builder setOnClickListener(DialogInterface.OnClickListener listener) {
            return super.setOnClickListener(listener);
        }
    }

    public static void showTip(Activity context, String title, String message){
        MarkaBaseDialog.Builder builder = getInstance().getBuilder(context)
                .setMessage(message)
                .setRightButtonText(context.getString(R.string.wzdl))
                .setOnClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!TextUtils.isEmpty(title)){
            builder.setTitle(title).haveTitle(true);
        }else {
            builder.haveTitle(false);
        }
        builder.create().show();
    }
}
