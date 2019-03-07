package com.zhongchuang.canting.been;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2018/6/30.
 */

public class ImageOptions {

    public boolean memCache = true;
    public boolean fileCache = true;
    public Bitmap preset;
    public int policy;

    public int targetWidth;
    public int fallback;
    public int animation;
    public float ratio;
    public int round;
    public float anchor = Float.MAX_VALUE;


}
