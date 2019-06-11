package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class HomessBasedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HOMES> datas;
    private int type = 0;//0 表示默认使用list数据
    private String types;



    private String[] names;

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setTypes(String types) {
        this.types = types;
        notifyDataSetChanged();
    }

    public HomessBasedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HOMES> data) {
       this.datas=data;
       notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas!=null?datas.size():0;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.homes_item, null);
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.tv_count = view.findViewById(R.id.tv_count);
            holder.llbg = view.findViewById(R.id.ll_bg);
            holder.cardView = view.findViewById(R.id.card);
            holder.img_icon = view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(datas.get(i).isread){
            if(datas.get(i).cout!=0){
                holder.tv_count.setVisibility(View.VISIBLE);
                if(datas.get(i).cout>99){
                    holder.tv_count.setText(99+"+");
                }else {
                    holder.tv_count.setText(datas.get(i).cout+"");
                }

            }else {
                holder.tv_count.setVisibility(View.GONE);
            }
        }

        holder.txt_name.setText(datas.get(i).name);
        holder.img_icon.setImageResource(datas.get(i).url);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(datas.get(i));
            }
        });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(HOMES url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder {
        LinearLayout llbg;
        CardView cardView;
        TextView txt_name;
        TextView tv_count;
        ImageView img_icon;

    }
}
