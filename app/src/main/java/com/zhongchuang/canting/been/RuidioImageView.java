package com.zhongchuang.canting.been;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/5/14.
 */


public class RuidioImageView extends ImageView {

    //圆角弧度
    private float[] rids = {10.0f,10.0f,10.0f,10.0f,10.0f,10.0f,10.0f,10.0f,};

    public RuidioImageView(Context context) {
        super(context);
    }

    public RuidioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RuidioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        //绘制圆角imageview
        path.addRoundRect(new RectF(0,0,w,h),rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}