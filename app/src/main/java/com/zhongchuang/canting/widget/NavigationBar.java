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


/**
 * Created by linquandong on 15/5/11.
 */
public class NavigationBar extends LinearLayout implements View.OnClickListener {

    private View naviView;
    private View line;
    private ImageView img_left;
    private ImageView right_img;
    private ImageView img_right;
    private TextView txt_right;
    private TextView txt_middle;

    private View layout_left;
    private View layout_right;

    private NavigationBarListener mListener;
    private NavigationBarStyle barStyle;

    public static final int MISS = 0;
    public static final int IMG = 1;
    public static final int TXT = 2;
    public static final int TOP = 3;
    public static final int TXTS = 4;

    public interface NavigationBarListener {
        void navigationLeft();

        void navigationRight();
        void navigationimg();
    }

    public void setNavigationBarListener(NavigationBarListener listener) {
        mListener = listener;
    }

    public NavigationBar(Context context) {
        this(context, null);
    }

    public NavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        barStyle = new NavigationBarStyle();
        //自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBar, defStyleAttr, 0);
        barStyle.left_type = a.getInt(R.styleable.NavigationBar_navigationLeft_type, IMG);//默认后退键为可见的
        barStyle.right_type = a.getInt(R.styleable.NavigationBar_navigationRight_type, MISS);//默认不可见
        barStyle.right_imgId = a.getResourceId(R.styleable.NavigationBar_navigationRight_img, R.drawable.back_white);
        barStyle.right_txt = a.getString(R.styleable.NavigationBar_navigationRight_txt);
        barStyle.right_txt_color = a.getResourceId(R.styleable.NavigationBar_navigationRight_txt_color, R.color.slow_black);
        barStyle.left_imgId = a.getResourceId(R.styleable.NavigationBar_navigationLeft_img, R.drawable.back_white);
        barStyle.right_img = a.getResourceId(R.styleable.NavigationBar_right_img, R.drawable.back_white);
        barStyle.strTitle = a.getString(R.styleable.NavigationBar_navigationTitle);
        barStyle.backgroundColor = a.getResourceId(R.styleable.NavigationBar_navigation_bg, R.color.coofeColor);
        a.recycle();
        showViewBarStyle(barStyle);
    }

    public void setRightGone() {

        layout_right.setVisibility(GONE);
    }
    public void setLineGone() {
        line.setVisibility(GONE);
    }

    public void setRightVisible() {
        layout_right.setVisibility(VISIBLE);
    }

    public void setLeftVisible() {
        img_left.setVisibility(VISIBLE);
    }

    public void setLeftGone(){
        img_left.setVisibility(GONE);
    }

    public void showViewBarStyle(NavigationBarStyle barStyle) {
        //left
        switch (barStyle.left_type) {
            case MISS:
                img_left.setVisibility(GONE);
                break;
            case IMG:
                img_left.setVisibility(VISIBLE);
                img_left.setImageResource(barStyle.left_imgId);
                break;
        }
        //right
        switch (barStyle.right_type) {
            case MISS:
                img_right.setVisibility(GONE);
                txt_right.setVisibility(GONE);
                break;
            case IMG:
                img_right.setVisibility(VISIBLE);
                txt_right.setVisibility(GONE);
                img_right.setImageResource(barStyle.right_imgId);
                break;
            case TXT:
                img_right.setVisibility(GONE);
                txt_right.setVisibility(VISIBLE);
                txt_right.setText(barStyle.right_txt);
                txt_right.setTextColor(getResources().getColor(R.color.white));
                break;
            case TOP:
                img_right.setVisibility(GONE);
                img_right.setVisibility(VISIBLE);
                right_img.setVisibility(VISIBLE);
                txt_right.setText(barStyle.right_txt);
                txt_right.setTextColor(getResources().getColor(barStyle.right_txt_color));
                break;
            case TXTS:
                img_right.setVisibility(GONE);
                txt_right.setVisibility(VISIBLE);
                txt_right.setText(barStyle.right_txt);
                txt_right.setTextColor(getResources().getColor(R.color.blue));
                break;
        }
        setNaviTitle(barStyle.strTitle);
        naviView.setBackgroundColor(getResources().getColor(barStyle.backgroundColor));
    }

    public void setNaviTransParent() {
        naviView.setBackgroundResource(android.R.color.transparent);
    }
    public void setTitleSize(int a,int b,int c,int d ){
        txt_middle.setTextSize(a);
        txt_middle.setTextColor(getResources().getColor(c));
        txt_right.setTextColor(getResources().getColor(d));
        txt_right.setTextSize(b);


    }
    public void setLeftViewInvisible() {
        layout_left.setVisibility(View.INVISIBLE);
    }

    public void setNaviTitle(String title) {
        txt_middle.setText(title);
    }

    public void setTitleColor(int color) {
        txt_middle.setTextColor(color);
    }

    public void setRightText(String rightText) {
        txt_right.setText(rightText);
    }

    public void setRightTextVisible() {
        txt_right.setVisibility(View.VISIBLE);
    }

    public void setBackgroundAlpha(float alpha) {
        naviView.setAlpha(alpha);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        naviView = inflater.inflate(R.layout.navigationbar, this);
        layout_left = naviView.findViewById(R.id.topview_left_layout);
        layout_right = naviView.findViewById(R.id.topview_right_layout);

        img_left = (ImageView) naviView.findViewById(R.id.top_view_back);
        right_img = (ImageView) naviView.findViewById(R.id.right_img);
        img_right = (ImageView) naviView.findViewById(R.id.top_view_right);

        txt_middle = (TextView) naviView.findViewById(R.id.navigationbar_title);
        txt_right = (TextView) naviView.findViewById(R.id.delete_text);
        line=findViewById(R.id.line);
        //
        layout_left.setOnClickListener(this);
        layout_right.setOnClickListener(this);
        right_img.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.topview_left_layout:
                mListener.navigationLeft();
                break;
            case R.id.topview_right_layout:
                mListener.navigationRight();
                break;
            case R.id.right_img:
                mListener.navigationimg();
                break;
        }
    }
    public void setRightTxt(String name){
        txt_right.setBackground(null);
        txt_right.setVisibility(VISIBLE);
       txt_right.setText(name);
    }

    public class NavigationBarStyle {
        public int left_type;//导航条左边的类型：0，不可见，1：图片，
        public int right_type;//导航条右边的类型：0：不可见，1：图片，2文字
        public String strTitle = ""; //中间标题
        public int right_imgId;
        public String right_txt = "";
        public int right_txt_color;
        public int left_imgId;
        public int backgroundColor;
        public int right_img;
    }
    public void setColors(int id){
        txt_right.setTextColor(getResources().getColor(id));
    }
    public void hide(){
        txt_right.setVisibility(GONE);
    }
}
