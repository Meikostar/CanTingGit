package com.zhongchuang.canting.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;


/**
 * Created by mykar on 17/4/12.
 */
public class ShopViewHolder extends RecyclerView.ViewHolder  {
    public TextView name;
    public TextView tv_adress;
    public ImageView img;
    public TextView tvName;
    public TextView tv_count;
    public TextView tvNum;
    public TextView tvDetail;
    public TextView tvMoney;
    public TextView tvCont;
    public TextView tv_type;
    public ImageView ivAdd;
    public LinearLayout llBg;
    public ShopViewHolder(View itemView) {
        super(itemView);
        img= (ImageView) itemView.findViewById(R.id.img);
        tv_type= (TextView) itemView.findViewById(R.id.tv_type);
        tvName= (TextView) itemView.findViewById(R.id.tv_name);
        tv_count= (TextView) itemView.findViewById(R.id.tv_count);
        tvNum= (TextView) itemView.findViewById(R.id.tv_num);
        tvDetail= (TextView) itemView.findViewById(R.id.tv_detail);
        tvMoney= (TextView) itemView.findViewById(R.id.tv_money);
        tvCont= (TextView) itemView.findViewById(R.id.tv_cont);
        llBg= (LinearLayout) itemView.findViewById(R.id.ll_bg);

    }
}
