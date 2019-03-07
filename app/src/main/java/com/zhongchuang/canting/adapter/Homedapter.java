package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Homedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Banner> list;
    private int type = 0;//0 表示默认使用list数据
    private String types;


    private int[] imgs;


    private String[] names;

    public Homedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Banner> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
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
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }



            if(TextUtil.isNotEmpty(list.get(i).category_name)){
                holder.txt_name.setText(list.get(i).category_name);
            }
        Glide.with(context).load(StringUtil.changeUrl(list.get(i).category_image)).asBitmap().placeholder(R.drawable.mall1).into(holder.img_icon);
//            holder.img_icon.setImageResource(imgs[i]);


        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView txt_name;
        ImageView img_icon;

    }
}
