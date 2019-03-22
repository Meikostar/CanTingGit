package com.zhongchuang.canting.allive.vodplayerview.view.control;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunMediaInfo;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.vodplayerview.constants.PlayParameter;
import com.zhongchuang.canting.allive.vodplayerview.theme.ITheme;
import com.zhongchuang.canting.allive.vodplayerview.utils.TimeFormater;
import com.zhongchuang.canting.allive.vodplayerview.view.interfaces.ViewAction;
import com.zhongchuang.canting.allive.vodplayerview.view.quality.QualityItem;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunScreenMode;
import com.zhongchuang.canting.allive.vodplayerview.widget.AliyunVodPlayerView;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.widget.ClearEditText;

import java.lang.ref.WeakReference;
import java.util.List;

/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

/**
 * 控制条界面。包括了顶部的标题栏，底部 的控制栏，锁屏按钮等等。是界面的主要组成部分。
 */

public class ControlLiveView extends RelativeLayout implements ViewAction, ITheme {

    private static final String TAG = ControlLiveView.class.getSimpleName();


    //标题，控制条单独控制是否可显示
    private boolean mTitleBarCanShow = true;
    private boolean mControlBarCanShow = true;
    private View mTitleBar;
    private View mControlBar;

    //这些是大小屏都有的==========START========
    //返回按钮
    private ImageView mTitlebarBackBtn;
    //标题
    private TextView mTitlebarText;
    //视频播放状态
    private PlayState mPlayState = PlayState.NotPlaying;
    //播放按钮
    private ImageView mPlayStateBtn;
    private ClearEditText et_comment;
    private Button btn_send_comment;

    //下载



    //锁定屏幕方向相关
    // 屏幕方向是否锁定
    private boolean mScreenLocked = false;



    //切换大小屏相关
    private AliyunScreenMode mAliyunScreenMode = AliyunScreenMode.Small;
    //全屏/小屏按钮
    private ImageView mScreenModeBtn;

    //大小屏公用的信息
    //视频信息，info显示用。
    private AliyunMediaInfo mAliyunMediaInfo;
    //播放的进度
    private int mVideoPosition = 0;
    //seekbar拖动状态
    private boolean isSeekbarTouching = false;
    //视频缓冲进度
    private int mVideoBufferPosition;
    //这些是大小屏都有的==========END========


    //这些是大屏时显示的
    //大屏的底部控制栏
    private View mLargeInfoBar;
    //当前位置文字

    //时长文字

    //进度条

    //当前的清晰度
    private String mCurrentQuality;
    //是否固定清晰度
    private boolean mForceQuality = false;
    //改变清晰度的按钮
    private Button mLargeChangeQualityBtn;
    //更多弹窗按钮
    private ImageView mTitleMore;
    //这些是小屏时显示的
    //底部控制栏
    private View mSmallInfoBar;
    //当前位置文字

    //时长文字

    //seek进度条



    //整个view的显示控制：
    //不显示的原因。如果是错误的，那么view就都不显示了。
    private HideType mHideType = null;

    //saas,还是mts资源,清晰度的显示不一样
    private boolean isMtsSource;

    //各种监听
    // 进度拖动监听
    private OnSeekListener mOnSeekListener;
    //菜单点击监听
    private OnMenuClickListener mOnMenuClickListener;
    //下载点击监听
    private OnDownloadClickListener onDownloadClickListener;
    //标题返回按钮监听
    private OnBackClickListener mOnBackClickListener;
    //播放按钮点击监听
    private OnPlayStateClickListener mOnPlayStateClickListener;
    //清晰度按钮点击监听
    private OnQualityBtnClickListener mOnQualityBtnClickListener;
    //锁屏按钮点击监听
    private OnScreenLockClickListener mOnScreenLockClickListener;
    //大小屏按钮点击监听
    private OnScreenModeClickListener mOnScreenModeClickListener;
    // 显示更多
    private OnShowMoreClickListener mOnShowMoreClickListener;


    public ControlLiveView(Context context) {
        super(context);
        init();
    }

    public ControlLiveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControlLiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //Inflate布局
        LayoutInflater.from(getContext()).inflate(R.layout.live_vet_view, this, true);
        findAllViews(); //找到所有的view

        setViewListener(); //设置view的监听事件

        updateAllViews(); //更新view的显示
    }

    private void findAllViews() {
        mTitleBar = findViewById(R.id.titlebar);
        mControlBar = findViewById(R.id.controlbar);

        mTitlebarBackBtn = (ImageView) findViewById(R.id.alivc_title_back);
        mTitlebarText = (TextView) findViewById(R.id.alivc_title_title);

        mTitleMore = findViewById(R.id.alivc_title_more);
        mScreenModeBtn = (ImageView) findViewById(R.id.alivc_screen_mode);

        mPlayStateBtn = (ImageView) findViewById(R.id.alivc_player_state);;
        et_comment = (ClearEditText) findViewById(R.id.et_comment);;
        btn_send_comment = (Button) findViewById(R.id.btn_send_comment);

        mLargeInfoBar = findViewById(R.id.alivc_info_large_bar);
        mLargeChangeQualityBtn = (Button) findViewById(R.id.alivc_info_large_rate_btn);


        mSmallInfoBar = findViewById(R.id.alivc_info_small_bar);
    }

    private void setViewListener() {
//标题的返回按钮监听
        mTitlebarBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBackClickListener != null) {
                    mOnBackClickListener.onClick();
                }
            }
        });

//控制栏的播放按钮监听
        mPlayStateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPlayStateClickListener != null) {
                    mOnPlayStateClickListener.onPlayStateClick();
                }
            }
        });




//大小屏按钮监听
        mScreenModeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnScreenModeClickListener != null) {
                    mOnScreenModeClickListener.onClick();
                }
            }
        });

//全屏下的切换分辨率按钮监听
        mLargeChangeQualityBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击切换分辨率 显示分辨率的对话框
                if (mOnQualityBtnClickListener != null) {
                    mOnQualityBtnClickListener.onQualityBtnClick(v, mAliyunMediaInfo.getQualities(), mCurrentQuality);
                }
            }
        });

        // 更多按钮点击监听
        mTitleMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "controlview ..... more");
                if (mOnShowMoreClickListener != null) {
                    mOnShowMoreClickListener.showMore();
                }
            }
        });
    }

    /**
     * 是不是MTS的源 //MTS的清晰度显示与其他的不太一样，所以这里需要加一个作为区分
     *
     * @param isMts true:是。false:不是
     */
    public void setIsMtsSource(boolean isMts) {
        isMtsSource = isMts;
    }

    /**
     * 设置当前播放的清晰度
     *
     * @param currentQuality 当前清晰度
     */
    public void setCurrentQuality(String currentQuality) {
        mCurrentQuality = currentQuality;
        updateLargeInfoBar();
        updateChangeQualityBtn();
    }

    /**
     * 设置是否强制清晰度。如果是强制，则不会显示切换清晰度按钮
     *
     * @param forceQuality true：是
     */
    public void setForceQuality(boolean forceQuality) {
        mForceQuality = forceQuality;
        updateChangeQualityBtn();
    }

    /**
     * 设置是否显示标题栏。
     *
     * @param show false:不显示
     */
    public void setTitleBarCanShow(boolean show) {
        mTitleBarCanShow = show;
        updateAllTitleBar();
    }

    /**
     * 设置是否显示控制栏
     *
     * @param show fase：不显示
     */
    public void setControlBarCanShow(boolean show) {
        mControlBarCanShow = show;
        updateAllControlBar();
    }

    /**
     * 设置当前屏幕模式：全屏还是小屏
     *
     * @param mode {@link AliyunScreenMode#Small}：小屏. {@link AliyunScreenMode#Full}:全屏
     */
    @Override
    public void setScreenModeStatus(AliyunScreenMode mode) {
        mAliyunScreenMode = mode;
        updateLargeInfoBar();
        updateSmallInfoBar();
        updateScreenModeBtn();
        updateShowMoreBtn();
        updateShowSendMessage();

    }







    /**
     *  更新更多按钮的显示和隐藏
     */
    private void updateShowMoreBtn() {
        if (mAliyunScreenMode == AliyunScreenMode.Full) {
            mTitleMore.setVisibility(VISIBLE);
        } else {
            mTitleMore.setVisibility(GONE);

        }
    }
    /**
     *  更新更多按钮的显示和隐藏
     */
    private void updateShowSendMessage() {
        if (mAliyunScreenMode == AliyunScreenMode.Full) {
            btn_send_comment.setVisibility(VISIBLE);
            et_comment.setVisibility(VISIBLE);
        } else {
            btn_send_comment.setVisibility(GONE);
            et_comment.setVisibility(GONE);

        }
    }

    /**
     * 设置主题色
     *
     * @param theme 支持的主题
     */
    @Override
    public void setTheme(AliyunVodPlayerView.Theme theme) {
        updateSeekBarTheme(theme);
    }

    /**
     * 设置当前的播放状态
     *
     * @param playState 播放状态
     */
    public void setPlayState(PlayState playState) {
        mPlayState = playState;
        updatePlayStateBtn();
    }

    /**
     * 设置视频信息
     *
     * @param aliyunMediaInfo 媒体信息
     * @param currentQuality  当前清晰度
     */
    public void setMediaInfo(AliyunMediaInfo aliyunMediaInfo, String currentQuality) {
        mAliyunMediaInfo = aliyunMediaInfo;
        mCurrentQuality = currentQuality;
        updateLargeInfoBar();
        updateChangeQualityBtn();
    }


    public void showMoreButton() {
        mTitleMore.setVisibility(VISIBLE);
    }

    public void hideMoreButton() {
        mTitleMore.setVisibility(GONE);
    }


    /**
     * 更新当前主题色
     *
     * @param theme 设置的主题色
     */
    private void updateSeekBarTheme(AliyunVodPlayerView.Theme theme) {
        //获取不同主题的图片
        int progressDrawableResId = R.drawable.alivc_info_seekbar_bg_blue;
        int thumbResId = R.drawable.alivc_info_seekbar_thumb_blue;
        if (theme == AliyunVodPlayerView.Theme.Blue) {
            progressDrawableResId = (R.drawable.alivc_info_seekbar_bg_blue);
            thumbResId = (R.drawable.alivc_seekbar_thumb_blue);
        } else if (theme == AliyunVodPlayerView.Theme.Green) {
            progressDrawableResId = (R.drawable.alivc_info_seekbar_bg_green);
            thumbResId = (R.drawable.alivc_info_seekbar_thumb_green);
        } else if (theme == AliyunVodPlayerView.Theme.Orange) {
            progressDrawableResId = (R.drawable.alivc_info_seekbar_bg_orange);
            thumbResId = (R.drawable.alivc_info_seekbar_thumb_orange);
        } else if (theme == AliyunVodPlayerView.Theme.Red) {
            progressDrawableResId = (R.drawable.alivc_info_seekbar_bg_red);
            thumbResId = (R.drawable.alivc_info_seekbar_thumb_red);
        }


        //这个很有意思。。哈哈。不同的seekbar不能用同一个drawable，不然会出问题。
        // https://stackoverflow.com/questions/12579910/seekbar-thumb-position-not-equals-progress

        //设置到对应控件中
        Resources resources = getResources();
        Drawable smallProgressDrawable = resources.getDrawable(progressDrawableResId);
        Drawable smallThumb = resources.getDrawable(thumbResId);

        Drawable largeProgressDrawable = resources.getDrawable(progressDrawableResId);
        Drawable largeThumb = resources.getDrawable(thumbResId);
    }

    /**
     * 是否锁屏。锁住的话，其他的操作界面将不会显示。
     *
     * @param screenLocked true：锁屏
     */
    public void setScreenLockStatus(boolean screenLocked) {
        mScreenLocked = screenLocked;

        updateAllTitleBar();
        updateAllControlBar();
        updateShowMoreBtn();

    }


    /**
     * 更新视频进度
     *
     * @param position 位置，ms
     */
    public void setVideoPosition(int position) {
        mVideoPosition = position;
        updateSmallInfoBar();
        updateLargeInfoBar();
    }

    /**
     * 获取视频进度
     *
     * @return 视频进度
     */
    public int getVideoPosition() {
        return mVideoPosition;
    }

    private void updateAllViews() {
        updateTitleView();//更新标题信息，文字
        updatePlayStateBtn();//更新播放状态
        updateLargeInfoBar();//更新大屏的显示信息
        updateSmallInfoBar();//更新小屏的显示信息
        updateChangeQualityBtn();//更新分辨率按钮信息
        updateScreenModeBtn();//更新大小屏信息
        updateAllTitleBar(); //更新标题显示
        updateAllControlBar();//更新控制栏显示
        updateShowMoreBtn();
        updateShowSendMessage();


    }

    /**
     * 更新切换清晰度的按钮是否可见，及文字。
     * 当forceQuality的时候不可见。
     */
    private void updateChangeQualityBtn() {
        if (mLargeChangeQualityBtn != null) {
            VcPlayerLog.d(TAG, "mCurrentQuality = " + mCurrentQuality + " , isMts Source = " + isMtsSource + " , mForceQuality = " + mForceQuality);
            mLargeChangeQualityBtn.setText(QualityItem.getItem(getContext(), mCurrentQuality, isMtsSource).getName());
            mLargeChangeQualityBtn.setVisibility(mForceQuality ? GONE : VISIBLE);
        }
    }

    /**
     * 更新控制条的显示
     */
    private void updateAllControlBar() {
        //单独设置可以显示，并且没有锁屏的时候才可以显示
        boolean canShow = mControlBarCanShow && !mScreenLocked;
        if (mControlBar != null) {
            mControlBar.setVisibility(canShow ? VISIBLE : INVISIBLE);
        }
    }

    /**
     * 更新标题栏的显示
     */
    private void updateAllTitleBar() {
        //单独设置可以显示，并且没有锁屏的时候才可以显示
        boolean canShow = mTitleBarCanShow && !mScreenLocked;
        if (mTitleBar != null) {
            mTitleBar.setVisibility(canShow ? VISIBLE : INVISIBLE);
        }
    }

    /**
     * 更新标题栏的标题文字
     */
    private void updateTitleView() {
        if (mAliyunMediaInfo != null && mAliyunMediaInfo.getTitle() != null && !("null".equals(mAliyunMediaInfo.getTitle()))) {
            mTitlebarText.setText(mAliyunMediaInfo.getTitle());
        } else {
            mTitlebarText.setText("");
        }
    }

    /**
     * 更新小屏下的控制条信息
     */
    private void updateSmallInfoBar() {
        if (mAliyunScreenMode == AliyunScreenMode.Full) {
            mSmallInfoBar.setVisibility(INVISIBLE);
        } else if (mAliyunScreenMode == AliyunScreenMode.Small) {
            //先设置小屏的info数据


            if (isSeekbarTouching) {
                //用户拖动的时候，不去更新进度值，防止跳动。
            } else {

            }
            //然后再显示出来。
            mSmallInfoBar.setVisibility(VISIBLE);
        }
    }

    /**
     * 更新大屏下的控制条信息
     */
    private void updateLargeInfoBar() {
        if (mAliyunScreenMode == AliyunScreenMode.Small) {
            //里面包含了很多按钮，比如切换清晰度的按钮之类的
            mLargeInfoBar.setVisibility(INVISIBLE);
        } else if (mAliyunScreenMode == AliyunScreenMode.Full) {



            if (isSeekbarTouching) {
                //用户拖动的时候，不去更新进度值，防止跳动。
            } else {

            }
            mLargeChangeQualityBtn.setText(QualityItem.getItem(getContext(), mCurrentQuality, isMtsSource).getName());

            //然后再显示出来。
            mLargeInfoBar.setVisibility(VISIBLE);
        }


    }

    /**
     * 更新切换大小屏按钮的信息
     */
    private void updateScreenModeBtn() {
        if (mAliyunScreenMode == AliyunScreenMode.Full) {
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mScreenModeBtn.getLayoutParams();
            params.weight= DensityUtil.dip2px(25);//设置当前控件布局的高度
            params.height= DensityUtil.dip2px(25);//设置当前控件布局的高度
            mScreenModeBtn.setLayoutParams(params);//将设置好的布局参数应用到控件中

            mScreenModeBtn.setImageResource(R.drawable.alivc_screen_mode_small);
        } else {
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mScreenModeBtn.getLayoutParams();
            params.weight= DensityUtil.dip2px(18);//设置当前控件布局的高度
            params.height= DensityUtil.dip2px(18);//设置当前控件布局的高度
            mScreenModeBtn.setLayoutParams(params);//将设置好的布局参数应用到控件中
            mScreenModeBtn.setImageResource(R.drawable.alivc_screen_mode_large);
        }
    }



    /**
     * 更新播放按钮的状态
     */
    private void updatePlayStateBtn() {

        if (mPlayState == PlayState.NotPlaying) {
            mPlayStateBtn.setImageResource(R.drawable.alivc_playstate_play);
        } else if (mPlayState == PlayState.Playing) {
            mPlayStateBtn.setImageResource(R.drawable.alivc_playstate_pause);
        }
    }

    /**
     * 监听view是否可见。从而实现5秒隐藏的功能
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@Nullable View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            //如果变为可见了。启动五秒隐藏。
            hideDelayed();
        }
    }

    /**
     * 隐藏类
     */
    private static class HideHandler extends Handler {
        private WeakReference<ControlLiveView> controlViewWeakReference;

        public HideHandler(ControlLiveView controlView) {
            controlViewWeakReference = new WeakReference<ControlLiveView>(controlView);
        }

        @Override
        public void handleMessage(Message msg) {

            ControlLiveView controlView = controlViewWeakReference.get();
            if (controlView != null) {
                controlView.hide(HideType.Normal);
            }

            super.handleMessage(msg);
        }
    }

    private HideHandler mHideHandler = new HideHandler(this);

    private static final int WHAT_HIDE = 0;
    private static final int DELAY_TIME = 5 * 1000; //5秒后隐藏

    private void hideDelayed() {
        mHideHandler.removeMessages(WHAT_HIDE);
        mHideHandler.sendEmptyMessageDelayed(WHAT_HIDE, DELAY_TIME);
    }

    /**
     * 重置状态
     */
    @Override
    public void reset() {
        mHideType = null;
        mAliyunMediaInfo = null;
        mVideoPosition = 0;
        mPlayState = PlayState.NotPlaying;
        isSeekbarTouching = false;
        updateAllViews();
    }

    /**
     * 显示画面
     */
    @Override
    public void show() {
        if (mHideType == HideType.End) {
            //如果是由于错误引起的隐藏，那就不能再展现了
            setVisibility(GONE);
            hideQualityDialog();
        } else {
            updateAllViews();
            setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏画面
     */
    @Override
    public void hide(HideType hideType) {
        if (mHideType != HideType.End) {
            mHideType = hideType;
        }
        setVisibility(GONE);
        hideQualityDialog();
    }

    /**
     * 隐藏清晰度对话框
     */
    private void hideQualityDialog() {
        if (mOnQualityBtnClickListener != null) {
            mOnQualityBtnClickListener.onHideQualityView();
        }
    }

    /**
     * 设置当前缓存的进度，给seekbar显示
     *
     * @param mVideoBufferPosition 进度，ms
     */
    public void setVideoBufferPosition(int mVideoBufferPosition) {
        this.mVideoBufferPosition = mVideoBufferPosition;
        updateSmallInfoBar();
        updateLargeInfoBar();
    }

    public void setOnMenuClickListener(OnMenuClickListener l) {
        mOnMenuClickListener = l;
    }


    public interface OnMenuClickListener {
        /**
         * 按钮点击事件
         */
        void onMenuClick();
    }

    public interface OnDownloadClickListener {
        /**
         * 下载点击事件
         */
        void onDownloadClick();
    }

    public void setOnDownloadClickListener(
        OnDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnQualityBtnClickListener(OnQualityBtnClickListener l) {
        mOnQualityBtnClickListener = l;
    }

    public interface OnQualityBtnClickListener {
        /**
         * 清晰度按钮被点击
         *
         * @param v              被点击的view
         * @param qualities      支持的清晰度
         * @param currentQuality 当前清晰度
         */
        void onQualityBtnClick(View v, List<String> qualities, String currentQuality);

        /**
         * 隐藏
         */
        void onHideQualityView();
    }


    public void setOnScreenLockClickListener(OnScreenLockClickListener l) {
        mOnScreenLockClickListener = l;
    }

    public interface OnScreenLockClickListener {
        /**
         * 锁屏按钮点击事件
         */
        void onClick();
    }

    public void setOnScreenModeClickListener(OnScreenModeClickListener l) {
        mOnScreenModeClickListener = l;
    }

    public interface OnScreenModeClickListener {
        /**
         * 大小屏按钮点击事件
         */
        void onClick();
    }


    public void setOnBackClickListener(OnBackClickListener l) {
        mOnBackClickListener = l;
    }

    public interface OnBackClickListener {
        /**
         * 返回按钮点击事件
         */
        void onClick();
    }

    public interface OnSeekListener {
        /**
         * seek结束事件
         */
        void onSeekEnd(int position);

        /**
         * seek开始事件
         */
        void onSeekStart();
    }


    public void setOnSeekListener(OnSeekListener onSeekListener) {
        mOnSeekListener = onSeekListener;
    }

    /**
     * 播放状态
     */
    public static enum PlayState {
        /**
         * Playing:正在播放
         * NotPlaying: 停止播放
         */
        Playing, NotPlaying
    }

    public interface OnPlayStateClickListener {
        /**
         * 播放按钮点击事件
         */
        void onPlayStateClick();
    }


    public void setOnPlayStateClickListener(OnPlayStateClickListener onPlayStateClickListener) {
        mOnPlayStateClickListener = onPlayStateClickListener;
    }

    /**
     * 横屏下显示更多
     */
    public interface OnShowMoreClickListener {
        void showMore();
    }

    public void setOnShowMoreClickListener(
        OnShowMoreClickListener listener) {
        this.mOnShowMoreClickListener = listener;
    }
}
