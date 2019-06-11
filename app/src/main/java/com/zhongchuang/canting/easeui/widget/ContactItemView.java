package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.CircleTransform;


public class ContactItemView extends LinearLayout {


    private TextView unreadMsgView;

    public ContactItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mContext=context;
    }

    public ContactItemView(Context context) {
        super(context);
        init(context, null);
        mContext=context;
    }
    private Context mContext;
    
    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContactItemView);
        String name = ta.getString(R.styleable.ContactItemView_contactItemName);
        Drawable image = ta.getDrawable(R.styleable.ContactItemView_contactItemImage);
        ta.recycle();
        
        LayoutInflater.from(context).inflate(R.layout.em_widget_contact_item, this);
         avatar = findViewById(R.id.avatar);
        unreadMsgView = findViewById(R.id.unread_msg_number);
         nameView = findViewById(R.id.name);
        if(image != null){
            avatar.setImageDrawable(image);
        }
        nameView.setText(name);
        mContext=context;
    }
    private TextView nameView;
    private ImageView avatar;
    public void setUnreadCount(int unreadCount){
        unreadMsgView.setText(String.valueOf(unreadCount));
    }

    public void setValues(String name,String url){
        nameView.setText(name);
        Glide.with(mContext).load(url).asBitmap().transform(new CircleTransform(mContext)).placeholder(R.drawable.editor_ava).into(avatar);

    }
    
    public void showUnreadMsgView(){
        unreadMsgView.setVisibility(View.VISIBLE);
    }
    public void hideUnreadMsgView(){
        unreadMsgView.setVisibility(View.INVISIBLE);
    }
    
}
