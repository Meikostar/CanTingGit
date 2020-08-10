package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodRecyAdapter extends RecyclerView.Adapter<GoodRecyAdapter.ViewHolder> {


    private Context hotContext;
    private List<Product> datas = new ArrayList<>();

    public GoodRecyAdapter(Context hotContext) {
        this.hotContext = hotContext;

    }


    public void setData(List<Product> list) {

        datas.clear();
        datas.addAll(list);
        notifyDataSetChanged();
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(View view, int position, ZhiBo_GuanZhongBean.DataBean url);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
            /* 条目监听事件* */


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewH = LayoutInflater.from(hotContext).inflate(R.layout.item_goods, parent, false);


        return new ViewHolder(viewH);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ViewHolder holders = holder;


        if (datas.size() % 2 == 0) {
            final Product data = datas.get(2 * position);
            final Product data1 = datas.get(2 * position + 1);
            if (TextUtil.isNotEmpty(data.pro_name)) {
                holders.tvName.setText(data.pro_name);
            }
            if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                if (TextUtil.isNotEmpty(data.pro_price)) {
                    if(Integer.valueOf(data.integral_price)>0){
                        holders.tvMoney.setText("￥" + data.pro_price+"+"+data.integral_price+"兑换值");
                    }else {
                        holders.tvMoney.setText("￥ " + data.pro_price);
                    }
                }
            } else {
                if (TextUtil.isNotEmpty(data.integral_price)) {
                    holders.tvMoney.setText("兑换值 " + data.integral_price);
                }
            }
//            if (TextUtil.isNotEmpty(data.pro_price)) {
//                holders.tvMoney.setText(data.pro_price);
//            }
            if (TextUtil.isNotEmpty(data1.pro_name)) {
                holders.tvName1.setText(data1.pro_name);
            }
            if (data1.pro_site.equals("1")||data.pro_site.equals("3")) {
                if (TextUtil.isNotEmpty(data1.pro_price)) {
                    if(Integer.valueOf(data1.integral_price)>0){
                        holders.tvMoney1.setText("￥" + data1.pro_price+"+"+data1.integral_price+"兑换值");
                    }else {
                        holders.tvMoney1.setText("￥ " + data1.pro_price);
                    }
                }
            } else {
                if (TextUtil.isNotEmpty(data1.integral_price)) {
                    holders.tvMoney1.setText("兑换值 " + data1.integral_price);
                }
            }
//            if (TextUtil.isNotEmpty(data1.pro_price)) {
//                holders.tvMoney1.setText(data1.pro_price);
//            }
            if (TextUtil.isNotEmpty(data1.mer_name)) {
                holders.tvStore1.setText(data1.mer_name);
            }
            if (TextUtil.isNotEmpty(data1.ship_address)) {
                holders.tvAddress1.setText(data1.ship_address);
            }
            if (TextUtil.isNotEmpty(data.mer_name)) {
                holders.tvStore.setText(data.mer_name);
            }
            if (TextUtil.isNotEmpty(data.ship_address)) {
                holders.tvAddress.setText(data.ship_address);
            }
            String[] split = data.picture_url.split(",");
            String[] splits = data1.picture_url.split(",");
            Glide.with(hotContext).load(StringUtil.changeUrl(split.length > 0 ? split[0] : "")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
            Glide.with(hotContext).load(StringUtil.changeUrl(splits.length > 0 ? splits[0] : "")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
            holders.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(data.product_sku_id,data.pro_site, position);
                }
            });
            holders.card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(data1.product_sku_id,data1.pro_site, position);
                }
            });
        } else {
            final Product data = datas.get(2 * position);

            if (TextUtil.isNotEmpty(data.pro_name)) {
                holders.tvName.setText(data.pro_name);
            }
            if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                if (TextUtil.isNotEmpty(data.pro_price)) {
                    if(Integer.valueOf(data.integral_price)>0){
                        holders.tvMoney.setText("￥" + data.pro_price+"+"+data.integral_price+"兑换值");
                    }else {
                        holders.tvMoney.setText("￥ " + data.pro_price);
                    }
                }
            } else {
                if (TextUtil.isNotEmpty(data.integral_price)) {
                    holders.tvMoney.setText("兑换值 " + data.integral_price);
                }
            }
            if (TextUtil.isNotEmpty(data.mer_name)) {
                holders.tvStore.setText(data.mer_name);
            }
            if (TextUtil.isNotEmpty(data.ship_address)) {
                holders.tvAddress.setText(data.ship_address);
            }
            holders.llBg.setVisibility(View.VISIBLE);

            String[] splits = data.picture_url.split(",");
            Glide.with(hotContext).load(StringUtil.changeUrl(splits.length > 0 ? splits[0] : "")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
            holders.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(data.product_sku_id,data.pro_site, position);
                }
            });
            if (position != datas.size() / 2) {

                final Product data1 = datas.get(2 * position + 1);
                if (TextUtil.isNotEmpty(data1.pro_name)) {
                    holders.tvName1.setText(data1.pro_name);
                }
                if (data1.pro_site.equals("1")||data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data1.pro_price)) {
                        if(Integer.valueOf(data1.integral_price)>0){
                            holders.tvMoney1.setText("￥" + data1.pro_price+"+"+data1.integral_price+"兑换值");
                        }else {
                            holders.tvMoney1.setText("￥ " + data1.pro_price);
                        }
                    }
                } else {
                    if (TextUtil.isNotEmpty(data1.integral_price)) {
                        holders.tvMoney1.setText("兑换值 " + data1.integral_price);
                    }
                }
                if (TextUtil.isNotEmpty(data1.mer_name)) {
                    holders.tvStore1.setText(data1.mer_name);
                }
                if (TextUtil.isNotEmpty(data1.ship_address)) {
                    holders.tvAddress1.setText(data1.ship_address);
                }
                String[] splitss = data1.picture_url.split(",");
                Glide.with(hotContext).load(StringUtil.changeUrl(splitss.length > 0 ? splitss[0] : "")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
                holders.card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data1.product_sku_id,data1.pro_site, position);
                    }
                });
            } else {
                holders.llBg.setVisibility(View.INVISIBLE);
            }
        }


    }


    @Override
    public int getItemCount() {
        return datas != null ? (datas.size() % 2 == 0 ? datas.size() / 2 : (datas.size() / 2 + 1)) : 0;

    }

    public interface ItemClikcListener {
        void itemClick(String data,String type, int poition);
    }

    public ItemClikcListener listener;

    public void setItemCikcListener(ItemClikcListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_store)
        TextView tvStore;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.iv_img1)
        ImageView ivImg1;
        @BindView(R.id.tv_name1)
        TextView tvName1;
        @BindView(R.id.tv_money1)
        TextView tvMoney1;
        @BindView(R.id.tv_store1)
        TextView tvStore1;
        @BindView(R.id.tv_address1)
        TextView tvAddress1;
        @BindView(R.id.card1)
        CardView card1;
        @BindView(R.id.ll_bg)
        LinearLayout llBg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
