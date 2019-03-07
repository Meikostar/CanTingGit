package com.zhongchuang.canting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by xzh on 15/8/17.
 */
public class RegularListView extends ListView {
    public RegularListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public RegularListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RegularListView(Context context) {
        super(context);
    }
    //不滚动
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandHeight);
    }
}
