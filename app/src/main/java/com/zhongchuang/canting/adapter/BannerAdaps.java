package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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

public class BannerAdaps extends BannerBaseAdapter {

    private List<DATA> list;
    private int type=0;//1是用药记录item

    public BannerAdaps(Context context) {
        super(context);
    }
    private int high;
    public BannerAdaps(Context context, int with) {
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

        return R.layout.banner_ites;
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


        Glide.with(mContext).load(StringUtil.changeUrl(banner.image_url)).asBitmap().placeholder(R.drawable.moren3).into(img);
    }


}
