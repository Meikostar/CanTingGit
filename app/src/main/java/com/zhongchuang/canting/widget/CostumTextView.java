package com.zhongchuang.canting.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aliyun.common.utils.StorageUtils;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;

import java.io.File;


/**
 * 自定义微信字体TextView
 * Created by zhangbo on 2018/12/1.
 */
@SuppressLint("AppCompatCustomView")
public class CostumTextView extends TextView {
    public CostumTextView(Context context) {
        super(context);
    }

    public CostumTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        changeTypeFace(context, attrs);
    }

    public CostumTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeTypeFace(context, attrs);
    }

    private void changeTypeFace(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CostumTextView);
            int fontType = typedArray.getInt(R.styleable.CostumTextView_fontType, -1);
            Typeface mtf = null;
            if (fontType == -1) {
            } else if (fontType == 0) {
                mtf = Typeface.createFromFile(StorageUtils.getCacheDirectory(CanTingAppLication.getInstance()).getAbsolutePath() + File.separator+
                        "live/fonts/ifun_ban.TTF");
//            }
//            else if (fontType == 1) {
//                mtf = Typeface.createFromAsset(context.getAssets(),
//                        "fonts/HYQIHEI-60S.OTF");
//            } else if (fontType == 2) {
//                mtf = Typeface.createFromAsset(context.getAssets(),
//                        "fonts/HYQIHEI-65S.OTF");
//            } else if (fontType == 3) {
//                mtf = Typeface.createFromAsset(context.getAssets(),
//                        "fonts/HYQIHEI-70S.OTF");
            } else if (fontType == 4) {
                mtf = Typeface.createFromFile(StorageUtils.getCacheDirectory(CanTingAppLication.getInstance()).getAbsolutePath() + File.separator+
                        "live/fonts/ifun_jianti.TTF");
            }
            if (mtf != null) {
                super.setTypeface(mtf);
            }
        }
        if (typedArray != null) {
            typedArray.recycle();
        }
    }
}
