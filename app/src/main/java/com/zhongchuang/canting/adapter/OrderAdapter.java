package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.RegularListView;
import com.zhongchuang.canting.widget.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghouyang on 16/12/23.
 */

public class OrderAdapter extends BaseAdapter {
    private Context context;

    private List<OrderData> list;
    private int type;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;

    public interface selectItemListener {
        void delete(OrderData data, int poistion);
    }

    public void setListener(selectItemListener listener) {
        this.listener = listener;
    }

    public void setData(List<OrderData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<OrderData> getDatas() {
        return list;
    }

    public OrderAdapter(Context context) {

        this.context = context;


    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getCount() {
        return list != null ? (list.size()) : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_recv_my_order,  null);
            holder.tvStoreName = convertView.findViewById(R.id.tv_store_name);
            holder.tvStatus = convertView.findViewById(R.id.tv_status);
            holder.tvCont3 = convertView.findViewById(R.id.tv_cont3);
            holder.tvCont2 = convertView.findViewById(R.id.tv_cont2);
            holder.tvCont1 = convertView.findViewById(R.id.tv_cont1);

            holder.tvGoodsCount = convertView.findViewById(R.id.tv_goods_count);
            holder.etContent = convertView.findViewById(R.id.et_content);
            holder.rlMenu = convertView.findViewById(R.id.rl_menu);


            holder.llHeadPurchases = convertView.findViewById(R.id.ll_head_purchases);
            holder.llEdit = convertView.findViewById(R.id.ll_edit);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderChildAdapter adapter = new OrderChildAdapter(context);
        holder.rlMenu.setAdapter(adapter);
        adapter.setType(type);
        adapter.setDatas(list.get(position).protList);


        if(TextUtil.isNotEmpty(list.get(position).merName)){
            holder.tvStoreName.setText(list.get(position).merName);
        }

        if(TextUtil.isNotEmpty(list.get(position).totalPrice)){
            holder.tvCont2.setText(context.getString(R.string.hjs)+list.get(position).totalPrice+(type==1?("￥"+"+"+list.get(position).totalIntegralPrice+"兑换值"):"兑换值"));
        } if(TextUtil.isNotEmpty(list.get(position).totalIntegralPrice)){
            if(type!=1){
                holder.tvCont2.setText(context.getString(R.string.hjs)+list.get(position).totalIntegralPrice+(type==1?"￥":"兑换值"));
            }

        } if(TextUtil.isNotEmpty(list.get(position).totalNumber)){
            holder.tvCont1.setText(context.getString(R.string.gong)+list.get(position).totalNumber+context.getString(R.string.jsp));
        }

        holder.etContent.setOnClearEditTextListener(new ClearEditText.ClearEditTextListener() {
            @Override
            public void afterTextChanged4ClearEdit(Editable s) {

            }

            @Override
            public void changeText(CharSequence s) {
                list.get(position).phoneMassege=s.toString();
            }
        });
       holder.llHeadPurchases.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.delete(list.get(position),position);
           }
       });

        return convertView;
    }

    class ViewHolder {


        TextView tvStoreName;
        TextView tvStatus;
        RegularListView rlMenu;
        TextView tvCont3;
        TextView tvCont2;
        TextView tvCont1;
        TextView tvGoodsCount;
        ImageView ivImg;

        LinearLayout llEdit;
        LinearLayout llHeadPurchases;
        ClearEditText etContent;
    }


    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
}
