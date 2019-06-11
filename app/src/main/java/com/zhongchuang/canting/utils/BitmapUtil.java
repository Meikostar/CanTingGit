package com.zhongchuang.canting.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

/***
 * 功能描述:
 * 作者:qiujialiu
 * 时间:2016/12/27
 * 版本:1.0
 ***/

public class BitmapUtil {
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return getBitmapFromUrl(url, width, height);
        }else {
            return ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MINI_KIND);
        }
    }

    public static String saveBitmapToFile(Context context, Bitmap mBitmap, String fileName, int quality, boolean isPng) {
        File file = new File(context.getCacheDir().getAbsolutePath()+"/image");
        file.mkdirs();// 创建文件夹
        fileName = context.getCacheDir().getAbsolutePath()+"/image" + File.separator + fileName;//图片名字
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(fileName);
            if (isPng) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, quality, b);// 把数据写入文件
            }else {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, b);// 把数据写入文件
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    private static Bitmap getBitmapFromUrl(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }




    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
