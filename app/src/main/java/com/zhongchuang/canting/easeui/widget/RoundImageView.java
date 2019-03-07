package com.zhongchuang.canting.easeui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.zhongchuang.canting.R;;

import io.valuesfeng.picker.universalimageloader.core.DisplayImageOptions;
import io.valuesfeng.picker.universalimageloader.core.ImageLoader;
import io.valuesfeng.picker.universalimageloader.core.display.RoundedBitmapDisplayer;


public class RoundImageView extends WebImageView {


    private int roundWidth = 5;
    private int roundHeight = 5;

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);
            roundWidth = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth, roundWidth);
            roundHeight = a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight, roundHeight);
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            roundWidth = (int) (roundWidth * density);
            roundHeight = (int) (roundHeight * density);
        }
    }


    public void setImageWithURL(final Context context, final String urlString, int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                            //使用内存缓存
                .cacheOnDisk(true)                              //使用硬盘缓存
                .considerExifParams(true)
                .showImageForEmptyUri(resId)
                .showImageOnLoading(resId)
                .displayer(new RoundedBitmapDisplayer(roundWidth))
                .build();//基本的图片形状
        ImageLoader.getInstance().displayImage(urlString, this, options);

    }
}   


