package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.LiveItemBean;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;


public class ListVideoPopAdapter extends BaseAdapter {
    private Context mContext;
    private List<LiveItemBean> list;
    private int type=0;//1是用药记录item
    public ListVideoPopAdapter(Context mContext) {

        this.mContext = mContext;
    }

    public interface ItemCliks{
        void getItem(LiveItemBean menu, int type);//type 1表示点击事件2 表示长按事件
    }
    private ItemCliks listener;
    public void setClickListener(ItemCliks listener){
        this.listener=listener;
    }
    public void setData(List<LiveItemBean> list){
        this.list=list;
        notifyDataSetChanged();
    }


    public void setType(int type){
        this.type=type;

    }
    @Override
    public int getCount() {
        return list!=null?list.size():0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null){
            holder = new ResultViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_pop, parent, false);
            holder.data= view.findViewById(R.id.tv_data);


            view.setTag(holder);
        }else{
            holder = (ResultViewHolder) view.getTag();
        }
        if(TextUtil.isNotEmpty(list.get(position).category_name)){
            holder.data.setText(list.get(position).category_name);
        }else if(TextUtil.isNotEmpty(list.get(position).sec_category_name)){
            holder.data.setText(list.get(position).sec_category_name);
        }
        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getItem(list.get(position),position);
            }
        });
        return view;


    }

    public  class ResultViewHolder{

        TextView data;



    }
}
