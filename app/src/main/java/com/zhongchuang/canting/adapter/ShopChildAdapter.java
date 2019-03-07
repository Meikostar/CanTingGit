package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.GoodsInfo;
import com.zhongchuang.canting.been.ShopFirst;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.UIUtil;
import com.zhongchuang.canting.widget.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ShopChildAdapter extends RecyclerView.Adapter<ShopChildAdapter.ChildViewHolder> {

    private int width;
    private Context mContext;
    private List<ShopFirst> shopList;

    public ShopChildAdapter(Context mContext, List<ShopFirst> shopList) {
        this.mContext = mContext;
        this.shopList = shopList;
        int total = UIUtil.getScreenWidth(mContext);
        width = (total - UIUtil.dip2px(mContext,17))/3;
    }

    public void setData(List<ShopFirst> firsts){
        shopList.clear();
        shopList.addAll(firsts);
        notifyDataSetChanged();
    }

    public void addData(List<ShopFirst> firsts){
        shopList.addAll(firsts);
        notifyDataSetChanged();
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shopchild_item, null);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        final ShopFirst shopBean = shopList.get(position);

        holder.pType.setText(shopBean.getName());
//        Glide.with(mContext).load(shopBean.goodsLogo).into(holder.bigImage);

        GridItemAdapter gridItemAdapter = new GridItemAdapter(shopBean.getList());
        holder.gridView.setAdapter(gridItemAdapter);

        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onClickMoreBtn(shopBean.getName());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.p_type)
        TextView pType;
        @BindView(R.id.more_btn)
        TextView moreBtn;
        @BindView(R.id.big_image)
        ImageView bigImage;
        @BindView(R.id.gridview)
        MyGridView gridView;


        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class GridItemAdapter extends BaseAdapter {

        private List<GoodsInfo> itemBeen;

        public GridItemAdapter(List<GoodsInfo> itemBeen) {
            this.itemBeen = itemBeen;
        }

        @Override
        public int getCount() {
//            return itemBeen.size();
            return 6;
        }

        @Override
        public Object getItem(int position) {
            return itemBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridHolder holder;
            if (convertView == null) {
                holder = new GridHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_layout, null);
                holder.image = convertView.findViewById(R.id.shop_img_one);
                holder.pName = convertView.findViewById(R.id.shop_name_one);
                holder.pPrice = convertView.findViewById(R.id.shop_price_one);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
                holder.image.setLayoutParams(params);
                convertView.setTag(holder);
            }else {
                holder = (GridHolder) convertView.getTag();
            }

            GoodsInfo bean = itemBeen.get(position);
            Glide.with(mContext).load(StringUtil.changeUrl(bean.goodsLogo)).into(holder.image);
            holder.pName.setText(bean.getGoodsName());
            holder.pPrice.setText("￥"+bean.getPrice());

            return convertView;
        }

        class GridHolder {

            private ImageView image;
            private TextView pName;
            private TextView pPrice;

        }
    }

    private OnMoreBtnClickListener listener;
    public void setOnMoreBtnClickListener(OnMoreBtnClickListener listener){
            this.listener = listener;
    }
    public interface OnMoreBtnClickListener{
        void onClickMoreBtn(String id);
    }
}
