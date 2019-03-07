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
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.Catage;

import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class GirdTypedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Catage> list;



    private int[] imgs;


    private String[] names;


    public GirdTypedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Catage> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.shop_grid_item, null);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            holder.llbg = (LinearLayout) view.findViewById(R.id.ll_bg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (TextUtil.isNotEmpty(list.get(i).sec_category_name)) {
            holder.txt_name.setText(list.get(i).sec_category_name);
        }


        Glide.with(context).load(StringUtil.changeUrl(list.get(i).category_img)).asBitmap().placeholder(R.drawable.moren1).into(holder.img_icon);
         holder.llbg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.itemClick(list.get(i).sec_category_name);
             }
         });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }
   public void setListener(ItemClickListener listener){
       this.listener=listener;
   }
   private ItemClickListener listener;
    public interface ItemClickListener {
        void itemClick(String cont);
    }
    class ViewHolder {
        TextView txt_name;
        ImageView img_icon;
        LinearLayout llbg;

    }
}
