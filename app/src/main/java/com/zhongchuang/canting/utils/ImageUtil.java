package com.zhongchuang.canting.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zhongchuang.canting.R;;

import java.util.ArrayList;

/***
 * 功能描述:图片、水印工具类
 * 作者:xiongning
 * 时间:2016/12/27
 * 版本:1.0
 ***/

public class ImageUtil {

    public static String getImageUrl_100(String url){
        return url+"_100";
    }
    public static String getImageUrl_200(String url){
        return url+"_200";
    }
    public static String getImageUrl_400(String url){
        return url+"_400";
    }
    public static String getImageUrl_800(String url){

        return url+"_800";
    }

    /**
     * 用字符串生成二维码
     * @param str
     * @author zhouzhe@lenovo-cw.com
     * @return
     * @throws WriterException
     */
    public static Bitmap Create2DCode(Context mContext, String str) throws WriterException
    {
        if (TextUtil.isEmpty(str)){
            return null;
        }
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 450, 450);

        int width = matrix.getWidth();

        int height = matrix.getHeight();

        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(matrix.get(x, y)){
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }
    /**
     * 获取图片URI实际目录
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }



    //分享多张图片
    public static void shareMultipleImage(Context context, ArrayList<Uri> uriList) {

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

        shareIntent.setType("image/*");

        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.fxd)));

    }
}
