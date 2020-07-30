package com.zhongchuang.canting.adapter.recycle;

import android.annotation.SuppressLint;
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
import com.zhongchuang.canting.adapter.BannerAdapter;
import com.zhongchuang.canting.adapter.Homedapter;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mykar on 17/4/12.
 */
public class ItemRecycleAdapter extends BaseRecycleViewAdapter {


    private Context context;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    private int mHeaderCount = 1;//头部View个数

    public ItemRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == ITEM_TYPE_HEADER) {

            return new ItemViewHolder(mHeaderView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_itemview, null);
            return new ItemViewHolder(view);
        }

    }

    private View mHeaderView;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    //内容长度
    public int getContentItemCount() {
        return datas != null ? (datas.size() % 2 == 0 ? datas.size() / 2 : datas.size() / 2 + 1) + 1 : 0;
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
//头部View
            return ITEM_TYPE_HEADER;
        } else {
//内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    private Homedapter homedapter;
    private BannerAdapter bannerAdapter;
    private List<Integer> list = new ArrayList<>();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        ItemViewHolder holders = (ItemViewHolder) holder;


        if (position != 0 && datas.size() != 0) {

            if (datas.size() % 2 == 0 && datas.size() != 0) {
                final Product data = (Product) datas.get(2 * (position - 1));
                final Product data1 = (Product) datas.get(2 * (position - 1) + 1);
                if (TextUtil.isNotEmpty(data.pro_name)) {
                    holders.tvName.setText(data.pro_name);
                }
                if (data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data.pro_price)) {
                        if(Integer.valueOf(data.integral_price)>0){
                            holders.tvType.setText("￥" + data.pro_price+"+"+data.integral_price+"兑换值");
                        }else {
                            holders.tvType.setText("￥ " + data.pro_price);
                        }

                    }
                } else if (data1.pro_site.equals("1")){
                    if (TextUtil.isNotEmpty(data.pro_price)) {

                        holders.tvType.setText("￥ " + data.pro_price);
                    }
                } else {
                    if (TextUtil.isNotEmpty(data.integral_price)) {

                        holders.tvType.setText("兑换值" + data.integral_price);
                    }
                }
                if (TextUtil.isNotEmpty(data1.pro_name)) {
                    holders.tvName1.setText(data1.pro_name);
                }
                if (data1.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data1.pro_price)) {
                        if(Integer.valueOf(data.integral_price)>0){
                            holders.tvType1.setText("￥" + data1.pro_price+"+ "+data1.integral_price+"兑换值");
                        }else {
                            holders.tvType1.setText("￥" + data1.pro_price);
                        }

                    }
                } else if (data1.pro_site.equals("1")){
                    if (TextUtil.isNotEmpty(data1.pro_price)) {
                        if(TextUtil.isNotEmpty(data1.market_price)&&Double.valueOf(data1.market_price)>0){
                            holders.tvType1.setText("￥ " + data1.pro_price +"送贡献值"+data1.market_price);
                        }else {
                            holders.tvType1.setText("￥ " + data1.pro_price );
                        }

                    }
                }else {
                    if (TextUtil.isNotEmpty(data1.integral_price)) {
                        holders.tvType1.setText("兑换值" + data1.integral_price);
                    }
                }

                String[] splits = data.picture_url.split(",");
                Glide.with(context).load(StringUtil.changeUrl(splits.length>0?splits[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
                String[] split = data1.picture_url.split(",");
                Glide.with(context).load(StringUtil.changeUrl(split.length>0?split[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
                holders.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data.product_sku_id, 2 * position - 1);
                    }
                });
                holders.card1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data1.product_sku_id, 2 * position);
                    }
                });
            } else {
                final Product data = (Product) datas.get(2 * (position - 1));

                if (TextUtil.isNotEmpty(data.pro_name)) {
                    holders.tvName.setText(data.pro_name);
                }
                if (data.pro_site.equals("3")) {
                    if (TextUtil.isNotEmpty(data.pro_price)) {
                        if(Integer.valueOf(data.integral_price)>0){
                            holders.tvType.setText("￥" + data.pro_price+"+ "+data.integral_price+"兑换值");
                        }else {
                            holders.tvType.setText("￥" + data.pro_price);
                        }

                    }
                } else if (data.pro_site.equals("1")){
                    if (TextUtil.isNotEmpty(data.pro_price)) {

                        holders.tvType.setText("￥ " + data.pro_price);
                    }
                }else {
                    if (TextUtil.isNotEmpty(data.integral_price)) {
                        holders.tvType.setText("兑换值"+ data.integral_price);
                    }
                }


                holders.llBg.setVisibility(View.VISIBLE);
                String[] split = data.picture_url.split(",");
                Glide.with(context).load(StringUtil.changeUrl(split.length>0?split[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg);
                holders.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.itemClick(data.product_sku_id, 2 * position - 1);
                    }
                });
                if (position != datas.size() / 2 + 1) {

                  final   Product data1 = (Product) datas.get(2 * (position - 1) + 1);
                    if (TextUtil.isNotEmpty(data1.pro_name)) {
                        holders.tvName1.setText(data1.pro_name);
                    }
                    if (data1.pro_site.equals("3")) {
                        if (TextUtil.isNotEmpty(data1.pro_price)) {
                            if(Integer.valueOf(data.integral_price)>0){
                                holders.tvType1.setText("￥" + data1.pro_price+"+ "+data1.integral_price+"兑换值");
                            }else {
                                holders.tvType1.setText("￥" + data1.pro_price);
                            }

                        }
                    } else if (data1.pro_site.equals("1")){
                        if (TextUtil.isNotEmpty(data1.pro_price)) {
                            if(TextUtil.isNotEmpty(data1.market_price)&&Double.valueOf(data1.market_price)>0){
                                holders.tvType1.setText("￥ " + data1.pro_price +"送贡献值"+data1.market_price);
                            }else {
                                holders.tvType1.setText("￥ " + data1.pro_price );
                            }

                        }
                    }else {
                        if (TextUtil.isNotEmpty(data1.integral_price)) {
                            holders.tvType1.setText("兑换值" + data1.integral_price);
                        }
                    }
                    String[] splitss = data1.picture_url.split(",");
                    Glide.with(context).load(StringUtil.changeUrl(splitss.length>0?splitss[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(holders.ivImg1);
                    holders.card1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.itemClick(data1.product_sku_id,2 * position);
                        }
                    });
                } else {
                    holders.llBg.setVisibility(View.INVISIBLE);
                }
            }


        }

    }

    public interface ItemClikcListener {
        void itemClick(String data,int poition);
    }

    public ItemClikcListener listener;

    public void setItemCikcListener(ItemClikcListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        int count = 1;

        if (datas != null && datas.size() > 0) {
            count = (datas.size() % 2 == 0 ? datas.size() / 2 : datas.size() / 2 + 1) + 1;
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
