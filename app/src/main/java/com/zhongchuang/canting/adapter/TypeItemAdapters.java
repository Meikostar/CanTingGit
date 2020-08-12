package com.zhongchuang.canting.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.AreaDto;

import java.util.List;

public class TypeItemAdapters extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<AreaDto> list;


    public TypeItemAdapters(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<AreaDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<AreaDto> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.type_item_views, null);
            holder.name = view.findViewById(R.id.tv_name);
            holder.cb_type = view.findViewById(R.id.cb_type);
            holder.ll_bg = view.findViewById(R.id.ll_bg);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(list.get(i).check){
            holder.cb_type.setChecked(true);
        }else {
            holder.cb_type.setChecked(false);
        }

        if(!TextUtils.isEmpty(list.get(i).title)){
            holder.name.setText(list.get(i).title);
        }
        holder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemclick(i,list.get(i).id+"",list.get(i).title);


            }
        });
        return view;
    }



    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void itemclick(int poistion, String id,String name);
    }

    class ViewHolder {

        TextView type;
        TextView     name;
        CheckBox cb_type;
        LinearLayout ll_bg;


    }

}


