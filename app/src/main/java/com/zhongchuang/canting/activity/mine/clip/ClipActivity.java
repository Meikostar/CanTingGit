package com.zhongchuang.canting.activity.mine.clip;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.common.utils.ToastUtil;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.base.BaseActivity1;
import com.zhongchuang.canting.been.UploadFileBean;
import com.zhongchuang.canting.utils.BitmapUtil;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.waitLoading.ShapeLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/***
 * 功能描述:图片批量裁剪页面
 * 作者:qiujialiu
 * 时间:2016/12/20
 * 版本:1.0
 ***/
public class ClipActivity extends BaseActivity1 {

    @BindView(R.id.tv_abandon)
    TextView mTextViewAbandon;
    @BindView(R.id.tv_title)
    TextView mTextViewTitle;
    @BindView(R.id.tv_clip)
    TextView mTextViewClip;
    @BindView(R.id.ll_content)
    LinearLayout mLayoutContent;


    // @ViewInject(R.id.clip_view)
    private List<ClipView> mClipViews;
    //    private ClipView mView;
//    private ClipView mView2;
    ArrayList<String> paths;
    private int currentIndex = 1;
    ArrayList<UploadFileBean> afterPaths = new ArrayList<>();
    private int size = 0;
    private int count;
    private int failCount = 0;
    private int scale;



    @Override
    public void initViews() {
        setContentView(R.layout.layout_activity_clip_photo);
        ButterKnife.bind(this);
        mClipViews = new ArrayList<>();
        LayoutTransition transition = new LayoutTransition();
        transition.getDuration(800);
        ObjectAnimator an = new ObjectAnimator();
        an.setDuration(800).setFloatValues(DensityUtil.getWidth(this), 0);
        an.setPropertyName("translationX");
        ObjectAnimator an2 = new ObjectAnimator();
        an2.setDuration(800).setFloatValues(0, -DensityUtil.getWidth(this));
        an2.setPropertyName("translationX");
        transition.setAnimator(LayoutTransition.APPEARING, an);
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING, transition.getAnimator(LayoutTransition.CHANGE_APPEARING));
        transition.setAnimator(LayoutTransition.DISAPPEARING, transition.getAnimator(LayoutTransition.DISAPPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, transition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
        mLayoutContent.setLayoutTransition(transition);
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("剪切中...")
                .build();
    }
    private ShapeLoadingDialog shapeLoadingDialog;
    @Override
    public void bindEvents() {
        addClipListener();
        mTextViewAbandon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDailogManager.getInstance().getBuilder(ClipActivity.this)
                        .setTitle("提示")
                        .haveTitle(true)
                        .setMessage("放弃裁剪不保存此次选择\n是否放弃裁剪")
                        .setLeftButtonText("是")
                        .setRightButtonText("继续裁剪")
                        .setOnClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (which == BaseDailogManager.LEFT_BUTTON) {
                                    finish();
                                }
                            }
                        }).create().show();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            clipImage();
        }
    };

    private void addClipListener() {
        mTextViewClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paths != null && paths.size() > 0) {
                    handler.sendEmptyMessageDelayed(1, 500);
                    if (paths.size() == 1) {
                        shapeLoadingDialog.show();

                        mTextViewClip.setEnabled(false);
                    }
                }
            }
        });
    }

    private void clipImage() {
        if (paths != null && paths.size() > 0) {
            clipPhoto(paths.get(0), currentIndex - 1);
            paths.remove(0);
            if (currentIndex < count) {
                mTextViewTitle.setText((currentIndex + 1) + "/" + count);
            }
            if (paths.size() > 0) {
                mClipViews.get(currentIndex).setBmpPath(paths.get(0));
                if (mClipViews.get(currentIndex - 1).getParent() != null) {
                    mLayoutContent.removeView(mClipViews.get(currentIndex - 1));
                }
                mLayoutContent.addView(mClipViews.get(currentIndex));
                if (currentIndex > 1) {
                    mLayoutContent.removeView(mClipViews.get(currentIndex - 2));
                }
                currentIndex++;
            } else {
                currentIndex = 1;
            }
        }
    }

    private void clipPhoto(final String path, final int index) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (StringUtil.isEmpty(path)) {
                    subscriber.onNext(path);
                } else {
                    Bitmap bitmap = mClipViews.get(index).getCroppedImage();
                    String path = BitmapUtil.saveBitmapToFile(ClipActivity.this, bitmap, "yunshlImage_" + TimeUtil.formatToFileName(System.currentTimeMillis()), 100, true);

                    subscriber.onNext(path);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                        mClipViews.get(index).release();
                        afterPaths.add(new UploadFileBean(1, s, true));
                        if (size == afterPaths.size()) {
                            if (failCount > 0) {
                                ToastUtil.showToast(ClipActivity.this,"剪裁完成，失败" + failCount + "张");
                            } else {
                                ToastUtil.showToast(ClipActivity.this,"剪裁完成");
                            }
                            failCount = 0;
                            clipFinish();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mClipViews.get(index).release();
                        failCount++;

                        throwable.printStackTrace();
                        afterPaths.add(new UploadFileBean(1, path, false));
                        if (size == afterPaths.size()) {
                            clipFinish();
                        }
                    }
                });

    }

    private void clipFinish() {
        mTextViewClip.setEnabled(true);
        shapeLoadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("result", afterPaths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void initData() {
        if (getIntent() != null) {
            paths = getIntent().getStringArrayListExtra("data");
            scale = getIntent().getIntExtra("scale", 1);
        }
        if (paths != null && paths.size() > 0) {
            size = paths.size();
            for (int i = 0; i < size; i++) {
                if (scale != 1) {
                    mClipViews.add(new ClipView(this, scale));
                } else {
                    mClipViews.add(new ClipView(this));
                }

            }

            mClipViews.get(0).setBmpPath(paths.get(0));
            mLayoutContent.addView(mClipViews.get(0));
            count = paths.size();
            mTextViewTitle.setText("1/" + count);
        } else {
            ToastUtil.showToast(ClipActivity.this,"裁剪出错，没有图片");
            finish();
        }
    }


}
