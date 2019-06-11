package com.zhongchuang.canting.adapter.recycle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mykar on 17/4/12.
 */
public class ChatDelReCycleAdapter extends BaseRecycleViewAdapter {


    private Map<Integer, Integer> map = new HashMap<>();

    private TakeawayListener listener;

    public interface TakeawayListener {
        void listener(int poistion);

        void clickType(int type);
    }

    private Context context;

    public ChatDelReCycleAdapter(Context hotContext) {
        context = hotContext;

    }

    private int count = 3;

    public void setcout(int cout) {
        this.count = cout;
    }

    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_detail, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.listener(0);
            }
        });
        return new ViewHolder(view);


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        Hand data = (Hand) datas.get(position);

            holder1.tvTime.setText(com.zhongchuang.canting.utils.TimeUtil.formatChatTime(data.startTime));

            holder1.tvTimes.setText(com.zhongchuang.canting.utils.TimeUtil.formatChatTime(data.endTime));
         if(TextUtil.isNotEmpty(data.remarkName)){
        long time=data.endTime-data.startTime;
        boolean  chat=time>60000;
             if(data.chatType.equals("1")){
                 if(chat){
                     holder1.tvCont.setText(context.getString(R.string.y)+data.remarkName+context.getString(R.string.yyth)+(time/60000)+context.getString(R.string.fenzhong));
                 }else {
                     holder1.tvCont.setText(context.getString(R.string.y)+data.remarkName+context.getString(R.string.yyth)+time/1000+context.getString(R.string.m));
                 }

             }else {
                 if(chat){
                     holder1.tvCont.setText(context.getString(R.string.y)+data.remarkName+context.getString(R.string.spth)+(time/60000)+context.getString(R.string.fenzhong));
                 }else {
                     holder1.tvCont.setText(context.getString(R.string.y)+data.remarkName+context.getString(R.string.spth)+time/1000+context.getString(R.string.m));
                 }
             }
         }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_times)
        TextView tvTimes;
        @BindView(R.id.tv_cont)
        TextView tvCont;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
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
