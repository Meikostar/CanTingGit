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

/**
 * Created by Administrator on 2018/6/22.
 */
public class TwoNevgBar extends LinearLayout implements View.OnClickListener {
    private ImageView[] imageViews = new ImageView[2];
    private TextView[] textViews = new TextView[2];
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
                break;
            case R.id.ll_nvg_2:
                if (mListener!=null){
                    mListener.onChagne(1);
                }
                break;

        }
    }
    public void setTitle2(String title1,String title2){
        textViews[0].setText(title1);
        textViews[1].setText(title2);

    }
    public TwoNevgBar(Context context) {
        this(context,null);
    }

    public TwoNevgBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TwoNevgBar(Context context, AttributeSet attrs, int defStyleAttr) {
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

        view = LayoutInflater.from(context).inflate(R.layout.nevagbar_two,this);
        imageViews[0] = view.findViewById(R.id.iv_checkbox_image1);
        imageViews[1] = view.findViewById(R.id.iv_checkbox_image2);

        textViews[0] = view.findViewById(R.id.tv_checkbox_text1);
        textViews[1]  = view.findViewById(R.id.tv_checkbox_text2);



        view.findViewById(R.id.ll_nvg_1).setOnClickListener(this);
        view.findViewById(R.id.ll_nvg_2).setOnClickListener(this);



    }

    public void setSelect(int index){

        if (index==0){
            imageViews[0].setImageResource(R.drawable.origin_rectangle_line);
            imageViews[1].setImageResource(R.drawable.black_rectangle_line);
            textViews[0].setTextColor(getResources().getColor(R.color.blue));
            textViews[1].setTextColor(getResources().getColor(R.color.color6));

        }else if (index==1){
            imageViews[0].setImageResource(R.drawable.black_rectangle_line);
            imageViews[1].setImageResource(R.drawable.origin_rectangle_line);

            textViews[0].setTextColor(getResources().getColor(R.color.color6));
            textViews[1].setTextColor(getResources().getColor(R.color.blue));

        }
    }

    public void setOnChangeListener(OnChangeListener listener){
        this.mListener = listener;
    }
    public interface OnChangeListener {
        void onChagne(int currentIndex);
    }

}

