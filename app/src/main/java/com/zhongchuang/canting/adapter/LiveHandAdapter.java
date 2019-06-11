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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.OrderDetailActivity;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RegularListView;
import com.zhongchuang.canting.widget.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghouyang on 16/12/23.
 */

public class LiveHandAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Hand> list;
    private int type;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;
    public interface selectItemListener{
        void delete(Hand data, int poistion);
    }
    public void setListener(selectItemListener listener){
        this.listener=listener;
    }
    public void setData( List<Hand> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public List<Hand> getDatas(){
        return list;
    }
    public LiveHandAdapter(Context context) {

        this.context = context;

    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getCount() {
        return list!=null?(list.size()):0;
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
            convertView = View.inflate(context,R.layout.item_live_hand, null);
            holder.tv_care_cout = convertView.findViewById(R.id.tv_care_cout);
            holder.tv_care = convertView.findViewById(R.id.tv_care);
            holder.name = convertView.findViewById(R.id.p_name);
            holder.tv_count = convertView.findViewById(R.id.tv_count);
            holder.img = convertView.findViewById(R.id.p_logo);
            holder.img_bg = convertView.findViewById(R.id.p_logo1);


            holder.llBg = convertView.findViewById(R.id.rl_bg);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Hand data=list.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.room_image)).asBitmap().placeholder(R.drawable.moren2).into(holder.img);
        if(TextUtil.isNotEmpty(data.direct_see_name)){
            holder.name.setText(data.direct_see_name);
        } if(TextUtil.isNotEmpty(data.fans_num)){
            holder.tv_care_cout.setText(data.fans_num);
        }
        if(position==0){
            holder.img_bg.setVisibility(View.VISIBLE);
            holder.img_bg.setImageDrawable(context.getResources().getDrawable(R.drawable.hand1));
            holder.tv_count.setTextColor(context.getColor(R.color.white));
        }else if(position==1){
            holder.img_bg.setVisibility(View.VISIBLE);
            holder.img_bg.setImageDrawable(context.getResources().getDrawable(R.drawable.hand2));
            holder.tv_count.setTextColor(context.getColor(R.color.white));
        }else if(position==2){
            holder.img_bg.setImageDrawable(context.getResources().getDrawable(R.drawable.hand3));
            holder.tv_count.setTextColor(context.getColor(R.color.white));
            holder.img_bg.setVisibility(View.VISIBLE);
        }else {
            holder.img_bg.setVisibility(View.GONE);
            holder.tv_count.setTextColor(context.getColor(R.color.slow_black));

        }
        holder.tv_count.setText((position+1)+"");
        if(!TextUtil.isEmpty(SpUtil.getUserInfoId(CanTingAppLication.getInstance()))){
           if( SpUtil.getUserInfoId(CanTingAppLication.getInstance()).equals(data.anchors_id)){
                holder.tv_care.setVisibility(View.GONE);
            }else {
               holder.tv_care.setVisibility(View.VISIBLE);
           }
        }

        if(TextUtil.isNotEmpty(data.favorite_type)){
            if(data.favorite_type.equals("1")){
                holder.tv_care.setText(R.string.qxgz);
            }else if(data.favorite_type.equals("2")){
                holder.tv_care.setText(R.string.gz);
            }else if(data.favorite_type.equals("0")){
                holder.tv_care.setText(R.string.gz);
            }
        }

       holder.tv_care.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.delete(data,0);
           }
       });



        return convertView;
    }

    class ViewHolder {



        TextView tv_care_cout;
        TextView tv_care;
        TextView name;
        ImageView img_bg;
        ImageView img;
        TextView tv_count;

        RelativeLayout llBg;
    }


}
