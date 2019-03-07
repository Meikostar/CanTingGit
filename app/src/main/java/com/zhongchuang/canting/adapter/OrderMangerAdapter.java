package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.mall.OrderDetailActivity;
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

public class OrderMangerAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<OrderData> list;
    private int type;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;
    public interface selectItemListener{
        void delete(OrderData data, int poistion);
    }
    public void setListener(selectItemListener listener){
        this.listener=listener;
    }
    public void setData( List<OrderData> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public List<OrderData> getDatas(){
        return list;
    }
    public OrderMangerAdapter(Context context) {

        this.context = context;

    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getCount() {
        return list!=null?(list.size()==0?0:list.size()):0;
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
            holder=new ViewHolder();
            convertView = convertView.inflate(context,R.layout.item_order_manger, null);
            holder.tvStoreName = (TextView) convertView.findViewById(R.id.tv_store_name);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tvCont3 = (TextView) convertView.findViewById(R.id.tv_cont3);
            holder.tvCont2 = (TextView) convertView.findViewById(R.id.tv_cont2);
            holder.tvCont1 = (TextView) convertView.findViewById(R.id.tv_cont1);
            holder.tvState1 = (TextView) convertView.findViewById(R.id.tv_state1);
            holder.tvState2 = (TextView) convertView.findViewById(R.id.tv_state2);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);


            holder.rlMenu = (RegularListView) convertView.findViewById(R.id.rl_menu);

            holder.llBg = (LinearLayout) convertView.findViewById(R.id.ll_bg);
            holder.llBgs = (LinearLayout) convertView.findViewById(R.id.ll_bgs);
            holder.llHeadPurchases = (LinearLayout) convertView.findViewById(R.id.ll_head_purchases);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
         holder.llBgs.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {

                 listener.delete(list.get(position),-2);
                 return true;
             }
         });
        holder.llHeadPurchases.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                listener.delete(list.get(position),-2);
                return true;
            }
        });
        OrderChildAdapter adapter=new OrderChildAdapter(context);
        holder.rlMenu.setAdapter(adapter);
        adapter.setType(Integer.valueOf(list.get(position).proSite));
        adapter.setDatas(list.get(position).protList);
        adapter.setListener(new OrderChildAdapter.ItemBuyClick() {
            @Override
            public void itemClik(int id) {
                listener.delete(list.get(position),-2);
            }
        });
        holder.llHeadPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,OrderDetailActivity.class));
            }
        });
        if(TextUtil.isNotEmpty(list.get(position).merName)){
            holder.tvStoreName.setText(list.get(position).merName);
        } if(TextUtil.isNotEmpty(list.get(position).totalPrice)){
            holder.tvCont2.setText(context.getString(R.string.hjs)+list.get(position).totalPrice+(type==1?"￥":context.getString(R.string.jf)));
        } if(TextUtil.isNotEmpty(list.get(position).totalIntegralPrice)){
            if(type!=1){
                holder.tvCont2.setText(context.getString(R.string.hjs)+list.get(position).totalIntegralPrice+(type==1?"￥":context.getString(R.string.jf)));
            }
        } if(TextUtil.isNotEmpty(list.get(position).totalNumber)){
            holder.tvCont1.setText(context.getString(R.string.gong)+list.get(position).totalNumber+context.getString(R.string.jsp));
        }
        if(TextUtil.isNotEmpty(list.get(position).orderType)){
          if(list.get(position).orderType.equals("1")){
              holder.tvStatus.setText(context.getString(R.string.dfk));
              holder.tvState1.setText(context.getString(R.string.qxdd));
              holder.tvState2.setText(context.getString(R.string.fk));
              holder.llBg.setVisibility(View.VISIBLE);
          }else if(list.get(position).orderType.equals("2")){
              holder.tvStatus.setText(context.getString(R.string.dfh));
              holder.tvState1.setVisibility(View.GONE);
              holder.tvState2.setText(context.getString(R.string.qxdd));
              holder.llBg.setVisibility(View.VISIBLE);
          }else if(list.get(position).orderType.equals("3")){
              holder.tvStatus.setText(context.getString(R.string.dfh));
              holder.tvState2.setText(R.string.qrsh);
              holder.tvState1.setVisibility(View.GONE);
              holder.llBg.setVisibility(View.VISIBLE);
          }else if(list.get(position).orderType.equals("7")){
              holder.tvStatus.setText(R.string.jywc);
              holder.llBg.setVisibility(View.GONE);
          }else if(list.get(position).orderType.equals("8")){
              holder.tvStatus.setText(R.string.ddyqx);
              holder.llBg.setVisibility(View.GONE);
          }else if(list.get(position).orderType.equals("4")){
              holder.tvStatus.setText(context.getString(R.string.ddyqx));
              holder.llBg.setVisibility(View.GONE);
          }
        }
        holder.tvState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(list.get(position),position);
            }
        });
        holder.tvState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(list.get(position),-4);
            }
        });
        holder.llHeadPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(list.get(position),-1);
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
        TextView tvState1;
        TextView tvState2;
        TextView tv_delete;

        LinearLayout llEdit;
        LinearLayout llHeadPurchases;
        LinearLayout llBg;
        LinearLayout llBgs;
        ClearEditText etContent;
    }


}
