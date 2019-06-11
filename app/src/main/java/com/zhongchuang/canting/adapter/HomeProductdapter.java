package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.recorder.util.gles.GeneratedTexture;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.CornerTransform;
import com.zhongchuang.canting.widget.NoScrollGridView;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class HomeProductdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Product> datas;


    private String[] names;


    public HomeProductdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Product> data) {
        this.datas = data;
        notifyDataSetChanged();


    }

    @Override
    public int getCount() {
        return datas!=null?(datas.size()<=6?datas.size():6):0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private HomessBasedapter adapter;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.home_product_item, null);
            holder.txt_name = view.findViewById(R.id.tv_name);
            holder.tv_money = view.findViewById(R.id.tv_money);
            holder.llbg = view.findViewById(R.id.ll_bg);
            holder.img = view.findViewById(R.id.iv_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Product data = datas.get(i);

        if (TextUtil.isNotEmpty(data.pro_name)) {
            holder.txt_name.setText(data.pro_name);
        }
        if (data.pro_site.equals("1")||data.pro_site.equals("3")) {
            if (TextUtil.isNotEmpty(data.pro_price)) {
                if(Integer.valueOf(data.integral_price)>0){
                    holder.tv_money.setText( data.pro_price+"+"+data.integral_price+"积分");
                }else {
                    holder.tv_money.setText( data.pro_price);
                }
            }
        } else {
            if (TextUtil.isNotEmpty(data.integral_price)) {
                holder.tv_money.setText("积分 " + data.integral_price);
            }
        }


        String[] splits = data.picture_url.split(",");

        Glide.with(context)
                .load(StringUtil.changeUrl(splits.length > 0 ? splits[0] : ""))
                .asBitmap()
                .skipMemoryCache(true)
                .placeholder(R.drawable.moren1)
                .error(R.drawable.moren1)
                .transform(new CornerTransform(context, DensityUtil.dip2px(8)))
                .into(holder.img);

        holder.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(data);
            }
        });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(Product url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    class ViewHolder {
        TextView txt_name;
        LinearLayout llbg;
        TextView tv_money;
        ImageView img;

    }
}
