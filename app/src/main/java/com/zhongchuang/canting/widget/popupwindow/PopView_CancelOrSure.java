package com.zhongchuang.canting.widget.popupwindow;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongchuang.canting.R;;

/**
 * Created by xzh on 15/8/14.
 */
public class PopView_CancelOrSure extends BasePopView {
    public static final int CANCEL = 0;
    public static final int SURE = 1;
    public TextView txt_title;
    public TextView txt_cancel;
    public TextView txt_sure;
    private View line;
    public OnPop_CancelOrSureLister lister;

    public PopView_CancelOrSure(Activity activity) {
        super(activity);
    }



    public interface OnPop_CancelOrSureLister {
        void choose4Sure();
    }

    public void setOnPop_CancelOrSureLister(OnPop_CancelOrSureLister lister) {
        this.lister = lister;
    }


    @Override
    protected View initPopView(LayoutInflater infalter) {
        View v = infalter.inflate(R.layout.op_circular_dialog, null);
        txt_title = (TextView) v.findViewById(R.id.txt_title);
        txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);
        txt_sure = (TextView) v.findViewById(R.id.txt_sure);
        line = v.findViewById(R.id.line);

        txt_cancel.setOnClickListener(this);
        txt_sure.setOnClickListener(this);
        return v;
    }
    public void setCancelGone(){
        txt_cancel.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
    }
    public void setTitle(String txt) {
        txt_title.setText(txt);
    }

    public void setTxtSure(String txt) {
        txt_sure.setText(txt);
    }

    public void setPositive(String positive) {
        txt_sure.setText(positive);
    }

    public void setGone(int type) {
        switch (type) {
            case CANCEL:
                txt_cancel.setVisibility(View.GONE);
                break;
            case SURE:
                txt_sure.setVisibility(View.GONE);
                break;
        }
    }

    public void setActivityFinish(){
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activity.finish();
            }
        });
    }
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.txt_cancel:
                    dismiss();
                    break;
                case R.id.txt_sure:
                    lister.choose4Sure();
                    dismiss();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

