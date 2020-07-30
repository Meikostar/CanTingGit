package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class OrderChildAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OrderData> foodlist;
    private String types;


    public void setDatas(List<OrderData> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public OrderChildAdapter(Context context) {
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

    public List<OrderData> getFoodlist() {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.orderchild_item, null);
            holder.pLogo = convertView.findViewById(R.id.p_logo);
            holder.pName = convertView.findViewById(R.id.p_name);
            holder.pDesc = convertView.findViewById(R.id.p_desc);
            holder.tvMoney = convertView.findViewById(R.id.tv_money);
            holder.tvCout = convertView.findViewById(R.id.tv_cout);
            holder.rlbg = convertView.findViewById(R.id.rl_bg);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(StringUtil.changeUrl(foodlist.get(i).picture_sku_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.pLogo);
        if(TextUtil.isNotEmpty(foodlist.get(i).proSku)){
            holder.pDesc.setText(foodlist.get(i).proSku);
        } if(TextUtil.isNotEmpty(foodlist.get(i).pro_name)){
            holder.pName.setText(foodlist.get(i).pro_name);
        }if(TextUtil.isNotEmpty(foodlist.get(i).number)){
            holder.tvCout.setText("x"+foodlist.get(i).number);
        }else {
            if(TextUtil.isNotEmpty(foodlist.get(i).prod_number)){
                holder.tvCout.setText("x"+foodlist.get(i).prod_number);
            }

        }
//        if(type==1){
//            holder.tvMoney.setText("￥"+foodlist.get(i).pro_price+(TextUtil.isNotEmpty(foodlist.get(i).integral_price)?("+"+foodlist.get(i).integral_price+context.getString(R.string.jf)):""));
//        }else {
            if(TextUtil.isNotEmpty(foodlist.get(i).pro_site)){
                if(foodlist.get(i).pro_site.equals("1")){
                    if(TextUtil.isNotEmpty(foodlist.get(i).integral_price)){
                        if(Double.valueOf(foodlist.get(i).integral_price)>0){
                            holder.tvMoney.setText("￥"+foodlist.get(i).pro_price+("+"+foodlist.get(i).integral_price+context.getString(R.string.jf)));
                        }else {
                            holder.tvMoney.setText("￥"+foodlist.get(i).pro_price);
                        }
                    }else {
                        holder.tvMoney.setText("￥"+foodlist.get(i).pro_price);
                    }

                }else  if(foodlist.get(i).pro_site.equals("3")){
                    if(TextUtil.isNotEmpty(foodlist.get(i).integral_price)){
                        if(Double.valueOf(foodlist.get(i).integral_price)>0){
                            holder.tvMoney.setText("￥"+foodlist.get(i).pro_price+("+"+"兑换值"+foodlist.get(i).integral_price));
                        }else {
                            holder.tvMoney.setText("￥"+foodlist.get(i).pro_price);
                        }
                    }else {
                        holder.tvMoney.setText("￥"+foodlist.get(i).pro_price);
                    }

                }  else {
                    holder.tvMoney.setText("兑换值"+foodlist.get(i).integral_price);
                }
//            }
//            if(TextUtil.isNotEmpty(foodlist.get(i).integral_price)){
//                holder.tvMoney.setText(context.getString(R.string.jf)+foodlist.get(i).integral_price);
//            }else {
//                holder.tvMoney.setText(context.getString(R.string.jf)+foodlist.get(i).market_price);
//            }

        }
      holder.rlbg.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
              itemBuyClick.itemClik(1);
              return true;
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
        public TextView pDesc;
        public TextView tvMoney;
        public TextView tvCout;
        public RelativeLayout rlbg;

    }
}
