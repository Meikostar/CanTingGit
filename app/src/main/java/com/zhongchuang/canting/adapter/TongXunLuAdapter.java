package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class TongXunLuAdapter extends RecyclerView.Adapter<TongXunLuAdapter.ViewHolder> {


    @BindView(R.id.friend_line)
    RelativeLayout friendLine;
    private Context mContext;
    private ArrayList<Integer> mList;
    private  LayoutInflater inflater;


    public TongXunLuAdapter(Context context, ArrayList<Integer> mList) {
        this.mContext = context;
        this.mList = mList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mContext);
        View view =inflater.inflate(R.layout.liaotian_tongxun_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.tongxunluNicName.setText("阿娇"+position);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @OnClick(R.id.friend_line)
    public void onViewClicked() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tongxunluFriendPic;
        TextView tongxunluNicName;
        public ViewHolder(View itemView) {
            super(itemView);

            tongxunluFriendPic=itemView.findViewById(R.id.tongxunlu_friend_pic);
            tongxunluNicName=itemView.findViewById(R.id.tongxunlu_nicName);
        }
    }
}
