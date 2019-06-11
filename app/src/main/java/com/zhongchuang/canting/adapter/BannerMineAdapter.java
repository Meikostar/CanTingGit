package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.DATA;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */

public class BannerMineAdapter extends BannerBaseAdapter {

    private List<DATA> list;
    private int cout=0;//1是用药记录item

    public BannerMineAdapter(Context context) {
        super(context);

    }

    public interface ItemCliks{
        void getItem(DATA menu, int type);//type 1表示点击事件2 表示长按事件
    }
    private ItemCliks listener;
    public void setClickListener(ItemCliks listener){
        this.listener=listener;
    }


    public void setTotal(int total){
        this.total=total;
    }
    private int total;
    @Override
    protected int getLayoutResID() {

        return R.layout.banner_mine_view;
    }
    private ImageView img;
    private TextView tv_poition;
    private Banner banner;
    @Override
    protected void convert(View convertView, Object data) {
        banner= (Banner) data;
        img=convertView.findViewById(R.id.iv_img);
        tv_poition=convertView.findViewById(R.id.tv_poition);
        tv_poition.setText(banner.poition+"/"+total);
        Glide.with(mContext).load(StringUtil.changeUrl(banner.image_url)).asBitmap().placeholder(R.drawable.moren3).into(img);
    }


}
