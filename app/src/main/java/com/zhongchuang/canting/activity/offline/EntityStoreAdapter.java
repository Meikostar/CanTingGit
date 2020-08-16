package com.zhongchuang.canting.activity.offline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.RecommendListDto;
import com.zhongchuang.canting.utils.LocationUtils;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.utils.location.LocationUtil;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class EntityStoreAdapter extends BaseQuickAdapter<RecommendListDto, BaseViewHolder> {
    private Context mContext;

    public EntityStoreAdapter(List<RecommendListDto> data, Context mContext) {
        super(R.layout.item_entity_store_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendListDto item) {
        if (item != null) {
            helper.setText(R.id.tv_entity_store_name, item.getShop_name());
            if(TextUtil.isNotEmpty(item.getAddress())){
                helper.setText(R.id.tv_entity_store_distance, (TextUtil.isNotEmpty(item.getAddress())?item.getAddress():""));
            }else {
                helper.setText(R.id.tv_entity_store_distance, "");
            }


            double disDouble = LocationUtils.getDistance(LocationUtil.longitude+"",LocationUtil.latitude+"",item.longitude+"",item.latitude+"");
            long total=(long)disDouble;
            String strDis = "";
            if(total>1000){
                float km=total*1.0f/1000;
                helper.setText(R.id.tv_jl, km + "km");

            }else {
                helper.setText(R.id.tv_jl, total + "m");
            }
            DecimalFormat format = new DecimalFormat("#0.00");

            TextView view = helper.getView(R.id.tv_label_one);
            TextView tv_pp = helper.getView(R.id.tv_pp);
            TextView view1 = helper.getView(R.id.tv_label_two);
            TextView view2 = helper.getView(R.id.tv_label_three);

            if(item.service_arr != null) {
                if(item.service_arr.size()==1){
                    view.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                    view.setText(item.service_arr.get(0));
                }else   if(item.service_arr.size()==2){
                    view.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                    view.setText(item.service_arr.get(0));
                    view1.setText(item.service_arr.get(1));
                }else   if(item.service_arr.size()>=3){
                    view.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    view.setText(item.service_arr.get(0));
                    view1.setText(item.service_arr.get(1));
                    view2.setText(item.service_arr.get(2));
                }else {
                    view.setVisibility(View.INVISIBLE);
                    view1.setVisibility(View.INVISIBLE);
                    view2.setVisibility(View.INVISIBLE);
                }
            }else {
                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
            }

            if(TextUtil.isNotEmpty(item.brand_img)){
                tv_pp.setVisibility(View.VISIBLE);
            }else {
//                tv_pp.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(item.getLogo()).asBitmap().placeholder(R.drawable.moren).into((ImageView) helper.getView(R.id.iv_entity_store));


            helper.getView(R.id.tv_go).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("geo:" + 23.111074 + "," + 114.414759 + "");
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(it);
                }
            });
        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}