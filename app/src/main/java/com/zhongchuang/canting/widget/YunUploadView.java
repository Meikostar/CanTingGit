package com.zhongchuang.canting.widget;

/**
 * Created by Meiko on 2017/4/18.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.ImageOptions;
import com.zhongchuang.canting.utils.StringUtil;


/**
 * Created by mykar on 17/4/12.
 */

public class YunUploadView extends LinearLayout {
    public static final int RESULT_SUCCESS = 1;
    private Context mContext;
    private View mView;
    private ImageView mImageViewContent;
    private ProgressBar mProgress;
    private ImageView mImageViewDelete;
    private View mViewMask;
    private TextView mTextViewReady;
    private TextView mTextViewMainImage;
    private ImageView mImageViewVideo;
    private int uploadResultCode;
    private boolean haveMainImage = false;

    public YunUploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public YunUploadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YunUploadView(Context context) {
        super(context);
        init(context);
    }

    public void setHaveMainImage(boolean haveMainImage){
        this.haveMainImage = haveMainImage;
    }

    private void init(Context context){
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_upload_image,this);
        mImageViewContent = (ImageView) mView.findViewById(R.id.iv_image_content);
        mImageViewDelete = (ImageView)mView.findViewById(R.id.iv_delete);
        mProgress = (ProgressBar)mView.findViewById(R.id.pb_upload_progress);
        mTextViewReady = (TextView)mView.findViewById(R.id.tv_ready_upload);
        mViewMask = mView.findViewById(R.id.view_mask);
        mTextViewMainImage = (TextView) mView.findViewById(R.id.tv_main_image);
        mImageViewVideo = (ImageView)mView.findViewById(R.id.iv_is_video);
    }
    public void setDiss(){
        mImageViewDelete.setVisibility(View.GONE);
    }
    public void setContent(String imagePath, ImageOptions options){
        if (StringUtil.isEmpty(imagePath)){
            setVisibility(GONE);
            return;
        }else {
            setVisibility(VISIBLE);
            mImageViewDelete.setVisibility(VISIBLE);
        }
        if (options!=null) {
            Glide.with(mContext)
                    .load(StringUtil.changeUrl(imagePath))
                    .override(200, 200)
                    .into(mImageViewContent);
            //    x.image().bind(mImageViewContent, imagePath, options);
        }else {
            //    x.image().bind(mImageViewContent,imagePath);
            Glide.with(mContext)
                    .load(StringUtil.changeUrl(imagePath))
                    .into(mImageViewContent);
        }
    }

    public ImageView getDeleteView(){
        return mImageViewDelete;
    }

    public void setMainImage(boolean isMain){
        if (isMain && haveMainImage){
            mTextViewMainImage.setVisibility(VISIBLE);
        }else {
            mTextViewMainImage.setVisibility(GONE);
        }
    }

    public void setIsVideo(boolean isVideo){
        if (isVideo){
            mImageViewVideo.setVisibility(VISIBLE);
        }else {
            mImageViewVideo.setVisibility(GONE);
        }
    }



    public void setProgress(double progress){
        mProgress.setProgress((int)(progress*100));
    }

    public int getProgress(){
        return mProgress.getProgress();
    }

    public void setUploadResult(int resultCode){
        uploadResultCode = resultCode;
        setResultView(resultCode);

    }

    private void setResultView(int resultCode) {
        if (resultCode==RESULT_SUCCESS){
            mViewMask.setVisibility(GONE);
            mProgress.setVisibility(GONE);
        }else {
            mProgress.setVisibility(GONE);
        }
        mTextViewReady.setVisibility(GONE);
        mImageViewDelete.setVisibility(VISIBLE);
    }

    public void readyToUpload(){
        mViewMask.setVisibility(VISIBLE);
        mProgress.setVisibility(GONE);
        mImageViewDelete.setVisibility(GONE);
        mTextViewReady.setVisibility(VISIBLE);
    }

    public void beginUpload(){
        mImageViewDelete.setVisibility(GONE);
        mViewMask.setVisibility(VISIBLE);
        mProgress.setVisibility(VISIBLE);
        mTextViewReady.setVisibility(GONE);
        mProgress.setProgress(1);
    }

    public void resetView(){
        mImageViewDelete.setVisibility(VISIBLE);
        mViewMask.setVisibility(GONE);
        mProgress.setVisibility(GONE);
        mTextViewReady.setVisibility(GONE);
    }
}
