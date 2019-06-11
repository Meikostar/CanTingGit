package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Type;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class ChatBgAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Type> list;


    public ChatBgAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Type> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<Type> getData() {
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
            view = inflater.inflate(R.layout.item_chat_bg, null);
            holder.img = view.findViewById(R.id.iv_img);

            holder.llchoose = view.findViewById(R.id.ll_choose);
            holder.card = view.findViewById(R.id.card);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (list.get(i).isChoose) {
            holder.llchoose.setVisibility(View.VISIBLE);
        } else {
            holder.llchoose.setVisibility(View.GONE);
        }

//        if(i==0){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg0));
//        }else if(i==1){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg1));
//        }else if(i==2){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg2));
//        }else if(i==3){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg3));
//        }else if(i==4){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg4));
//        }else if(i==5){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg5));
//        }else if(i==6){
//            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.bg6));
//        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              for(int j=0;j<list.size();j++){
                  list.get(j).isChoose = j == i;
              }
              notifyDataSetChanged();
            }
        });
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
        ImageView img;
        LinearLayout llchoose;
        CardView card;


    }

}
