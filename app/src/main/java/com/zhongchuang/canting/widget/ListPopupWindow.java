package com.zhongchuang.canting.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.ListPopAdapter;
import com.zhongchuang.canting.been.DATA;
import com.zhongchuang.canting.been.GAME;

import java.util.List;



/***
 * 功能描述:底部弹出的popupwindow
 * 作者:chenwei
 * 时间:2016/12/26
 * 版本:1.0
 ***/

public class ListPopupWindow extends PopupWindow{
    private View mView;
    private Context mContext;
    private ListView listView;
    private TextView tv_cancel;
    private CardView card;

    private ListPopAdapter adapter;
    public ListPopupWindow(Activity context, List<GAME> data){
        super(context);
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.list_popwindow, null);
        listView = (ListView) mView.findViewById(R.id.list);
        card = (CardView) mView.findViewById(R.id.card);
        adapter=new ListPopAdapter(context);
        adapter.setData(data);
        listView.setAdapter(adapter);
         tv_cancel = (TextView) mView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        adapter.setClickListener(new ListPopAdapter.ItemCliks() {
            @Override
            public void getItem(GAME menu, int poistion) {
                listener.clickListener(menu,poistion);
                dismiss();
            }
        });
    }
   public void remove(){
       card.setVisibility(View.GONE);
   }
   public void setSureListener(ClickListener listener){
       this.listener=listener;
   }
   private ClickListener listener;
   public interface  ClickListener{
       void clickListener(GAME menu, int poistion);
   }
    @Override
    public void showAsDropDown(View anchor) {
        init();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        init();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        init();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        init();
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init() {
        // TODO Auto-generated method stub
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //设置非PopupWindow区域是否可触摸
//        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext,0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha((Activity) mContext, 1f);
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha)
    {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showPopView(ImageView imgs) {
        showAtLocation(imgs, Gravity.BOTTOM, 0, 0);
    }

    public ListPopupWindow bindView(View view) {
        mView = view;
        return this;
    }
}
