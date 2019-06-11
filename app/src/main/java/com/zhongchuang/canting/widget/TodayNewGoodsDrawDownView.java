package com.zhongchuang.canting.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.ActionItem;
import com.zhongchuang.canting.utils.DensityUtil;

import java.util.ArrayList;


/***
 * 功能描述:下接view
 * 作者:chewnei
 * 时间:2016/12/25
 * 版本:v1.0
 ***/

public class TodayNewGoodsDrawDownView extends PopupWindow implements View.OnClickListener, PopupWindow.OnDismissListener {

    private TextView tv_no_look;

    private TextView tv_cancle_concer;

    private ImageView iv_show;

    // 实例化一个矩形
    private Rect mRect = new Rect();
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    // 弹窗子类项选中时的监听
    private OnItemClickListener mItemClickListener;
    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public ArrayList<ActionItem> getmActionItems() {
        return mActionItems;
    }

    public void setmActionItems(ArrayList<ActionItem> mActionItems) {
        this.mActionItems = mActionItems;
    }

    public TodayNewGoodsDrawDownView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_draw_down, null);

        tv_no_look = view.findViewById(R.id.tv_no_look);

        tv_cancle_concer = view.findViewById(R.id.tv_cancle_concer);

        tv_no_look.setOnClickListener(this);

        tv_cancle_concer.setOnClickListener(this);

        this.setContentView(view);

        this.setWidth(DensityUtil.dip2px(context, 83));

        this.setHeight(DensityUtil.dip2px(context, 61));

        this.setFocusable(true);

        this.setOutsideTouchable(true);

        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        this.setAnimationStyle(R.style.pop_up_to_down_anim);

        this.setOnDismissListener(this);

        initItemData();
    }

    private void initItemData() {
        addAction(new ActionItem("不想看Ta"));
        addAction(new ActionItem("取消关注"));
    }


    public void showView(View parent){

        iv_show=(ImageView)parent;

        parent.getLocationOnScreen(mLocation);
        // 设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + parent.getWidth(),mLocation[1] + parent.getHeight());
        tv_no_look.setText(mActionItems.get(0).mTitle);
        if(!this.isShowing()){
           showAtLocation(parent, Gravity.NO_GRAVITY,mLocation[0]-this.getWidth()+30,mLocation[1]+50);
//            showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0] - this.getWidth()
//                    , mLocation[1] - ((this.getHeight() - parent.getHeight()) / 2));
        }else{
            dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_no_look:
                if(mItemClickListener!=null)
                    mItemClickListener.onItemClick(mActionItems.get(0), 0);
                break;
            case R.id.tv_cancle_concer:
                if(mItemClickListener!=null)
                    mItemClickListener.onItemClick(mActionItems.get(1), 1);
                break;
            default:
                break;
        }
    }
    /**
     * 添加子类项
     */
    public void addAction(ActionItem action) {
        if (action != null) {
            mActionItems.add(action);
        }
    }

    @Override
    public void onDismiss() {
       // iv_show.setImageResource(R.drawable.common_icon_filtrate_arrow_up);
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemClickListener {
        void onItemClick(ActionItem item, int position);
    }
}
