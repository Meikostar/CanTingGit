package com.zhongchuang.canting.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Smgapply;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.List;

public class AppBasedapterSmg extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<Smgapply>    data;


    private String[] names;


    public AppBasedapterSmg(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Smgapply> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
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
            view = inflater.inflate(R.layout.app_store_item, null);
            holder.txt_name = view.findViewById(R.id.tv_type);
            holder.gird = view.findViewById(R.id.grid_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BasedapterSmg  adapter = new BasedapterSmg(context);
        holder.gird.setAdapter(adapter);
        adapter.setData(data.get(i).apps);
        holder.txt_name.setText(data.get(i).title);
        adapter.setOnItemClickListener(new BasedapterSmg.OnItemClickListener() {
            @Override
            public void onItemClick(Smgapply url) {
                    mOnItemClickListener.onItemClick(url);
            }
        });

        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(Smgapply url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    class ViewHolder {
        TextView         txt_name;
        NoScrollGridView gird;

    }
}
