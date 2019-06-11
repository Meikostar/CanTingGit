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
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.apply;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class ChatMenberBasedapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FriendListBean> data;
    private int type = 0;//0 表示默认使用list数据


    public ChatMenberBasedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<FriendListBean> data) {
       this.data=data;
      notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data!=null?data.size()+2:0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
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
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.llbg = view.findViewById(R.id.ll_bg);
            holder.img_icon = view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(i==data.size()){
            holder.img_icon.setImageResource(R.drawable.qunjia);
            holder.txt_name.setText(context.getString(R.string.add));
            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(null,1);
                }
            });
        }else if(i==data.size()+1){
            holder.txt_name.setText(R.string.yc);
            holder.img_icon.setImageResource(R.drawable.qunjian);
            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(null,2);
                }
            });
        }else {
            Glide.with(context).load(StringUtil.changeUrl(data.get(i).head_image)).asBitmap().placeholder(R.drawable.ease_default_avatar).into(holder.img_icon);

            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(data.get(i),0);
                }
            });
            if(TextUtil.isNotEmpty(data.get(i).remark_name)){
                holder.txt_name.setText(data.get(i).remark_name);
            }else {
                if(TextUtil.isNotEmpty(data.get(i).nickname)){
                    holder.txt_name.setText(data.get(i).nickname);
                }

            }
        }

        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick( FriendListBean url,int type);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder {
        LinearLayout llbg;
        TextView txt_name;
        ImageView img_icon;

    }
}
