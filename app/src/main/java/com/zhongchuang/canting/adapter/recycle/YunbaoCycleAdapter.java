package com.zhongchuang.canting.adapter.recycle;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.adapter.viewholder.YuanbaoViewHolder;
import com.zhongchuang.canting.been.CateCaBean;
import com.zhongchuang.canting.been.SHOP;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by mykar on 17/4/12.
 */
public class YunbaoCycleAdapter extends BaseRecycleViewAdapter {
    public final static int ITEM_HEAD = 99;
    public final static int ITEM_HOT = 100;
    public int ALL = 1;
    public int NEAR = 2;
    public int ROW = 3;
    private Context context;
    private Map<Integer, CateCaBean> map = new HashMap<>();

    private TakeawayListener listener;

    public YunbaoCycleAdapter(Context context) {
        this.context = context;

    }

    public interface TakeawayListener {
        void listener(int poistion);

        void clickType(int type);
    }

    private int count = 0;

    public void setcout(int cout) {
        this.count = cout;
    }

    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yuanbao_item, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.listener(0);
            }
        });
        return new YuanbaoViewHolder(view);

//        }


    }

    public CountDownTimer countDownTimer;

    public void setTimerCancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {

        SHOP shop = (SHOP) datas.get(position);
        YuanbaoViewHolder holder= (YuanbaoViewHolder) holders;
        holder.img.setImageResource(shop.img);
        holder.tvMoney.setText(shop.price);
        holder.tvName.setText(shop.name);


    }

    public int getTotal() {
        int total = 0;
        for (int i = 0; i < map.size(); i++) {
            total = total + map.get(i).count;
        }
        return total;
    }
    public double getTotalSum() {
        double total = 0;
        for (int i = 0; i < map.size(); i++) {
            if(map.get(i).count>0){
                total = total + (map.get(i).count)*(map.get(i).greens_price);
            }

        }
        return total;
    }
    //    @Override
//    public int getItemViewType(int position) {
//
//        if (position == 0) {
//            return ITEM_HEAD;
//        } else {
//            return ITEM_HOT;
//        }
//    }
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
