package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.ShopCar;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class SaleMangerAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopCar> shopList;


    public SaleMangerAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<ShopCar> list) {
        shopList.clear();
        shopList.addAll(list);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return shopList != null ? shopList.size() : 4;
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
            view = View.inflate(mContext, R.layout.item_sale_manger, null);
            holder = new ViewHolder(view);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

//        final ShopCar shopCar = shopList.get(position);
//
//        Glide.with(mContext).load(shopCar.getGoodsLogo()).into(holder.p_logo);
//        holder.p_name.setText(shopCar.getGoodsName());
//        holder.p_desc.setText(shopCar.getDescribeContent());
//        holder.p_price.setText("￥" + shopCar.getPrice());

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
        @BindView(R.id.tv_code)
        TextView tvCode;
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
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_detail)
        TextView tvDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
