package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.mall.ShopDetailActivity;
import com.zhongchuang.canting.activity.mall.ShopMallDetailActivity;
import com.zhongchuang.canting.been.Favor;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.SwipeListLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by honghouyang on 16/12/23.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Favor> list;
    private int type;
    private ListView lv_content;
    private Set<SwipeListLayout> sets = new HashSet();
    private selectItemListener listener;

    public interface selectItemListener {
        void delete(String id, int type, int poistion);
    }

    public void setListener(selectItemListener listener) {
        this.listener = listener;
    }

    public void setData(List<Favor> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Favor> getDatas() {
        return list;
    }

    public CollectionAdapter(Context context, List<Favor> list, ListView lv_content) {
        this.lv_content = lv_content;
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        lv_content.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int getCount() {
        return list != null ? (list.size()) : 0;
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
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_collection, parent, false);
            holder.tvTime = convertView.findViewById(R.id.tv_link);
            holder.tv_phone = convertView.findViewById(R.id.tv_phone);
            holder.tv_delete = convertView.findViewById(R.id.tv_delete);
            holder.tvAdress = convertView.findViewById(R.id.tv_address);
            holder.tvName = convertView.findViewById(R.id.tv_name);

            holder.ivImg = convertView.findViewById(R.id.iv_img);
            holder.line = convertView.findViewById(R.id.line);

            holder.rl_bg = convertView.findViewById(R.id.rl_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final SwipeListLayout swipeListLayout = (SwipeListLayout) convertView;

        swipeListLayout.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                swipeListLayout));

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                swipeListLayout.setStatus(SwipeListLayout.Status.Close, true);
                if (listener != null) {
                    listener.delete(list.get(position).shop_id, list.size() == 1 ? 1 : 0, position);
                }
            }
        });

        if (list.size() == 1 || position == list.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }

        if (TextUtil.isNotEmpty(list.get(position).link_man)) {
            holder.tvTime.setText(context.getString(R.string.lxrs) + list.get(position).link_man);
        }
        if (TextUtil.isNotEmpty(list.get(position).mer_phone)) {
            holder.tv_phone.setText(context.getString(R.string.dhs) + list.get(position).mer_phone);
        }
        if (TextUtil.isNotEmpty(list.get(position).mer_name)) {
            holder.tvName.setText(list.get(position).mer_name);
        }
        Glide.with(context).load(StringUtil.changeUrl(list.get(position).mer_Image_url)).asBitmap().placeholder(R.drawable.moren1).into(holder.ivImg);
           holder.rl_bg.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, ShopDetailActivity.class);

                   intent.putExtra("id", list.get(position).m_id);
                   context.startActivity(intent);
               }
           });
        return convertView;
    }

    class ViewHolder {
        View line;
        View view;

        TextView tvTime;
        TextView tvAdress;
        TextView tv_phone;
        TextView tvName;
        ImageView ivImg;
        TextView tv_delete;

        RelativeLayout rl_bg;
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
}
