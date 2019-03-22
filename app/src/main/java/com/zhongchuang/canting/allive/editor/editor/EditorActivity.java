/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.editor.editor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.editor.editor.timeline.TimelineBar;
import com.zhongchuang.canting.allive.editor.editor.timeline.TimelineOverlay;
import com.zhongchuang.canting.allive.editor.effects.control.BottomAnimation;
import com.zhongchuang.canting.allive.editor.effects.control.EditorService;
import com.zhongchuang.canting.allive.editor.effects.control.EffectInfo;
import com.zhongchuang.canting.allive.editor.effects.control.OnDialogButtonClickListener;
import com.zhongchuang.canting.allive.editor.effects.control.OnEffectChangeListener;
import com.zhongchuang.canting.allive.editor.effects.control.OnTabChangeListener;
import com.zhongchuang.canting.allive.editor.effects.control.TabGroup;
import com.zhongchuang.canting.allive.editor.effects.control.TabViewStackBinding;
import com.zhongchuang.canting.allive.editor.effects.control.UIEditorPage;
import com.zhongchuang.canting.allive.editor.effects.control.ViewStack;
import com.zhongchuang.canting.allive.editor.effects.filter.AnimationFilterController;
import com.zhongchuang.canting.allive.editor.effects.paint.PaintMenuView;
import com.zhongchuang.canting.allive.editor.effects.paint.PaintMenuView.OnPaintOpera;
import com.zhongchuang.canting.allive.editor.msg.Dispatcher;
import com.zhongchuang.canting.allive.editor.msg.body.FilterTabClick;
import com.zhongchuang.canting.allive.editor.msg.body.LongClickAnimationFilter;
import com.zhongchuang.canting.allive.editor.msg.body.LongClickUpAnimationFilter;
import com.zhongchuang.canting.allive.editor.msg.body.SelectColorFilter;
import com.zhongchuang.canting.allive.editor.publish.PublishActivity;
import com.zhongchuang.canting.allive.editor.util.Common;
import com.zhongchuang.canting.allive.editor.widget.AliyunPasterWithImageView;
import com.zhongchuang.canting.allive.editor.widget.AliyunPasterWithTextView;
import com.aliyun.common.media.ShareableBitmap;
import com.aliyun.common.utils.StorageUtils;
import com.aliyun.common.utils.ToastUtil;
import com.aliyun.crop.AliyunCropCreator;
import com.aliyun.crop.struct.CropParam;
import com.aliyun.crop.supply.AliyunICrop;
import com.aliyun.crop.supply.CropCallback;
import com.aliyun.editor.EditorCallBack;
import com.aliyun.editor.EffectType;
import com.aliyun.editor.TimeEffectType;
import com.aliyun.querrorcode.AliyunEditorErrorCode;
import com.aliyun.querrorcode.AliyunErrorCode;
import com.aliyun.qupai.editor.AliyunICanvasController;
import com.aliyun.qupai.editor.AliyunIEditor;
import com.aliyun.qupai.editor.AliyunIThumbnailFetcher;
import com.aliyun.qupai.editor.AliyunPasterController;
import com.aliyun.qupai.editor.AliyunPasterManager;
import com.aliyun.qupai.editor.AliyunThumbnailFetcherFactory;
import com.aliyun.qupai.editor.OnAnimationFilterRestored;
import com.aliyun.qupai.editor.impl.AliyunEditorFactory;
import com.aliyun.struct.AliyunIClipConstructor;
import com.aliyun.struct.common.AliyunClip;
import com.aliyun.struct.common.AliyunVideoParam;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoDisplayMode;
import com.aliyun.struct.effect.EffectBean;
import com.aliyun.struct.effect.EffectFilter;
import com.aliyun.struct.effect.EffectPaster;
import com.aliyun.struct.effect.EffectPicture;
import com.aliyun.struct.encoder.VideoCodecs;
import com.duanqu.transcode.NativeParser;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.waitLoading.ShapeLoadingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Subscription;
import rx.functions.Action1;

public class EditorActivity extends FragmentActivity implements
        OnTabChangeListener, OnEffectChangeListener, BottomAnimation, View.OnClickListener, OnAnimationFilterRestored {
    private static final String TAG = "EditorActivity";
    public static final String KEY_VIDEO_PARAM = "video_param";
    public static final String KEY_PROJECT_JSON_PATH = "project_json_path";
    public static final String KEY_TEMP_FILE_LIST = "temp_file_list";

    private LinearLayout mBottomLinear;
    private SurfaceView mSurfaceView;
    private TabGroup mTabGroup;
    private ViewStack mViewStack;
    private EditorService mEditorService;

    private AliyunIEditor mAliyunIEditor;

    private AliyunPasterManager mPasterManager;
    private RecyclerView mThumbnailView;
    private TimelineBar mTimelineBar;
    private RelativeLayout mActionBar;
    private FrameLayout resCopy;
    private FrameLayout mTransCodeTip;
    private ProgressBar mTransCodeProgress;
    private FrameLayout mPasterContainer;
    private FrameLayout mGlSurfaceContainer;
    private Uri mUri;
    private EffectPicture mPicture;
    private int mScreenWidth;
    private int mScreenHeight;
    private ImageView mIvLeft;
    private Button mIvRight;
    private TextView mTvCenter;
    private LinearLayout mBarLinear;
    private ImageView mPlayImage;
    private TextView mTvCurrTime;
    private AliyunVideoParam mVideoParam;
    private boolean mIsComposing = false; //当前是否正在合成视频
    private boolean isFullScreen = false; //导入视频是否全屏显示
    private ProgressDialog dialog;
    private MediaScannerConnection mMediaScanner;
    private RelativeLayout mEditor;
    private AliyunICanvasController mCanvasController;
    private ArrayList<String> mTempFilePaths = null;
    private AliyunIThumbnailFetcher mThumbnailFetcher;
    private Bitmap mWatermarkBitmap;
    private File mWatermarkFile;
    private AnimationFilterController mAnimationFilterController;
    private TimelineOverlay mTimeEffectOverlay;
    private TimelineOverlay.TimelineOverlayView mTimeEffectOverlayView;
    private boolean mUseInvert = false;
    private boolean mUseAnimationFilter = false;
    private boolean mStopAnimation = false;
    private boolean mIsTranscoding = false;
    private boolean mIsDestroyed = false;
    private AliyunICrop mTranscoder;
    private PasterUISimpleImpl mCurrentEditEffect;
    private SurfaceView mSurfaceView2;
    private TextureView mTextureView;
    private int mVolume = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String[] models = {Build.MODEL};
//        NativeAdaptiveUtil.decoderAdaptiveList(models, new int[]{0});//强制使用硬解码
        Dispatcher.getInstance().register(this);
        mWatermarkFile = new File(StorageUtils.getCacheDirectory(EditorActivity.this) + "/live/tail/logo.png");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
        setContentView(R.layout.aliyun_svideo_activity_editor);
        Intent intent = getIntent();
        if (intent.getStringExtra(KEY_PROJECT_JSON_PATH) != null) {
            mUri = Uri.fromFile(new File(intent.getStringExtra(KEY_PROJECT_JSON_PATH)));
        }
        if (intent.getSerializableExtra(KEY_VIDEO_PARAM) != null) {
            mVideoParam = (AliyunVideoParam) intent.getSerializableExtra(KEY_VIDEO_PARAM);
        }
        mTempFilePaths = intent.getStringArrayListExtra(KEY_TEMP_FILE_LIST);
        initView();
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
        initListView();
        add2Control();
        initEditor();
        mMediaScanner = new MediaScannerConnection(this, null);
        mMediaScanner.connect();
        copyAssets();

    }
    private Subscription mSubscription;

    private void initView() {
        mEditor = (RelativeLayout) findViewById(R.id.activity_editor);
        resCopy = (FrameLayout) findViewById(R.id.copy_res_tip);
        mTransCodeTip = (FrameLayout) findViewById(R.id.transcode_tip);
        mTransCodeProgress = (ProgressBar) findViewById(R.id.transcode_progress);
        mBarLinear = (LinearLayout) findViewById(R.id.bar_linear);
        mBarLinear.bringToFront();
        mActionBar = (RelativeLayout) findViewById(R.id.action_bar);
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mTvCenter = (TextView) findViewById(R.id.tv_center);
        mIvRight = (Button) findViewById(R.id.iv_right);
        mIvLeft.setImageResource(R.mipmap.aliyun_svideo_icon_back);
        mTvCenter.setText(getString(R.string.edit_nav_edit));
//        mIvRight.setImageResource(R.mipmap.aliyun_svideo_icon_next);
        mIvLeft.setVisibility(View.VISIBLE);
        mIvRight.setVisibility(View.VISIBLE);
        mTvCenter.setVisibility(View.VISIBLE);
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTvCurrTime = (TextView) findViewById(R.id.tv_curr_duration);

        mGlSurfaceContainer = (FrameLayout) findViewById(R.id.glsurface_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.play_view);
        mBottomLinear = (LinearLayout) findViewById(R.id.edit_bottom_tab);

        mPasterContainer = (FrameLayout) findViewById(R.id.pasterView);

        mPlayImage = (ImageView) findViewById(R.id.play_button);
        mPlayImage.setOnClickListener(this);

        final GestureDetector mGesture = new GestureDetector(this,
                new MyOnGestureListener());
        View.OnTouchListener pasterTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGesture.onTouchEvent(event);
                return true;
            }
        };

        mPasterContainer.setOnTouchListener(pasterTouchListener);

        mThumbnailView = (RecyclerView) findViewById(R.id.rv_thumbnail);
        mThumbnailView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mThumbnailFetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
        mThumbnailFetcher.fromConfigJson(mUri.getPath());
        int thumbnailSize = getResources().getDimensionPixelSize(R.dimen.aliyun_svideo_square_thumbnail_size);
        mThumbnailView.setAdapter(new ThumbnailAdapter(10, mThumbnailFetcher, mScreenWidth, thumbnailSize, thumbnailSize));

        mTimeEffectOverlayView = new TimelineOverlay.TimelineOverlayView() {
            View rootView = LayoutInflater.from(EditorActivity.this).inflate(R.layout.aliyun_svideo_layout_timeline_overlay, null);
            View headView = rootView.findViewById(R.id.head_view);
            View tailView = rootView.findViewById(R.id.tail_view);
            View middleView = rootView.findViewById(R.id.middle_view);

            @Override
            public ViewGroup getContainer() {
                return (ViewGroup) rootView;
            }

            @Override
            public View getHeadView() {
                return headView;
            }

            @Override
            public View getTailView() {
                return tailView;
            }

            @Override
            public View getMiddleView() {
                return middleView;
            }
        };

    }

    private void initGlSurfaceView() {
        if (mVideoParam == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mGlSurfaceContainer.getLayoutParams();
        FrameLayout.LayoutParams surfaceLayout = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
        int rotation = mAliyunIEditor.getRotation();
        int outputWidth = mVideoParam.getOutputWidth();
        int outputHeight = mVideoParam.getOutputHeight();
        if ((rotation == 90 || rotation == 270)) {
            int temp = outputWidth;
            outputWidth = outputHeight;
            outputHeight = temp;
        }

        float percent;
        if (outputWidth >= outputHeight) {
            percent = (float) outputWidth / outputHeight;
        } else {
            percent = (float) outputHeight / outputWidth;
        }
        surfaceLayout.height = Math.round((float) outputHeight * mScreenWidth / outputWidth);
        if (percent < 1.5 || (rotation == 90 || rotation == 270)) {
            layoutParams.addRule(RelativeLayout.BELOW, R.id.bar_linear);
        } else {
            isFullScreen = true;
            mBottomLinear.setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
            mActionBar.setBackgroundColor(getResources().getColor(R.color.action_bar_bg_50pct));
        }

        mGlSurfaceContainer.setLayoutParams(layoutParams);
        mSurfaceView.setLayoutParams(surfaceLayout);
    }



    private void initListView() {
        mEditorService = new EditorService();
        mTabGroup = new TabGroup();
        mViewStack = new ViewStack(this);
        mViewStack.setEditorService(mEditorService);
        mViewStack.setEffectChange(this);
        mViewStack.setBottomAnimation(this);
        mViewStack.setDialogButtonClickListener(mDialogButtonClickListener);

        mTabGroup.addView(findViewById(R.id.tab_effect_filter));
        mTabGroup.addView(findViewById(R.id.tab_effect_overlay));
        mTabGroup.addView(findViewById(R.id.tab_effect_caption));
        mTabGroup.addView(findViewById(R.id.tab_effect_mv));
        mTabGroup.addView(findViewById(R.id.tab_effect_audio_mix));
        mTabGroup.addView(findViewById(R.id.tab_paint));
        mTabGroup.addView(findViewById(R.id.tab_effect_time));
    }

    private void add2Control() {
        TabViewStackBinding tabViewStackBinding = new TabViewStackBinding();
        tabViewStackBinding.setViewStack(mViewStack);
        mTabGroup.setOnCheckedChangeListener(tabViewStackBinding);
        mTabGroup.setOnTabChangeListener(this);
    }

    private void initEditor() {
        mAliyunIEditor = AliyunEditorFactory.creatAliyunEditor(mUri, mEditorCallback);
        {//该代码块中的操作必须在AliyunIEditor.init之前调用，否则会出现动图、动效滤镜的UI恢复回调不执行，开发者将无法恢复动图、动效滤镜UI
            mPasterManager = mAliyunIEditor.createPasterManager();
//            mPasterManager.setOnPasterRestoreListener(mOnPasterRestoreListener);
            mAnimationFilterController = new AnimationFilterController(getApplicationContext(),
                    mAliyunIEditor);
            mAliyunIEditor.setAnimationRestoredListener(EditorActivity.this);
        }
        mTranscoder = AliyunCropCreator.getCropInstance(EditorActivity.this);
        ScaleMode mode = mVideoParam.getScaleMode();
        int ret = mAliyunIEditor.init(mSurfaceView, getApplicationContext());
        switch (mode) {
            case LB:
                mAliyunIEditor.setDisplayMode(VideoDisplayMode.FILL);
                break;
            case PS:
                mAliyunIEditor.setDisplayMode(VideoDisplayMode.SCALE);
                break;
            default:
                break;
        }
        mAliyunIEditor.setVolume(mVolume);
        mAliyunIEditor.setFillBackgroundColor(Color.RED);
        if (ret != AliyunErrorCode.OK) {
            ToastUtil.showToast(EditorActivity.this, R.string.aliyun_svideo_editor_init_failed);
            return;
        }
        initGlSurfaceView();

        mEditorService.setFullScreen(isFullScreen);
        mEditorService.addTabEffect(UIEditorPage.MV, mAliyunIEditor.getMVLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.FILTER_EFFECT, mAliyunIEditor.getFilterLastApplyId());
        mEditorService.addTabEffect(UIEditorPage.AUDIO_MIX, mAliyunIEditor.getMusicLastApplyId());
        mEditorService.setPaint(mAliyunIEditor.getPaintLastApply());

        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                //合成方式分为两种，当前页面合成（前台页面）和其他页面合成（后台合成，这里后台并不是真正的app退到后台）
                //前台合成如下：如果要直接合成（当前页面合成），请打开注释，参考注释代码这种方式
//                int ret = mAliyunIEditor.compose(mVideoParam, "/sdcard/output_compose.mp4", new AliyunIComposeCallBack() {
//                    @Override
//                    public void onComposeError(int errorCode) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                v.setEnabled(true);
//                            }
//                        });
//
//                        Log.d(AliyunTag.TAG, "Compose error, error code "+errorCode);
//                    }
//
//                    @Override
//                    public void onComposeProgress(int progress) {
//                        Log.d(AliyunTag.TAG, "Compose progress "+progress+"%");
//                    }
//
//                    @Override
//                    public void onComposeCompleted() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                v.setEnabled(true);
//                            }
//                        });
//                        Log.d(AliyunTag.TAG, "Compose complete");
//                    }
//                });
//                if(ret != AliyunErrorCode.OK) {
//                    Log.e(AliyunTag.TAG, "Compose error, error code "+ret);
//                    v.setEnabled(true);//compose error
//                }

                //后台合成如下：如果要像Demo默认的这样，在其他页面合成，请参考下面这种方式
                final AliyunIThumbnailFetcher fetcher = AliyunThumbnailFetcherFactory.createThumbnailFetcher();
                fetcher.fromConfigJson(mUri.getPath());
                fetcher.setParameters(mAliyunIEditor.getVideoWidth(), mAliyunIEditor.getVideoHeight(), AliyunIThumbnailFetcher.CropMode.Mediate, ScaleMode.LB, 1);
                fetcher.requestThumbnailImage(new long[]{0},
                        new AliyunIThumbnailFetcher.OnThumbnailCompletion() {
                            @Override
                            public void onThumbnailReady(ShareableBitmap frameBitmap, long time) {
                                String path = getExternalFilesDir(null) + "thumbnail.jpeg";
                                try {
                                    frameBitmap.getData().compress(Bitmap.CompressFormat.JPEG, 100,
                                            new FileOutputStream(path));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(EditorActivity.this, PublishActivity.class);
                                intent.putExtra(PublishActivity.KEY_PARAM_THUMBNAIL, path);
                                intent.putExtra(PublishActivity.KEY_PARAM_CONFIG, mUri.getPath());
                                startActivity(intent);

                                fetcher.release();
                            }

                            @Override
                            public void onError(int errorCode) {
                                fetcher.release();
                            }
                        });
            }
        });

        if (mTimelineBar == null) {
            mTimelineBar = new TimelineBar(
                    mAliyunIEditor.getStreamDuration(),
                    EditorActivity.this.getResources().getDimensionPixelOffset(R.dimen.aliyun_svideo_square_thumbnail_size),
                    new TimelineBar.TimelinePlayer() {
                        @Override
                        public long getCurrDuration() {
                            return mAliyunIEditor.getCurrentStreamPosition();
                        }
                    });
            mTimelineBar.setThumbnailView(new TimelineBar.ThumbnailView() {
                @Override
                public RecyclerView getThumbnailView() {
                    return mThumbnailView;
                }

                @Override
                public ViewGroup getThumbnailParentView() {
                    return (ViewGroup) mThumbnailView.getParent();
                }

                @Override
                public void updateDuration(long duration) {
                    mTvCurrTime.setText(convertDuration2Text(duration));
                }
            });
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) mThumbnailView.getLayoutParams();
            layoutParams.width = mScreenWidth;
            mTimelineBar.setTimelineBarDisplayWidth(mScreenWidth);
            mTimelineBar.setBarSeekListener(new TimelineBar.TimelineBarSeekListener() {
                @Override
                public void onTimelineBarSeek(long duration) {
                    mAliyunIEditor.seek(duration);
                    mTimelineBar.pause();
                    mPlayImage.setSelected(true);
                    Log.d(TimelineBar.TAG, "OnTimelineSeek duration = " + duration);
                    if (mCurrentEditEffect != null
                            && !mCurrentEditEffect.isEditCompleted()) {
                        if (!mCurrentEditEffect.isVisibleInTime(duration)) {
                            //隐藏
                            mCurrentEditEffect.mPasterView.setVisibility(View.GONE);
                        } else {
                            //显示
                            mCurrentEditEffect.mPasterView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onTimelineBarSeekFinish(long duration) {
                    mAliyunIEditor.seek(duration);
                    mTimelineBar.pause();
                    mPlayImage.setSelected(true);
                }
            });
        }

        mTimelineBar.start();

        mPasterContainer.post(new Runnable() {
            @Override
            public void run() {
                mPasterManager.setDisplaySize(mPasterContainer.getWidth(),
                        mPasterContainer.getHeight());
            }
        });

//        if (mWatermarkFile.exists()) {
//            if (mWatermarkBitmap == null) {
//                mWatermarkBitmap = BitmapFactory.decodeFile(StorageUtils.getCacheDirectory(EditorActivity.this) + "/live/tail/logo.png");
//            }
//            mSurfaceView.post(new Runnable() {
//                @Override
//                public void run() {
//                    /**
//                     * 水印例子 水印的大小为 ：水印图片的宽高和显示区域的宽高比，注意保持图片的比例，不然显示不完全  水印的位置为 ：以水印图片中心点为基准，显示区域宽高的比例为偏移量，0,0为左上角，1,1为右下角
//                     */
//                    mAliyunIEditor.applyWaterMark(StorageUtils.getCacheDirectory(EditorActivity.this) + "/live/tail/logo.png",
//                            (float) mWatermarkBitmap.getWidth() / (mSurfaceView.getMeasuredWidth() * 2),/*用水印图片大小/SurfaceView的大小，得到的就是水印图片相对于surfaceView的归一化大小*/
//                            (float) mWatermarkBitmap.getHeight() / (mSurfaceView.getMeasuredHeight() * 2),
////                            1f - (float) mWatermarkBitmap.getWidth() / (mSurfaceView.getMeasuredWidth() * 4),//水印位于右边
//                            100.f / mSurfaceView.getMeasuredWidth(),//水印位于左边
//                            100.f / mSurfaceView.getMeasuredHeight());//Demo中这套参数表示size是图片原始大小，位置是x轴靠右边，y轴从上往下偏移100像素
//
//
//                    mAliyunIEditor.addTailWaterMark(StorageUtils.getCacheDirectory(EditorActivity.this) + "/live/tail/logo.png",
//                            (float) mWatermarkBitmap.getWidth() / mSurfaceView.getMeasuredWidth(),
//                            (float) mWatermarkBitmap.getHeight() / mSurfaceView.getMeasuredHeight(), 0.5f,
//                            0.5f, 2000 * 1000);
//
//                }
//            });
//        }

        Log.d(TAG, "start play");
        mAliyunIEditor.play();

    }

//        private final OnPasterRestored mOnPasterRestoreListener = new OnPasterRestored() {
//
//        @Override
//        public void onPasterRestored(final List<AliyunPasterController> controllers) {
//            final List<PasterUISimpleImpl> aps = new ArrayList<>();
//
//            mPasterContainer.post(new Runnable() {//之所以要放在这里面，是因为下面的操作中有UI相关的，需要保证布局完成后执行，才能保证UI更新的正确性
//                @Override
//                public void run() {
//                    for (AliyunPasterController c : controllers) {
//                        if (!c.isPasterExists()) {
//                            continue;
//                        }
//                        if (c.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
//                            mCurrentEditEffect = addPaster(c);
//                        } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
//                            mCurrentEditEffect = addSubtitle(c, true);
//                        } else if (c.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION) {
//                            mCurrentEditEffect = addCaption(c);
//                        }
//
//                        mCurrentEditEffect.showTimeEdit();
//                        mCurrentEditEffect.getPasterView().setVisibility(View.INVISIBLE);
//                        aps.add(mCurrentEditEffect);
//                        //                mCurrentEditEffect.editTimeCompleted();
//                        mCurrentEditEffect.moveToCenter();
//                    }
//
//                    for (PasterUISimpleImpl pui : aps) {
//                        pui.editTimeCompleted();
//                    }
//                    //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
//                    mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
//                    if (mCanvasController.hasCanvasPath()) {
//                        mCanvasController.removeCanvas();
//                        mCanvasController.resetPaintCanvas();
//                    }
//                }
//            });
//
//        }
//
//    };


    @Override
    protected void onResume() {
        super.onResume();
        mAliyunIEditor.resume();
        mPlayImage.setSelected(false);
        if (mTimelineBar != null) {
            mTimelineBar.resume();
        }
        checkAndRemovePaster();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
            mCurrentEditEffect.editTimeCompleted();
        }
        mAliyunIEditor.pause();
        if (mTimelineBar != null) {
            mTimelineBar.pause();
        }

        mPlayImage.setSelected(true);
        if (dialog != null && dialog.isShowing()) {
            mIsComposing = false;
            dialog.cancel();
        }
        mAliyunIEditor.saveEffectToLocal();
        Log.d("xxx", "EditorActivity onPause");
    }

    @Override
    protected void onDestroy() {
        mIsDestroyed = true;
        if (mAliyunIEditor != null) {
            mAliyunIEditor.onDestroy();
        }
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        if (mAnimationFilterController != null) {
            mAnimationFilterController.destroyController();
        }

        if (mTimelineBar != null) {
            mTimelineBar.stop();
        }
        if (mMediaScanner != null) {
            mMediaScanner.disconnect();
        }

        if (mThumbnailFetcher != null) {
            mThumbnailFetcher.release();
        }

        if (mCanvasController != null) {
            mCanvasController.release();
        }

        if (mTranscoder != null) {
            if (mIsTranscoding) {
                mTranscoder.cancel();
                ;
            }
        }
        super.onDestroy();

        //退出编辑界面，将编辑生成的文件（编辑添加的文字图片会保存为文件存在project相应目录）及project config配置删除，如果后续还有合成该视频的需求则不应该删除
        //        String path = mUri.getPath();
        //        File f = new File(path);
        //        if(!f.exists()){
        //            return ;
        //        }
        //        FileUtils.deleteDirectory(f.getParentFile());
        //删除录制生成的临时文件
        //deleteTempFiles();由于返回依然可以接着录，因此现在不能删除
    }

    @Override
    public void onTabChange() {
        //暂停播放
        //        if (mAliyunIPlayer.isPlaying()) {
        //            playingPause();
        //        }

        //tab切换时通知
        hideBottomView();
        UIEditorPage index = UIEditorPage.get(mTabGroup.getCheckedIndex());
        int ix = mEditorService.getEffectIndex(index);
        switch (index) {
            case FILTER_EFFECT:
                break;
            case OVERLAY:
                break;
            default:
                break;
        }
        Log.e("editor", "====== onTabChange " + ix + " " + index);
    }

    @Override
    public void onEffectChange(EffectInfo effectInfo) {
        Log.e("editor", "====== onEffectChange ");
        //返回素材属性

        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        UIEditorPage type = effectInfo.type;
        final AliyunPasterController c;
        Log.d(TAG, "effect path " + effectInfo.getPath());
        switch (type) {
            case AUDIO_MIX:
                if (!effectInfo.isAudioMixBar) {
                    mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MIX);
                    mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MV_AUDIO);
                    if (effect.getPath() != null) {
                        effect.setStartTime(effectInfo.startTime * 1000);//单位是us所以要x1000
                        effect.setDuration(effectInfo.endTime == 0 ? Integer.MAX_VALUE : (effectInfo.endTime - effectInfo.startTime) * 1000);//单位是us所以要x1000
                        effect.setStreamStartTime(effectInfo.streamStartTime * 1000);
                        effect.setStreamDuration((effectInfo.streamEndTime - effectInfo.streamStartTime) * 1000);//单位是us所以要x1000
                        effectInfo.mixId = mAliyunIEditor.applyMusic(effect);
                        mAliyunIEditor.resume();
                    } else {
                        mAliyunIEditor.resume();
                    }
                    mTimelineBar.resume();
                    mPlayImage.setSelected(false);
                } else {
                    effectInfo.mixId = mAliyunIEditor.getMusicLastApplyId();
                }
                mAliyunIEditor.applyMusicMixWeight(effectInfo.mixId, effectInfo.musicWeight);
                //                mAudioEffect = effect;
                //                if (!effectInfo.isAudioMixBar) {
                //                    if(TextUtils.isEmpty(effectInfo.getPath())){
                //                        mAudioTimePicker.removeAudioTimePicker();
                //                        mPicker.performClick();
                //                    }else{
                //                        mAudioTimePicker.showAudioTimePicker();
                //                        playingPause();
                //                    }
                //
                //                }
                break;
            case FILTER_EFFECT:
                if (effect.getPath().contains("Vertigo")) {
                    EffectFilter filter = new EffectFilter(effect.getPath());
                    mAliyunIEditor.addAnimationFilter(filter);
                } else {
                    mAliyunIEditor.applyFilter(effect);
                }
                break;
            case MV:
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                }

                String path = null;
                if (effectInfo.list != null) {
                    path = Common.getMVPath(effectInfo.list, mScreenWidth, mScreenHeight);
                }
                effect.setPath(path);
                if (path != null) {

                    mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MIX);
                    Log.d(TAG, "editor resetEffect end");
                    mAliyunIEditor.applyMV(effect);
                } else {
                    mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_MV);
                }
                //重新播放，倒播重播流时间轴需要设置到最后
                if (mUseInvert) {
                    mAliyunIEditor.seek(mAliyunIEditor.getStreamDuration());
                } else {
                    mAliyunIEditor.seek(0);
                }
                mAliyunIEditor.resume();
                mTimelineBar.resume();
                mPlayImage.setSelected(false);
                break;
            case CAPTION:
                c = mPasterManager.addPaster(effectInfo.getPath());
                if (c != null) {
                    c.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                    PasterUICaptionImpl cui = addCaption(c);
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                        mCurrentEditEffect.editTimeCompleted();
                    }
                    playingPause();
                    mCurrentEditEffect = cui;
                    mCurrentEditEffect.showTimeEdit();
                } else {
                    ToastUtil.showToast(EditorActivity.this, "添加字幕失败");
                }
                break;
            case OVERLAY:
                c = mPasterManager.addPaster(effectInfo.getPath());
                if (c != null) {//add success
                    c.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                    PasterUIGifImpl gifui = addPaster(c);
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                        mCurrentEditEffect.editTimeCompleted();
                    }
                    playingPause();
                    mCurrentEditEffect = gifui;
                    mCurrentEditEffect.showTimeEdit();
                } else {//add failed
                    ToastUtil.showToast(EditorActivity.this, "添加动图失败");
                }

                break;
            case FONT:
                c = mPasterManager.addSubtitle(null, effectInfo.fontPath + "/font.ttf");
                if (c != null) {
                    c.setPasterStartTime(mAliyunIEditor.getCurrentStreamPosition());
                    PasterUITextImpl textui = addSubtitle(c, false);
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                        mCurrentEditEffect.editTimeCompleted();
                    }
                    playingPause();
                    mCurrentEditEffect = textui;
                    mCurrentEditEffect.showTimeEdit();
                    textui.showTextEdit();
                } else {
                    ToastUtil.showToast(EditorActivity.this, "添加文字失败");
                }
//                mCurrentEditEffect.setImageView((ImageView) findViewById(R.id.test_image));

                break;
            case PAINT:
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                    mCurrentEditEffect.editTimeCompleted();
                }
                if (mCanvasController == null) {
                    mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                }
                mCanvasController.removeCanvas();
                addPaint(mCanvasController);
                break;
            case TIME:
                mUseInvert = false;
                if (mAliyunIEditor.getSourcePartManager().getAllClips().size() > 1) {
                    ToastUtil.showToast(this, getString(R.string.aliyun_svideo_time_effect_not_support));
                    return;//时间特效不支持多段视频
                }
                if (mTimeEffectOverlay != null) {
                    mTimelineBar.removeOverlay(mTimeEffectOverlay);
                }
                if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_NONE)) {
                    mAliyunIEditor.resetEffect(EffectType.EFFECT_TYPE_TIME);
                    mAliyunIEditor.resume();
                } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_RATE)) {
                    if (effectInfo.isMoment) {
                        long startTime = mAliyunIEditor.getCurrentStreamPosition();
                        mTimeEffectOverlay = mTimelineBar.addOverlay(startTime, 1000 * 1000, mTimeEffectOverlayView, 0, false);
                        mAliyunIEditor.stop();
                        mAliyunIEditor.rate(effectInfo.timeParam, startTime / 1000, 1000, false);
                        mAliyunIEditor.play();
                    } else {
                        mTimeEffectOverlay = mTimelineBar.addOverlay(0, 1000000000L, mTimeEffectOverlayView, 0, false);
                        mAliyunIEditor.stop();
                        mAliyunIEditor.rate(effectInfo.timeParam, 0, 1000000000L, false);
                        mAliyunIEditor.play();
                    }
                } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_INVERT)) {
                    mUseInvert = true;
                    mTimeEffectOverlay = mTimelineBar.addOverlay(0, 1000000000L, mTimeEffectOverlayView, 0, false);
                    mAliyunIEditor.stop();
                    checkAndTranscode(TimeEffectType.TIME_EFFECT_TYPE_INVERT, 0, 0, 0, false);

                } else if (effectInfo.timeEffectType.equals(TimeEffectType.TIME_EFFECT_TYPE_REPEAT)) {
                    long startTime = mAliyunIEditor.getCurrentStreamPosition();
                    mTimeEffectOverlay = mTimelineBar.addOverlay(startTime, 1000 * 1000, mTimeEffectOverlayView, 0, false);
                    mAliyunIEditor.stop();
                    checkAndTranscode(TimeEffectType.TIME_EFFECT_TYPE_REPEAT, 3, startTime / 1000, 1000, false);
                }
                if (mTimeEffectOverlay != null) {
                    mTimeEffectOverlay.switchState(TimelineOverlay.STATE_FIX);
                }
                mTimelineBar.resume();
                break;
            default:
                break;
        }
    }

    /**
     * 对于Gop比较大的视频做时间特效时需要先检查是否满足实时，如果不满足实时，需要提前做转码，逻辑如下
     *
     * @param type
     * @param times
     * @param startTime
     * @param duration
     * @param needDuration
     */
    private void checkAndTranscode(final TimeEffectType type, final int times, final long startTime, final long duration, final boolean needDuration) {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                AliyunClip clip = mAliyunIEditor.getSourcePartManager().getAllClips().get(0);
                final AtomicInteger flag = new AtomicInteger(0);
                if (clip == null) {
                    return null;
                }
                boolean ret = checkInvert(clip.getSource());
                if (!ret) {
                    mAliyunIEditor.saveEffectToLocal();
                    final CountDownLatch countDownLatch = new CountDownLatch(1);

                    CropParam param = new CropParam();
                    param.setGop(1);
                    param.setVideoBitrate(8000);//8mbps
                    param.setInputPath(clip.getSource());
                    param.setVideoCodec(VideoCodecs.H264_SOFT_OPENH264);
                    param.setOutputPath(clip.getSource() + "_invert_transcode");
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(clip.getSource());
                    int width = 0;
                    int height = 0;
                    int rotate = 0;
                    try {
                        rotate = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                        if (rotate == 90 || rotate == 270) {
                            height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                            width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        } else {
                            width = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                            height = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        }
                    } catch (Exception e) {
                        width = mVideoParam.getOutputWidth();
                        height = mVideoParam.getOutputHeight();
                    }
                    mmr.release();
                    param.setOutputWidth(width);
                    param.setOutputHeight(height);
                    mTranscoder.setCropParam(param);
                    mTranscoder.setCropCallback(new CropCallback() {
                        @Override
                        public void onProgress(final int percent) {
                            Log.d(TAG, "percent" + percent);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTransCodeProgress.setProgress(percent);
                                }
                            });
                        }

                        @Override
                        public void onError(int code) {
                            Log.d(TAG, "onError" + code);
                            flag.set(1);
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }

                        @Override
                        public void onComplete(long duration) {
                            AliyunIClipConstructor clipConstructor = mAliyunIEditor.getSourcePartManager();
                            AliyunClip clip = clipConstructor.getMediaPart(0);
                            clip.setSource(clip.getSource() + "_invert_transcode");
                            clipConstructor.updateMediaClip(0, clip);
                            mAliyunIEditor.applySourceChange();
                            flag.set(2);
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }

                        @Override
                        public void onCancelComplete() {
                            flag.set(3);
                            if (mIsDestroyed) {
                                mTranscoder.dispose();
                            }
                            countDownLatch.countDown();
                            mIsTranscoding = false;
                        }
                    });
                    mIsTranscoding = true;
                    mTranscoder.startCrop();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTransCodeTip.setVisibility(View.VISIBLE);
                        }
                    });
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return flag;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (mIsDestroyed) {
                    return;
                }
                mTransCodeTip.setVisibility(View.GONE);
                if (o instanceof AtomicInteger) {
                    if (((AtomicInteger) o).get() == 0 || ((AtomicInteger) o).get() == 2) {
                        if (type == TimeEffectType.TIME_EFFECT_TYPE_INVERT) {
                            mAliyunIEditor.invert();
                        } else if (type == TimeEffectType.TIME_EFFECT_TYPE_REPEAT) {
                            mAliyunIEditor.repeat(times, startTime, duration, needDuration);
                        }

                    }
                }
                mAliyunIEditor.play();
            }
        }.execute(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void checkAndRemovePaster() {
        int count = mPasterContainer.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View pv = mPasterContainer.getChildAt(i);
            PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
            if (uic != null && !uic.isPasterExists()) {
                Log.e(TAG, "removePaster");
                uic.removePaster();
            }
        }
    }

    protected void playingPause() {
        if (mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.pause();
            mTimelineBar.pause();
            mPlayImage.setSelected(true);
        }
    }

    protected void playingResume() {
        if (!mAliyunIEditor.isPlaying()) {
            mAliyunIEditor.resume();
            mTimelineBar.resume();
            mPlayImage.setSelected(false);
        }
    }

    private PasterUIGifImpl addPaster(AliyunPasterController controller) {
        AliyunPasterWithImageView pasterView = (AliyunPasterWithImageView) View.inflate(this,
                R.layout.aliyun_svideo_qupai_paster_gif, null);

        mPasterContainer.addView(pasterView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUIGifImpl(pasterView, controller, mTimelineBar);
    }

    /**
     * 添加字幕
     *
     * @param controller
     * @return
     */
    private PasterUICaptionImpl addCaption(AliyunPasterController controller) {
        AliyunPasterWithImageView captionView = (AliyunPasterWithImageView) View.inflate(this,
                R.layout.aliyun_svideo_qupai_paster_caption, null);
        //        ImageView content = (ImageView) captionView.findViewById(R.id.qupai_overlay_content_animation);
        //        Glide.with(getApplicationContext())
        //                .load("file://" + controller.getPasterIconPath())
        //                .into(content);
        mPasterContainer.addView(captionView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUICaptionImpl(captionView, controller, mTimelineBar);
    }

    /**
     * 添加文字
     *
     * @param controller
     * @param restore
     * @return
     */
    private PasterUITextImpl addSubtitle(AliyunPasterController controller, boolean restore) {
        AliyunPasterWithTextView captionView = (AliyunPasterWithTextView) View.inflate(this,
                R.layout.aliyun_svideo_qupai_paster_text, null);
        mPasterContainer.addView(captionView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        return new PasterUITextImpl(captionView, controller, mTimelineBar, restore);
    }

    /**
     * 添加涂鸦
     *
     * @param canvasController
     * @return
     */
    private View addPaint(AliyunICanvasController canvasController) {
        hideBottomView();
        View canvasView = canvasController.getCanvas();
        mPasterContainer.removeView(canvasView);
        mPasterContainer.addView(canvasView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addPaintMenu(canvasController);
        return canvasView;
    }

    private void addPaintMenu(AliyunICanvasController canvasController) {
        PaintMenuView menuView = new PaintMenuView(canvasController);
        menuView.setOnPaintOpera(onPaintOpera);
        menuView.setEditorService(mEditorService);
        View view = menuView.getPaintMenu(this);
        if (isFullScreen) {
            view.findViewById(R.id.paint_menu).setBackgroundColor(getResources().getColor(R.color.tab_bg_color_50pct));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        mEditor.addView(view);
    }

    private OnPaintOpera onPaintOpera = new OnPaintOpera() {
        @Override
        public void removeView(View view) {
            mEditor.removeView(view);
            mPasterContainer.removeView(mCanvasController.getCanvas());
            showBottomView();
        }

        @Override
        public void completeView() {
            mCanvasController.applyPaintCanvas();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewStack.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIvRight.setEnabled(true);
    }

    @Override
    public void showBottomView() {
        mBottomLinear.setVisibility(View.VISIBLE);
        mActionBar.setVisibility(View.VISIBLE);
        mPlayImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomView() {
        mBottomLinear.setVisibility(View.GONE);
        mActionBar.setVisibility(View.GONE);
        mPlayImage.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == mPlayImage && mAliyunIEditor != null) {
            if (mAliyunIEditor.isPlaying()) {
                playingPause();
            } else {
                playingResume();
                if (mCurrentEditEffect != null && !mCurrentEditEffect.isPasterRemoved()) {
                    mCurrentEditEffect.editTimeCompleted();
                    //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
                    mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this, mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                    if (mCanvasController.hasCanvasPath()) {
                        mCanvasController.removeCanvas();
                        mCanvasController.resetPaintCanvas();
                    }
                }
            }
        }
    }


    /**
     * 恢复动效滤镜UI（这里主要是编辑页面顶部时间轴的覆盖
     *
     * @param animationFilters
     */
    @Override
    public void animationFilterRestored(final List<EffectFilter> animationFilters) {
        mPasterContainer.post(new Runnable() {
            @Override
            public void run() {
                mAnimationFilterController.setTimelineBar(mTimelineBar);
                if (mAnimationFilterController != null) {
                    mAnimationFilterController.restoreAnimationFilters(animationFilters);
                }
            }
        });
    }

    private class MyOnGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        float mPosX;
        float mPosY;
        boolean shouldDrag = true;

        boolean shouldDrag() {
            return shouldDrag;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.d("MOVE", "onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("MOVE", "onSingleTapConfirmed");

            if (!shouldDrag) {
                boolean outside = true;
                int count = mPasterContainer.getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    View pv = mPasterContainer.getChildAt(i);
                    PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                    if (uic != null) {
                        if (uic.isVisibleInTime(mAliyunIEditor.getCurrentStreamPosition())
                                && uic.contentContains(e.getX(), e.getY())) {
                            outside = false;
                            if (mCurrentEditEffect != null && mCurrentEditEffect != uic
                                    && !mCurrentEditEffect.isEditCompleted()) {
                                mCurrentEditEffect.editTimeCompleted();
                            }
                            mCurrentEditEffect = uic;
                            if (uic.isEditCompleted()) {
                                playingPause();
                                uic.editTimeStart();
                            }
                            break;
                        } else {
                            if (mCurrentEditEffect != uic && uic.isVisibleInTime(mAliyunIEditor.getCurrentStreamPosition())) {
                                uic.editTimeCompleted();
                                playingResume();
                            }
                        }
                    }
                }

                if (outside) {
                    if (mCurrentEditEffect != null && !mCurrentEditEffect.isEditCompleted()) {
                        //                        Log.d("LLLL", "CurrPosition = " + mAliyunIPlayer.getCurrentStreamPosition());
                        mCurrentEditEffect.editTimeCompleted();
                        //要保证涂鸦永远在动图的上方，则需要每次添加动图时都把已经渲染的涂鸦remove掉，添加完动图后，再重新把涂鸦加上去
                        mCanvasController = mAliyunIEditor.obtainCanvasController(EditorActivity.this.getApplicationContext(), mGlSurfaceContainer.getWidth(), mGlSurfaceContainer.getHeight());
                        if (mCanvasController.hasCanvasPath()) {
                            mCanvasController.removeCanvas();
                            mCanvasController.resetPaintCanvas();
                        }
                    }
                }
            } else {
                playingPause();
                mCurrentEditEffect.showTextEdit();
            }

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("MOVE", "onShowPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            if (shouldDrag()) {
                if (mPosX == 0 || mPosY == 0) {
                    mPosX = e1.getX();
                    mPosY = e1.getY();
                }
                float x = e2.getX();
                float y = e2.getY();

                mCurrentEditEffect.moveContent(x - mPosX, y - mPosY);

                mPosX = x;
                mPosY = y;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("MOVE", "onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            Log.d("MOVE", "onFling");
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (mCurrentEditEffect != null && mCurrentEditEffect.isPasterRemoved()) {
                mCurrentEditEffect = null;
            }

            if (mCurrentEditEffect != null) {
                shouldDrag = !mCurrentEditEffect.isEditCompleted()
                        && mCurrentEditEffect.contentContains(e.getX(), e.getY())
                        && mCurrentEditEffect.isVisibleInTime(mAliyunIEditor.getCurrentStreamPosition());
            } else {
                shouldDrag = false;
            }

            mPosX = 0;
            mPosY = 0;
            return false;
        }
    }

    StringBuilder mDurationText = new StringBuilder(5);

    private String convertDuration2Text(long duration) {
        mDurationText.delete(0, mDurationText.length());
        int sec = Math.round(((float) duration) / (1000 * 1000));// us -> s
        int min = (sec % 3600) / 60;
        sec = (sec % 60);
        //TODO:优化内存,不使用String.format
        if (min >= 10) {
            mDurationText.append(min);
        } else {
            mDurationText.append("0").append(min);
        }
        mDurationText.append(":");
        if (sec >= 10) {
            mDurationText.append(sec);
        } else {
            mDurationText.append("0").append(sec);
        }
        return mDurationText.toString();
    }

    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Common.copyAll(EditorActivity.this, resCopy);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
            }
        }.execute();
    }

    public AliyunIEditor getPlayer() {
        return this.mAliyunIEditor;
    }

    public void showMessage(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(id);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private boolean checkInvert(String filePath) {
        NativeParser parser = new NativeParser();
        parser.init(filePath);
        boolean ret = parser.getMaxGopSize() <= 5;
        parser.release();
        parser.dispose();
        return ret;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private OnDialogButtonClickListener mDialogButtonClickListener = new OnDialogButtonClickListener() {
        @Override
        public void onPositiveClickListener(int index) {

        }

        @Override
        public void onNegativeClickListener(int index) {
            UIEditorPage in = UIEditorPage.get(index);
            int count = mPasterContainer.getChildCount();
            switch (in) {
                case OVERLAY://清除所有动图
                    for (int i = count - 1; i >= 0; i--) {
                        View pv = mPasterContainer.getChildAt(i);
                        PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                        if (uic != null && uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_GIF) {
                            uic.removePaster();
                        }
                    }
                    break;
                case CAPTION:
                    for (int i = count - 1; i >= 0; i--) {
                        View pv = mPasterContainer.getChildAt(i);
                        PasterUISimpleImpl uic = (PasterUISimpleImpl) pv.getTag();
                        if (uic == null) {
                            return;
                        }
                        if (uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_CAPTION
                                || uic.mController.getPasterType() == EffectPaster.PASTER_TYPE_TEXT) {
                            uic.removePaster();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private void deleteTempFiles() {
        if (mTempFilePaths != null) {
            for (String path : mTempFilePaths) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventColorFilterSelected(SelectColorFilter selectColorFilter) {
        EffectInfo effectInfo = selectColorFilter.getEffectInfo();
        EffectBean effect = new EffectBean();
        effect.setId(effectInfo.id);
        effect.setPath(effectInfo.getPath());
        mAliyunIEditor.applyFilter(effect);
    }

    /**
     * 长按时需要恢复播放
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterLongClick(LongClickAnimationFilter filter) {
        if (!mAliyunIEditor.isPlaying() && !mStopAnimation) {
            playingResume();
            if (!mUseAnimationFilter) {
                mUseAnimationFilter = true;
            }
        }
    }

    /**
     * 长按抬起手指需要暂停播放
     *
     * @param filter
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAnimationFilterClickUp(LongClickUpAnimationFilter filter) {
        if (mAliyunIEditor.isPlaying()) {
            playingPause();
            if (mUseAnimationFilter) {
                mUseAnimationFilter = false;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventFilterTabClick(FilterTabClick ft) {
        //切换到特效的tab需要暂停播放，切换到滤镜的tab需要恢复播放
        if (mAliyunIEditor != null) {
            switch (ft.getPosition()) {
                case FilterTabClick.POSITION_ANIMATION_FILTER:
                    if (mAliyunIEditor.isPlaying()) {
                        playingPause();
                    }
                    break;
                case FilterTabClick.POSITION_COLOR_FILTER:
                    if (!mAliyunIEditor.isPlaying()) {
                        playingResume();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private EditorCallBack mEditorCallback = new EditorCallBack() {
        @Override
        public void onEnd(int state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAliyunIEditor.play();
                    mTimelineBar.restart();
                }
            });

//            if(mUseAnimationFilter){
//                Dispatcher.getInstance().postMsg(new DeleteLastAnimationFilter());
//                if(mCurrentAnimationFilter != null){
//                   Dispatcher.getInstance().postMsg(mCurrentAnimationFilter);
//                }
//            }
        }

        @Override
        public void onError(final int errorCode) {
            Log.e(TAG, "play error " + errorCode);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (errorCode) {
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_WRONG_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_PROCESS_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_NO_FREE_DISK_SPACE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CREATE_DECODE_GOP_TASK_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_AUDIO_STREAM_DECODER_INIT_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_VIDEO_STREAM_DECODER_INIT_FAILED:

                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_QUEUE_FULL_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_SPS_PPS_NULL:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_H264_PARAM_SET_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_HEVC_PARAM_SET_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_QUEUE_EMPTY_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INPUT:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_NO_BUFFER_AVAILABLE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_INTERRUPT:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_VIDEO_DECODER_ERROR_DECODE_SPS:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_QUEUE_EMPTY_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_QUEUE_FULL_WARNING:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_CREATE_DECODER_FAILED:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_STATE:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_INPUT:
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_AUDIO_DECODER_ERROR_NO_BUFFER_AVAILABLE:
                            ToastUtil.showToast(EditorActivity.this, "错误码是" + errorCode);
                            finish();
                            break;
                        case AliyunEditorErrorCode.ALIVC_FRAMEWORK_MEDIA_POOL_CACHE_DATA_SIZE_OVERFLOW:
                            ToastUtil.showToast(EditorActivity.this, "错误码是" + errorCode);
                            mTimelineBar.restart();
                            mAliyunIEditor.play();
                            break;
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_AUDIO:
                            ToastUtil.showToast(EditorActivity.this, R.string.not_supported_audio);
                            finish();
                            break;
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_VIDEO:
                            ToastUtil.showToast(EditorActivity.this, R.string.not_supported_video);
                            finish();
                            break;
                        case AliyunErrorCode.ERROR_MEDIA_NOT_SUPPORTED_PIXEL_FORMAT:
                            ToastUtil.showToast(EditorActivity.this, R.string.not_supported_pixel_format);
                            finish();
                            break;
                        default:
                            ToastUtil.showToast(EditorActivity.this, R.string.play_video_error);
                            break;
                    }
                }
            });

        }

        @Override
        public int onCustomRender(int srcTextureID, int width, int height) {
            return srcTextureID;
        }

        @Override
        public void onPlayProgress(long currentPlayTime, long currentStreamPlayTime) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long currentPlayTime = mAliyunIEditor.getCurrentPlayPosition();
                    if (mUseAnimationFilter && mAliyunIEditor.getDuration() - currentPlayTime < 100 * 1000) {
                        playingPause();
                        mUseAnimationFilter = false;
                        mStopAnimation = true;
                    }
                    if (mAliyunIEditor.getDuration() - currentPlayTime >= 100 * 1000) {
                        mStopAnimation = false;
                    }
                }
            });

        }
    };


}
