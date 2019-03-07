package com.zhongchuang.canting.easeui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/11/15.
 */
public class MyViewHolder {
	public SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public MyViewHolder(Context context, ViewGroup parent, int layoutId,
                        int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static MyViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {

			return new MyViewHolder(context, parent, layoutId, position);
		} else {
			MyViewHolder holder = (MyViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}

	}

	/** 通过viewId获取控件 */
	public <T extends View> T getView(int Id) {

		View view = mViews.get(Id);
		if (view == null) {
			view = mConvertView.findViewById(Id);
			mViews.put(Id, view);
		}

		return (T) view;
	}

	public View getConvertView() {

		return mConvertView;
	}

}
