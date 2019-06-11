package com.zhongchuang.canting.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.chat.AddFriendActivity;
import com.zhongchuang.canting.activity.mine.NewPersonDetailActivity;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendSearchBean;

import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.utils.GlideRoundTransform;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/11/23.
 */

public class Liaotian_haoyouRecAdapter extends RecyclerView.Adapter<Liaotian_haoyouRecAdapter.ViewHolder> {

    @BindView(R.id.friend_item_line)
    LinearLayout friendItemLine;
    @BindView(R.id.friend_item_pic)
    ImageView friendItemPic;//头像

    @BindView(R.id.friend_item_tex)
    TextView friendItemTex;//添加好友text
    private Activity mContext;
    private int index;//当前选中item的下标


    private List<FriendSearchBean.DataBean> lists;


    public Liaotian_haoyouRecAdapter(Activity mContext, List<FriendSearchBean.DataBean> lists) {
        this.mContext = mContext;
        this.lists = lists;
    }

    public void setData(List<FriendSearchBean.DataBean> data) {

        if(data!=null&&data.size()>0){
            lists.clear();
            lists.addAll(data);
            notifyDataSetChanged();
        }

    }

    public void clear(){
        lists.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemAddListener {
        void onItemClick(FriendSearchBean.DataBean dataBean);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }


    private OnItemClickListener mOnItemClickListener;
    private OnItemAddListener mOnItemAddListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public void setOnAddClickListener(OnItemAddListener mOnItemClickListener) {
        this.mOnItemAddListener = mOnItemClickListener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //此方法需慎用
        //View view = View.inflate(mContext, R.layout.liaotian_friend_item, parent);

        View view = LayoutInflater.from(mContext).inflate(R.layout.liaotian_friend_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final FriendSearchBean.DataBean dataBean = lists.get(position);
        //好友图片
        Glide.with(mContext).load(StringUtil.changeUrl(dataBean.getHeadImage()))
                .placeholder(R.drawable.ease_default_avatar)
                .error(R.drawable.ease_default_avatar)
                .transform(new GlideRoundTransform(mContext,10))
                .into(holder.friendItemPic);

        //holder.friendItemPic.setImageResource(R.mipmap.liaotian_tongxun);

        //好友昵称
        holder.friendItemNicName.setText(dataBean.getNickname());
        //好友个性签名
        holder.friendItemQianmin.setText(dataBean.getPersonalitySign());
        if(TextUtil.isNotEmpty(dataBean.isFriends)){
            holder.friendItemTex.setVisibility(View.VISIBLE);
            if(dataBean.isFriends.equals("0")){
                holder.friendItemTex.setText(mContext.getString(R.string.tjhy));
                holder.friendItemTex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LogUtil.d("添加好友的ID = " + dataBean.getRingLetterName());


                        mOnItemAddListener.onItemClick(dataBean);

                    }
                });
            }else {
                holder.friendItemTex.setText(R.string.tst);
                holder.friendItemTex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent intentc = new Intent(mContext, ChatActivity.class);
                        intentc.putExtra("userId",dataBean.getRingLetterName());
                        mContext.startActivity(intentc);

                    }
                });
                holder.llbg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intentc = new Intent(mContext, ChatActivity.class);
                        intentc.putExtra("userId",dataBean.getRingLetterName());
                        mContext.startActivity(intentc);

                    }
                });
            }
        }

       holder.friendItemPic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(mContext, NewPersonDetailActivity.class);
               intent.putExtra("id", dataBean.getRingLetterName() + "");
               mContext.startActivity(intent);
           }
       });
    //子条目点击事件
    //判断是否设置了监听器
        if(mOnItemClickListener !=null)

    {
//        //为ItemView设置监听器
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getLayoutPosition(); // 1
//                index = position;
//                mOnItemClickListener.onItemClick(holder.itemView, position); // 2
//            }
//        });
    }
        if(mOnItemLongClickListener !=null)

    {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                //返回true 表示消耗了事件 事件不会继续传递
                return true;
            }
        });
    }

}

    @Override
    public int getItemCount() {
        return lists.size();
    }


    @OnClick({R.id.friend_item_pic })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.friend_item_pic:
                break;
//            case R.id.friend_item_tex:
//                try {
//                    EMClient.getInstance().contactManager().addContact(nicNameList.get(index), "可以认识一下吗");
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }

//                break;
        }
    }

public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView friendItemPic;//头像
    TextView friendItemNicName;//昵称
    TextView friendItemQianmin;//签名
    TextView friendItemTex;//添加好友text
    LinearLayout llbg;//添加好友text


    public ViewHolder(View itemView) {
        super(itemView);
        friendItemPic = itemView.findViewById(R.id.friend_item_pic);
        friendItemNicName = itemView.findViewById(R.id.friend_item_nicName);
        friendItemQianmin = itemView.findViewById(R.id.friend_item_qianmin);
        friendItemTex = itemView.findViewById(R.id.friend_item_tex);
        llbg = itemView.findViewById(R.id.friend_item_line);

    }
}




}
