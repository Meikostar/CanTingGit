package com.zhongchuang.canting.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/20
 * 版本:
 ***/
public class MPopupWindow extends PopupWindow {
    private IShowListener mListener;

    public MPopupWindow(Context context) {
        super(context);
    }

    public MPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MPopupWindow() {
    }

    public MPopupWindow(View contentView) {
        super(contentView);
    }

    public MPopupWindow(int width, int height) {
        super(width, height);
    }

    public MPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public MPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void setShowListener(IShowListener listener){
        this.mListener = listener;
    }
    public interface IShowListener{
        void onShow();
        void onDismiss();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        if (mListener!=null){
            mListener.onDismiss();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        if (mListener!=null){
            mListener.onShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h-yoff);
        }
        super.showAsDropDown(anchor, xoff, yoff);
        if (mListener!=null){
            mListener.onShow();
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
        if (mListener!=null){
            mListener.onShow();
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        if (mListener!=null){
            mListener.onShow();
        }
    }
}

