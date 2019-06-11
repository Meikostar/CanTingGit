package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Type;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class TypeAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Type> list;


    public TypeAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Type> list) {
        this.list = list;
        notifyDataSetChanged();
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
            view = inflater.inflate(R.layout.type_adapter, null);
            holder.tvName = view.findViewById(R.id.tv_name);
            holder.line = view.findViewById(R.id.line);
            holder.rlBg = view.findViewById(R.id.rl_bg);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(list.get(i).isChoose){
            holder.rlBg.setBackgroundResource(R.color.white);
            holder.line.setVisibility(View.VISIBLE);
        }else {
            holder.line.setVisibility(View.INVISIBLE);
            holder.rlBg.setBackgroundResource(R.color.content_bg);
        }
        holder.rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemclick(i,list.get(i).id);
                for (int a = 0; a < list.size(); a++) {
                    list.get(a).isChoose = a == i;
                }
                notifyDataSetChanged();

            }
        });
        holder.tvName.setText(list.get(i).cont);
        return view;
    }

    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void itemclick(int poistion,String id);
    }

    class ViewHolder {
        View line;
        TextView tvName;
        RelativeLayout rlBg;


    }

}
