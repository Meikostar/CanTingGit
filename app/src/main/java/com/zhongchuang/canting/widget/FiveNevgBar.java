package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.presenter.OnChangeListener;


/**
 * Created by yunshang on 2016/12/20.
 */

public class FiveNevgBar extends LinearLayout implements View.OnClickListener {
    private ImageView[] imageViews = new ImageView[5];
    private TextView[] textViews = new TextView[5];
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
            case R.id.ll_nvg_4:
                if (mListener!=null){
                    mListener.onChagne(3);
                }
                setSelect(3);
                break;
            case R.id.ll_nvg_5:
                if (mListener!=null){
                    mListener.onChagne(4);
                }
                setSelect(4);
                break;
        }
    }

    public FiveNevgBar(Context context) {
        this(context,null);
    }

    public FiveNevgBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FiveNevgBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

        view = LayoutInflater.from(context).inflate(R.layout.five_tab,this);
        imageViews[0] = view.findViewById(R.id.iv_checkbox_image1);
        imageViews[1] = view.findViewById(R.id.iv_checkbox_image2);
        imageViews[2] = view.findViewById(R.id.iv_checkbox_image3);
        imageViews[3] = view.findViewById(R.id.iv_checkbox_image4);
        imageViews[4] = view.findViewById(R.id.iv_checkbox_image5);

        textViews[0] = view.findViewById(R.id.tv_checkbox_text1);
        textViews[1]  = view.findViewById(R.id.tv_checkbox_text2);
        textViews[2]  = view.findViewById(R.id.tv_checkbox_text3);
        textViews[3]  = view.findViewById(R.id.tv_checkbox_text4);
        textViews[4]  = view.findViewById(R.id.tv_checkbox_text5);



        view.findViewById(R.id.ll_nvg_1).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_2).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_3).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_4).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_5).setOnClickListener(this);



    }

    public void setSelect(int index){

        if (index==0){
            imageViews[0].setVisibility(VISIBLE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(GONE);
            imageViews[3].setVisibility(GONE);
            imageViews[4].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.blue));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[3].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[4].setTextColor(getResources().getColor(R.color.slow_black));

        }else if (index==1){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(VISIBLE);
            imageViews[2].setVisibility(GONE);
            imageViews[3].setVisibility(GONE);
            imageViews[4].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.blue));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[3].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[4].setTextColor(getResources().getColor(R.color.slow_black));

        }else if (index==2){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(VISIBLE);
            imageViews[3].setVisibility(GONE);
            imageViews[4].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.blue));
            textViews[3].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[4].setTextColor(getResources().getColor(R.color.slow_black));

        }else if (index==3){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(GONE);
            imageViews[3].setVisibility(VISIBLE);
            imageViews[4].setVisibility(GONE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[3].setTextColor(getResources().getColor(R.color.blue));
            textViews[4].setTextColor(getResources().getColor(R.color.slow_black));
        }else if (index==4){
            imageViews[0].setVisibility(GONE);
            imageViews[1].setVisibility(GONE);
            imageViews[2].setVisibility(GONE);
            imageViews[3].setVisibility(GONE);
            imageViews[4].setVisibility(VISIBLE);

            textViews[0].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[1].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[2].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[3].setTextColor(getResources().getColor(R.color.slow_black));
            textViews[4].setTextColor(getResources().getColor(R.color.blue));
        }
    }

    public void setOnChangeListener(OnChangeListener listener){
        this.mListener = listener;
    }
}
