package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.viewholder.CommentViewHolder;
import com.zhongchuang.canting.been.CommentBean;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.Hands;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mykar on 17/4/12.
 */
public class HandGitReCycleAdapter extends BaseRecycleViewAdapter {

    private Map<Integer, Integer> map = new HashMap<>();

    private TakeawayListener listener;

    public interface TakeawayListener {
        void listener(int poistion);

        void clickType(int type);
    }

    private Context context;

    public HandGitReCycleAdapter(Context hotContext) {
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_itemview, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.listener(0);
            }
        });
        return new CommentViewHolder(view);


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommentViewHolder holder1 = (CommentViewHolder) holder;
        Hand data = (Hand) datas.get(position);
        Glide.with(context).load(StringUtil.changeUrl(data.head_image)).asBitmap().placeholder(R.drawable.moren2).into(holder1.img);
        if(TextUtil.isNotEmpty(data.user_nick_name)){
            holder1.tvName.setText(context.getString(R.string.ncs)+data.user_nick_name);
        }
        if(TextUtil.isNotEmpty(data.integralTotal)){
            holder1.tvMoney.setText(data.integralTotal);
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
