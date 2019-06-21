package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.HOMES;
import com.zhongchuang.canting.been.LiveItemBean;
import com.zhongchuang.canting.been.TabEntity;
import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mykar on 161/4/13.
 */

public class VideoItemdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<VideoData>  datas;
    private int type = 0;//0 表示默认使用list数据
    private String types;


    private int[] imgs;


    private String[] names;

    public VideoItemdapter(Context context) {
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
        return datas != null ? datas.size() : 0;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {

            view = inflater.inflate(R.layout.live_new_item, null);
            holder = new ViewHolder(view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
      final   VideoData bean=datas.get(i);
        if(TextUtil.isNotEmpty(bean.secondName))
        holder.tvTitle.setText(bean.secondName);
        VideoItemItemdapter adapter=new VideoItemItemdapter(context);
        if(bean.video==null){
            adapter.setData(bean.directRoom);
        }else {
            adapter.setData(bean.video);
        }

        holder.listview.setAdapter(adapter);
        adapter.setOnItemClickListener(new VideoItemItemdapter.OnItemClickListener() {
            @Override
            public void onItemClick( int position, VideoData url) {
                mOnItemClickListener.onItemClick(position,url);
            }
        });
        holder.llMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(-1,bean);
            }
        });
        return view;
    }


    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(int position,VideoData url);
    }



    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    static  class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ll_more)
        LinearLayout llMore;
        @BindView(R.id.listview)
        ListView listview;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
