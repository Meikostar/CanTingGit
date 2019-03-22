/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.importer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.crop.AliyunImageCrop;
import com.zhongchuang.canting.allive.crop.AliyunVideoCrop;
import com.zhongchuang.canting.allive.editor.editor.EditorActivity;
import com.zhongchuang.canting.allive.editor.publish.PublishActivity;
import com.zhongchuang.canting.allive.importer.media.GalleryDirChooser;
import com.zhongchuang.canting.allive.importer.media.GalleryMediaChooser;
import com.zhongchuang.canting.allive.importer.media.MediaDir;
import com.zhongchuang.canting.allive.importer.media.MediaInfo;
import com.zhongchuang.canting.allive.importer.media.MediaStorage;
import com.zhongchuang.canting.allive.importer.media.SelectedMediaAdapter;
import com.zhongchuang.canting.allive.importer.media.SelectedMediaViewHolder;
import com.zhongchuang.canting.allive.importer.media.ThumbnailGenerator;
import com.zhongchuang.canting.allive.pusher.ui.activity.PushConfigActivity;
import com.zhongchuang.canting.allive.quview.ProgressDialog;
import com.aliyun.common.global.AliyunTag;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.jasonparse.JSONSupportImpl;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.import_core.AliyunIImport;
import com.aliyun.qupai.import_core.AliyunImportCreator;
import com.aliyun.struct.common.AliyunDisplayMode;
import com.aliyun.struct.common.AliyunImageClip;
import com.aliyun.struct.common.AliyunVideoClip;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.encoder.VideoCodecs;
import com.duanqu.transcode.NativeParser;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;


public class MediaActivity extends Activity implements View.OnClickListener {
    private static final int[][] RESOLUTIONS = new int[][]{new int[]{540, 720}, new int[]{540, 540}, new int[]{540, 960}};

    private static final int REQUEST_CODE_VIDEO_CROP = 1;
    private static final int REQUEST_CODE_IMAGE_CROP = 2;
    private MediaStorage storage;
    private ProgressDialog progressDialog;
    private GalleryDirChooser galleryDirChooser;
    private ThumbnailGenerator thumbnailGenerator;
    private GalleryMediaChooser galleryMediaChooser;
    private RecyclerView galleryView;
    private RecyclerView mRvSelectedVideo;
    private TextView mTvTotalDuration;
    private EditText mEtVideoPath;
    private ImageView back;
    private TextView title;
    private int mRatio;
    private ScaleMode scaleMode = ScaleMode.LB;
    private int frameRate;
    private int gop;
    private int mBitrate;
    private VideoQuality quality = VideoQuality.SSD;
    private SelectedMediaAdapter mSelectedVideoAdapter;
    private AliyunIImport mImport;
    private Transcoder mTransCoder;
    private MediaInfo mCurrMediaInfo;
    private int mCropPosition;
    private boolean mIsReachedMaxDuration = false;
    private AliyunVideoParam mVideoParam;
    private int[] mOutputResolution = null;
    private Button mBtnNextStep;

    private int requestWidth;
    private int requestHeight;

    private static final String MIME_IMAGE = "image";
    private static final String MIME_VIDEO = "video";

    private static final String FORMAT_GIF = "gif";
    private static final String FORMAT_PNG = "png";
    private static final String FORMAT_JPG = "jpg";
    private static final String FORMAT_JPEG = "jpeg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aliyun_svideo_import_activity_media);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getData();
        init();
    }
        private Subscription mSubscription;


    private void getData() {
        mRatio = getIntent().getIntExtra(CropKey.VIDEO_RATIO, CropKey.RATIO_MODE_3_4);
        scaleMode = (ScaleMode) getIntent().getSerializableExtra(CropKey.VIDEO_SCALE);
        if (scaleMode == null) {
            scaleMode = ScaleMode.LB;
        }
        frameRate = getIntent().getIntExtra(CropKey.VIDEO_FRAMERATE, 25);
        gop = getIntent().getIntExtra(CropKey.VIDEO_GOP, 125);
        mBitrate = getIntent().getIntExtra(CropKey.VIDEO_BITRATE, 0);
        quality = (VideoQuality) getIntent().getSerializableExtra(CropKey.VIDEO_QUALITY);
        if (quality == null) {
            quality = VideoQuality.SSD;
        }
        mOutputResolution = RESOLUTIONS[mRatio];
        mVideoParam = new AliyunVideoParam.Builder()
                .frameRate(frameRate)
                .gop(gop)
                .crf(25)
                .bitrate(mBitrate)
                .videoQuality(quality)
                .scaleMode(scaleMode)
                .outputWidth(mOutputResolution[0])
                .outputHeight(mOutputResolution[1])
                .videoCodec(VideoCodecs.H264_SOFT_FFMPEG)
                .build();
        try {
            requestWidth = Integer.parseInt(getIntent().getStringExtra("width"));
        } catch (Exception e) {
            requestWidth = 0;
        }
        try {
            requestHeight = Integer.parseInt(getIntent().getStringExtra("height"));
        } catch (Exception e) {
            requestHeight = 0;
        }

    }

    private void init() {
        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.LIVE_FINISH) {
                    finish();
                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        mTransCoder = new Transcoder();
        mTransCoder.init(this);
        mTransCoder.setTransCallback(new Transcoder.TransCallback() {
            @Override
            public void onError(Throwable e, final int errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        switch (errorCode) {
                            case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_not_supported_audio);
                                break;
                            case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_video_crop_error);
                                break;
                            case AliyunErrorCode.ERROR_UNKNOWN:
                            default:
                                ToastUtil.showToast(MediaActivity.this, R.string.aliyun_video_error);
                        }
                    }
                });

            }

            @Override
            public void onProgress(int progress) {
                if (progressDialog != null) {
                    progressDialog.setProgress(progress);
                }
            }

            @Override
            public void onComplete(List<MediaInfo> resultVideos) {
                Log.d("TRANCODE", "ONCOMPLETED, dialog : " + (progressDialog == null));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                mImport = AliyunImportCreator.getImportInstance(MediaActivity.this);
                mImport.setVideoParam(mVideoParam);
                NativeParser nativeParser = new NativeParser();
                for (int i = 0; i < resultVideos.size(); i++) {
                    MediaInfo mediaInfo = resultVideos.get(i);
                    if (i == 0 && resultVideos.size() > 1) {//first one
                        if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 0,1000,1000, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(
                                    new AliyunVideoClip.Builder()
                                            .source(mediaInfo.filePath)
                                            .startTime(mediaInfo.startTime)
                                            .endTime(mediaInfo.startTime+mediaInfo.duration)
                                            .inDuration(0)
                                            .outDuration(1000)
                                            .overlapDuration(1000)
                                            .displayMode(AliyunDisplayMode.DEFAULT)
                                            .build());
                        } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                            int duration = 5000;
                            if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                                nativeParser.init(mediaInfo.filePath);
                                duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                                nativeParser.release();
                            }
//                            mImport.addImage(mediaInfo.filePath, 0 ,1000,0, duration, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(new AliyunImageClip.Builder()
                                    .source(mediaInfo.filePath)
                                    .inDuration(0)
                                    .outDuration(1000)
                                    .overlapDuration(0)
                                    .duration(duration)
                                    .displayMode(AliyunDisplayMode.DEFAULT)
                                    .build());
                        }
                    }else if(resultVideos.size() > 1 && i == resultVideos.size() - 1){//last one
                        if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 1000,0,1000, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(
                                    new AliyunVideoClip.Builder()
                                            .source(mediaInfo.filePath)
                                            .startTime(mediaInfo.startTime)
                                            .endTime(mediaInfo.startTime+mediaInfo.duration)
                                            .inDuration(1000)
                                            .outDuration(0)
                                            .overlapDuration(1000)
                                            .displayMode(AliyunDisplayMode.DEFAULT)
                                            .build());
                        } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                            int duration = 5000;
                            if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                                nativeParser.init(mediaInfo.filePath);
                                duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                                nativeParser.release();
                            }
//                            mImport.addImage(mediaInfo.filePath, 1000 ,0,0, duration, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(new AliyunImageClip.Builder()
                                    .source(mediaInfo.filePath)
                                    .inDuration(1000)
                                    .outDuration(0)
                                    .overlapDuration(1000)
                                    .duration(duration)
                                    .displayMode(AliyunDisplayMode.DEFAULT)
                                    .build());
                        }
                    }else if(resultVideos.size() > 1){//middle one
                        if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 1000,1000,1000, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(new AliyunVideoClip.Builder()
                                    .source(mediaInfo.filePath)
                                    .startTime(mediaInfo.startTime)
                                    .endTime(mediaInfo.startTime+mediaInfo.duration)
                                    .inDuration(1000)
                                    .outDuration(1000)
                                    .overlapDuration(1000)
                                    .displayMode(AliyunDisplayMode.DEFAULT)
                                    .build());
                        } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                            int duration = 5000;
                            if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                                nativeParser.init(mediaInfo.filePath);
                                duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                                nativeParser.release();
                            }
//                            mImport.addImage(mediaInfo.filePath, 1000, 1000,1000,duration, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(new AliyunImageClip.Builder()
                                    .source(mediaInfo.filePath)
                                    .inDuration(1000)
                                    .outDuration(1000)
                                    .overlapDuration(1000)
                                    .duration(duration)
                                    .displayMode(AliyunDisplayMode.DEFAULT)
                                    .build());
                        }
                    }else {//only one
                        if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 0,0,0, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(
                                    new AliyunVideoClip.Builder()
                                            .source(mediaInfo.filePath)
                                            .startTime(mediaInfo.startTime)
                                            .endTime(mediaInfo.startTime+mediaInfo.duration)
                                            .inDuration(0)
                                            .outDuration(0)
                                            .overlapDuration(0)
                                            .displayMode(AliyunDisplayMode.DEFAULT)
                                            .build());
                        } else if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
                            int duration = 5000;
                            if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                                nativeParser.init(mediaInfo.filePath);
                                duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                                nativeParser.release();
                            }
//                            mImport.addImage(mediaInfo.filePath, 0, 0,0,duration, AliyunDisplayMode.DEFAULT);
                            mImport.addMediaClip(new AliyunImageClip.Builder()
                                    .source(mediaInfo.filePath)
                                    .inDuration(0)
                                    .outDuration(0)
                                    .overlapDuration(0)
                                    .duration(duration)
                                    .displayMode(AliyunDisplayMode.DEFAULT)
                                    .build());
                        }
                    }
                }
                String projectJsonPath = mImport.generateProjectConfigure();

                if (projectJsonPath != null) {
                    Intent intent = new Intent(MediaActivity.this,EditorActivity.class);
                    intent.putExtra("video_param", mVideoParam);
                    intent.putExtra("project_json_path", projectJsonPath);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelComplete() {
                //取消完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnNextStep.setEnabled(true);
                    }
                });
            }
        });
        mBtnNextStep = (Button) findViewById(R.id.btn_next_step);
        galleryView = (RecyclerView) findViewById(R.id.gallery_media);
        title = (TextView) findViewById(R.id.gallery_title);
        title.setText(R.string.gallery_all_media);
        mEtVideoPath = (EditText) findViewById(R.id.et_video_path);
//        mEtVideoPath.setText("sdcard/test.gif;sdcard/2A965369-89B.A-495A-AF84-1F4EC63D390F.gif");
        back = (ImageView) findViewById(R.id.gallery_closeBtn);
        back.setOnClickListener(this);
        storage = new MediaStorage(this, new JSONSupportImpl());
        thumbnailGenerator = new ThumbnailGenerator(this);
        galleryDirChooser = new GalleryDirChooser(this, findViewById(R.id.topPanel), thumbnailGenerator, storage);
        galleryMediaChooser = new GalleryMediaChooser(galleryView, galleryDirChooser, storage, thumbnailGenerator);
        storage.setSortMode(MediaStorage.SORT_MODE_MERGE);
        storage.startFetchmedias();
        storage.setOnMediaDirChangeListener(new MediaStorage.OnMediaDirChange() {
            @Override
            public void onMediaDirChanged() {
                MediaDir dir = storage.getCurrentDir();
                if (dir.id == -1) {
                    title.setText(getString(R.string.gallery_all_media));
                } else {
                    title.setText(dir.dirName);
                }
                galleryMediaChooser.changeMediaDir(dir);
            }
        });
        storage.setOnCurrentMediaInfoChangeListener(new MediaStorage.OnCurrentMediaInfoChange() {
            @Override
            public void onCurrentMediaInfoChanged(MediaInfo info) {
                MediaInfo infoCopy = new MediaInfo();
                infoCopy.addTime = info.addTime;
                if (info.mimeType.startsWith(MIME_IMAGE)) {
                    infoCopy.duration = 5000;//图片的时长设置为3s
                } else {
                    infoCopy.duration = info.duration;
                }
                infoCopy.filePath = info.filePath;
                infoCopy.id = info.id;
                infoCopy.isSquare = info.isSquare;
                infoCopy.mimeType = info.mimeType;
                infoCopy.thumbnailPath = info.thumbnailPath;
                infoCopy.title = info.title;
                infoCopy.type = info.type;
                mSelectedVideoAdapter.addMedia(infoCopy);
//                mImport.addVideo(infoCopy.filePath, 3000, AliyunDisplayMode.DEFAULT);    //导入器中添加视频
                mTransCoder.addMedia(infoCopy);
            }
        });
        mRvSelectedVideo = (RecyclerView) findViewById(R.id.rv_selected_video);
        mTvTotalDuration = (TextView) findViewById(R.id.tv_duration_value);
        //最大时长5分钟
        mSelectedVideoAdapter = new SelectedMediaAdapter(new MediaImageLoader(this), 5 * 60 * 1000);
        mRvSelectedVideo.setAdapter(mSelectedVideoAdapter);
        mRvSelectedVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTvTotalDuration.setText(convertDuration2Text(0));
        mTvTotalDuration.setActivated(false);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //首先回调的方法 返回int表示是否监听该方向
                //拖拽
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                //侧滑删除
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //滑动事件
                mSelectedVideoAdapter.swap((SelectedMediaViewHolder) viewHolder, (SelectedMediaViewHolder) target);
                //mImport.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mTransCoder.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                //是否可拖拽
                return true;
            }
        });
        itemTouchHelper.attachToRecyclerView(mRvSelectedVideo);
        mSelectedVideoAdapter.setItemViewCallback(new SelectedMediaAdapter.OnItemViewCallback() {
            @Override
            public void onItemPhotoClick(MediaInfo info, int position) {
                mCurrMediaInfo = info;
                mCropPosition = position;
                if (info.mimeType.startsWith(MIME_VIDEO)) {
                    Intent intent = new Intent(MediaActivity.this, AliyunVideoCrop.class);
                    intent.putExtra(CropKey.VIDEO_PATH, info.filePath);
                    intent.putExtra(CropKey.VIDEO_DURATION, info.duration);
                    intent.putExtra(CropKey.VIDEO_RATIO, mRatio);
                    intent.putExtra(CropKey.VIDEO_SCALE, scaleMode);
                    intent.putExtra(CropKey.VIDEO_QUALITY, quality);
                    intent.putExtra(CropKey.VIDEO_GOP, gop);
                    intent.putExtra(CropKey.VIDEO_BITRATE, mBitrate);
                    intent.putExtra(CropKey.VIDEO_FRAMERATE, frameRate);
                    intent.putExtra(CropKey.ACTION, CropKey.ACTION_SELECT_TIME);//是否真裁剪
                    startActivityForResult(intent, REQUEST_CODE_VIDEO_CROP);
                } else if (info.mimeType.startsWith(MIME_IMAGE)) {
                    Intent intent = new Intent(MediaActivity.this, AliyunImageCrop.class);
                    intent.putExtra(CropKey.VIDEO_PATH, info.filePath);
                    intent.putExtra(CropKey.VIDEO_DURATION, info.duration);
                    intent.putExtra(CropKey.VIDEO_RATIO, mRatio);
                    intent.putExtra(CropKey.VIDEO_SCALE, scaleMode);
                    intent.putExtra(CropKey.VIDEO_QUALITY, quality);
                    intent.putExtra(CropKey.VIDEO_GOP, gop);
                    intent.putExtra(CropKey.VIDEO_BITRATE, mBitrate);
                    intent.putExtra(CropKey.VIDEO_FRAMERATE, frameRate);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE_CROP);
                }
            }

            @Override
            public void onItemDeleteClick(MediaInfo info) {
//                mImport.removeVideo(info.filePath); //从导入器中移除视频
                mTransCoder.removeMedia(info);
            }

            @Override
            public void onDurationChange(long currDuration, boolean isReachedMaxDuration) {
                mTvTotalDuration.setText(convertDuration2Text(currDuration));
                mTvTotalDuration.setActivated(isReachedMaxDuration);
                mIsReachedMaxDuration = isReachedMaxDuration;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(CropKey.RESULT_KEY_CROP_PATH);
            switch (requestCode) {
                case REQUEST_CODE_VIDEO_CROP:
                    long duration = data.getLongExtra(CropKey.RESULT_KEY_DURATION, 0);
                    long startTime = data.getLongExtra(CropKey.RESULT_KEY_START_TIME, 0);
                    if (!TextUtils.isEmpty(path) && duration > 0 && mCurrMediaInfo != null) {
                        mSelectedVideoAdapter.changeDurationPosition(mCropPosition, duration);
                        int index = mTransCoder.removeMedia(mCurrMediaInfo);
                        mCurrMediaInfo.filePath = path;
                        mCurrMediaInfo.startTime = startTime;
                        mCurrMediaInfo.duration = (int) duration;
                        mTransCoder.addMedia(index, mCurrMediaInfo);
                    }
                    break;
                case REQUEST_CODE_IMAGE_CROP:
                    if (!TextUtils.isEmpty(path) && mCurrMediaInfo != null) {
                        int index = mTransCoder.removeMedia(mCurrMediaInfo);
                        mCurrMediaInfo.filePath = path;
                        mTransCoder.addMedia(index, mCurrMediaInfo);
                    }
                    break;
                default:
                    break;
            }

        }
    }
    public void showPopWindows() {

        View view = LayoutInflater.from(MediaActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_choose = (TextView) view.findViewById(R.id.tv_choose);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_choose.setText("编辑视频");
        tv_camera.setText("直接上传");
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.addLiveRecordVod();
                List<MediaInfo> data = mTransCoder.getData();
                if(data!=null&&data.size()>0){
                 String path=   data.get(0).filePath;
                 String thpath=   data.get(0).thumbnailPath;
                    Intent intent = new Intent(MediaActivity.this, PublishActivity.class);
                    intent.putExtra(PublishActivity.KEY_PARAM_THUMBNAIL, thpath);
                    intent.putExtra(PublishActivity.KEY_PARAM_CONFIG, path);
                    startActivity(intent);
                }


                mWindowAddPhoto.dismiss();
            }
        });
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooses(1);
                mWindowAddPhoto.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowAddPhoto.dismiss();
            }
        });
        mWindowAddPhoto = new PhotoPopupWindow(MediaActivity.this).bindView(view);
        mWindowAddPhoto.showAtLocation(mEtVideoPath, Gravity.BOTTOM, 0, 0);

    }

    private PhotoPopupWindow mWindowAddPhoto;
    private String convertDuration2Text(long duration) {
        int sec = Math.round(((float) duration) / 1000);
        int hour = sec / 3600;
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        return String.format(getString(R.string.video_duration),
                hour,
                min,
                sec);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storage.saveCurrentDirToCache();
        storage.cancelTask();
        mSubscription.unsubscribe();
        mTransCoder.release();
        thumbnailGenerator.cancelAllTask();
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        } else if (v.getId() == R.id.btn_next_step) {//点击下一步
           showPopWindows();
        }
    }


    public void chooses(int poition){

        String videoPath = mEtVideoPath.getText().toString();
        ArrayList<MediaInfo> resultVideos = new ArrayList<>();
        if(videoPath != null && !videoPath.isEmpty()){
            String[] videos = videoPath.split(";");
            for(String path : videos){
                MediaInfo mediaInfo = new MediaInfo();
                mediaInfo.filePath = path;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                if(path.endsWith(FORMAT_GIF)||path.endsWith(FORMAT_PNG)||path.endsWith(FORMAT_JPG)||path.endsWith(FORMAT_JPEG)){
                    mediaInfo.mimeType = MIME_IMAGE;
                }else{
                    try {
                        retriever.setDataSource(path);
                        int duration = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        mediaInfo.mimeType = MIME_VIDEO;
                        mediaInfo.startTime = 0;
                        mediaInfo.duration = duration;
                    }catch (Exception e){
                        mediaInfo.mimeType = MIME_IMAGE;
                    }
                }
                retriever.release();
                resultVideos.add(mediaInfo);
            }

            mImport = AliyunImportCreator.getImportInstance(MediaActivity.this);
            mImport.setVideoParam(mVideoParam);
            for (int i = 0; i < resultVideos.size(); i++) {
                NativeParser nativeParser = new NativeParser();
                MediaInfo mediaInfo = resultVideos.get(i);
                if (i == 0 && resultVideos.size() > 1) {//first one
                    if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 0,1000,1000, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(
                                new AliyunVideoClip.Builder()
                                        .source(mediaInfo.filePath)
                                        .startTime(mediaInfo.startTime)
                                        .endTime(mediaInfo.startTime+mediaInfo.duration)
                                        .inDuration(0)
                                        .outDuration(1000)
                                        .overlapDuration(1000)
                                        .displayMode(AliyunDisplayMode.DEFAULT)
                                        .build());
                    } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                        int duration = 5000;
                        if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                            nativeParser.init(mediaInfo.filePath);
                            duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                            nativeParser.release();
                        }
//                            mImport.addImage(mediaInfo.filePath, 0 ,1000,0, duration, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(new AliyunImageClip.Builder()
                                .source(mediaInfo.filePath)
                                .inDuration(0)
                                .outDuration(1000)
                                .overlapDuration(0)
                                .duration(duration)
                                .displayMode(AliyunDisplayMode.DEFAULT)
                                .build());
                    }
                }else if(resultVideos.size() > 1 && i == resultVideos.size() - 1){//last one
                    if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 1000,0,1000, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(
                                new AliyunVideoClip.Builder()
                                        .source(mediaInfo.filePath)
                                        .startTime(mediaInfo.startTime)
                                        .endTime(mediaInfo.startTime+mediaInfo.duration)
                                        .inDuration(1000)
                                        .outDuration(0)
                                        .overlapDuration(1000)
                                        .displayMode(AliyunDisplayMode.DEFAULT)
                                        .build());
                    } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                        int duration = 5000;
                        if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                            nativeParser.init(mediaInfo.filePath);
                            duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                            nativeParser.release();
                        }
//                            mImport.addImage(mediaInfo.filePath, 1000 ,0,0, duration, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(new AliyunImageClip.Builder()
                                .source(mediaInfo.filePath)
                                .inDuration(1000)
                                .outDuration(0)
                                .overlapDuration(1000)
                                .duration(duration)
                                .displayMode(AliyunDisplayMode.DEFAULT)
                                .build());
                    }
                }else if(resultVideos.size() > 1){//middle one
                    if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 1000,1000,1000, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(new AliyunVideoClip.Builder()
                                .source(mediaInfo.filePath)
                                .startTime(mediaInfo.startTime)
                                .endTime(mediaInfo.startTime+mediaInfo.duration)
                                .inDuration(1000)
                                .outDuration(1000)
                                .overlapDuration(1000)
                                .displayMode(AliyunDisplayMode.DEFAULT)
                                .build());
                    } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                        int duration = 5000;
                        if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                            nativeParser.init(mediaInfo.filePath);
                            duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                            nativeParser.release();
                        }
//                            mImport.addImage(mediaInfo.filePath, 1000, 1000,1000,duration, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(new AliyunImageClip.Builder()
                                .source(mediaInfo.filePath)
                                .inDuration(1000)
                                .outDuration(1000)
                                .overlapDuration(1000)
                                .duration(duration)
                                .displayMode(AliyunDisplayMode.DEFAULT)
                                .build());
                    }
                }else {//only one
                    if (mediaInfo.mimeType.startsWith(MIME_VIDEO)) {
//                            mImport.addVideo(mediaInfo.filePath, mediaInfo.startTime, mediaInfo.startTime + mediaInfo.duration, 0,0,0, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(
                                new AliyunVideoClip.Builder()
                                        .source(mediaInfo.filePath)
                                        .startTime(mediaInfo.startTime)
                                        .endTime(mediaInfo.startTime+mediaInfo.duration)
                                        .inDuration(0)
                                        .outDuration(0)
                                        .overlapDuration(0)
                                        .displayMode(AliyunDisplayMode.DEFAULT)
                                        .build());
                    } else if (mediaInfo.mimeType.startsWith(MIME_IMAGE)) {
                        int duration = 5000;
                        if(mediaInfo.filePath.endsWith(FORMAT_GIF)){
                            nativeParser.init(mediaInfo.filePath);
                            duration = Integer.parseInt(nativeParser.getValue(NativeParser.VIDEO_DURATION)) / 1000;
                            nativeParser.release();
                        }
//                            mImport.addImage(mediaInfo.filePath, 0, 0,0,duration, AliyunDisplayMode.DEFAULT);
                        mImport.addMediaClip(new AliyunImageClip.Builder()
                                .source(mediaInfo.filePath)
                                .inDuration(0)
                                .outDuration(0)
                                .overlapDuration(0)
                                .duration(duration)
                                .displayMode(AliyunDisplayMode.DEFAULT)
                                .build());
                    }
                }
                nativeParser.dispose();
            }
            String projectJsonPath = mImport.generateProjectConfigure();

            if (projectJsonPath != null ) {
                Intent intent = new Intent(this,EditorActivity.class);
                intent.putExtra("video_param", mVideoParam);
                intent.putExtra("project_json_path", projectJsonPath);
                startActivity(intent);
            }
            return;
        }
        if (mIsReachedMaxDuration) {
            ToastUtil.showToast(MediaActivity.this, R.string.message_max_duration_import);
            return;
        }
        //对于大于720P的视频需要走转码流程

        int videoCount = mTransCoder.getVideoCount();
        if (videoCount > 0) {
            progressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.wait));
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mBtnNextStep.setEnabled(false);//为了防止未取消成功的情况下就开始下一次转码，这里在取消转码成功前会禁用下一步按钮
                    mTransCoder.cancel();
                }
            });
            mTransCoder.init(this);
            mTransCoder.setTransResolution(requestWidth, requestHeight);
            mTransCoder.transcode(mOutputResolution, quality, scaleMode);
        } else {
            ToastUtil.showToast(this, R.string.please_select_video);
        }
    }
}
