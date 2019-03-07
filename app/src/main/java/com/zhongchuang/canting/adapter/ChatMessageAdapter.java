package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FriendListBean> data;
    private int type = 0;//0 表示默认使用list数据


    public ChatMessageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<FriendListBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data != null ? data.size()+2 : 0;
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
        ChatMessageAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ChatMessageAdapter.ViewHolder();
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
            holder.tv_count = (TextView) view.findViewById(R.id.tv_count);
            holder.llbg = (LinearLayout) view.findViewById(R.id.ll_bg);
            holder.img_icon = (ImageView) view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ChatMessageAdapter.ViewHolder) view.getTag();
        }


        if(i==data.size()){
            holder.tv_count.setVisibility(View.GONE);
            holder.img_icon.setImageResource(R.drawable.qunjia);
            holder.txt_name.setText(context.getString(R.string.add));
            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(null,1);
                }
            });
        }else if(i==data.size()+1){
            holder.tv_count.setVisibility(View.GONE);
            holder.txt_name.setText(context.getString(R.string.yc));
            holder.img_icon.setImageResource(R.drawable.qunjian);
            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(null,2);
                }
            });
        }else {
            Glide.with(context).load(StringUtil.changeUrl(data.get(i).head_image)).asBitmap().placeholder(R.drawable.ease_default_avatar).into(holder.img_icon);
            if(TextUtil.isNotEmpty(data.get(i).chat_user_id)){
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(data.get(i).chat_user_id);
                if(conversation!=null){
                    int unreadMsgCount = conversation.getUnreadMsgCount();
                    if(unreadMsgCount>0&&unreadMsgCount<100){
                        holder.tv_count.setText(unreadMsgCount+"");
                        holder.tv_count.setVisibility(View.VISIBLE);
                    }else if(unreadMsgCount>100){
                        holder.tv_count.setText("99+");
                        holder.tv_count.setVisibility(View.VISIBLE);
                    }else {
                        holder.tv_count.setVisibility(View.GONE);
                    }
                }else {
                    holder.tv_count.setVisibility(View.GONE);
                }

            }

            holder.llbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(data.get(i), 0);
                }
            });
            if (TextUtil.isNotEmpty(data.get(i).remark_name)) {
                holder.txt_name.setText(data.get(i).remark_name);
            } else {
                if (TextUtil.isNotEmpty(data.get(i).nickname)) {
                    holder.txt_name.setText(data.get(i).nickname);
                }

            }
        }




        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(FriendListBean url, int type);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private ChatMenberBasedapter.OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(ChatMenberBasedapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder {
        LinearLayout llbg;
        TextView txt_name;
        TextView tv_count;
        ImageView img_icon;

    }
}
