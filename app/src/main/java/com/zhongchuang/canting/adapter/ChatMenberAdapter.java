package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.CircleTransform;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class ChatMenberAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GAME> foodlist;
    private String types;


    public void setDatas(List<GAME> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public ChatMenberAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<GAME> getFoodlist() {
        return foodlist;
    }

    @Override
    public int getCount() {
        return foodlist != null ? foodlist.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return foodlist.get(i);
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
            view = inflater.inflate(R.layout.chat_menber_adapter, null);

            holder.txt_text = view.findViewById(R.id.tv_text);
            holder.iv_img = view.findViewById(R.id.iv_img);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (i == foodlist.size() - 1) {
            holder.txt_text.setText(context.getString(R.string.add));
            holder.iv_img.setImageResource(R.drawable.chat_add);
        } else {
            if (!TextUtils.isEmpty(foodlist.get(i).chatGroupName)) {
                if(!TextUtils.isEmpty(foodlist.get(i).groupBackImage)){
                    Glide.with(context).load(StringUtil.changeUrl(foodlist.get(i).groupBackImage)).asBitmap().transform(new CircleTransform(context)).placeholder(R.drawable.chats1).into(holder.iv_img);
                }else {
                    if (foodlist.get(i).chatGroupName.equals(context.getString(R.string.jiaren))) {
                        holder.iv_img.setImageResource(R.drawable.chats1);
                    } else if (foodlist.get(i).chatGroupName.equals(context.getString(R.string.pengyou))) {
                        holder.iv_img.setImageResource(R.drawable.chats2);
                    } else if (foodlist.get(i).chatGroupName.equals(context.getString(R.string.tongshi))) {
                        holder.iv_img.setImageResource(R.drawable.chats3);
                    } else {
                        if (i % 3 == 0) {
                            holder.iv_img.setImageResource(R.drawable.chats1);
                        } else if (i % 3 == 1) {
                            holder.iv_img.setImageResource(R.drawable.chats2);
                        } else if (i % 3 == 2) {
                            holder.iv_img.setImageResource(R.drawable.chats3);
                        }
                    }
                }

                holder.txt_text.setText(foodlist.get(i).chatGroupName);
            }


        }


        return view;
    }

    public class ViewHolder {
        TextView txt_text;
        ImageView iv_img;

    }
}
