package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.recycle.BaseRecycleViewAdapter;
import com.zhongchuang.canting.adapter.viewholder.CommentViewHolder;
import com.zhongchuang.canting.adapter.viewholder.ImageViewHolder;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by mykar on 17/4/12.
 */
public class HandGitsAdapter extends BaseAdapter {


    private List<Hand> datas;
    @Override
    public int getCount() {
        int counts = 0;
        if (datas != null && datas.size() > 0) {
            counts = datas.size();
        }
        return counts;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private View view;
    private CommentViewHolder holder1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (view == null) {
            view = View.inflate(context, R.layout.live_hand_gift, null);
            holder1 = new CommentViewHolder(view);
            view.setTag(holder1);
        } else {
            holder1 = (CommentViewHolder) view.getTag();
        }
        Hand data = datas.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.head_image)).asBitmap().placeholder(R.drawable.moren2).into(holder1.img);
        if(TextUtil.isNotEmpty(data.user_nick_name)){
            holder1.tvName.setText(context.getString(R.string.ncs)+data.user_nick_name);
        }
        if(TextUtil.isNotEmpty(data.integralTotal)){
            holder1.tvMoney.setText(data.integralTotal);
        }
        return view;
    }



    private Context context;

    public HandGitsAdapter(Context hotContext) {
        context = hotContext;

    }

    private int count = 3;
    public void setDatas(List<Hand> datas){
        this.datas=datas;

    }





}
