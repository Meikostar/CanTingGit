package com.zhongchuang.canting.widget.popupwindow;

import android.app.Activity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhongchuang.canting.R;


public class PopView_UploadImg extends BasePopView {

    public TextView txt_progress;
    public AppCompatSeekBar seekBar;

    public PopView_UploadImg(Activity activity) {
        super(activity);
    }


    @Override
    protected View initPopView(LayoutInflater infalter) {
        View popView = infalter.inflate(R.layout.popview_upload_img, null);
        txt_progress = popView.findViewById(R.id.txt_progress);
        seekBar= popView.findViewById(R.id.seekbar);
        return popView;
    }
    public void setData(int current,int max){
        txt_progress.setText(current+"/"+max);
        seekBar.setMax(max);
        seekBar.setProgress(current);
    }

    @Override
    public void onClick(View view) {

    }

}
