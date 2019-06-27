package com.zhongchuang.canting.easeui.adapter;

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
import com.zhongchuang.canting.adapter.recycle.BaseRecycleViewAdapter;
import com.zhongchuang.canting.been.GrapRed;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mykar on 17/4/12.
 */
public class RedItemRecycleAdapter extends BaseRecycleViewAdapter {



    private Context context;
    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    private int mHeaderCount = 1;//头部View个数

    public RedItemRecycleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == ITEM_TYPE_HEADER) {

            return new ItemViewHolder(mHeaderView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.red_detail_item, null);
            return new ItemViewHolder(view);
        }

    }
    private int state=0;
    public void setState(int state){
        this.state=state;
    }
    private View mHeaderView;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    //内容长度
    public int getContentItemCount() {
        return datas != null ? (datas.size() % 2 == 0 ? datas.size() / 2 : datas.size() / 2 + 1) + 1 : 1;
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

    private List<Integer> list = new ArrayList<>();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ItemViewHolder holders = (ItemViewHolder) holder;
        if(position>0&&datas.size()!=0){
            holders.ivImg.setVisibility(View.GONE);
            GrapRed red= (GrapRed) datas.get(position-1);
            holders.tvBest.setVisibility(View.GONE);
            if(TextUtil.isNotEmpty(red.send_user_id)){
                if(TextUtil.isNotEmpty(red.remarkName)){
                    holders.tvName.setText(red.remarkName);
                }

                holders.tvAmount.setText(red.grab_envelope_count+"积分");
            }else {
                if(red.red_envelope_type==1){
                    holders.tvName.setText("群红包");
                }else {
                    holders.tvName.setText("普通红包");
                }

                holders.tvAmount.setText(red.red_envelope_count+"积分");
            }
            if(state==0){
                holders.tvTime.setText(TimeUtil.formatRedTime(red.create_time));
            }else {
                holders.tvTime.setText(TimeUtil.formatRedTimes(red.create_time));
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
        int count = 1;

        if (datas != null && datas.size() > 0) {
            count = datas.size() + 1;
        }

        return count;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_best)
        TextView tvBest;

        public ItemViewHolder(View itemView) {
            super(itemView);
            if (mHeaderView == itemView) return;
            ButterKnife.bind(this, itemView);

        }
    }
}
