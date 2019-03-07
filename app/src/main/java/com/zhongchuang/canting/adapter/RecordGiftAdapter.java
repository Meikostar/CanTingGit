package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.INTEGRALIST;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class RecordGiftAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<INTEGRALIST> foodlist;
    private String types;




    public void setDatas( List<INTEGRALIST> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public RecordGiftAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public interface ItemBuyClick{
        void itemClik(int id);
    }
    private ItemBuyClick itemBuyClick;
    public void setListener(ItemBuyClick click){
        itemBuyClick=click;

    }
     public List<INTEGRALIST> getFoodlist(){
         return foodlist;
     }

    @Override
    public int getCount() {
        return foodlist!=null?foodlist.size():0;
    }

    @Override
    public Object getItem(int i) {
        return foodlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.record_git_item, null);
            holder.tv_in1 =view.findViewById(R.id.tv_in1);
            holder.tv_in2 =view.findViewById(R.id.tv_in2);
            holder.tv_in3 =view.findViewById(R.id.tv_in3);
            holder.tv_in4 =view.findViewById(R.id.tv_time);
            holder.ll_bg =view.findViewById(R.id.ll_bg);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        INTEGRALIST integralist = foodlist.get(i);
        if(!TextUtils.isEmpty(integralist.changeType)){
            if(integralist.changeType.equals("1")){
                holder.tv_in1.setText(context.getString(R.string.jfhyb));
            }else if(integralist.changeType.equals("2")){
                holder.tv_in1.setText(context.getString(R.string.jfhyb));
            }else {
                holder.tv_in1.setText(integralist.changeType);
            }

        }

        if(integralist.createTime!=0){
            holder.tv_in2.setText(TimeUtil.formatTtimeName(integralist.createTime));
        }
        if(integralist.integralType.equals("1")){
            holder.tv_in3.setText("+"+integralist.integralBh);
            holder.tv_in3.setTextColor(context.getResources().getColor(R.color.btn_blue));
        }else {
            holder.tv_in3.setText("-"+integralist.integralBh);
            holder.tv_in3.setTextColor(context.getResources().getColor(R.color.color3));
        }




        return view;
    }

   public
    class ViewHolder {
        TextView tv_adress;
        TextView tv_in1;
        TextView tv_in2;
        TextView tv_in3;
        TextView tv_in4;
        LinearLayout ll_bg;
    }
}
