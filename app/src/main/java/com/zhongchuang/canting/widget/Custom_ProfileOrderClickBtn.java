package com.zhongchuang.canting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;

/**
 * Created by Administrator on 2018/6/22.
 */
public class Custom_ProfileOrderClickBtn extends RelativeLayout {
    private ImageView img_orderstatus_icon;
    private TextView txt_unread;
    private TextView txt_order_status;
    private LayoutInflater mInflater;
    public Custom_ProfileOrderClickBtn(Context context) {
        this(context,null);
    }

    public Custom_ProfileOrderClickBtn(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Custom_ProfileOrderClickBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.custom_profile_order_clickbtn,this);
        initView(view);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Custom_ProfileOrderClickBtn,defStyle,0);
        Drawable drawable = a.getDrawable(R.styleable.Custom_ProfileOrderClickBtn_btn_order_icon);
        String status = a.getString(R.styleable.Custom_ProfileOrderClickBtn_btn_order_txt);
        a.recycle();

        setStatusText(status);
        setResource(drawable);
    }

    private void initView(View view) {
        img_orderstatus_icon = view.findViewById(R.id.img_orderstatus_icon);
        txt_unread = view.findViewById(R.id.txt_unread);
        txt_order_status = view.findViewById(R.id.txt_order_status);
    }

    public void setUnReadNum(int num){
        txt_unread.setText(num+"");
        switch (num){
            case 0:
                txt_unread.setVisibility(GONE);
                break;
            default:
                txt_unread.setVisibility(VISIBLE);
                break;
        }
    }

    public void setStatusText(String statusText) {
        txt_order_status.setText(statusText);
    }

    public void setResource(Drawable resource) {
        img_orderstatus_icon.setBackground(resource);
    }
}

