package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mykar on 161/4/13.
 */

public class VideoItemItemdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<VideoData>  datas;
    private int type = 0;//0 表示默认使用list数据
    private String types;


    private int[] imgs;


    private String[] names;

    public VideoItemItemdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<VideoData> list) {
        this.datas = list;
        notifyDataSetChanged();
    }
    private int state;

    public void setType(int state) {
        this.state = state;
    }

    @Override
    public int getCount() {
        return datas != null ? (datas.size()%2==0?(datas.size()/2):((datas.size()/2)+1)): 0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.video_item_item, null);
            holder = new ViewHolder(view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

            if(2*i==datas.size()-1){
                holder.rlVideo1.setVisibility(View.GONE);
                holder.tvContent1.setVisibility(View.GONE);
            }else {
                holder.tvContent1.setVisibility(View.VISIBLE);
                final VideoData dataBean = datas.get(2*i+1);
                if(TextUtil.isNotEmpty(dataBean.cover_image)){
                    Glide.with(context).load(StringUtil.changeUrl(dataBean.cover_image)).asBitmap().placeholder(R.drawable.moren).into(holder.ivVideo1);

                }else {
                    Glide.with(context).load(StringUtil.changeUrl(dataBean.room_image)).asBitmap().placeholder(R.drawable.moren).into(holder.ivVideo1);

                }
                //直播昵称
                //String nicName=liveNicName.get(position);
                if (TextUtil.isNotEmpty(dataBean.nickname)) {
                    holder.tvName1.setText(dataBean.nickname);
                } else if (TextUtil.isNotEmpty(dataBean.user_info_nickname)){
                    holder.tvName1.setText(dataBean.user_info_nickname);
                }

                if (TextUtil.isNotEmpty(dataBean.third_category_name)) {
                    holder.tvType1.setText(dataBean.third_category_name);
                }
                if (TextUtil.isNotEmpty(dataBean.video_name)) {
                    holder.tvContent1.setText(dataBean.video_name);
                } else if (TextUtil.isNotEmpty(dataBean.direct_overview)){
                    holder.tvContent1.setText(dataBean.direct_overview);
                }else {
                    holder.tvContent1.setText("");
                }
                holder.rlVideo1.setVisibility(View.VISIBLE);
                holder.rlVideo1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(i,dataBean);
                    }
                });
            }
          final   VideoData dataBean = datas.get(2*i);
        if(TextUtil.isNotEmpty(dataBean.cover_image)){
            Glide.with(context).load(StringUtil.changeUrl(dataBean.cover_image)).asBitmap().placeholder(R.drawable.moren).into(holder.ivVideo);

        }else {
            Glide.with(context).load(StringUtil.changeUrl(dataBean.room_image)).asBitmap().placeholder(R.drawable.moren).into(holder.ivVideo);

        }
        //直播昵称
        //String nicName=liveNicName.get(position);
        if (TextUtil.isNotEmpty(dataBean.nickname)) {
            holder.tvName.setText(dataBean.nickname);
        } else if (TextUtil.isNotEmpty(dataBean.user_info_nickname)){
            holder.tvName.setText(dataBean.user_info_nickname);
        }

        if (TextUtil.isNotEmpty(dataBean.third_category_name)) {
            holder.tvType.setText(dataBean.third_category_name);
        }
        if (TextUtil.isNotEmpty(dataBean.video_name)) {
            holder.tvContent.setText(dataBean.video_name);
        } else if (TextUtil.isNotEmpty(dataBean.direct_overview)){
            holder.tvContent.setText(dataBean.direct_overview);
        }else {
            holder.tvContent.setText("");
        }
        holder.rlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(i,dataBean);
            }
        });






        return view;
    }


    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick( int position, VideoData data);
    }



    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static  class ViewHolder {

        @BindView(R.id.iv_video)
        ImageView ivVideo;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.rl_video)
        RelativeLayout rlVideo;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_video1)
        ImageView ivVideo1;
        @BindView(R.id.tv_type1)
        TextView tvType1;
        @BindView(R.id.tv_name1)
        TextView tvName1;
        @BindView(R.id.tv_count1)
        TextView tvCount1;
        @BindView(R.id.rl_video1)
        RelativeLayout rlVideo1;
        @BindView(R.id.tv_content1)
        TextView tvContent1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
