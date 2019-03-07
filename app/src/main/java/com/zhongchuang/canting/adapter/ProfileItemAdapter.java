package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.PROFILE_ITEM;

import java.util.ArrayList;

/**
 * Created by honghouyang on 16/12/15.
 */

public class ProfileItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<PROFILE_ITEM> list;

    public ProfileItemAdapter(Context context, ArrayList<PROFILE_ITEM> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    private int cout;
    public void setMessCout(int cout){
        this.cout=cout;
    }
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lp_griditem_profile_item, null);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);

            holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        PROFILE_ITEM item = list.get(i);
        holder.txt_name.setText(item.item_name);
        holder.img_icon.setImageResource(item.item_resource);
        return view;
    }

    class ViewHolder {
        TextView txt_name;

        ImageView img_icon;
    }
}
