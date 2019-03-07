package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongchuang.canting.R;;


/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/20
 * 版本:
 ***/
public class MCheckBox extends LinearLayout {
    private Context mContext;
    private boolean isChecked=false;
    private ImageView check;
    private boolean canNoClick;
    private CompoundButton.OnCheckedChangeListener mListener;
    private OnClickListener mClickListener;
    private  int id=-1;
    private  int rsid;
    private  int choose;

    public MCheckBox(Context context) {
        super(context);
    }

    public MCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        TypedArray mTypedArray = null;
        if (attrs!=null){
            mTypedArray = getResources().obtainAttributes(attrs,
                    R.styleable.MCheckBox);
            isChecked = mTypedArray.getBoolean(R.styleable.MCheckBox_isCheckeds,false);
            canNoClick = mTypedArray.getBoolean(R.styleable.MCheckBox_canNoClick,false);
            rsid=mTypedArray.getResourceId(R.styleable.MCheckBox_drawables,-1);
            choose=mTypedArray.getResourceId(R.styleable.MCheckBox_drawablechoose,-1);
        }
        init();
    }

    public void init(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
       View view = inflater.inflate( R.layout.checkout_layout, this);
         check = (ImageView) view.findViewById(R.id.iv_checkbox);
        if (!canNoClick) {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener!=null){
                        mClickListener.onClick(MCheckBox.this);
                    }else {
                        if (isChecked) {
                            isChecked = false;
                            setUncheckView();
                        } else {
                            isChecked = true;
                            setCheckedView();
                        }
                    }
                }
            });
        }
        if (isChecked){
            setCheckedView();
        }else {
            setUncheckView();
        }
    }
    public void setDrawble(int id){
        this.id=id;
    }
    public void setUncheckView() {
        isChecked=false;
        if(rsid==-1){
            check.setImageResource(R.drawable.choose);
        }else {
            check.setImageResource(rsid);
        }

        if (mListener!=null){
            mListener.onCheckedChanged(null,false);
        }
    }

    public void setCheckedView() {
        isChecked=true;
        if(choose==-1){
            check.setImageResource(R.drawable.unchoose);
        }else {
            check.setImageResource(choose);
        }

        if (mListener!=null){
            mListener.onCheckedChanged(null,true);
        }
    }
    public void Click(){
        if(isChecked){

            setUncheckView();
        }else {

            setCheckedView();
        }
    }

    public boolean isCheck(){
        return isChecked;
    }

    public void setChecked(boolean checked){
        if (checked){
            isChecked = true;
            setCheckedView();
        }else {
            isChecked = false;
            setUncheckView();
        }
    }

    public void setOnCheckChangeListener(CompoundButton.OnCheckedChangeListener listener){
        mListener = listener;
    }

    public void setOnCheckClickListener(OnClickListener listener){
        mClickListener = listener;
    }
}
