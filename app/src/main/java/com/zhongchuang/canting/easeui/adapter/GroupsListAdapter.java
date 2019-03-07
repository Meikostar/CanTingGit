package com.zhongchuang.canting.easeui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.easeui.bean.GROUP;
import com.zhongchuang.canting.easeui.widget.NineGridImageView;
import com.zhongchuang.canting.easeui.widget.NineGridImageViewAdapter;
import com.zhongchuang.canting.easeui.widget.WebImageView;

import java.util.List;

//import com.yshstudio.concrete.R;
//import com.yshstudio.concrete.widget.groupavater.NineGridImageView;
//import com.yshstudio.concrete.widget.groupavater.NineGridImageViewAdapter;

/**
 * Created by syj on 2016/11/30.
 */
public class GroupsListAdapter extends BaseAdapter {


    private NineGridImageViewAdapter adapter;
    protected Context mContext;
    protected List<GROUP> mDatas;
    protected LayoutInflater inflater;
    protected int layoutId;

    public GroupsListAdapter(Context context, List<GROUP> datas) {
        mContext = context;
        mDatas = datas;
        inflater = LayoutInflater.from(context);
        this.layoutId = R.layout.em_item_group_list;
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
    }
   public void setData( List<GROUP> datas){
       mDatas = datas;
       notifyDataSetChanged();
   }
    @Override
    public GROUP getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder;
        if (convertView == null) {
            viewholder = new Viewholder();
            convertView = inflater.inflate(layoutId, null);
            viewholder.group_avatar = (NineGridImageView) convertView.findViewById(R.id.group_avatar);
            viewholder.group_name = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewholder);
        } else viewholder = (Viewholder) convertView.getTag();
        viewholder.group_name.setText(mDatas.get(position).groupname+"("+mDatas.get(position).headimage.size()+")");
        viewholder.group_avatar.setAdapter(new NineGridImageViewAdapter() {
            @Override
            protected void onDisplayImage(Context context, WebImageView imageView, Object o) {
                if (TextUtils.isEmpty((String) o)) {
                    imageView.setImageResource(R.drawable.dingdantouxiang);
                    return;
                }
                Glide.with(context).load((String)o).asBitmap().placeholder(R.drawable.dingdantouxiang).into(imageView);
//                imageView.setImageWithURL(context,(String)o,R.drawable.dingdantouxiang);
            }
        });
        viewholder.group_avatar.setImagesData(mDatas.get(position).headimage);
        return convertView;
    }

    private static class Viewholder {
        public NineGridImageView group_avatar;
        public TextView group_name;
    }
}
