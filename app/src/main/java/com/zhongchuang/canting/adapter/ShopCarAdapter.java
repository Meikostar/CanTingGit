package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.AddEditText;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ShopCarAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> shopList;


    public ShopCarAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<Product> list) {
        shopList = list;

        notifyDataSetChanged();
    }

    public List<Product> getData( ) {
       return  shopList;
    }
    @Override
    public int getCount() {
        return shopList != null ? shopList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
       final ShopCarHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.shopcar_item, null);
            holder = new ShopCarHolder();
            holder.ivchoose = view.findViewById(R.id.iv_choose);
            holder.p_logo = view.findViewById(R.id.p_logo);
            holder.p_name = view.findViewById(R.id.p_name);
            holder.p_desc = view.findViewById(R.id.p_desc);
            holder.p_price = view.findViewById(R.id.p_price);
            holder.card = view.findViewById(R.id.card);
            holder.add = view.findViewById(R.id.add);
            view.setTag(holder);

        } else {
            holder = (ShopCarHolder) view.getTag();
        }
        holder.add.setListener(new AddEditText.ChangeListener() {
            @Override
            public void change(String name) {
                shopList.get(position).number=name;
                checkListener.checks(null);
            }
        });
        if(TextUtil.isNotEmpty(shopList.get(position).number)){
            holder.add.setTexts(shopList.get(position).number);
        }else {
            holder.add.setTexts(1+"");
        }

        String[] splits = shopList.get(position).picture_url.split(",");
        Glide.with(mContext).load(StringUtil.changeUrl(shopList.get(position).product_sku_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.p_logo);
        if (TextUtil.isNotEmpty(shopList.get(position).pro_name)) {
            holder.p_name.setText(shopList.get(position).pro_name);
        }
        if (shopList.get(position).pro_site.equals("1")) {
            holder.p_price.setText("￥ "+shopList.get(position).pro_price);
        }else  if (shopList.get(position).pro_site.equals("3")) {
            holder.p_price.setText("￥ "+shopList.get(position).pro_price);
        }else  {
            holder.p_price.setText("积分 "+shopList.get(position).integral_price);
        }
         if (TextUtil.isNotEmpty(shopList.get(position).proSku)) {
            holder.p_desc.setText(shopList.get(position).proSku);
        }

        if (shopList.get(position).isChoose) {
            holder.ivchoose.setChecked(true);
        }else {
            holder.ivchoose.setChecked(false);
        }
       holder.ivchoose.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(!holder.ivchoose.isCheck()){
                   shopList.get(position).isChoose=true;
                   holder.ivchoose.setChecked(true);
               }else {
                   shopList.get(position).isChoose=false;
                   holder.ivchoose.setChecked(false);
               }
               checkListener.checks(null);
           }
       });
         holder.card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkListener.checks(shopList.get(position));
             }
         });

        return view;
    }


    public static class ShopCarHolder {

        MCheckBox ivchoose;
        ImageView p_logo;
        TextView p_name;
        TextView p_desc;
        TextView p_price;
        CardView card;
        AddEditText add;


    }


    public interface onCheckAllListener {
        void checks(Product data);
    }

    private onCheckAllListener checkListener;

    public void setOnCheckAllListener(onCheckAllListener listener) {
        this.checkListener = listener;
    }
}
