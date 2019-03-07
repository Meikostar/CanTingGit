package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.Care;
import com.zhongchuang.canting.been.OrderData;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.ClearEditText;
import com.zhongchuang.canting.widget.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghouyang on 16/12/23.
 */

public class CareMineAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Care> list;
    private int type=0;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;
    public interface selectItemListener{
        void delete(Care data, int poistion);
    }
    public void setListener(selectItemListener listener){
        this.listener=listener;
    }
    public void setData( List<Care> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public List<Care> getDatas(){
        return list;
    }
    public CareMineAdapter(Context context) {

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
            convertView = convertView.inflate(context,R.layout.item_care_mine, null);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.name = (TextView) convertView.findViewById(R.id.p_name);
            holder.dese = (TextView) convertView.findViewById(R.id.p_desc);
            holder.tv_cout = (TextView) convertView.findViewById(R.id.tv_cout);
            holder.tvjf = (TextView) convertView.findViewById(R.id.tv_jf);
            holder.tvState2 = (TextView) convertView.findViewById(R.id.tv_state2);
            holder.tvState3 = (TextView) convertView.findViewById(R.id.tv_state3);
            holder.img = (ImageView) convertView.findViewById(R.id.p_logo);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(type==0){
            holder.tvState2.setVisibility(View.VISIBLE);
            holder.tvState3.setText(R.string.jrhmd);
            holder.tvState3.setBackground(context.getResources().getDrawable(R.drawable.selector_hui));
        }else {
            holder.tvState2.setVisibility(View.GONE);
            holder.tvState3.setText(R.string.ychmd);
            holder.tvState3.setBackground(context.getResources().getDrawable(R.drawable.selector_blue));
        }
        final Care data=list.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.head_image)).asBitmap().placeholder(R.drawable.moren2).into(holder.img);
        if(TextUtil.isNotEmpty(data.totalNum)){
            holder.tv_cout.setText(data.totalNum);
        } if(TextUtil.isNotEmpty(data.integralTotal)){
            holder.tvjf.setText(data.integralTotal);
        } if(TextUtil.isNotEmpty(data.mobile_number)){
            holder.dese.setText(context.getString(R.string.sjhms)+data.mobile_number);
        } if(TextUtil.isNotEmpty(data.nickname)){
            holder.name.setText(context.getString(R.string.ncs)+data.nickname);
        }
       holder.tvState2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.delete(data,0);
           }
       });
        holder.tvState3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delete(data,1);
            }
        });
        return convertView;
    }

    class ViewHolder {


        TextView tv_state;
        TextView dese;
        TextView name;
        ImageView img;
        TextView tvState1;
        TextView tvState2;
        TextView tv_cout;
        TextView tvState3;
        TextView tvjf;
        ClearEditText etContent;


    }


}
