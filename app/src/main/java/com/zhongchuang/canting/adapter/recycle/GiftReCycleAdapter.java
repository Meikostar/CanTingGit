package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.viewholder.CommentViewHolder;
import com.zhongchuang.canting.adapter.viewholder.GiftViewHolder;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mykar on 17/4/12.
 */
public class GiftReCycleAdapter extends BaseRecycleViewAdapter {

    private Map<Integer, Integer> map = new HashMap<>();

    private TakeawayListener listener;

    public interface TakeawayListener {
        void listener(int poistion);

        void clickType(int type);
    }

    private Context context;

    public GiftReCycleAdapter(Context hotContext) {
        context = hotContext;

    }




    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_itemview, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new GiftViewHolder(view);


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GiftViewHolder holder1 = (GiftViewHolder) holder;
        Hand data = (Hand) datas.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.gift_image)).asBitmap().placeholder(R.drawable.moren).into(holder1.img);
        if(TextUtil.isNotEmpty(data.user_nick_name)){
            holder1.tvName.setText(data.user_nick_name);
        }
//        if(TextUtil.isNotEmpty(data.integral)){
//            holder1.tvMoney.setText((TextUtil.isEmpty(data.gift_name)?"":data.gift_name)+"  "+data.integral);
//        }
        holder1.tvTime.setText(TimeUtil.formatTime(data.create_time));
    }

    @Override
    public int getItemCount() {
        int counts = 0;
        if (datas != null && datas.size() > 0) {
            counts = datas.size();
        }
        return counts;
    }
}
