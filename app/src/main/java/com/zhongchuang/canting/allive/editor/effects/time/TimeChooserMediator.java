package com.zhongchuang.canting.allive.editor.effects.time;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.editor.effects.control.BaseChooser;
import com.zhongchuang.canting.allive.editor.effects.control.EffectInfo;
import com.zhongchuang.canting.allive.editor.effects.control.UIEditorPage;
import com.aliyun.editor.TimeEffectType;

public class TimeChooserMediator extends BaseChooser implements View.OnClickListener{

    private ImageView mTimeNone,mTimeSlow,mTimeFast,mTimeRepeat2Invert;
    private TextView mTimeMoment,mTimeAll;
    private boolean mIsMoment = true;

    public static TimeChooserMediator newInstance(){
        TimeChooserMediator dialog = new TimeChooserMediator();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.aliyun_svideo_time_view, container);
        mTimeNone = rootView.findViewById(R.id.time_effect_none);
        mTimeNone.setOnClickListener(this);
        mTimeSlow = rootView.findViewById(R.id.time_effect_slow);
        mTimeSlow.setOnClickListener(this);
        mTimeFast = rootView.findViewById(R.id.time_effect_speed_up);
        mTimeFast.setOnClickListener(this);
        mTimeRepeat2Invert = rootView.findViewById(R.id.time_effect_repeat_invert);
        mTimeRepeat2Invert.setOnClickListener(this);
        mTimeMoment = rootView.findViewById(R.id.time_moment);
        mTimeMoment.setOnClickListener(this);
        mTimeAll = rootView.findViewById(R.id.time_all);
        mTimeAll.setOnClickListener(this);
        mTimeMoment.performClick();
        return rootView;
    }

    private void resetBtn(){
        mTimeNone.setSelected(false);
        mTimeSlow.setSelected(false);
        mTimeFast.setSelected(false);
        mTimeRepeat2Invert.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        resetBtn();
        v.setSelected(true);
        if(id == R.id.time_effect_none) {
            if(mOnEffectChangeListener != null){
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.TIME;
                effectInfo.timeEffectType = TimeEffectType.TIME_EFFECT_TYPE_NONE;
                effectInfo.isMoment = mIsMoment;
                mOnEffectChangeListener.onEffectChange(effectInfo);
            }
        }else if(id == R.id.time_effect_slow){
            if(mOnEffectChangeListener != null){
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.TIME;
                effectInfo.timeEffectType = TimeEffectType.TIME_EFFECT_TYPE_RATE;
                effectInfo.timeParam = 0.5f;
                effectInfo.isMoment = mIsMoment;
                mOnEffectChangeListener.onEffectChange(effectInfo);
            }

        }else if(id == R.id.time_effect_speed_up){
            if(mOnEffectChangeListener != null){
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.TIME;
                effectInfo.timeEffectType = TimeEffectType.TIME_EFFECT_TYPE_RATE;
                effectInfo.timeParam = 2.0f;
                effectInfo.isMoment = mIsMoment;
                mOnEffectChangeListener.onEffectChange(effectInfo);
            }

        }else if(id == R.id.time_effect_repeat_invert){
            if(mOnEffectChangeListener != null){
                EffectInfo effectInfo = new EffectInfo();
                effectInfo.type = UIEditorPage.TIME;
                effectInfo.isMoment = mIsMoment;
                if(mIsMoment)
                {
                    effectInfo.timeEffectType = TimeEffectType.TIME_EFFECT_TYPE_REPEAT;
                }else{
                    effectInfo.timeEffectType = TimeEffectType.TIME_EFFECT_TYPE_INVERT;
                }
                mOnEffectChangeListener.onEffectChange(effectInfo);
            }
        }else if(id == R.id.time_moment){
            mTimeAll.setTextColor(getResources().getColor(R.color.aliyun_powered_text_color));
            mTimeMoment.setTextColor(getResources().getColor(R.color.white));
            mTimeRepeat2Invert.setImageResource(R.drawable.aliyun_svideo_video_edit_time_repeat_selector);
            mIsMoment = true;
        }else if(id == R.id.time_all){
            mTimeMoment.setTextColor(getResources().getColor(R.color.aliyun_powered_text_color));
            mTimeAll.setTextColor(getResources().getColor(R.color.white));
            mTimeRepeat2Invert.setImageResource(R.drawable.aliyun_svideo_video_edit_time_invert_selector);
            mIsMoment = false;
        }

    }


}

