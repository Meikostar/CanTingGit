/*
	Copyright (c) 2011 Rapture In Venice

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in
	all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
	THE SOFTWARE.
*/

package com.zhongchuang.canting.easeui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import io.valuesfeng.picker.universalimageloader.core.DisplayImageOptions;
import io.valuesfeng.picker.universalimageloader.core.ImageLoader;
import io.valuesfeng.picker.universalimageloader.core.display.SimpleBitmapDisplayer;


public class WebImageView extends ImageView {

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attSet) {
        super(context, attSet);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable, int diskCacheTimeoutInSeconds) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                            //使用内存缓存
                .cacheOnDisk(true)                              //使用硬盘缓存
                .considerExifParams(true)
                .showImageForEmptyUri(placeholderDrawable)
                .showImageOnLoading(placeholderDrawable)
                .displayer(new SimpleBitmapDisplayer())
                .build();//基本的图片形状
        ImageLoader.getInstance().displayImage(urlString, this, options);
    }

    public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable) {
        setImageWithURL(context, urlString, placeholderDrawable, -1);
    }

//	public void setImageWithURL(final Context context, final String urlString, int diskCacheTimeoutInSeconds) {
//	    setImageWithURL(context, urlString, null, diskCacheTimeoutInSeconds);
//	}

    public void setImageWithURL(final Context context, final String urlString, int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                            //使用内存缓存
                .cacheOnDisk(true)                              //使用硬盘缓存
                .considerExifParams(true)
                .showImageForEmptyUri(resId)
                .showImageOnLoading(resId)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();//基本的图片形状
        ImageLoader.getInstance().displayImage(urlString, this, options);

    }

    public void setImageWithURL(final Context context, final String urlString, int resId, int diskCacheTimeoutInSeconds) {
        Resources rsrc = this.getResources();
        Drawable placeholderDrawable = rsrc.getDrawable(resId);
        setImageWithURL(context, urlString, placeholderDrawable, diskCacheTimeoutInSeconds);
    }

    public void setImageWithURL(final Context context, final String urlString) {
        setImageWithURL(context, urlString, null, -1);
    }

//	public void cancelCurrentLoad() {
//	    WebImageManager mgr = WebImageManager.getInstance();
//
//	    // cancel any existing request
//	    mgr.cancelForWebImageView(this);
//	}

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

}
