package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.recycle.VideoCycleAdapter;
import com.zhongchuang.canting.been.PREFIX;
import com.zhongchuang.canting.been.Param;
import com.zhongchuang.canting.been.videobean;
import com.zhongchuang.canting.utils.PinYinUtils;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class VideoLiveAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;


    public VideoLiveAdapter(Context context) {
        this.mContext = context;

    }
    private List<videobean> list;
    public void setData(List<videobean> list) {
        this.list = list;
        notifyDataSetChanged();
    }






    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public videobean getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, R.layout.video_item_view, null);
            holder = new CityViewHolder();
            holder.hotPic = view.findViewById(R.id.hot_pic);
            holder.cardView = view.findViewById(R.id.card);
            holder.tv_time = view.findViewById(R.id.tv_time);
            holder.tv_desc = view.findViewById(R.id.tv_desc);
            holder.rl_bg = view.findViewById(R.id.rl_bg);
            holder.ll_bgs = view.findViewById(R.id.ll_bgs);
            view.setTag(holder);
        } else {
            holder = (CityViewHolder) view.getTag();
        }
        videobean shop=list.get(position);
        Glide.with(mContext).load(StringUtil.changeUrl(shop.cover_image)).asBitmap().placeholder(R.drawable.moren3).into(holder.hotPic);
        if (TextUtil.isNotEmpty(shop.video_name)) {
            holder.tv_desc.setText(shop.video_name);
        }
        holder.ll_bgs.setVisibility(View.GONE);
        holder.tv_time.setText(TimeUtil.formatChatTime(shop.create_time));
        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position);
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position);
            }
        });

        return view;
    }
    public interface TakeawayListener {
        void listener(int poistion);

    }


    private TakeawayListener listener;
    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }
    public static class CityViewHolder {
        ImageView hotPic;


        TextView tv_time;
        TextView tv_desc;
        LinearLayout rl_bg;
        LinearLayout ll_bgs;
        CardView cardView;
    }




}
