package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.viewholder.ShopViewHolder;
import com.zhongchuang.canting.been.CateCaBean;
import com.zhongchuang.canting.been.SHOP;
import com.zhongchuang.canting.been.videobean;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;

;


/**
 * Created by mykar on 17/4/12.
 */
public class VideoCycleAdapter extends BaseRecycleViewAdapter {


    private Context context;
    private Map<Integer, CateCaBean> map = new HashMap<>();

    private TakeawayListener listener;

    public VideoCycleAdapter(Context context) {
        this.context = context;

    }

    public interface TakeawayListener {
        void listener(int poistion);

    }



    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_view, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.listener(0);
            }
        });
        return new ViewHolder(view);

//        }


    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {

        videobean shop = (videobean) datas.get(position);
        ViewHolder holder = (ViewHolder) holders;
        Glide.with(context).load(StringUtil.changeUrl(shop.cover_image)).asBitmap().placeholder(R.drawable.moren).into(holder.hotPic);
        if (TextUtil.isNotEmpty(shop.video_name)) {
            holder.tv_desc.setText(shop.video_name);
        }

        holder.tv_time.setText(TimeUtil.formatChatTime(shop.create_time));
        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position);
            }
        });

    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < map.size(); i++) {
            total = total + map.get(i).count;
        }
        return total;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotPic;


        TextView tv_time;
        TextView tv_desc;
        LinearLayout rl_bg;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            hotPic = itemView.findViewById(R.id.hot_pic);
            cardView = itemView.findViewById(R.id.card);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            rl_bg = itemView.findViewById(R.id.rl_bg);

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

    public ChangeListener changeListener;

    public void setOnChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }

    public interface ChangeListener {
        void changeListener(int total, double sum, String shop_gre_set_id, int type);//type==1增加 2减
    }
}
