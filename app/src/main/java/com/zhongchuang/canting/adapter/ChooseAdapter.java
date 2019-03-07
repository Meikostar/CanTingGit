package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.ChooseBean;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.List;


public class ChooseAdapter extends BaseAdapter {
    private Context mContext;

    public ChooseAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    public List<ChooseBean> getDatas(){
        return list;
    }
    private LayoutInflater inflater;
    public void setDatas(List<ChooseBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<ChooseBean> list;


    public int getCount() {
        return list == null ?0 : list.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.choose_item_view, null);
            holder.content=view.findViewById(R.id.tv_content);
            holder.ll_bg=view.findViewById(R.id.ll_bg);
            holder.line=view.findViewById(R.id.line);
            holder.choose=view.findViewById(R.id.iv_chooses);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(position==0){
            holder.line.setVisibility(View.VISIBLE);
        }else {
            holder.line.setVisibility(View.GONE);
        }
        if(list.get(position).ischeck){
            holder.choose.setChecked(true);
        }else {
            holder.choose.setChecked(false);
        }
         holder.content.setText(list.get(position).content);
        return view;
        }



      class ViewHolder {
        MCheckBox choose;
        TextView content;
        View line;
        LinearLayout ll_bg;

    }
}
