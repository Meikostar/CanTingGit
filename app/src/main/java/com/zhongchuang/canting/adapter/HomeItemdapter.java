package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class HomeItemdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HOMES> datas;
    private int type = 0;//0 表示默认使用list数据
    private String types;


    private int[] imgs;


    private String[] names;

    public HomeItemdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HOMES> list) {
        this.datas = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas!=null?2:0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
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
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.txt_unread = view.findViewById(R.id.txt_unread);
            holder.img_icon = view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(i==4){
            if(datas.get(i).cout!=0){
                holder.txt_unread.setVisibility(View.VISIBLE);
                if(datas.get(i).cout>99){
                    holder.txt_unread.setText(99+"+");
                }else {
                    holder.txt_unread.setText(datas.get(i).cout+"");
                }

            }else {
                holder.txt_unread.setVisibility(View.GONE);
            }
        }

        holder.txt_name.setText(datas.get(i).name);
        if(datas.get(i).url==0){
            Glide.with(context).load(StringUtil.changeUrl(datas.get(i).urls)).asBitmap().placeholder(R.drawable.mall1).into(holder.img_icon);

        }else {
            holder.img_icon.setImageResource(datas.get(i).url);
        }


//            if(TextUtil.isNotEmpty(list.get(i).category_name)){
//                holder.txt_name.setText(list.get(i).category_name);
//            }
////            holder.img_icon.setImageResource(imgs[i]);


        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView txt_name;
        TextView txt_unread;
        ImageView img_icon;


    }
}
