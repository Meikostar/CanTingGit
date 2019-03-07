package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @author syj
 * @createTime 2016/04/30
 * @在ScrollView里面使用的GridView
 * */

public class GridViewInScroll extends GridView {

	public GridViewInScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true; 
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
