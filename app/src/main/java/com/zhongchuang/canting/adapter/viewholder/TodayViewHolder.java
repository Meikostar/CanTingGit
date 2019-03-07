package com.zhongchuang.canting.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.widget.GoodsCommentListView;
import com.zhongchuang.canting.widget.PraiseTextView;
import com.zhongchuang.canting.widget.TodayNewGoodsDrawDownView;


/***
 * 功能描述:RecycleViewAdapter ViewHolder
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/

public abstract class TodayViewHolder extends RecyclerView.ViewHolder {



    public final static int TYPE_IMAGE = 0;


    public int viewType;

    public ImageView iv_shop_head;

    public TextView tv_shop_name;
    public PraiseTextView mPraiseTextView;

    public TextView tv_good_name;
    public TextView tv_count;

    public Button btn_concern;

    public RelativeLayout rl_down_show_area;

    public ImageView iv_down_show;
    public ImageView   iv_prases;
    public LinearLayout iv_comment;
    public LinearLayout iv_prase;

    public TextView tv_titel;

    public TextView tv_time;


    public TextView tv_type;

    public LinearLayout rl_qf1;
    public LinearLayout rl_qf2;

    public TextView tv_address;


    public GoodsCommentListView lv_comments;

    public TodayNewGoodsDrawDownView mTodayNewGoodsDrawDownView;

    public abstract void initSubView(int viewType, ViewStub viewStub);

    public TodayViewHolder(View itemView, int viewType)
    {
        super(itemView);

        this.viewType=viewType;

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.viewStub);

        initSubView(viewType, viewStub);

        iv_shop_head=(ImageView)itemView.findViewById(R.id.iv_shop_head);
        iv_prase=(LinearLayout)itemView.findViewById(R.id.iv_prase);
        iv_prases=(ImageView)itemView.findViewById(R.id.iv_prases);
        iv_comment=(LinearLayout)itemView.findViewById(R.id.iv_comment);

        tv_shop_name=(TextView) itemView.findViewById(R.id.tv_shop_name);

        mPraiseTextView=(PraiseTextView) itemView.findViewById(R.id.praise);

        tv_good_name=(TextView) itemView.findViewById(R.id.tv_good_name);
        tv_count=(TextView) itemView.findViewById(R.id.tv_count);

        btn_concern=(Button)itemView.findViewById(R.id.btn_concern);

        rl_down_show_area=(RelativeLayout)itemView.findViewById(R.id.rl_down_show_area);

        iv_down_show=(ImageView)itemView.findViewById(R.id.iv_down_show);

        tv_titel=(TextView)itemView.findViewById(R.id.tv_titel);

        tv_time=(TextView)itemView.findViewById(R.id.tv_time);
        tv_type= (TextView) itemView.findViewById(R.id.tv_type);
        rl_qf1= (LinearLayout) itemView.findViewById(R.id.ll_qf1);


        tv_address= (TextView) itemView.findViewById(R.id.tv_address);





        lv_comments=(GoodsCommentListView)itemView.findViewById(R.id.lv_comments);

        mTodayNewGoodsDrawDownView=new TodayNewGoodsDrawDownView(itemView.getContext());

    }



}
