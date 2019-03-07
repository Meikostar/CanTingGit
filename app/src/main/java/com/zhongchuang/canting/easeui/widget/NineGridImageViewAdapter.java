package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.widget.ImageView;


/**
 * Created by Jaeger on 16/2/24.
 *
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */
public abstract class NineGridImageViewAdapter<T> {
    protected abstract void onDisplayImage(Context context, WebImageView imageView, T t);

    protected WebImageView generateImageView(Context context){
        WebImageView imageView = new WebImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}
