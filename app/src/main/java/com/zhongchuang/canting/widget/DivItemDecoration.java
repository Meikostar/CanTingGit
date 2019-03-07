package com.zhongchuang.canting.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhongchuang.canting.utils.DensityUtil;


/***
 * 功能描述:DivItemDecoration
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/
public class DivItemDecoration extends RecyclerView.ItemDecoration {
    private int divHeight;
    private boolean hasHead;
    public DivItemDecoration(int divHeight, boolean hasHead){
        this.divHeight = divHeight;
        this.hasHead = hasHead;
    }

    public DivItemDecoration(int divHeight, boolean hasHead, boolean isDp){
        if (isDp) {
            this.divHeight = DensityUtil.dip2px(divHeight);
        }else {
            this.divHeight = divHeight;
        }
        this.hasHead = hasHead;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if(hasHead && position == 0){
            return;
        }
        outRect.bottom = divHeight;
    }
}