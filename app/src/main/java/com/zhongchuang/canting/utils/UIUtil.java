package com.zhongchuang.canting.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


/**
 *    UI大小转换类
 *
 */
public class UIUtil {

    public static int dp(Context context, int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics()) + 0.5F);
    }


    public static View infalte(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);

    }

    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return ( int ) (dpValue * scale + 0.5f);

    }

    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return ( int ) (pxValue / scale + 0.5f);

    }


    public static int px2sp(Context context, float pxValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return ( int ) (pxValue / fontScale + 0.5f);

    }

    public static int sp2px(Context context, float spValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return ( int ) (spValue * fontScale + 0.5f);

    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    public static double mapValueFromRangeToRange(
            double value,
            double fromLow,
            double fromHigh,
            double toLow,
            double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    /**
     * set margins of the specific view
     * @param target
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargin(View target, int l, int t, int r, int b){
        if (target.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
            p.setMargins(l, t, r, b);
            target.requestLayout();
        }
    }

    /**
     * convert drawable to bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static void hideKeyBroad(Context context,View v){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }


    public static double change(double a){
        return a * Math.PI  / 180;
    }

    public static double changeAngle(double a){
        return a * 180 / Math.PI;
    }

    public static DisplayMetrics displayMetrics(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        return dm;
    }

    /**
     * 对TextView、Button等设置不同状态时其文字颜色。
     * 参见：http://blog.csdn.net/sodino/article/details/6797821
     * Modified by liyujiang at 2015.08.13
     */
    public static ColorStateList toColorStateList(@ColorInt int normalColor, @ColorInt int pressedColor,
                                                  @ColorInt int focusedColor, @ColorInt int unableColor) {
        int[] colors = new int[]{pressedColor, focusedColor, normalColor, focusedColor, unableColor, normalColor};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList toColorStateList(@ColorInt int normalColor, @ColorInt int pressedColor) {
        return toColorStateList(normalColor, pressedColor, pressedColor, normalColor);
    }

    public static String formatNum(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }

    public static String formatMillisecond(int millisecond) {
        String retMillisecondStr;

        if (millisecond > 99) {
            retMillisecondStr = String.valueOf(millisecond / 10);
        } else if (millisecond <= 9) {
            retMillisecondStr = "0" + millisecond;
        } else {
            retMillisecondStr = String.valueOf(millisecond);
        }

        return retMillisecondStr;
    }


}
