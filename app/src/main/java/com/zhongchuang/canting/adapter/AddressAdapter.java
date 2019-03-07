package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class AddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressBase> shopList;


    public AddressAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<AddressBase> list) {
        shopList=list;

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return shopList != null ? shopList.size() :0;
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
            view = View.inflate(mContext, R.layout.item_address, null);
            holder = new ViewHolder(view);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final AddressBase shopCar = shopList.get(position);
         if(TextUtil.isNotEmpty(shopCar.shipping_name)){
             holder.tvName.setText(shopCar.shipping_name);
         }if(TextUtil.isNotEmpty(shopCar.shipping_address)){
            holder.tvAddress.setText(shopCar.shipping_address+shopCar.detailed_address);
        }if(TextUtil.isNotEmpty(shopCar.link_number)){
            holder.tvPhone.setText(shopCar.link_number);
        }
        if(shopCar.is_default.equals("true")){
            holder.ivChoose.setChecked(true);
        }else {
            holder.ivChoose.setChecked(false);
        }
        holder.llBgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.checkAll(shopCar,1);
            }
        });
        holder.llBj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.checkAll(shopCar,2);
            }
        });
        holder.llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.checkAll(shopCar,3);
            }
        });
        holder.llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.checkAll(shopCar,4);
            }
        });

        return view;
    }




    public interface onCheckAllListener {
        void checkAll(AddressBase isAll,int type);
    }

    private onCheckAllListener checkListener;

    public void setOnCheckAllListener(onCheckAllListener listener) {
        this.checkListener = listener;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_choose)
        MCheckBox ivChoose;
        @BindView(R.id.ll_bj)
        LinearLayout llBj;
        @BindView(R.id.ll_del)
        LinearLayout llDel;
        @BindView(R.id.ll_bg)
        LinearLayout llBg;
        @BindView(R.id.ll_bgs)
        LinearLayout llBgs;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
