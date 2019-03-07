package com.zhongchuang.canting.widget.red;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.widget.CircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 红包弹框
 * @date 2017/11/27  15:12
 */

public class RedPacketViewHolder {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_send_rp)
    TextView tvSendRp;
    @BindView(R.id.tv_no_rp)
    TextView tvNoRp;
    @BindView(R.id.iv_open_rp)
    ImageView ivOpenRp;
    @BindView(R.id.tv_look_others)
    TextView tvLookOthers;
    private Context mContext;
    private OnRedPacketDialogClickListener mListener;

    private int[] mImgResIds = new int[]{
            R.mipmap.icon_open_red_packet1
    };
    private FrameAnimation mFrameAnimation;


    public RedPacketViewHolder(Context context, View view) {
        mContext = context;
        ButterKnife.bind(this, view);
        initData();
    }

    public void initData() {
        ivOpenRp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFrameAnimation != null) {
                    //如果正在转动，则直接返回
                    return;
                }

                startAnim();

                if (mListener != null) {
                    mListener.onOpenClick();
                }

            }
        });
    }

    @OnClick({R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                stopAnim();
                if (mListener != null) {
                    mListener.onCloseClick();
                }
                break;


        }
    }

    public void setData(RedInfo entity) {


        Glide.with(mContext)
                .load(entity.sendHeadImage).asBitmap().transform(new CircleTransform(mContext))
                .into(ivHeader);

        tvName.setText(entity.sendRemarkName);
        if (entity.isAll == 1) {
            ivOpenRp.setVisibility(View.INVISIBLE);
            tvSendRp.setVisibility(View.GONE);
            tvNoRp.setText("手慢了，红包派送完了");
        } else {
            ivOpenRp.setVisibility(View.VISIBLE);
            tvSendRp.setVisibility(View.VISIBLE);
            tvNoRp.setText(entity.leav_message);
        }

    }

    public void setGones() {
        ivOpenRp.setVisibility(View.INVISIBLE);
        tvSendRp.setVisibility(View.GONE);
        tvNoRp.setText("手慢了，红包派送完了");

    }

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(ivOpenRp, mImgResIds, 200, true);
        mFrameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
                Log.i("", "start");
            }

            @Override
            public void onAnimationEnd() {
                Log.i("", "end");
            }

            @Override
            public void onAnimationRepeat() {
                Log.i("", "repeat");
            }

            @Override
            public void onAnimationPause() {
                ivOpenRp.setBackgroundResource(R.mipmap.icon_open_red_packet1);
            }
        });
    }

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }

    public void setOnRedPacketDialogClickListener(OnRedPacketDialogClickListener listener) {
        mListener = listener;
    }
}
