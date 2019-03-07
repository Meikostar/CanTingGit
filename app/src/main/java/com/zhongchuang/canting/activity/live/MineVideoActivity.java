package com.zhongchuang.canting.activity.live;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.common.utils.StorageUtils;
import com.aliyun.struct.common.CropKey;
import com.aliyun.struct.common.ScaleMode;
import com.aliyun.struct.common.VideoQuality;
import com.aliyun.struct.encoder.VideoCodecs;
import com.aliyun.struct.recorder.CameraType;
import com.aliyun.struct.recorder.FlashType;
import com.aliyun.struct.snap.AliyunSnapVideoParam;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.recycle.GiftReCycleAdapter;
import com.zhongchuang.canting.allive.importer.MediaActivity;
import com.zhongchuang.canting.allive.recorder.AliyunVideoRecorder;
import com.zhongchuang.canting.allive.recorder.util.Common;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.PhotoPopupWindow;
import com.zhongchuang.canting.widget.loadingView.LoadingPager;
import com.zhongchuang.canting.widget.loadingView.OnNetworkRetryListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MineVideoActivity extends BaseActivity1 implements BaseContract.View {


    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.zhuye_geren)
    ImageView zhuyeGeren;
    @BindView(R.id.serch_edit)
    TextView serchEdit;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.loadingView)
    LoadingPager loadingView;
    private Intent perIntent;
    private Unbinder bind;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private int currpage = 1;//第几页
    private BasesPresenter presenter;

    private View view;


    private int state;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_mine_video);
        ButterKnife.bind(this);
        tvSend.setEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        mSuperRecyclerView.setLayoutManager(layoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        presenter = new BasesPresenter(this);

        adapter = new GiftReCycleAdapter(this);
        mSuperRecyclerView.setAdapter(adapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage = 1;
                presenter.getGiftDetailedList(currpage + "", TYPE_PULL_REFRESH);


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
        loadingView.showPager(LoadingPager.STATE_LOADING);
        zhuyeGeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSuperRecyclerView.setAdapter(adapter);
        reflash();

        initAssetPath();
        copyAssets();
    }

    private String[] mEffDirs;

    private void initAssetPath() {
        String path = StorageUtils.getCacheDirectory(this).getAbsolutePath() + File.separator + Common.QU_NAME + File.separator;
        File filter = new File(new File(path), "filter");

        String[] list = filter.list();
        if (list == null || list.length == 0) {
            return;
        }
        mEffDirs = new String[list.length + 1];
        mEffDirs[0] = null;
        for (int i = 0; i < list.length; i++) {
            mEffDirs[i + 1] = filter.getPath() + "/" + list[i];
        }
    }


    private void copyAssets() {
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                Common.copyAll(MineVideoActivity.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                tvSend.setEnabled(true);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void bindEvents() {
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindows();
            }
        });
        loadingView.setNetworkRetryListenner(new OnNetworkRetryListener() {
            @Override
            public void networkRetry() {
                showPopWindows();
            }
        });
    }

    private int mRatio;
    private VideoQuality videoQuality;
    private ScaleMode scaleMode = CropKey.SCALE_CROP;
    private int mResolutionMode, mRatioMode;
    private VideoQuality mVideoQuality;
    private VideoCodecs mVideoCodec = VideoCodecs.H264_HARDWARE;
    private static final int DEFAULT_FRAMR_RATE = 25;
    private static final int DEFAULT_GOP = 125;

    public void showPopWindows() {
        videoQuality = VideoQuality.HD;
        mRatio = CropKey.RATIO_MODE_3_4;
        mResolutionMode = AliyunSnapVideoParam.RESOLUTION_540P;
        mVideoQuality = VideoQuality.HD;
        mRatioMode = AliyunSnapVideoParam.RATIO_MODE_3_4;
        View view = LayoutInflater.from(MineVideoActivity.this).inflate(R.layout.chat_phone_popwindow_view, null);
        TextView tv_camera = (TextView) view.findViewById(R.id.tv_camera);
        TextView tv_choose = (TextView) view.findViewById(R.id.tv_choose);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_choose.setText(R.string.ps);
        tv_camera.setText(R.string.csjzxz);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineVideoActivity.this, MediaActivity.class);
                intent.putExtra(CropKey.VIDEO_RATIO, mRatio);
                intent.putExtra(CropKey.VIDEO_SCALE, scaleMode);
                intent.putExtra(CropKey.VIDEO_QUALITY, videoQuality);
                intent.putExtra(CropKey.VIDEO_FRAMERATE, DEFAULT_FRAMR_RATE);
                intent.putExtra(CropKey.VIDEO_GOP, DEFAULT_GOP);

                intent.putExtra(CropKey.VIDEO_BITRATE, 0);

//            intent.putExtra("width",etWidth.getText().toString());
//            intent.putExtra("height",etHeight.getText().toString());
                startActivity(intent);
//                Intent intent = new Intent(MineVideoActivity.this, ImportEditSettingTest.class);
//                intent.putExtra("pass", 1);
//                startActivity(intent);
                mWindowAddPhoto.dismiss();
            }
        });
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int min = 2000;
                int max = 3600000;
                int gop = 5;
                int bitrate = 0;
                AliyunSnapVideoParam recordParam = new AliyunSnapVideoParam.Builder()
                        .setResolutionMode(mResolutionMode)
                        .setRatioMode(mRatioMode)
                        .setRecordMode(AliyunSnapVideoParam.RECORD_MODE_AUTO)
                        .setFilterList(mEffDirs)
                        .setBeautyLevel(80)
                        .setBeautyStatus(true)
                        .setCameraType(CameraType.FRONT)
                        .setFlashType(FlashType.ON)
                        .setNeedClip(true)
                        .setMaxDuration(max)
                        .setMinDuration(min)
                        .setVideoQuality(mVideoQuality)
                        .setGop(gop)
                        .setVideoBitrate(bitrate)
                        .setVideoCodec(mVideoCodec)
                        /**
                         * 裁剪参数
                         */
                        .setMinVideoDuration(4000)
                        .setMaxVideoDuration(3599 * 1000)
                        .setMinCropDuration(3000)
                        .setFrameRate(28)
                        .setCropMode(ScaleMode.PS)
                        .build();
                AliyunVideoRecorder.startRecord(MineVideoActivity.this, recordParam);

                mWindowAddPhoto.dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowAddPhoto.dismiss();
            }
        });
        mWindowAddPhoto = new PhotoPopupWindow(MineVideoActivity.this).bindView(view);
        mWindowAddPhoto.showAtLocation(serchEdit, Gravity.BOTTOM, 0, 0);

    }

    private PhotoPopupWindow mWindowAddPhoto;

    @Override
    public void initData() {

    }

    private GiftReCycleAdapter adapter;

    private List<Hand> datas = new ArrayList<>();
    private int cout = 12;

    public void onDataLoaded(int loadType, final boolean haveNext, List<Hand> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            datas.clear();
            for (Hand info : list) {
                datas.add(info);
            }
        } else {
            for (Hand info : list) {
                datas.add(info);
            }
        }
        if (datas != null && datas.size() != 0) {

            loadingView.showPager(LoadingPager.STATE_SUCCEED);

        } else {

            loadingView.setSpecialText(getString(R.string.scsp), 0);
            loadingView.showPager(LoadingPager.STATE_EMPTY);

        }
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
        mSuperRecyclerView.hideMoreProgress();

        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();
                    presenter.getGiftDetailedList(currpage + "", TYPE_PULL_MORE);

                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }


    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        dimessProgress();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public <T> void toEntity(T entity, int type) {
        Hands data = (Hands) entity;
        if (data != null && data.giftList != null) {
            onDataLoaded(type, cout == data.giftList.size(), data.giftList);

        } else {
            adapter.notifyDataSetChanged();
            loadingView.setContent(getString(R.string.zwsj));
            loadingView.showPager(LoadingPager.STATE_EMPTY);
        }
    }

    private void reflash() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }



}

