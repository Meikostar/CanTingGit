package com.zhongchuang.canting.easeui.adapter;

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
import com.zhongchuang.canting.been.RedInfo;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.AddEditText;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class RedDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<RedInfo> shopList;


    public RedDetailAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<RedInfo> list) {
        shopList = list;

        notifyDataSetChanged();
    }

    public List<RedInfo> getData( ) {
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
            view = View.inflate(mContext, R.layout.red_detail_item, null);
            holder = new ShopCarHolder();

            holder.iv_img = view.findViewById(R.id.iv_img);
            holder.tv_name = view.findViewById(R.id.tv_name);
            holder.tv_time = view.findViewById(R.id.tv_time);
            holder.tv_amount = view.findViewById(R.id.tv_amount);
            holder.tv_best = view.findViewById(R.id.tv_best);
            view.setTag(holder);

        } else {
            holder = (ShopCarHolder) view.getTag();
        }


        Glide.with(mContext).load(StringUtil.changeUrl(shopList.get(position).head_image)).asBitmap().placeholder(R.drawable.moren2).into(holder.iv_img);

        if (shopList.get(position).best_luck.equals("1")) {
            holder.tv_best.setVisibility(View.VISIBLE);
        } else {
            holder.tv_best.setVisibility(View.GONE);
        }
         if (TextUtil.isNotEmpty(shopList.get(position).nickname)) {
            holder.tv_name.setText(shopList.get(position).nickname);
        }
        holder.tv_amount.setText(shopList.get(position).grab_envelope_count+"兑换值");
        holder.tv_time.setText(TimeUtil.formatRedTime(shopList.get(position).create_time));


        return view;
    }


    public static class ShopCarHolder {


        ImageView iv_img;
        TextView tv_name;
        TextView tv_time;
        TextView tv_amount;
        TextView tv_best;


    }


    public interface onCheckAllListener {
        void checks(String data);
    }

    private onCheckAllListener checkListener;

    public void setOnCheckAllListener(onCheckAllListener listener) {
        this.checkListener = listener;
    }
}
