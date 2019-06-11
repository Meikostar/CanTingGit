package com.zhongchuang.canting.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.widget.MultiImageView;


/***
 * 功能描述:今日新款Holder
 * 作者:xiongning
 * 时间:2016/12/22
 * 版本:v1.0
 ***/

public class ImageViewHolder extends TodayViewHolder {

    /** 图片*/
    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView){
        super(itemView, TYPE_IMAGE);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.view_stub_img);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = subView.findViewById(R.id.multiImagView);
        if(multiImageView != null){
            this.multiImageView = multiImageView;
        }
    }
}
