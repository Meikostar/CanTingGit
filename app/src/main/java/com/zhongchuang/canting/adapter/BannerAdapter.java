package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.DATA;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class BannerAdapter extends BannerBaseAdapter {

    private List<DATA> list;
    private int type=0;//1是用药记录item

    public BannerAdapter(Context context) {
        super(context);
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

        return R.layout.banner_item;
    }
    private ImageView img;
    private Banner banner;
    @Override
    protected void convert(View convertView, Object data) {
        banner= (Banner) data;
        img=convertView.findViewById(R.id.iv_img);
        Glide.with(mContext).load(StringUtil.changeUrl(banner.image_url)).asBitmap().placeholder(R.drawable.moren3).into(img);
    }


}
