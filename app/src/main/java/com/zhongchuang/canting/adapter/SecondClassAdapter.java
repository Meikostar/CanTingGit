package com.zhongchuang.canting.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.SecondClassItem;

import java.util.List;

/**
 * 二级分类（即右侧菜单）的adapter
 * Created by hanj on 14-9-25.
 */
public class SecondClassAdapter extends BaseAdapter {
    private Context context;
    private List<SecondClassItem> list;

    public SecondClassAdapter(Context context, List<SecondClassItem> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.right_listview_item, null);
            holder.nameTV = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_choose = (ImageView) convertView.findViewById(R.id.iv_choose);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTV.setText(list.get(position).getName());
        if (position == selectedPosition){
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.my_color_blue));
            holder.iv_choose.setVisibility(View.VISIBLE);
        }else{
            holder.iv_choose.setVisibility(View.GONE);
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.my_color_212121));
        }
        return convertView;
    }

    private class ViewHolder{
        TextView nameTV;
        ImageView iv_choose;
    }
}

