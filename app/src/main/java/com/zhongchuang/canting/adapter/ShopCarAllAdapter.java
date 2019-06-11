package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Product;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RegularListView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class ShopCarAllAdapter extends BaseAdapter {

    private Context mContext;
    private List<Product> shopList;



    public ShopCarAllAdapter(Context mContext) {
        this.mContext = mContext;


    }

    public void setData(List<Product> list) {
        shopList=list;
        notifyDataSetChanged();
    }



   public List<Product>  getDatas(){
       return shopList;
   }

    @Override
    public int getCount() {
        return shopList!=null?shopList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ShopCarHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.shopcar_items, null);
            holder = new ShopCarHolder();
            holder.listView = view.findViewById(R.id.listview);

            view.setTag(holder);

        } else {
            holder = (ShopCarHolder) view.getTag();
        }
        final ShopCarAdapter adapter=new ShopCarAdapter(mContext);
        adapter.setData(shopList.get(position).protList);
        adapter.setOnCheckAllListener(new ShopCarAdapter.onCheckAllListener() {
            @Override
            public void checks(String data) {
                if(TextUtil.isNotEmpty(data)){
                    checkListener.checks(data);
                }else {
                    shopList.get(position).protList=adapter.getData();
                    checkListener.checks(null);
                }

            }
        });
        holder.listView.setAdapter(adapter);

        return view;
    }


   public static class ShopCarHolder {

       RegularListView listView;



    }



    public interface onCheckAllListener {
        void checks(String data);
    }

    private onCheckAllListener checkListener;

    public void setOnCheckAllListener(onCheckAllListener listener) {
        this.checkListener = listener;
    }
}
