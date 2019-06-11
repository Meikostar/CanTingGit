package com.zhongchuang.canting.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;


public abstract class HorizontalScrollViewAdapter<T> {
    private LayoutInflater mInflater;
    List<T> mDatas;
    Context context;

    public HorizontalScrollViewAdapter(Context context, List<T> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.context = context;

    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {

        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_list_item, parent, false);
            viewHolder.textView = convertView.findViewById(R.id.text_list_item);
            viewHolder.img = convertView.findViewById(R.id.img_list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        T shopDetails = mDatas.get(position);
        setTvData(shopDetails,viewHolder.textView,viewHolder.img);


//        viewHolder.textView.setVisibility(View.GONE);
//
//        Glide.with(context).load(shopDetails.getCat_name()).placeholder(R.drawable.ic_error).error(R.drawable.ic_error).centerCrop().into(viewHolder.img);
        return convertView;
    }
    private class ViewHolder {
        public TextView textView;
        public ImageView img;
    }

    public abstract void setTvData(T t, TextView tv, ImageView iv);

}