
package com.google.zxing.client.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import com.google.zxing.Result;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.camera.CameraManager;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.lang.reflect.Field;



import com.google.zxing.client.android.decode.DecodeThread;
import com.google.zxing.client.android.decode.DecodeUtils;
import com.google.zxing.client.android.encoding.ScanCodeEvent;
import com.google.zxing.client.android.utils.AudioPlayer;
import com.google.zxing.client.android.utils.BeepManager;
import com.google.zxing.client.android.utils.CaptureActivityHandler;
import com.google.zxing.client.android.utils.InactivityTimer;
import com.google.zxing.client.android.utils.LogUtil;
import com.google.zxing.client.android.utils.MyUtil;
import com.google.zxing.client.android.utils.OttoAppConfig;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity implements SurfaceHolder.Callback,
        View.OnClickListener {

    private static final String LOG_TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private LinearLayout scanContainer;
    private LinearLayout scanCropView;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        OttoAppConfig.getInstance().register(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (LinearLayout) findViewById(R.id.capture_container);
        scanCropView = (LinearLayout) findViewById(R.id.capture_crop_view);
        ImageView scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(2000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

        initButton();
    }

    private ImageView mLightBtn;

    private void initButton() {
        ImageView backBtn = (ImageView) findViewById(R.id.action_back);
        TextView albumBtn = (TextView) findViewById(R.id.action_album);
        mLightBtn = (ImageView) findViewById(R.id.action_light);

        backBtn.setOnClickListener(this);
        albumBtn.setOnClickListener(this);
        mLightBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
        offLight();
        OttoAppConfig.getInstance().unregister(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(LOG_TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();

        if (rawResult == null) {
            Snackbar.make(scanContainer, getString(R.string.decode_null), Snackbar.LENGTH_SHORT).show();
            return;
        }

//        beepManager.playBeepSoundAndVibrate();
        AudioPlayer.getInstance(this).playRaw(R.raw.scan, false, false);
//        MyUtil.vibrate(this);

        operateResult(rawResult);
    }

    private void operateResult(Result rawResult) {
        String codeType = rawResult.getBarcodeFormat().toString();
        String scanResult = rawResult.getText();
        // 二维码
        if ("QR_CODE".equals(codeType) || "DATA_MATRIX".equals(codeType)) {
            boolean isUrl = MyUtil.checkWebSite(scanResult);
            // 不是标准网址
            if (!isUrl) {
                // 如果是没有添加协议的网址
                if (MyUtil.checkWebSitePath(scanResult)) {
                    scanResult = "http://" + scanResult;
                    isUrl = true;
                }
            }

            // 扫描结果为网址
            if (isUrl) {
                try {
//                    Intent intent = new Intent("android.intent.action.VIEW");
                    Intent intent = new Intent();
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    Uri uri = Uri.parse(scanResult);
//                    intent.setData(uri);
                    intent.putExtra(SCAN_RESULT, scanResult);
                    myFinish(intent);
                } catch (Exception e) {
                    LogUtil.e(LOG_TAG, "handleDecode: " + e.toString());
                    displayResult(scanResult, 0);
                }
            } else {
                displayResult(scanResult, 0);
            }
            // 条形码
        } else if ("EAN_13".equals(codeType)) {
            displayResult(scanResult, 1);
        } else {
            Snackbar.make(scanContainer, getString(R.string.decode_null), Snackbar.LENGTH_SHORT).show();
        }
    }

    public static final String SCAN_RESULT = "result";
    public static final String SCAN_TYPE = "scan_type";

    /**
     * 显示扫描结果
     *
     * @param scanResult 扫描内容
     * @param type       扫描类型：0，二维码；1，条形码
     */
    private void displayResult(String scanResult, int type) {
        Intent intent = new Intent();
        intent.putExtra(SCAN_RESULT, scanResult);
        intent.putExtra(SCAN_TYPE, type);
        myFinish(intent);
    }

    private void myFinish(Intent intent) {
        setResult(RESULT_OK,intent);
        finish();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(LOG_TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(LOG_TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(LOG_TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private static final int REQUEST_MY_DIALOG = 1;

    private void displayFrameworkBugMessageAndExit() {
        // camera error
       finish();

/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_MY_DIALOG) {
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void initCrop() {
        // http://skillcollege.github.io/2015/02/05/-打造极致二维码扫描系列-基于ZBar的Android平台解码/

        // 预览图的高度，也即camera的分辨率宽高
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        // 布局文件中扫描框的左上角定点坐标
        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        // 布局文件中扫描框的宽高
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean mIsLightOpen;

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.action_back) {
            finish();

            // 开关灯按钮
        } else if (i == R.id.action_light) {
            operateLight();

            // 相册按钮
        } else if (i == R.id.action_album) {//                该功能带添加
//                if (MyUtil.isFastDoubleClick()) {
//                    return;
//                }
//
//                offLight();
//
//                Intent intent = new Intent(this, LocalAlbumActivity.class);
//                intent.putExtra(WeacConstants.REQUEST_LOCAL_ALBUM_TYPE, 1);
//                startActivity(intent);
//                overridePendingTransition(R.anim.zoomin, 0);

        }
    }

    /**
     * 开关灯
     */
    private void operateLight() {
        if (!mIsLightOpen) {
            cameraManager.openLight();
            mIsLightOpen = true;
            mLightBtn.setBackground(getResources().getDrawable(R.drawable.light_pressed));
        } else {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightBtn.setBackground(getResources().getDrawable(R.drawable.light_normal));
        }
    }

    /**
     * 关灯
     */
    private void offLight() {
        if (mIsLightOpen) {
            cameraManager.offLight();
            mIsLightOpen = false;
            mLightBtn.setBackground(getResources().getDrawable(R.drawable.light_normal));
        }
    }

    private ViewGroup progressBarLlyt;

    @Subscribe
    public void scanQRcodeEvent(ScanCodeEvent event) {
        progressBarLlyt = (ViewGroup) findViewById(R.id.progress_bar_llyt);
        progressBarLlyt.setVisibility(View.VISIBLE);

        String imagePath = event.getImageUrl();
        if (!TextUtils.isEmpty(imagePath)) {
            int myWidth = getResources().getDisplayMetrics().widthPixels;
            int myHeight = getResources().getDisplayMetrics().heightPixels;

            Glide.with(getApplicationContext()).load("file://" + imagePath).asBitmap().into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    progressBarLlyt.setVisibility(View.GONE);

                    Result resultZxing = new DecodeUtils(DecodeUtils.DECODE_DATA_MODE_ALL)
                            .decodeWithZxing(resource);
                    handleDecode(resultZxing, null);
                }
            });
        }
    }
}
