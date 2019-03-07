package io.valuesfeng.picker.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PhotoUtils {


    public Activity activity;
    public String imgPath = null;
    // 图片的前缀和后缀
    public static final String JPG_PREFIX = "IMG_";
    public static final String JPG_SUFFIX = ".jpg";

    public PhotoUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 相册
     */
    public void pickPhoto() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            //从文件系统直接选取图片
            intent = new Intent(Intent.ACTION_GET_CONTENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        } else {
            //从图库中获取图片
            intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, FrameworkConstant.REQUESTCODE_LOCATION);
    }

    /**
     * 相机
     */
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mkLocalDir(FrameworkConstant.PICTURE_PATH));//拍照得到的图片输入到该路径下
        activity.startActivityForResult(intent, FrameworkConstant.REQUESTCODE_CAMERA);
    }

    /**
     * 头像（截取图片）
     */
    private void cropImg(Uri uri, int outputX, int outputY, int requestCode,
                         boolean isCrop) {
        Intent intent = null;
        if (isCrop) {
            intent = new Intent("com.android.camera.action.CROP");
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        }
        intent.setDataAndType(uri, "image/*"); //传入原图的路径
        intent.putExtra("crop", "true");//crop    String  发送裁剪信号
        intent.putExtra("aspectX", 1);//X方向上的比例
        intent.putExtra("aspectY", 1);//Y方向上的比例
        intent.putExtra("outputX", outputX);//裁剪区的宽
        intent.putExtra("outputY", outputY);//裁剪区的高
        intent.putExtra("scale", true);//boolean 是否保留比例
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mkLocalDir(FrameworkConstant.CROPPICTURE_PATH));//URI 将URI指向相应的file，把裁剪后的图片输出到该路径
        intent.putExtra("return-data", false);// boolean 是否将数据保留在Bitmap中返回
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent,FrameworkConstant. REQUESTCODE_CROP);
    }

    /**
     * 创建图片路径名称，并由imgPath 保存文件的路径
     */
    public Uri mkLocalDir(String fileName) {
        File dir = new File(fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 为以时间为图片命名
        File imageFile = new File(dir,
                new SimpleDateFormat("yyMMddHHmmss").format(new Date())
                        + ".jpg");
        imgPath = imageFile.getAbsolutePath();
        Uri imgUri = Uri.fromFile(imageFile);
        return imgUri;
    }

    /**
     * 获取截取后的图片的路径
     */
    public String getCropImgPath(int requestCode, int resultCode, Intent data) {
        if (resultCode == activity.RESULT_OK) {
            if (requestCode == FrameworkConstant.REQUESTCODE_CROP) {
                return imgPath;

            } else if (requestCode == FrameworkConstant.REQUESTCODE_CAMERA || requestCode ==FrameworkConstant. REQUESTCODE_LOCATION) {
                getImgPath(requestCode, resultCode, data);
                if (imgPath != null) {
                    Uri imgUri = Uri.fromFile(new File(imgPath));
                    cropImg(imgUri, 250, 250,FrameworkConstant. REQUESTCODE_CROP, true);
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 获取图片的路径
     */
    public String getImgPath(int requestCode, int resultCode, Intent data) {
        /**
         * 从相册中获取图片的路径
         *
         * */
        if (requestCode == FrameworkConstant.REQUESTCODE_LOCATION && data != null) {
            String[] pojo = {MediaStore.Images.Media.DATA};
            Uri photoUri = null;
            photoUri = data.getData();
            if (photoUri != null) {
                Cursor cursor = activity.getContentResolver().query(photoUri,
                        pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    imgPath = cursor.getString(columnIndex);
                    return imgPath;
                }
            }
            /**
             * 拍照，直接返回自定义的路径
             * */
        } else if (requestCode == FrameworkConstant.REQUESTCODE_CAMERA) {
            return imgPath;
        }
        return null;
    }

    public static String drawTextToRightBom(Context context, String imgPath, String loaction,String time) {
        if (imgPath == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, options);
        options.inSampleSize = calculateInSampleSize(options, 640);
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        int degree = readPictureDegree(imgPath);
        //旋转照片角度并以最大宽度为640压缩图片
        bitmap = rotateBitmap(bitmap, degree, 640);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(dp2px(context,10));

        Bitmap txtBitMap = drawTextToBitmap(context,bitmap,loaction,time,paint,10,10);
        return compressBitmap(txtBitMap);
    }

    //图片上绘制文字
    public static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String loaction, String time,
                                          Paint paint, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Rect LcationBounds = new Rect();
        //位置
        paint.getTextBounds(loaction, 0, loaction.length(), LcationBounds);
        int locationR = (int) (bitmap.getWidth() - LcationBounds.width() - dp2px(context, 10));
        int ptop = (int) (bitmap.getHeight() - dp2px(context, 10));
        canvas.drawText(loaction, locationR, ptop, paint);

        //时间
        Rect timeBounds = new Rect();
        paint.getTextBounds(time, 0, time.length(), timeBounds);
        int timeR = (int) (bitmap.getWidth() - timeBounds.width() - dp2px(context, 10));
        canvas.drawText(time, timeR, ptop-LcationBounds.height(), paint);
        return bitmap;
    }


    /**
     * dip转pix
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String commpressImg(String path) {
        if (path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, 640);
            options.inJustDecodeBounds = false;
            options.inScaled = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int degree = readPictureDegree(path);
            //旋转照片角度并以最大宽度为640压缩图片
            bitmap = rotateBitmap(bitmap, degree, 640);
            return compressBitmap(bitmap);
        }
        return null;
    }

    public static String commpressImg(Context context, String path, boolean add_watermark, String watermark) {
        if (path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, 640);
            options.inJustDecodeBounds = false;
            options.inScaled = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            int degree = readPictureDegree(path);
            //旋转照片角度并以最大宽度为640压缩图片
            bitmap = rotateBitmap(bitmap, degree, 640);

            return compressBitmap(bitmap);
        }
        return null;
    }
    /**
     * 对图片进行压缩并保存
     */
    public static String compressBitmap(Bitmap bitmap) {
        File dir = new File(FrameworkConstant.PICTURE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 自定义图片路径
        File imageFile = new File(dir, getDefImgPath());
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        try {
            //质量压缩
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            byte[] byteArray = baos.toByteArray();
            fos = new FileOutputStream(imageFile);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 自定义的图片路径
     */
    public static String getDefImgPath() {
        String randomName = UUID.randomUUID().toString();
        String imageName = new SimpleDateFormat("yyMMddHHmmss")
                .format(new Date());
        imageName = imageName + randomName;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(imageName.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString() + JPG_SUFFIX;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return imageName.replaceAll("[^A-Za-z0-9]", "#") + JPG_SUFFIX;
    }

    //按宽度640，等比例比例压缩方案一：
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth) {
            final int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    //拍摄角度
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress, int newWidth) {
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            if (degress != 0) {
                matrix.postRotate(degress);
            }
            //是否缩放图片
            // 获得图片的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newHeight = newWidth * height / width;
            if (width > newWidth) {
                // 计算缩放比例
//                float scaleWidth = ((float) newWidth) / width;
//                float scaleHeight = ((float) newHeight) / height;
//                matrix.postScale(scaleWidth, scaleHeight);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            return bitmap;
        }
        return bitmap;
    }

    //拍摄角度
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public Bitmap getFileBitMap(String url, int widthDimen) {
        if (url != null) {
            File file = new File(url);
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(url, options);
                int width = options.outWidth;
                int height = options.outHeight;
                int zoomWidth = activity.getResources().getDimensionPixelSize(
                        widthDimen);
                int zoomHeight = height * zoomWidth / width;
                options.outHeight = zoomHeight;
                options.outWidth = zoomWidth;
                options.inJustDecodeBounds = false;
                options.inSampleSize = width / zoomWidth;

                Bitmap bitmap = BitmapFactory.decodeFile(url, options);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * saveBitmap
     *
     * @param @param filename---完整的路径格式-包含目录以及文件名
     * @param @param bitmap
     * @param @param isDelete --是否只留一张
     * @return void
     * @throws
     */
    public void saveBitmap(String filename, Bitmap bitmap, boolean isDelete) {
        File file = new File(filename);
        // 若存在即删除-默认只保留一张
        if (isDelete) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Url 转 Bitmap
     * @return
     */
    public static Bitmap getBitmap(final String url) {
        Bitmap bm = null;
        try {

            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 保存图片至相册
     *
     * @param
     */
    public static String saveImageToGallery(Context context ,Bitmap bmp) {
        File dirFile = new File(FrameworkConstant.PICTURE_PATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        String filename = System.currentTimeMillis()+".jpg";
        File file = new File(FrameworkConstant.PICTURE_PATH + filename);
        try{
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            bmp.recycle();
        }

        //更新图库
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        return file.getAbsolutePath();
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}