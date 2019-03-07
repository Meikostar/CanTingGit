/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.editor.effects.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zhongchuang.canting.allive.editor.editor.EditorActivity;
import com.zhongchuang.canting.allive.editor.effectmanager.MoreCaptionActivity;
import com.zhongchuang.canting.allive.editor.effectmanager.MoreMVActivity;
import com.zhongchuang.canting.allive.editor.effects.audiomix.AudioMixChooserMediator;
import com.zhongchuang.canting.allive.editor.effects.caption.CaptionChooserMediator;
import com.zhongchuang.canting.allive.editor.effects.filter.FilterChooserMediator;
import com.zhongchuang.canting.allive.editor.effects.imv.ImvChooserMediator;
import com.zhongchuang.canting.allive.editor.effects.overlay.OverlayChooserMediator;
import com.zhongchuang.canting.allive.editor.effects.time.TimeChooserMediator;

public class ViewStack {

    private final Context mContext;
    private EditorService mEditorService;
    private OnEffectChangeListener mOnEffectChangeListener;
    private OnDialogButtonClickListener mDialogButtonClickListener;
    private BottomAnimation mBottomAnimation;

    private FilterChooserMediator mFilterChooserMediator;
    private OverlayChooserMediator mOverlayChooserMediator;
    private AudioMixChooserMediator mAudioMixChooserMediator;
    private CaptionChooserMediator mCaptionChooserMediator;
    private ImvChooserMediator mImvChooserMediator;
    private TimeChooserMediator mTimeChooserMediator;

    public ViewStack(Context context) {
        mContext = context;
    }

    public void setActiveIndex(int value) {

        UIEditorPage index = UIEditorPage.get(value);
        switch (index) {
            case FILTER_EFFECT:
                mFilterChooserMediator = FilterChooserMediator.newInstance();
                mFilterChooserMediator.setEditorService(mEditorService);
                mFilterChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mFilterChooserMediator.setBottomAnimation(mBottomAnimation);
                mFilterChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "filter");
                break;
            case MV:
                mImvChooserMediator = ImvChooserMediator.newInstance();
                mImvChooserMediator.setEditorService(mEditorService);
                mImvChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mImvChooserMediator.setBottomAnimation(mBottomAnimation);
                mImvChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "imv");
                break;
            case OVERLAY:
                mOverlayChooserMediator = OverlayChooserMediator.newInstance();
                mOverlayChooserMediator.setEditorService(mEditorService);
                mOverlayChooserMediator.setBottomAnimation(mBottomAnimation);
                mOverlayChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mOverlayChooserMediator.setDialogButtonClickListener(mDialogButtonClickListener);
                mOverlayChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "aliyun_svideo_overlay");
                break;
            case CAPTION:
                mCaptionChooserMediator = CaptionChooserMediator.newInstance();
                mCaptionChooserMediator.setEditorService(mEditorService);
                mCaptionChooserMediator.setBottomAnimation(mBottomAnimation);
                mCaptionChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mCaptionChooserMediator.setDialogButtonClickListener(mDialogButtonClickListener);
                mCaptionChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "aliyun_svideo_caption");
                break;
            case AUDIO_MIX:
                mAudioMixChooserMediator = AudioMixChooserMediator.newInstance();
                mAudioMixChooserMediator.setBottomAnimation(mBottomAnimation);
                mAudioMixChooserMediator.setEditorService(mEditorService);
                mAudioMixChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mAudioMixChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "audioMix");
                break;
            case PAINT:
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.PAINT;
                mOnEffectChangeListener.onEffectChange(effectInfo);
                break;
            case TIME:
                mTimeChooserMediator = TimeChooserMediator.newInstance();
                mTimeChooserMediator.setBottomAnimation(mBottomAnimation);
                mTimeChooserMediator.setOnEffectChangeListener(mOnEffectChangeListener);
                mTimeChooserMediator.setEditorService(mEditorService);
                mTimeChooserMediator.show(((EditorActivity) mContext).getSupportFragmentManager(), "time");
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case BaseChooser.CAPTION_REQUEST_CODE:
                if(mCaptionChooserMediator == null) {
                    break;
                }
                if (resultCode == Activity.RESULT_OK) {
                    int id = data.getIntExtra(MoreCaptionActivity.SELECTED_ID, 0);
                    mCaptionChooserMediator.initResourceLocalWithSelectId(id);
                }else if(resultCode == Activity.RESULT_CANCELED){
                    mCaptionChooserMediator.initResourceLocalWithSelectId(0);
                }
                break;
            case BaseChooser.IMV_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mImvChooserMediator != null) {
                    int id = data.getIntExtra(MoreMVActivity.SELECTD_ID, 0);
                    mImvChooserMediator.initResourceLocalWithSelectId(id);
                }
                break;
            case BaseChooser.PASTER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mOverlayChooserMediator != null) {
                    int id = data.getIntExtra(MoreCaptionActivity.SELECTED_ID, 0);
                    mOverlayChooserMediator.setCurrResourceID(id);
                }
                break;
        }
    }

    public void setEditorService(EditorService editorService) {
        mEditorService = editorService;
    }

    public void setEffectChange(OnEffectChangeListener onEffectChangeListener) {
        mOnEffectChangeListener = onEffectChangeListener;
    }

    public void setBottomAnimation(BottomAnimation bottomAnimation) {
        mBottomAnimation = bottomAnimation;
    }

    public void setDialogButtonClickListener(OnDialogButtonClickListener dialogButtonClickListener) {
        mDialogButtonClickListener = dialogButtonClickListener;
    }
}
