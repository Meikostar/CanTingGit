package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.utils.DensityUtil;


/**
 * 自定义tag的点击条
 */


public class Custom_TagBtn extends RelativeLayout {

    private View selfView;

    //
    private View rl_bg;
    public View rl_delete;
    public TextView txt_content;

    private Context context;
    private int poition;
    private Custom_TagBtnListener listener;

    private LayoutInflater inflater;

    public interface Custom_TagBtnListener {
        void clickDelete(int type);
    }

    public void setCustom_TagBtnListener(Custom_TagBtnListener listener) {
        this.listener = listener;
    }


    public Custom_TagBtn(Context context) {
        this(context, null);
        this.context=context;
    }

    public void setPoition(int poition){
        this.poition=poition;
    }

    public Custom_TagBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context=context;
    }

    public Custom_TagBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        inflater = LayoutInflater.from(context);
        selfView = inflater.inflate(R.layout.custom_tag, this);
        initView(selfView);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Custom_TagBtn, defStyleAttr, 0);
        //名字
        String nameStr = a.getString(R.styleable.Custom_TagBtn_btn_tag_txt);
        a.recycle();
        setCustomText(nameStr);
    }

    public void setBg(int resources) {
        txt_content.setBackgroundResource(resources);
    }


    public void setCustomText(String nameStr) {
        txt_content.setText(nameStr);
    }
    public void setColors(int nameStr) {
        txt_content.setTextColor(getResources().getColor(nameStr));
    }


    private void initView(View selfView) {

        txt_content = (TextView) selfView.findViewById(R.id.txt_content);

        txt_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickDelete(1);
            }
        });



    }
    public void setSize(int with,int height,int size,int type){

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) txt_content.getLayoutParams();
         if(type==1){
             lp.width = DensityUtil.dip2px(context, with<=35?35:with);
         }else {
             lp.width = DensityUtil.dip2px(context, with<=55?55:with);
         }

        lp.height=DensityUtil.dip2px(context, height);
        txt_content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,size);
        txt_content.setLayoutParams(lp);

    }

}
