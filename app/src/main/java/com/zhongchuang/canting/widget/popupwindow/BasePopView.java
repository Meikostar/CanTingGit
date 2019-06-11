package com.zhongchuang.canting.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

public abstract class BasePopView implements View.OnClickListener {
    protected Activity activity;
    public View popView;
    public PopupWindow pop;
    protected LayoutInflater infalter;

    public BasePopView(Activity activity) {
        this.activity = activity;
        infalter = LayoutInflater.from(activity);
        popView = initPopView(infalter);
        initPopUpView();
    }

    protected abstract View initPopView(LayoutInflater infalter);

    public void initPopUpView(){
        pop = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);
    }

    // 显示选择图片的popview
    public void showPopView() {
//        if (pop == null) {
//            pop = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT);
//            pop.setFocusable(true);
//            pop.setOutsideTouchable(true);
//            ColorDrawable dw = new ColorDrawable(0xb0000000);
//            // 设置SelectPicPopupWindow弹出窗体的背景
//            pop.setBackgroundDrawable(dw);
//        }
        if (!pop.isShowing()) {
            pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.RIGHT
                    | Gravity.BOTTOM, 0, 0);
        }

    }
    // 窗口显示前显示输入法软键盘
    protected void showKeyBoard()
    {
        InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        pop.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层
    }

    public void dismiss() {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
//        InputMethodManager inputMethodManager = (InputMethodManager)
//                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    public boolean isShowing() {
        return pop.isShowing();
    }
}