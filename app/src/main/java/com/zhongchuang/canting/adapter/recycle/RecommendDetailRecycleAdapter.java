package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.been.Profit;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;
import com.zhongchuang.canting.widget.DashView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mykar on 17/4/12.
 */
public class RecommendDetailRecycleAdapter extends BaseRecycleViewAdapter {


    private Context context;
    private int type;

    public RecommendDetailRecycleAdapter(Context context) {
        this.context = context;
    }


    public void setType(int type) {
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_detail, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new DoctorItemViewHolder(view);
    }

    private Gson mGson = new Gson();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DoctorItemViewHolder holders = (DoctorItemViewHolder) holder;
        Profit data = (Profit) datas.get(position);
        Profit dat=null;
        if(position>0){
             dat = (Profit) datas.get(position-1);
        }


        if (position == 0) {
            String time = TimeUtil.formatToMf(data.create_time);
            String[] split = time.split("##");
            holders.tvTime.setVisibility(View.VISIBLE);
            if(TextUtil.isNotEmpty(data.profit_count)){
                holders.tvCout.setText("+"+data.profit_count+" ￥");
            }
            if(TextUtil.isNotEmpty(data.order_id)){
                holders.tvCode.setText("订单号："+data.order_id);
            }
            if(TextUtil.isNotEmpty(data.register_name)){

                holders.tvType.setText(data.register_name+"在商城购买"+(TextUtil.isNotEmpty(data.product_name)?data.product_name:""));
            }
//            if(TextUtil.isNotEmpty(data.integral_instruction)){
//                holders.tvType.setText(data.integral_instruction);
//            }
            if (split != null && split.length == 2) {
                holders.tvTime.setText(split[0]);
                holders.tvTimes.setText(split[1]);
            }

        } else {
            String time = TimeUtil.formatToMf(data.create_time);
            String times = TimeUtil.formatToMf(dat.create_time);
            String[] split = time.split("##");
            String[] splits = times.split("##");
            if(split[0].equals(splits[0])){
                holders.tvTime.setVisibility(View.INVISIBLE);
            }else {
                holders.tvTime.setVisibility(View.VISIBLE);
            }

            holders.tvTime.setVisibility(View.VISIBLE);
            holders.tvTime.setText(split[0]);
            holders.tvTimes.setText(split[1]);
            if(TextUtil.isNotEmpty(data.profit_count)){
                holders.tvCout.setText("+"+data.profit_count+" ￥");
            }
            if(TextUtil.isNotEmpty(data.order_id)){
                holders.tvCode.setText("订单号："+data.order_id);
            }
            if(TextUtil.isNotEmpty(data.register_name)){

                holders.tvType.setText(data.register_name+"在商城购买"+(TextUtil.isNotEmpty(data.product_name)?data.product_name:""));
            }

        }

        if (position != 0) {
            holders.line1.setVisibility(View.VISIBLE);
        } else {
            holders.line1.setVisibility(View.GONE);

        }
//        if (position % 2 == 0) {
//
//
//        } else {
//
//            holders.tvTime.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        int count = 0;

        if (datas != null && datas.size() > 0) {
            count = datas.size();
        }

        return count;
    }

    public static class DoctorItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.line1)
        DashView line1;
        @BindView(R.id.line2)
        DashView line2;
        @BindView(R.id.iv_cyc)
        ImageView ivCyc;
        @BindView(R.id.tv_times)
        TextView tvTimes;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_cout)
        TextView tvCout;
        @BindView(R.id.tv_code)
        TextView tvCode;

        public DoctorItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void setClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener listener;

    public interface OnItemClickListener {
        void clickListener(int poiston, String id);
    }
}
