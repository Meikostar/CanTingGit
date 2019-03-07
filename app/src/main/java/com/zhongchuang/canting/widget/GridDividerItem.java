package com.zhongchuang.canting.widget;

import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;


/**
 * getItemOffsets(),可以实现类似padding的效果

  onDraw(),可以实现类似绘制背景的效果，内容在上面

  onDrawOver()，可以绘制在内容的上面，覆盖内容
 */
public class GridDividerItem extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mDividerWidth;
    private int mDividerheight;
    private int mDividerLR;

    public GridDividerItem(int mDividerLR,int width,int height, @ColorInt int color) {
        this.mDividerLR = mDividerLR;
        mDividerWidth = width;
        mDividerheight = height;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int count = parent.getAdapter().getItemCount() - 1;
        int spanCount = getSpanCount(parent);
        int childCount = parent.getChildCount() -1;

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        Log.d("--------------","总数为 = "+count +"，，，当前 = "+itemPosition + ",,列的数量 = "+spanCount +",,childCount = "+childCount);
        int surplus = count%spanCount;
        //水平方向
        if ((itemPosition!=0)&&(childCount<=count-surplus)){
            outRect.bottom = mDividerheight;

        }

        //锤子方向
        if (itemPosition>0){

            Log.d("---------","itemPosition = "+itemPosition);
            if (itemPosition%spanCount!=0){
                outRect.right = mDividerWidth;     //画中间间距
            }
            if (itemPosition%spanCount==0){
                outRect.right = mDividerLR;     //画屏幕右边间距
            }
            if (itemPosition%spanCount==1){
                outRect.left = mDividerLR;     //画屏幕左边间距
            }
        }


    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }
}
