package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.OrderDetailActivity;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RegularListView;
import com.zhongchuang.canting.widget.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghouyang on 16/12/23.
 */

public class LiveCareAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Care> list;
    private int type;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;
    public interface selectItemListener{
        void delete(Care data, int poistion);
    }
    public void setListener(selectItemListener listener){
        this.listener=listener;
    }
    public void setData( List<Care> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public List<Care> getDatas(){
        return list;
    }
    public LiveCareAdapter(Context context) {

        this.context = context;

    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getCount() {
        return list!=null?(list.size()==0?0:list.size()):0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = convertView.inflate(context,R.layout.item_live_care, null);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.name = (TextView) convertView.findViewById(R.id.p_name);
            holder.dese = (TextView) convertView.findViewById(R.id.p_desc);
            holder.tvState1 = (TextView) convertView.findViewById(R.id.tv_state1);
            holder.tvState2 = (TextView) convertView.findViewById(R.id.tv_state2);
            holder.tvState3 = (TextView) convertView.findViewById(R.id.tv_state3);
            holder.img = (ImageView) convertView.findViewById(R.id.p_logo);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Care data=list.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.room_image)).asBitmap().placeholder(R.drawable.moren2).into(holder.img);
        if(TextUtil.isNotEmpty(data.is_enabled)){
            if(data.is_enabled.equals("1")){
                holder.tv_state.setText(R.string.zbz);
                holder.tv_state.setBackground(context.getResources().getDrawable(R.drawable.green_rectangle_lines));
                holder.tvState2.setBackground(context.getResources().getDrawable(R.drawable.selector_blue));

            }else {
                holder.tv_state.setBackground(context.getResources().getDrawable(R.drawable.black_line_rectangle_light));
                holder.tvState2.setBackground(context.getResources().getDrawable(R.drawable.selector_hui));
                holder.tv_state.setText(R.string.wzb);
            }

        }  if(data.favorite_time!=0){
            holder.dese.setText(context.getString(R.string.gzsjs)+ TimeUtil.formatTtimeNames(data.favorite_time));
        } if(TextUtil.isNotEmpty(data.direct_see_name)){
            holder.name.setText(data.direct_see_name);
        }
        holder.tvState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(data,1);
            }
        });
        holder.tvState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(data,2);
            }
        });
        holder.tvState3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(data,3);
            }
        });

        return convertView;
    }

    class ViewHolder {


        TextView tv_state;
        TextView dese;
        TextView name;
        ImageView img;
        TextView tvState1;
        TextView tvState2;
        TextView tvState3;
        ClearEditText etContent;


    }


}
