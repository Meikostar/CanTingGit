package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class AppBasedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<apply> data;


    private String[] names;


    public AppBasedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<apply> data) {
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
        Basedapter  adapter = new Basedapter(context);
        holder.gird.setAdapter(adapter);
        adapter.setData(data.get(i).appList);
        holder.txt_name.setText(data.get(i).appTypeName);
        adapter.setOnItemClickListener(new Basedapter.OnItemClickListener() {
            @Override
            public void onItemClick(apply url) {
                mOnItemClickListener.onItemClick(url);
            }
        });

        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(apply url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    class ViewHolder {
        TextView txt_name;
        NoScrollGridView gird;

    }
}
