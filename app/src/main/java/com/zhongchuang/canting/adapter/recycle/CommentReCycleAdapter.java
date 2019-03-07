package com.zhongchuang.canting.adapter.recycle;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.viewholder.CommentViewHolder;
import com.zhongchuang.canting.been.CommentBean;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mykar on 17/4/12.
 */
public class CommentReCycleAdapter extends BaseRecycleViewAdapter {
    public final static int ITEM_HEAD = 99;
    public final static int ITEM_HOT = 100;
    public int ALL = 1;
    public int NEAR = 2;
    public int ROW = 3;
    private Map<Integer, Integer> map = new HashMap<>();

    private TakeawayListener listener;

    public interface TakeawayListener {
        void listener(int poistion);

        void clickType(int type);
    }

    private int count=3;

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
                listener.listener(0);
            }
        });
        return new CommentViewHolder(view);



    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CommentViewHolder holder1 = (CommentViewHolder) holder;


    }

    @Override
    public int getItemCount() {
        int counts =0 ;
        if (datas != null && datas.size() > 0) {
            count = datas.size();
        }
        return counts;
    }
}
