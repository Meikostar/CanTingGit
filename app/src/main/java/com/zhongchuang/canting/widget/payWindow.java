package com.zhongchuang.canting.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.GeguarAdapter;
import com.zhongchuang.canting.been.ProductBuy;
import com.zhongchuang.canting.been.Type;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * 功能描述:底部弹出的popupwindow
 * 作者:chenwei
 * 时间:2016/12/26
 * 版本:1.0
 ***/

public class payWindow extends PopupWindow {

    private View mView;
    private Context mContext;
    private TextView tv_exit;
    private TextView tv_cancel;
    private GeguarAdapter adapter;
    private ImageView image;
    private CheckBox cbWx;
    private CheckBox cbZfb;
    private CheckBox cbPp;
    private LinearLayout ll_wx;
    private LinearLayout ll_zfb;
    private LinearLayout ll_paypal;
    private ImageView close;
    private RegularListView rlMenu;
    private AddEditText add;
    private TextView tv_pay;
    private TextView sureButton;
     private int type=1;
    public payWindow(Activity context) {
        super(context);
        mContext = context;
        mView = View.inflate(context, R.layout.pay_popwindow_view, null);
        cbWx = (CheckBox)mView.findViewById(R.id.cb_wx);
        cbZfb = (CheckBox)mView.findViewById(R.id.cb_zfb);
        cbPp = (CheckBox)mView.findViewById(R.id.cb_paypal);
        ll_zfb = mView.findViewById(R.id.ll_zfb);
        ll_wx = mView.findViewById(R.id.ll_wx);
        tv_pay = mView.findViewById(R.id.tv_pay);
        ll_paypal = mView.findViewById(R.id.ll_paypal);
        close = mView.findViewById(R.id.close);
        sureButton = mView.findViewById(R.id.sure_button);

         sureButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.clickListener(0);
             }
         });
        ll_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.clickListener(3);
                type = 3;
                cbPp.setChecked(true);
                cbWx.setChecked(false);
                cbZfb.setChecked(false);
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.clickListener(2);
                cbPp.setChecked(false);
                type = 2;
                cbWx.setChecked(true);
                cbZfb.setChecked(false);
            }
        });
        ll_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickListener(1);

                type = 1;
                cbPp.setChecked(false);
                cbWx.setChecked(false);
                cbZfb.setChecked(true);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


    }



   public void setData(String money){
        tv_pay.setText(money);
   }

    public void setSureListener(ClickListener listener) {
        this.listener = listener;
    }

    private ClickListener listener;

    public interface ClickListener {
        void clickListener(int type);
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
        this.setHeight(DensityUtil.dip2px(520));
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
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha((Activity) mContext, 1f);
            }
        });

    }

    private List<List<Type>> data = new ArrayList<>();
    private List<List<Type>> datas = new ArrayList<>();


    private List<Type> dty;
    private List<ProductBuy> list;
    private int cout;


    public void initData() {


    }




    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showPopView(View imgs) {
        showAtLocation(imgs, Gravity.BOTTOM, 0, 0);
    }


}
