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
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mykar on 17/4/12.
 */
public class ItemShopRecycleAdapter extends BaseRecycleViewAdapter {



    private Context context;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    private int mHeaderCount = 1;//头部View个数

    public ItemShopRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_itemview, null);
            return new ItemViewHolder(view);

    }

    private View mHeaderView;



    //内容长度
    public int getContentItemCount() {
        return datas != null ? datas.size()  : 0;
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder holders = (ItemViewHolder) holder;



            if(datas.size()%2==0&&datas.size()!=0){
                final Product data = (Product) datas.get(2*position);
                final   Product data1 = (Product) datas.get(2*(position)+1);
                if(TextUtil.isNotEmpty(data.pro_name)){
                    holders.tvName.setText(data.pro_name);
                }   if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data.pro_price)) {
                        if(Integer.valueOf(data.integral_price)>0){
                            holders.tvType.setText("￥" + data.pro_price+"+"+data.integral_price+context.getString(R.string.jft));
                        }else {
                            holders.tvType.setText("￥ " + data.pro_price);
                        }
                    }
                } else {
                    if (TextUtil.isNotEmpty(data.integral_price)) {
                        holders.tvType.setText(context.getString(R.string.jft) + data.integral_price);
                    }
                } if(TextUtil.isNotEmpty(data1.pro_name)){
                    holders.tvName1.setText(data1.pro_name);
                }  if (data1.pro_site.equals("1")||data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data1.pro_price)) {
                        if(Integer.valueOf(data1.integral_price)>0){
                            holders.tvType1.setText("￥" + data1.pro_price+"+"+data1.integral_price+context.getString(R.string.jft));
                        }else {
                            holders.tvType1.setText("￥ " + data1.pro_price);
                        }
                    }
                } else {
                    if (TextUtil.isNotEmpty(data1.integral_price)) {
                        holders.tvType1.setText(context.getString(R.string.jft) + data1.integral_price);
                    }
                }
                String[] split = data.picture_url.split(",");
                String[] splits = data1.picture_url.split(",");
                Glide.with(context).load(StringUtil.changeUrl(split[0])).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
                Glide.with(context).load(StringUtil.changeUrl(splits[0])).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
                holders.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data.product_sku_id,2*position-1);
                    }
                });
                holders.card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data1.product_sku_id,2*position);
                    }
                });
            }else {
                final   Product data = (Product) datas.get(2*(position));

                if(TextUtil.isNotEmpty(data.pro_name)){
                    holders.tvName.setText(data.pro_name);
                }   if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data.pro_price)) {
                        if(Integer.valueOf(data.integral_price)>0){
                            holders.tvType.setText("￥" + data.pro_price+"+"+data.integral_price+context.getString(R.string.jft));
                        }else {
                            holders.tvType.setText("￥ " + data.pro_price);
                        }
                    }
                } else {
                    if (TextUtil.isNotEmpty(data.integral_price)) {
                        holders.tvType.setText(context.getString(R.string.jf) + data.integral_price);
                    }
                }
                holders.llBg.setVisibility(View.VISIBLE);
                String[] split = data.picture_url.split(",");
                Glide.with(context).load(StringUtil.changeUrl(split[0])).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
                holders.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data.product_sku_id,2*position-1);
                    }
                });
                if(position!=datas.size()/2){

                   final Product data1 = (Product) datas.get(2*(position)+1);
                    if (data1.pro_site.equals("1")||data.pro_site.equals("3")) {
                        if (TextUtil.isNotEmpty(data1.pro_price)) {
                            if(Integer.valueOf(data1.integral_price)>0){
                                holders.tvType1.setText("￥" + data1.pro_price+"+"+data1.integral_price+context.getString(R.string.jft));
                            }else {
                                holders.tvType1.setText("￥ " + data1.pro_price);
                            }
                        }
                    } else {
                        if (TextUtil.isNotEmpty(data1.integral_price)) {
                            holders.tvType1.setText(context.getString(R.string.jft) + data1.integral_price);
                        }
                    }
                    if(TextUtil.isNotEmpty(data1.pro_name)){
                        holders.tvName1.setText(data1.pro_name);
                    }
                    String[] splits = data1.picture_url.split(",");
                    Glide.with(context).load(StringUtil.changeUrl(splits[0])).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
                    holders.card1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.itemClick(data1.product_sku_id,2*position-1);
                        }
                    });
                }else {
                    holders.llBg.setVisibility(View.INVISIBLE);
                }
            }




    }

    public interface ItemClikcListener {
        void itemClick(String data, int poition);
    }

    public ItemClikcListener listener;

    public void setItemCikcListener(ItemClikcListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (datas != null && datas.size() > 0) {
            count = (datas.size()%2==0?datas.size()/2:datas.size()/2+1);
        }

        return count;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.card)
        CardView card;
        @BindView(R.id.iv_img1)
        ImageView ivImg1;
        @BindView(R.id.tv_name1)
        TextView tvName1;
        @BindView(R.id.tv_type1)
        TextView tvType1;
        @BindView(R.id.card1)
        CardView card1;
        @BindView(R.id.ll_bgs)
        LinearLayout llBg;
        public ItemViewHolder(View itemView) {
            super(itemView);
            if (mHeaderView == itemView) return;
            ButterKnife.bind(this, itemView);

        }
    }
}
