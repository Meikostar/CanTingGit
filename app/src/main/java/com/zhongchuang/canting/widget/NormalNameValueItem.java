package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.StringUtil;


/***
 * 功能描述:新增出入库单头部的列表项
 * 作者:qiujialiu
 * 时间:2016-08-26
 * 版本:
 ***/
public class NormalNameValueItem extends LinearLayout {
//    <enum name="type_number" value="1" />
//    <enum name="type_char" value="2" />
//    <enum name="type_number_decimal" value="3" />
//    <enum name="type_email" value="4" />
    private Context mContext;
    private String hint;
    private String content;
    private boolean haveImage;
    private boolean haveImages;
    private boolean haveBottomText;
    private boolean haveCheck;
    private boolean haveLine;
    private boolean borderTop;
    private boolean borderBottom;
    private boolean haveFocus;
    private String name;
    private String names;
    private View mView;
    private YunShlEditText mEditTextContent;
    private ImageView mImageViewArrows;
    private ImageView mImageViewImg;
    private CheckBox mImageViewCheck;
    private TextView mTextViewName;
    private TextView mTextViewNames;
    private LinearLayout mLinearLayoutItem;
    private LinearLayout bg;
    private View mViewBorderBottom;
    private View mViewBorderTop;
    private View mViewLine;
    private int maxLines;
    private int macLength;
    private float heigh;
    private int inputType;
    private int gravity;
    private float lineSpacingExtra;
    private int imgId;
    private OnFocusChangeListener mFocusChangeListener;

    public CheckBoxChangeListener listener;
  public  interface  CheckBoxChangeListener{
        void CheckBoxListener(boolean isCheck);
    }
    public void  setListener(CheckBoxChangeListener listener){
        this.listener=listener;
    }
    public NormalNameValueItem(Context context) {
        this(context,null);
    }

    public NormalNameValueItem(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NormalNameValueItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray mTypedArray = null;
        if (attrs!=null){
             mTypedArray = getResources().obtainAttributes(attrs,
                    R.styleable.NormalNameValueItem);
            hint = mTypedArray.getString(R.styleable.NormalNameValueItem_context_hint);
            content = mTypedArray.getString(R.styleable.NormalNameValueItem_context);
            haveImage = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_arrows,true);
            haveImages = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_img,false);
            haveBottomText = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_bottom_text,false);
            haveCheck = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_check,false);
            name = mTypedArray.getString(R.styleable.NormalNameValueItem_content_name);
            names = mTypedArray.getString(R.styleable.NormalNameValueItem_bottom_name);
            haveFocus = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_edit_have_focus,false);
            haveLine = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_line,true);
            maxLines = mTypedArray.getInt(R.styleable.NormalNameValueItem_max_lines,0);
            macLength = mTypedArray.getInt(R.styleable.NormalNameValueItem_max_length,0);
            heigh = mTypedArray.getFloat(R.styleable.NormalNameValueItem_heighs,0);
            imgId = mTypedArray.getResourceId(R.styleable.NormalNameValueItem_src_,0);
            inputType = mTypedArray.getInt(R.styleable.NormalNameValueItem_input_type,0);
            gravity = mTypedArray.getInt(R.styleable.NormalNameValueItem_text_gravity,0);
            borderBottom = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_bottom_border,false);
            borderTop = mTypedArray.getBoolean(R.styleable.NormalNameValueItem_have_top_border,false);
            lineSpacingExtra= mTypedArray.getFloat(R.styleable.NormalNameValueItem_line_spacing_extra,0);
        }
        initView();
       if (mTypedArray!=null){
           mTypedArray.recycle();
       }
    }
    public void setLocatonEnd(){
        mEditTextContent.setSelection(mEditTextContent.getText().length());
    }
    public void setEtRight(){
        mEditTextContent.setGravity(Gravity.RIGHT);
    }
    public void setEtColor(int id){
        mEditTextContent.setTextColor(getResources().getColor(id));
    }
    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mView = inflater.inflate(R.layout.item_normal_name_value,this);
        mEditTextContent = mView.findViewById(R.id.edt_value);
        mLinearLayoutItem = mView.findViewById(R.id.ll_item);
        bg = mView.findViewById(R.id.ll_bg);
        mTextViewName = mView.findViewById(R.id.tv_name);
        mTextViewNames = mView.findViewById(R.id.tv_names);
        mViewBorderBottom = mView.findViewById(R.id.view_border_bottom);
        mViewBorderTop = mView.findViewById(R.id.view_border_top);
        mViewLine = mView.findViewById(R.id.view_line);
        mImageViewArrows = mView.findViewById(R.id.iv_arrows);
        mImageViewImg = mView.findViewById(R.id.iv_img);
        mImageViewCheck = mView.findViewById(R.id.cb_check);
        if (StringUtil.isNotEmpty(hint)){
            mEditTextContent.setHint(hint);
        }
        if (StringUtil.isNotEmpty(content)){
            mEditTextContent.setText(content);
        }
         if(heigh!=0){
             ViewGroup.LayoutParams params = mLinearLayoutItem.getLayoutParams();
             params.width = ViewGroup.LayoutParams.MATCH_PARENT;
             params.height = DensityUtil.dip2px(heigh);
             mLinearLayoutItem.setLayoutParams(params);
         }
        if (StringUtil.isNotEmpty(name)){
            mTextViewName.setText(name);
        }
        if (StringUtil.isNotEmpty(names)){
            mTextViewNames.setText(names);
        }
        if (haveImage){
            mImageViewArrows.setVisibility(VISIBLE);
        }else {
            mImageViewArrows.setVisibility(GONE);
        }
        if(imgId!=0){
            mImageViewImg.setImageResource(imgId);
        }
        if (haveImages){
            mImageViewImg.setVisibility(VISIBLE);
        }else {
            mImageViewImg.setVisibility(GONE);
        }
        if (haveBottomText){
            mTextViewNames.setVisibility(VISIBLE);
        }else {
            mTextViewNames.setVisibility(GONE);
        }
        if (haveCheck){
            mImageViewCheck.setVisibility(VISIBLE);
        }else {
            mImageViewCheck.setVisibility(GONE);
        }
        mImageViewCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener!=null) {
                    listener.CheckBoxListener(isChecked);
                }
            }
        });

        if (lineSpacingExtra>0f){
            mEditTextContent.setLineSpacing(lineSpacingExtra,1f);
        }

        if (gravity==1){
            mEditTextContent.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        }else {
            mEditTextContent.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
        }

        if (borderBottom){
            mViewBorderBottom.setVisibility(VISIBLE);
            mViewLine.setVisibility(GONE);
        }else {
            mViewBorderBottom.setVisibility(GONE);
            if (haveLine){
                mViewLine.setVisibility(VISIBLE);
            }else {
                mViewLine.setVisibility(GONE);
            }
        }

        if (borderTop){
            mViewBorderTop.setVisibility(VISIBLE);
        }else {
            mViewBorderTop.setVisibility(GONE);
        }

        if (haveFocus){
            mEditTextContent.setFocusable(true);
            mEditTextContent.setFocusableInTouchMode(true);
            //mEditTextContent.requestFocus();
        }else {
            mEditTextContent.setFocusable(false);
            mEditTextContent.setFocusableInTouchMode(false);
            mEditTextContent.clearFocus();
        }
        mEditTextContent.addFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusChangeListener!=null) {
                    mFocusChangeListener.onFocusChange(v,hasFocus);
                }
            }
        });
        if (maxLines>1){
            mEditTextContent.setMaxLines(maxLines);
        }else if (maxLines==1){
            mEditTextContent.setSingleLine(true);
        }else {
            mEditTextContent.setMaxLines(Integer.MAX_VALUE);
        }
        if (macLength>0){
            mEditTextContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(macLength)});
        }
        switch (inputType){
            case 1:
                mEditTextContent.setType(YunShlEditText.TYPE_NUMBER);
                mEditTextContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:
                mEditTextContent.setType(YunShlEditText.CATEGORY_CHARACTER);
                break;
            case 3:
                mEditTextContent.setType(YunShlEditText.TYPE_NUMBER_DECIMAL);
                mEditTextContent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_CLASS_NUMBER);
                break;
            case 4:
                mEditTextContent.setType(YunShlEditText.CATEGORY_EMAIL);
                break;
            case 5:
                mEditTextContent.setInputType(InputType.TYPE_CLASS_PHONE);
                mEditTextContent.setType(YunShlEditText.NO_FILTER);
                break;
            case 6:
                mEditTextContent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_CLASS_NUMBER);
                break;
            case 7:
                mEditTextContent.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL| InputType.TYPE_CLASS_NUMBER);
               // mEditTextContent.setFilters(new InputFilter[]{TextViewUtil.get2NumPoint(2,9)});
                break;
            case 8:
                mEditTextContent.setType(YunShlEditText.CATEGORY_NUMBER_);
                // mEditTextContent.setFilters(new InputFilter[]{TextViewUtil.get2NumPoint(2,9)});
                break;
            default:
                break;
        }

        mEditTextContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (haveFocus){
                    return false;
                }else {
                    NormalNameValueItem.this.onTouchEvent(event);
                    return true;
                }
            }
        });
    }
   public void setChangeImg(int id){
      mImageViewImg.setImageResource(id);
  }
   public void setFous(boolean isFous){
       if (isFous){
           mEditTextContent.setFocusable(true);
           mEditTextContent.setFocusableInTouchMode(true);
           //mEditTextContent.requestFocus();
       }else {
           mEditTextContent.setFocusable(false);
           mEditTextContent.setFocusableInTouchMode(false);
           mEditTextContent.clearFocus();
       }
   }
    public YunShlEditText getContentEditText(){
        return mEditTextContent;
    }

    public void setContent(CharSequence content){
        if (StringUtil.isNotEmpty(content)){
            mEditTextContent.setText(content);
        }else {
            mEditTextContent.setText("");
        }
    }

   public void setColor(int color){
       mEditTextContent.setTextColor(color);
   }
   public void setColors(int color){
       mTextViewName.setTextColor(color);
   }
   public void setSize(float size){
       mTextViewName.setTextSize(size);
   }

    public void setName(CharSequence name){
        if (StringUtil.isNotEmpty(name)){
            mTextViewName.setText(name);
        }
    }

    public boolean isChecked(){
        return mImageViewCheck.isChecked();
    }




    public enum TextGravity{
        Right,Left
    }

    public void addView(View view){

    }

    public void setGravity(TextGravity gravity){
        if (gravity == TextGravity.Right){
            mEditTextContent.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        }else {
            mEditTextContent.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
        }
    }

    public String getContent(){
        try {
            return mEditTextContent.getText().toString().trim();
        }catch (Exception e){
            return null;
        }
    }

    public String getName(){
        try {
            return mTextViewName.getText().toString();
        }catch (Exception e){
            return null;
        }
    }


    public void setContentHint(CharSequence hint){
        if (StringUtil.isNotEmpty(hint)){
            mEditTextContent.setHint(hint);
        }
    }

//    public void setEditTextContentColor(){
//        mEditTextContent.setTextColor(mContext.getResources().getColor(R.color.color_bar));
//    }

    public void setmEditTextHintColor(int color){
        mEditTextContent.setHintTextColor(color);
    }

    public void hideImage(){
        mImageViewArrows.setVisibility(GONE);
    }
    public void hideCheck(){
        mImageViewCheck.setVisibility(GONE);
    }

    public void showImage(){
        mImageViewArrows.setVisibility(VISIBLE);
        mImageViewCheck.setVisibility(GONE);
    }
    public void showCheck(){
        mImageViewCheck.setVisibility(VISIBLE);
        mImageViewArrows.setVisibility(GONE);
    }
    public void setCheck(boolean check){
        mImageViewCheck.setChecked(check);
    }

    public void showLine(){
        mViewLine.setVisibility(VISIBLE);
    }

    public void hideLine(){
        mViewLine.setVisibility(GONE);
    }

    public void showBottomBorder(){
        mViewBorderBottom.setVisibility(VISIBLE);
    }

    public void hideBottomBorder(){
        mViewBorderBottom.setVisibility(GONE);
    }

    public void showTopBorder(){
        mViewBorderTop.setVisibility(VISIBLE);
    }

    public void hideTopBorder(){
        mViewBorderTop.setVisibility(GONE);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
//        mLinearLayoutItem.setOnClickListener(l);
        mEditTextContent.setOnClickListener(l);
        super.setOnClickListener(l);
    }

    @Override
    public void setEnabled(boolean enabled) {
//        mLinearLayoutItem.setEnabled(enabled);
//        mEditTextContent.setEnabled(enabled);
        super.setEnabled(enabled);
        if (enabled){
            showImage();
        }else {
            hideImage();
        }
    }

    public void setEditTextFocusChangeListener(OnFocusChangeListener listener) {
        mFocusChangeListener = listener;
    }
}
