package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.been.ShopCar;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class OrderItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderData> shopList;


    public OrderItemAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<OrderData> list) {

        shopList=list;

        notifyDataSetChanged();
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
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_orders, null);
            holder = new ViewHolder(view);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final OrderData shopCar = shopList.get(position);

        Glide.with(mContext).load(StringUtil.changeUrl(shopCar.picture_sku_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.pLogo);
        if(TextUtil.isNotEmpty(shopCar.proSku)){
            holder.pDesc.setText(shopCar.proSku);
        } if(TextUtil.isNotEmpty(shopCar.prod_number)){
            holder.tvCout.setText(shopCar.prod_number);
        }
        if(TextUtil.isNotEmpty(shopCar.pro_name)){
            holder.pName.setText(shopCar.pro_name);
        }

        if(shopCar.pro_site.equals("1")){
            holder.tvType.setText("￥ "+shopCar.pro_price);
        }else if(shopCar.pro_site.equals("3")){
            holder.tvType.setText("￥ "+shopCar.pro_price);
        }else {
            holder.tvType.setText(mContext.getString(R.string.jft)+shopCar.integral_price);
        }


        return view;
    }


    public interface onCheckAllListener {
        void checkAll(Set<ShopCar> list, boolean isAll);
    }

    private onCheckAllListener checkListener;

    public void setOnCheckAllListener(onCheckAllListener listener) {
        this.checkListener = listener;
    }


    static class ViewHolder {
        @BindView(R.id.p_logo)
        ImageView pLogo;
        @BindView(R.id.p_name)
        TextView pName;
        @BindView(R.id.p_desc)
        TextView pDesc;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_cout)
        TextView tvCout;
        @BindView(R.id.tv_copy)
        TextView tvCopy;
        @BindView(R.id.ll_bg)
        LinearLayout llBg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
