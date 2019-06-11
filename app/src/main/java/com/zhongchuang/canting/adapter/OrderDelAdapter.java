package com.zhongchuang.canting.adapter;

import android.content.Context;
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
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class OrderDelAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OrderData> foodlist;
    private String types;


    public void setDatas(List<OrderData> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public OrderDelAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public interface ItemBuyClick {
        void itemClik(int id);
    }

    private ItemBuyClick itemBuyClick;

    public void setListener(ItemBuyClick click) {
        itemBuyClick = click;

    }

    public List<OrderData> getData() {
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.choose_del_item_view, null);
            holder.pLogo = convertView.findViewById(R.id.img);
            holder.pName = convertView.findViewById(R.id.tv_name);
            holder.checkBox = convertView.findViewById(R.id.iv_choose);
            holder.llbg = convertView.findViewById(R.id.ll_bg);
            holder.line = convertView.findViewById(R.id.line);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(StringUtil.changeUrl(foodlist.get(i).picture_sku_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.pLogo);
        if(TextUtil.isNotEmpty(foodlist.get(i).pro_name)){
            holder.pName.setText(foodlist.get(i).pro_name);
        }


        if(foodlist.size()==1){
            holder.line.setVisibility(View.GONE);
            holder.checkBox.setChecked(true);
            foodlist.get(i).isChoose=true;
        }
       if(foodlist.get(i).isChoose){
            holder.checkBox.setChecked(true);
       }else {
           holder.checkBox.setChecked(false);
       }
        holder.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isCheck()){
                    foodlist.get(i).isChoose=false;
                    holder.checkBox.setChecked(false);
                }else {
                    foodlist.get(i).isChoose=true;
                    holder.checkBox.setChecked(true);
                }
//                itemBuyClick.itemClik(1);
            }
        });

//        holder.tvMoney.setText("￥"+foodlist.get(i).);
        return convertView;
    }
    private int type;
    public void setType(int type){
        this.type=type;
    }
    class ViewHolder {
        public ImageView pLogo;
        public TextView pName;
        public View line;
        public MCheckBox checkBox;

        public LinearLayout llbg;

    }
}
