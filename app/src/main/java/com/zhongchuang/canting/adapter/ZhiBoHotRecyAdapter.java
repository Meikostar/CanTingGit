package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CircleTransform;

import java.util.List;


/**
 * Created by Administrator on 2017/11/8.
 */

public class ZhiBoHotRecyAdapter extends RecyclerView.Adapter<ZhiBoHotRecyAdapter.ViewHolder> {

    private Context hotContext;
    private List<ZhiBo_GuanZhongBean.DataBean> roomList;

    public ZhiBoHotRecyAdapter(Context hotContext, List<ZhiBo_GuanZhongBean.DataBean> roomList ) {
        this.hotContext = hotContext;
        this.roomList = roomList;

    }


    public void setData(List<ZhiBo_GuanZhongBean.DataBean> list){
        roomList.clear();
        roomList.addAll(list);
        notifyDataSetChanged();
    }
    //新建两个内部接口：
    public interface OnItemClickListener{
        void onItemClick(View view,int position,ZhiBo_GuanZhongBean.DataBean url);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }
    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
            /* 条目监听事件* */



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewH = LayoutInflater.from(hotContext).inflate(R.layout.zhibo_hot_item, parent, false);
        ViewHolder holder = new ViewHolder(viewH);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holders, int position) {
       final ViewHolder holder=holders;
        final ZhiBo_GuanZhongBean.DataBean dataBean = roomList.get(position);

        Glide.with(hotContext).load(StringUtil.changeUrl(dataBean.room_image)).asBitmap().placeholder(R.drawable.moren).into(holder.hotPic);

        //直播昵称
        //String nicName=liveNicName.get(position);
        if(TextUtil.isNotEmpty(dataBean.direct_see_name)){
            holder.tv_name.setText(dataBean.direct_see_name);
        }else {
            holder.tv_name.setText("");
        }
        if(dataBean.is_enabled==0){
            holder.iv_bg.setBackground(hotContext.getResources().getDrawable(R.drawable.hui_rectangle));
        }else {
            holder.iv_bg.setBackground(null);
        }
        if(TextUtil.isNotEmpty(dataBean.fans_num)){
            holder.tv_count.setText(dataBean.fans_num);
        }
        if(TextUtil.isNotEmpty(dataBean.direct_overview)){
            holder.tv_desc.setText(dataBean.direct_overview);
        }else {
            holder.tv_desc.setText("");
        }



        //Log.d(TAG, "onBindViewHolder: "+desName);
        String onlineNumber = dataBean.getOnlineNumber();
        if(!TextUtils.isEmpty(onlineNumber)&&!onlineNumber.equals("null")){
            holder.tv_count.setText("" +onlineNumber  + hotContext.getString(R.string.ren));
        }




        /**如果设置了回调，则设置点击事件*/

            //为ItemView设置监听器
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position,dataBean); // 2
                }
            });

        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return roomList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotPic;
        ImageView iv_bg;

        TextView tv_name;
        TextView tv_desc;
        TextView tv_count;
        RelativeLayout rl_bg;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            hotPic = itemView.findViewById(R.id.hot_pic);
            iv_bg = itemView.findViewById(R.id.iv_bg);
            cardView = itemView.findViewById(R.id.card);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_count = itemView.findViewById(R.id.tv_count);
            rl_bg = itemView.findViewById(R.id.rl_bg);

        }
    }




}
