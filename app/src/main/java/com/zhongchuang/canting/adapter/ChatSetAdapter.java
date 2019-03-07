package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class ChatSetAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GAME> foodlist;
    private String types;




    public void setDatas( List<GAME> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public ChatSetAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public interface ItemBuyClick{
        void itemClik(int type,GAME id);
    }
    private ItemBuyClick itemBuyClick;
    public void setListener(ItemBuyClick click){
        itemBuyClick=click;

    }
     public List<GAME> getFoodlist(){
         return foodlist;
     }

    @Override
    public int getCount() {
        return foodlist!=null?foodlist.size():0;
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
            view = inflater.inflate(R.layout.item_chat_setting, null);
            holder.tvSetting =view.findViewById(R.id.tv_setting);
            holder.tvUp =view.findViewById(R.id.tv_up);
            holder.tvDown =view.findViewById(R.id.tv_down);
            holder.tvTitle =view.findViewById(R.id.tv_title);
            holder.ll_bg =view.findViewById(R.id.ll_bg);
            holder.tvUp.setTag(foodlist.get(i));
            holder.tvDown.setTag(foodlist.get(i));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final   GAME game = foodlist.get(i);
        if(i==0){
            holder.tvDown.setTextColor(context.getResources().getColor(R.color.color6));
            holder.tvUp.setTextColor(context.getResources().getColor(R.color.color9));
            holder.tvDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GAME tag = (GAME) v.getTag();
                    itemBuyClick.itemClik(2,tag);
                }
            });
            holder.tvUp.setClickable(false);
            holder.tvUp.setOnClickListener(null);
            holder.tvDown.setClickable(true);
        }else if(i==foodlist.size()-1){
            holder.tvDown.setTextColor(context.getResources().getColor(R.color.color9));
            holder.tvUp.setTextColor(context.getResources().getColor(R.color.color6));
            holder.tvUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GAME tag = (GAME) v.getTag();
                    itemBuyClick.itemClik(1,tag);
                }
            });
            holder.tvDown.setClickable(false);
            holder.tvDown.setOnClickListener(null);
            holder.tvUp.setClickable(true);
        }else {
            holder.tvDown.setClickable(true);
            holder.tvUp.setClickable(true);
            holder.tvDown.setTextColor(context.getResources().getColor(R.color.color6));
            holder.tvUp.setTextColor(context.getResources().getColor(R.color.color6));
            holder.tvUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GAME tag = (GAME) v.getTag();
                    itemBuyClick.itemClik(1,tag);
                }
            });

            holder.tvDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GAME tag = (GAME) v.getTag();
                    itemBuyClick.itemClik(2,tag);
                }
            });
        }
        holder.tvSetting.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  itemBuyClick.itemClik(3,game);
              }
          });


         if(TextUtil.isNotEmpty(foodlist.get(i).chatGroupName)){
             holder.tvTitle.setText(foodlist.get(i).chatGroupName);
         }
        return view;
    }

   public
    class ViewHolder {
        TextView tvSetting;
        TextView tvUp;
        TextView tvDown;
        TextView tvTitle;

        LinearLayout ll_bg;
    }
}
