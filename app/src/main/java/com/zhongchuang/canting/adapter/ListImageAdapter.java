package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.IMG;
import com.zhongchuang.canting.utils.StringUtil;

import java.util.List;


/**
 * Created by xzh on 2016/12/21.
 */

public class ListImageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> list;
    IMG guide_img = new IMG();//导视图
    public ListImageAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }
   public void setData(List<String> list){
       for(String url:list){
           IMG tem_img = new IMG();
           tem_img.type = IMG.IMG_URL;
           tem_img.img_url = url;
           guide_img.child_list.add(tem_img);
       }
       this.list = list;
       notifyDataSetChanged();
   }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final   ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lp_griditem_image, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.image.setScaleType(ScaleType.CENTER_CROP);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(StringUtil.changeUrl(list.get(position))).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                double w=(resource.getWidth())*1.0;
                int h=resource.getHeight();
                double fx=width/w;
                int height= (int) (h*fx);


                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,height);
                holder.image.setLayoutParams(params);
                holder.image.setImageBitmap(resource);
            }
        });
       // Glide.with(mContext).load( ).asBitmap().placeholder(R.drawable.all_img).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ImageListWitesActivity.class);
//                intent.putExtra("img", guide_img);
//                intent.putExtra("position", position);
//                mContext.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {
        ImageView image;
    }
}
