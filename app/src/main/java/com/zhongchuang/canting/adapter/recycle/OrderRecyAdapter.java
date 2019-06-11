package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RegularListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/11/8.
 */

public class OrderRecyAdapter extends RecyclerView.Adapter<OrderRecyAdapter.ViewHolder> {



    private Context hotContext;
    private List<ZhiBo_GuanZhongBean.DataBean> roomList;

    public OrderRecyAdapter(Context hotContext) {
        this.hotContext = hotContext;

    }


    public void setData(List<ZhiBo_GuanZhongBean.DataBean> list) {
        roomList.clear();
        roomList.addAll(list);
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

        View viewH = LayoutInflater.from(hotContext).inflate(R.layout.item_recv_my_order, parent, false);


        return new ViewHolder(viewH);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


    }


    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 8;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_store_name)
        TextView tvStoreName;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.ll_head_purchases)
        LinearLayout llHeadPurchases;
        @BindView(R.id.rl_menu)
        RegularListView rlMenu;
        @BindView(R.id.tv_goods_count)
        TextView tvGoodsCount;
        @BindView(R.id.et_content)
        ClearEditText etContent;
        @BindView(R.id.ll_edit)
        LinearLayout llEdit;
        @BindView(R.id.tv_cont1)
        TextView tvCont1;
        @BindView(R.id.tv_cont2)
        TextView tvCont2;
        @BindView(R.id.tv_cont3)
        TextView tvCont3;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
