package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.HomeActivitys;
import com.zhongchuang.canting.activity.mall.BanDetailActivity;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.DATA;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class BannerAdapters extends BannerBaseAdapter {

    private List<DATA> list;
    private int type=0;//1是用药记录item

    public BannerAdapters(Context context) {
        super(context);
    }
    private int high;
    public BannerAdapters(Context context,int with) {
        super(context);
        high=with;
    }

    public interface ItemCliks{
        void getItem(DATA menu, int type);//type 1表示点击事件2 表示长按事件
    }
    private ItemCliks listener;
    public void setClickListener(ItemCliks listener){
        this.listener=listener;
    }



    @Override
    protected int getLayoutResID() {

        return R.layout.banner_items;
    }
    private ImageView img;
    private Banner banner;
    @Override
    protected void convert(View convertView, Object data) {
        banner= (Banner) data;
        img=convertView.findViewById(R.id.iv_img);
        if(high!=0){
            ViewGroup.LayoutParams params = img.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = high;
            img.setLayoutParams(params);
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(banner!=null&& TextUtil.isNotEmpty(banner.linkName)&&(banner.linkName.contains("156")||banner.linkName.contains("jpg")||banner.linkName.contains("png")) ){
                    Intent intent = new Intent(mContext, BanDetailActivity.class);
                    intent.putExtra("url", banner.linkName);
                    mContext.startActivity(intent);
                }
            }
        });
        Glide.with(mContext).load(StringUtil.changeUrl(banner.image_url)).asBitmap().placeholder(R.drawable.moren3).into(img);
    }


}
