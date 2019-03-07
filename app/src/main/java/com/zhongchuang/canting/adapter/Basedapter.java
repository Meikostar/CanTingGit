package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.recycle.GoodRecyAdapter;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Basedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<apply> data;
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

    public Basedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<apply> data) {
       this.data=data;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            holder.llbg = (LinearLayout) view.findViewById(R.id.ll_bg);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(TextUtil.isNotEmpty(data.get(i).application_image_name)){
            holder.txt_name.setText(data.get(i).application_image_name);
        }
        Glide.with(context).load(StringUtil.changeUrl(data.get(i).application_image_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.img_icon);

        holder.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(data.get(i));
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
        LinearLayout llbg;
        TextView txt_name;
        ImageView img_icon;

    }
}
