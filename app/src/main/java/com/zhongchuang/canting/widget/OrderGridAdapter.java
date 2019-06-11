package com.zhongchuang.canting.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;


public class OrderGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<GAME> list;


    private Boolean aBoolean=false;

    private String[] content;
    public OrderGridAdapter(Context mContext) {

        this.mContext = mContext;
        if(list!=null){
            list.clear();
        }

    }
    public List<GAME> getList(){
        return list;
    }
    public void setLists(List<GAME> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

         return list == null ?0: list.size();
    }

    @Override
    public GAME getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ResultViewHolder holder;
            if (view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.grid_text_view, parent, false);
                holder = new ResultViewHolder();
                holder.name = view.findViewById(R.id.tv_name);


                view.setTag(holder);
            }else{
                holder = (ResultViewHolder) view.getTag();
            }


                if(TextUtil.isNotEmpty(list.get(position).chatGroupName)){
                    holder.name.setText(list.get(position).chatGroupName);
                }else {
                    if(TextUtil.isNotEmpty(list.get(position).directTypeName)){
                        holder.name.setText(list.get(position).directTypeName);
                    }

                }

            if(list.get(position).isChoose){
                holder.name.setTextColor(mContext.getResources().getColor(R.color.blue));
            }else {
                holder.name.setTextColor(mContext.getResources().getColor(R.color.slow_black));

            }

            return view;


    }

    public static class ResultViewHolder{
        TextView name;

    }
}
