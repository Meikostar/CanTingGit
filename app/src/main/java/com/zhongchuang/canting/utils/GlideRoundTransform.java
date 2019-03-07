package com.zhongchuang.canting.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2017/11/30.
 *
 *  glide圆角方法
 */

public class GlideRoundTransform extends BitmapTransformation {

    private float radious = 0;

    public GlideRoundTransform(Context context) {
        this(context,4);
    }

    public GlideRoundTransform(Context context,int r) {
        super(context);
        this.radious = Resources.getSystem().getDisplayMetrics().density * r;
    }



    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        return transformRadius(pool,toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName()+Math.round(radious);
    }

    private Bitmap transformRadius(BitmapPool pool, Bitmap source){
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radious, radious, paint);
        return result;
    }
}
