package com.zhongchuang.canting.widget;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.zhongchuang.canting.R;;


public class ClearEditText extends EditText implements
        OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用 
     */  
    private Drawable mClearDrawable;
    /** 
     * 控件是否有焦点 
     */  
    private boolean hasFoucs;
    private boolean isShow=false;


    private boolean isSwitch2PwdFunction;
    private boolean mbDisplayFlg;
    /**
     * 显示密码与否的功能
     * */
    public void pwdDispalyFunction(boolean b) {
        isSwitch2PwdFunction = b;
    }

    //监听输入框文字变化的listenr
    public interface ClearEditTextListener
    {
        void afterTextChanged4ClearEdit(Editable s);
        void changeText(CharSequence s);
    }
    public void setOnClearEditTextListener(ClearEditTextListener listener)
    {
        this.listener = listener;
    }

    private ClearEditTextListener listener;
    public ClearEditText(Context context) {
        this(context, null);   
    }   
   
    public ClearEditText(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义  
        this(context, attrs, android.R.attr.editTextStyle);   
    }   
      
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);  
        init();  
    }  

    private void init() {   
        /**
        * 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
         setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
        * **/
        mClearDrawable = getCompoundDrawables()[2];   
        if (mClearDrawable == null) {   
//          throw new NullPointerException("You can add drawableRight attribute in XML");  
            mClearDrawable = getResources().getDrawable(R.mipmap.lp_img_delete);
        }   
          
        mClearDrawable.setBounds(0-10, 0, mClearDrawable.getIntrinsicWidth()-10, mClearDrawable.getIntrinsicHeight());

        //默认设置隐藏图标
        setClearIconVisible(false);   
        //设置焦点改变的监听  
        setOnFocusChangeListener(this);   
        //设置输入框里面内容发生改变的监听  
        addTextChangedListener(this);   
    }   
   
   
    /** 
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和 
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑 
     */  
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {  
  
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())  
                        && (event.getX() < ((getWidth() - getPaddingRight())));  
                  
                if (touchable) {
                    if(isSwitch2PwdFunction){
//                        switch2PwdFunction();
                    }else{
                        this.setText("");
                    }
                }
            }  
        }  
  
        return super.onTouchEvent(event);  
    }

    /**
     * 切换到选择是否显示密码的功能
     * */
//    private void switch2PwdFunction() {
//                if (!mbDisplayFlg) {
//                    // display password text, for example "123456"
//                   setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.mykar_eye_ac,0);
//                } else {
//                    // hide password, display "."
//                   setTransformationMethod(PasswordTransformationMethod.getInstance());
//                    setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.mykar_eye_nor,0);
//                }
//
//                mbDisplayFlg = !mbDisplayFlg;
//               postInvalidate();
//    }

    /** 
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏 
     */  
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;  
        if (hasFocus) {   
            setClearIconVisible(getText().length() > 0&&isShows);
        } else {   
            setClearIconVisible(false);   
        }   
    }   
    public void setShow(boolean isShow){
        this.isShows=isShow;
    }
   
    /** 
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去 
     * @param visible 
     */  
    public void setClearIconVisible(boolean visible) {

        Drawable right = visible ? mClearDrawable : null;
        if(isShow){
             right=null;
        }
        setCompoundDrawables(getCompoundDrawables()[0],   
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);   
    }   
       
      
    /** 
     * 当输入框里面内容发生变化的时候回调的方法 
     */  
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
                if(hasFoucs&&isShow){
                    setClearIconVisible(s.length() > 0&&isShows);
                }
        if(listener !=null){
            listener.changeText(s);

        }

    }
   private boolean isShows=true;
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
           
    }   
   
    @Override
    public void afterTextChanged(Editable s) {
           if(listener !=null){
               listener.afterTextChanged4ClearEdit(s);

        }
    }   
    public void setdelteIconHilde(){
        isShow=true;
    }
     
    /** 
     * 设置晃动动画 
     */  
    public void setShakeAnimation(){  
        this.setAnimation(shakeAnimation(5));  
    }  
      
      
    /** 
     * 晃动动画 
     * @param counts 1秒钟晃动多少下 
     * @return 
     */  
    public static Animation shakeAnimation(int counts){
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);  
        return translateAnimation;  
    }  

}  