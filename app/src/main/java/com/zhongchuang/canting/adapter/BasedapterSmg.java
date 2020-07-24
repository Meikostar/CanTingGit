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
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Smgapply;

import java.util.List;

public class BasedapterSmg extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<Smgapply>    data;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;



    private String[] names;

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setTypes(String types) {
        this.types = types;
        notifyDataSetChanged();
    }

    public BasedapterSmg(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<Smgapply> data) {
        this.data=data;

    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
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
        if(!TextUtils.isEmpty(data.get(i).title)){
            holder.txt_name.setText(data.get(i).title);
        }
//        Glide.with(context).load(changeUrl(data.get(i).application_image_url)).asBitmap().placeholder(R.drawable.moren).into(holder.img_icon);

//        GlideUtils.getInstances().loadRoundCornerImg(context, holder.img_icon, 2.0f,data.get(i).img,R.drawable.moren_product);
        Glide.with(context).load("http://bbsc.2aa6.com/uploads/"+data.get(i).img).asBitmap().placeholder(R.drawable.moren1).into(holder.img_icon);

        holder.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(data.get(i));
            }
        });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(Smgapply url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    class ViewHolder {
        LinearLayout llbg;
        TextView     txt_name;
        ImageView    img_icon;

    }
    public static String changeUrl(String url){
        String urls=null;
        if(TextUtils.isEmpty(url)){
            urls="";
        }else {
            if(url.contains("http://pa7efx2i6.bkt.clouddn.com")){
                urls=url.replace("http://pa7efx2i6.bkt.clouddn.com","http://chushenduojin.cn");
            } else  if(url.contains("http://pifb36x2h.bkt.clouddn.com")){
                urls=url.replace("http://pifb36x2h.bkt.clouddn.com","http://chushenduojin.cn");
            }else {
                urls=url;
            }
        }
        return urls;
    }
}
