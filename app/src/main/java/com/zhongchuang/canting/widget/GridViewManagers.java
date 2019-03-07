package com.zhongchuang.canting.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/5/14.
 */
public class GridViewManagers extends GridLayoutManager {
    public GridViewManagers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GridViewManagers(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridViewManagers(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//        if (state!=null&&state.getItemCount()>0) {
//            View view = recycler.getViewForPosition(0);
//            if (view != null) {
//                measureChild(view, widthSpec, heightSpec);
//                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                int measuredHeight = measuredWidth;
//                setMeasuredDimension(measuredWidth, measuredHeight);
//            }
//        }
//    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
