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
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.HashMap;
import java.util.Map;


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
        void listener(int poistion,int type);

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

   public void setType(int type){
        this.type=type;
   }
  private int type;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {

        videobean shop = (videobean) datas.get(position);
        ViewHolder holder = (ViewHolder) holders;
        Glide.with(context).load(StringUtil.changeUrl(shop.cover_image)).asBitmap().placeholder(R.drawable.moren).into(holder.hotPic);
        if (TextUtil.isNotEmpty(shop.video_name)) {
            holder.tv_desc.setText(shop.video_name);
        }

        holder.tv_time.setText(TimeUtil.formatChatTime(shop.create_time));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position,0);

            }
        });
        if(shop.new_type.equals("0")){
            holder.tv_change.setText("切换全屏");
            holder.choose.setChecked(false);
        }else {
            holder.tv_change.setText("还原视频");
            holder.choose.setChecked(true);
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.listener(position,1);
                return true;

            }
        });
        holder.rl_bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.listener(position,1);
                return true;

            }
        });

        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position,0);
            }
        });

        holder.ll_bgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(position,-2);
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
        TextView tv_change;
        MCheckBox choose;
        LinearLayout rl_bg;
        LinearLayout ll_bgs;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            hotPic = itemView.findViewById(R.id.hot_pic);
            cardView = itemView.findViewById(R.id.card);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_change = itemView.findViewById(R.id.tv_change);
            rl_bg = itemView.findViewById(R.id.rl_bg);
            choose = itemView.findViewById(R.id.iv_choose);
            ll_bgs = itemView.findViewById(R.id.ll_bgs);

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
