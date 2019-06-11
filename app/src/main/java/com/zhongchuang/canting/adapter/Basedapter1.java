package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.HOMES;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Basedapter1 extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> list;
    private int type = 0;//0 表示默认使用list数据
    private String types;

    private int[] homeimgs ;

    private int[] imgs;


    private String[] names;

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setTypes(String types) {
        this.types = types;
        notifyDataSetChanged();
    }
    private int couts;
    public void setCount(int couts){
        this.couts=couts;
        notifyDataSetChanged();
    }
    public Basedapter1(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    private List<HOMES> datas;
    public void setData(List<HOMES> datas) {

        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas!=null?datas.size():0;
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
            view = View.inflate(context,R.layout.tools_item, null);
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
            imgs = homeimgs;
            holder.txt_name.setText(datas.get(i).name);
            holder.img_icon.setImageResource(datas.get(i).url);


        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView txt_name;
        TextView txt_unread;
        ImageView img_icon;

    }
}
