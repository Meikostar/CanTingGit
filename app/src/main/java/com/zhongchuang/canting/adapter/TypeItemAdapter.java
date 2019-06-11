package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Param;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class TypeItemAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Param> list;


    public TypeItemAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Param> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Param> getData() {
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
            view = inflater.inflate(R.layout.type_item_view, null);
            holder.name = view.findViewById(R.id.tv_name);
            holder.type = view.findViewById(R.id.tv_type);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(TextUtil.isNotEmpty(list.get(i).function_attribute_name)){
            holder.name.setText(list.get(i).function_attribute_name);
        }
        if(TextUtil.isNotEmpty(list.get(i).function_attribute_value)){
            holder.type.setText(list.get(i).function_attribute_value);
        }
        return view;
    }

    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void itemclick(int poistion, String id);
    }

    class ViewHolder {

        TextView type;
        TextView name;


    }

}
