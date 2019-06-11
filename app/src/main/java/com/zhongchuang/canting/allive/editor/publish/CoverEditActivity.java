package com.zhongchuang.canting.allive.editor.publish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.aliyun.common.media.ShareableBitmap;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.qupai.editor.impl.AliyunThumbnailFetcher;
import com.aliyun.struct.common.ScaleMode;
import com.zhongchuang.canting.utils.PhotoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.valuesfeng.picker.ImageSelectActivity;

/**
 * Created by macpro on 2017/11/7.
 */

public class CoverEditActivity extends Activity implements View.OnClickListener{

    public static final String KEY_PARAM_VIDEO = "vidseo_path";
    public static final String KEY_PARAM_RESULT = "thumbnail";
    private ImageView mIvLeft;
    private ImageView mCoverImage;
    private Button mIvRight;
    private TextView mTitle;
    private View mSlider;
    private LinearLayout mThumbnailList;

    private String mVideoPath;
    private AliyunIThumbnailFetcher mThumbnailFetcher;
    private AliyunIThumbnailFetcher mCoverFetcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_svideo_activity_cover_edit);
        initView();
        mVideoPath = getIntent().getStringExtra(KEY_PARAM_VIDEO);
        mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        mCoverFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        mThumbnailFetcher.addVideoSource(mVideoPath, 0, Integer.MAX_VALUE);
        mCoverFetcher.addVideoSource(mVideoPath, 0, Integer.MAX_VALUE);
        mThumbnailList.post(mInitThumbnails);
        mCoverImage.post(new Runnable() {
            @Override
            public void run() {
                initCoverParameters();
                mCoverFetcher.requestThumbnailImage(new long[]{0}, mThumbnailCallback);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThumbnailFetcher.release();
        mCoverFetcher.release();
    }

    private void initView(){
        mIvLeft = findViewById(R.id.iv_left);
        mIvRight = findViewById(R.id.iv_right);
        mTitle = findViewById(R.id.tv_center);

        mIvLeft.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);

        mTitle.setText(R.string.edit_cover);
        mIvLeft.setImageResource(R.drawable.arrow_left_white);

        mIvLeft.setOnClickListener(this);
        mIvRight.setOnClickListener(this);

        mSlider = findViewById(R.id.indiator);
        mSlider.setOnTouchListener(mSliderListener);
        mCoverImage = findViewById(R.id.cover_image);
        mThumbnailList = findViewById(R.id.cover_thumbnail_list);
        mThumbnailList.setOnTouchListener(mClickListener);
    }
    private String img_path;
    private String path;
    private File fileCropUri;
    private Uri cropImageUri;

    @Override
    public void onClick(View v) {
        if(v == mIvLeft){
            onBackPressed();
        }else if(v == mIvRight){
            ShareableBitmap sbmp = (ShareableBitmap) mCoverImage.getTag();
            if(sbmp != null || sbmp.getData() != null){
                int output_X = 360, output_Y = 360;
                 path=Environment.getExternalStorageDirectory().getPath() + "/"+ System.currentTimeMillis()+".jpg";
                fileCropUri = new File(path);

                    img_path = getExternalFilesDir(null) + "thumbnail.jpeg";
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.fromFile(new File(img_path));
                    PhotoUtils.cropImageUris(CoverEditActivity.this, newUri, cropImageUri, 16, 9, output_X, output_Y, 99);
                    img_path="";

                try{
                    sbmp.getData().compress(Bitmap.CompressFormat.JPEG, 100,
                            new FileOutputStream(path));
                }catch (IOException e){
                    e.printStackTrace();
                }


            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int output_X = 360, output_Y = 360;
            switch (requestCode) {
                //上传照片

                case 99:
                    Intent datas = new Intent();
                    datas.putExtra(KEY_PARAM_RESULT, path);
                    setResult(RESULT_OK, datas);
                    finish();
                    break;
            }
        }
    }
    private final View.OnTouchListener mClickListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int offset = mSlider.getLeft() - mSlider.getPaddingLeft();
            int vw = mSlider.getWidth() - mSlider.getPaddingRight() - mSlider.getPaddingLeft();
            int endOffset = mSlider.getLeft() + mThumbnailList.getWidth() - vw - mSlider.getPaddingLeft();
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                float x = motionEvent.getX();
                float px = x + mSlider.getLeft() - mSlider.getPaddingLeft();
                if(px >= endOffset){
                    px = endOffset;
                }
                if(px <= offset){
                    px = offset;
                }
                long time = (long)(mCoverFetcher.getTotalDuration() * px / mThumbnailList.getWidth());
                fetcheThumbnail(time);

                mSlider.setX(px);
            }
            return true;
        }
    };

    private final View.OnTouchListener mSliderListener = new View.OnTouchListener() {

        private float lastX;
        private float dx;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int offset = v.getLeft() - v.getPaddingLeft();
            int vw = v.getWidth() - v.getPaddingRight() - v.getPaddingLeft();
            int endOffset = v.getLeft() + mThumbnailList.getWidth() - vw - v.getPaddingLeft();
            long time = 0;
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    dx =  lastX - v.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
//                    float distance = event.getX() - lastX;
                    lastX = event.getRawX();
                    float nx = lastX - dx;
                    if(nx >= endOffset){
                        nx = endOffset;
                    }
                    if(nx <= offset){
                        nx = offset;
                    }

                    v.setX(nx);
                    time = (long)(mCoverFetcher.getTotalDuration() * (nx - offset) / mThumbnailList.getWidth());
                    fetcheThumbnail(time);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    float x = v.getX() - offset;
                    time = (long)(mCoverFetcher.getTotalDuration() * x / mThumbnailList.getWidth());
                    fetcheThumbnail(time);
                    break;
            }
            return true;
        }
    };

    private int mFetchingThumbnailCount;
    private void fetcheThumbnail(long time){
        Log.d("FETCHER", "fetcher time : " + time + "  count : " + mFetchingThumbnailCount
        + " duration ：" + mCoverFetcher.getTotalDuration());
        if(time >= mCoverFetcher.getTotalDuration()){
            time = mCoverFetcher.getTotalDuration() - 500;
        }
        if(mFetchingThumbnailCount > 2){
            return;
        }
        mFetchingThumbnailCount++;
        mCoverFetcher.requestThumbnailImage(new long[]{time}, mThumbnailCallback);
    }

    private final Runnable mInitThumbnails = new Runnable() {
        @Override
        public void run() {
            mSlider.setX(mSlider.getX() - mSlider.getPaddingLeft());
            initThumbnails();
        }
    };

    private void initThumbnails(){
        int width = mThumbnailList.getWidth();
        int itemWidth= width / 8;
        mThumbnailFetcher.setParameters(itemWidth, itemWidth,
                AliyunIThumbnailFetcher.CropMode.Mediate, ScaleMode.LB, 8);
        long duration = mThumbnailFetcher.getTotalDuration();
        long itemTime = duration / 8;
//        long[] times = new long[8];
        for(int i = 0; i < 8; i++){
//            times[i] = itemTime * i;
            long time = itemTime * i;
            mThumbnailFetcher.requestThumbnailImage(new long[]{time},
                    new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
                        @Override
                        public void onThumbnailReady(ShareableBitmap frameBitmap, long time) {
                            initThumbnails(frameBitmap.getData());
                        }

                        @Override
                        public void onError(int errorCode) {

                        }
                    });
        }

//        mThumbnailFetcher.requestThumbnailImage(times,
//                new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
//                    private int count;
//                    List<Bitmap> thumbnails = new ArrayList<>();
//                    @Override
//                    public void onThumbnailReady(ShareableBitmap frameBitmap, long time) {
//                        count++;
//                        thumbnails.add(frameBitmap.getData());
//                        if(count == 8){
//                            initThumbnails(thumbnails);
//                        }
//                    }
//
//                    @Override
//                    public void onError(int errorCode) {
//                        count++;
//                        if(count == 8){
//                            initThumbnails(thumbnails);
//                        }
//                    }
//                });
    }

    private void initThumbnails(Bitmap thumbnail){
        ImageView image = new ImageView(this);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageBitmap(thumbnail);
        mThumbnailList.addView(image);
    }

    private void initCoverParameters(){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mVideoPath);
        String sw = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String sh = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        int w = Integer.parseInt(sw);
        int h = Integer.parseInt(sh);
        int maxWidth = getResources().getDisplayMetrics().widthPixels;
        float scale = (float) h / w;
        h = (int)(maxWidth * scale);
        int maxHeight = mCoverImage.getHeight();
        if(h > maxHeight){
            h = maxHeight;
            w = (int)(maxHeight / scale);
        }else{
            w = maxWidth;
        }

        mCoverFetcher.setParameters(w, h, AliyunIThumbnailFetcher.CropMode.Mediate, ScaleMode.LB, 2);
    }

    private final AliyunThumbnailFetcher.OnThumbnailCompletion mThumbnailCallback = new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
        @Override
        public void onThumbnailReady(ShareableBitmap frameBitmap, long time) {
            mFetchingThumbnailCount--;
            if(mFetchingThumbnailCount < 0){
                mFetchingThumbnailCount = 0;
            }
            Log.d("FETCHER", "fetcher onThumbnailReady time : " + time + "  count : " + mFetchingThumbnailCount);
            ShareableBitmap sbmp = (ShareableBitmap) mCoverImage.getTag();
            if(sbmp != null /*&& sbmp != frameBitmap*/){
                sbmp.release();
            }
            mCoverImage.setImageBitmap(frameBitmap.getData());
            mCoverImage.setTag(frameBitmap);
        }

        @Override
        public void onError(int errorCode) {
            mFetchingThumbnailCount--;
            if(mFetchingThumbnailCount < 0){
                mFetchingThumbnailCount = 0;
            }
            Log.d("FETCHER", "fetcher onError  count : " + mFetchingThumbnailCount);
        }
    };

}
