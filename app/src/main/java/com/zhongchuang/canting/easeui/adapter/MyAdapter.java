package com.zhongchuang.canting.easeui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2015/11/15.
 */
public abstract class MyAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater inflater;
	protected int layoutId;

	public MyAdapter(Context context, List<T> datas, int layoutId) {
		mContext = context;
		mDatas = datas;
		inflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		} else {
			return 0;
		}

	}
   public void setmDatas(List<T> datas){
	   this.mDatas=datas;
	   notifyDataSetChanged();
   }

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder holder = MyViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(holder, mDatas.get(position), position);
		return holder.getConvertView();
	}

	public abstract void convert(MyViewHolder holder, T t, int position);

	protected void showToast(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}
}
