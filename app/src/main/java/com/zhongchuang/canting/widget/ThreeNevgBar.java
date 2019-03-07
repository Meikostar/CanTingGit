package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.presenter.OnChangeListener;


/**
 * Created by yunshang on 2016/12/20.
 */

public class ThreeNevgBar extends LinearLayout implements View.OnClickListener {
    private ImageView[] imageViews = new ImageView[3];
    private TextView[] textViews = new TextView[3];
    private Context context;
    private View view;
    private boolean type;
    private OnChangeListener mListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_nvg_1:
                if (mListener!=null){
                    mListener.onChagne(0);
                }
                setSelect(0);
                break;
            case R.id.ll_nvg_2:
                if (mListener!=null){
                    mListener.onChagne(1);
                }
                setSelect(1);
                break;
            case R.id.ll_nvg_3:
                if (mListener!=null){
                    mListener.onChagne(2);
                }
                setSelect(2);
                break;
        }
    }
    public void setTitle2(String title1, String title2){
        textViews[0].setText(title1);
        textViews[1].setText(title2);

    }
    public ThreeNevgBar(Context context) {
        this(context,null);
    }

    public ThreeNevgBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ThreeNevgBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        TypedArray mTypedArray = null;
        if (attrs!=null){
            mTypedArray = getResources().obtainAttributes(attrs,
                    R.styleable.TwoNevgBar);
            type = mTypedArray.getBoolean(R.styleable.TwoNevgBar_types,true);

        }
        init();
        if (mTypedArray!=null){
            mTypedArray.recycle();
        }
    }

    private void init() {

        view = LayoutInflater.from(context).inflate(R.layout.nevagbar_three,this);
        imageViews[0] = (ImageView)view.findViewById(R.id.iv_checkbox_image1);
        imageViews[1] = (ImageView)view.findViewById(R.id.iv_checkbox_image2);
        imageViews[2] = (ImageView)view.findViewById(R.id.iv_checkbox_image3);

        textViews[0] = (TextView)view.findViewById(R.id.tv_checkbox_text1);
        textViews[1]  = (TextView)view.findViewById(R.id.tv_checkbox_text2);
        textViews[2]  = (TextView)view.findViewById(R.id.tv_checkbox_text3);



        view.findViewById(R.id.ll_nvg_1).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_2).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_3).setOnClickListener(this);



    }

    public void setSelect(int index){

        if (index==0){
            imageViews[0].setVisibility(VISIBLE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.blue));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));

        }else if (index==1){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(VISIBLE);
            imageViews[2].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.blue));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));
        }else if (index==2){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(VISIBLE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.blue));
        }
    }

    public void setOnChangeListener(OnChangeListener listener){
        this.mListener = listener;
    }
}
