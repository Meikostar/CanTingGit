package com.zhongchuang.canting.activity.offline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.RecommendListDto;
import com.zhongchuang.canting.utils.QiniuUtils;
import com.zhongchuang.canting.utils.TextUtil;

import java.text.DecimalFormat;
import java.util.List;


/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class EntityAuditeStoreAdapter extends BaseQuickAdapter<RecommendListDto, BaseViewHolder> {
    private Context mContext;

    public EntityAuditeStoreAdapter(List<RecommendListDto> data, Context mContext) {
        super(R.layout.item_audit_entity_store_list, data);
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



            TextView view = helper.getView(R.id.tv_label_one);
            TextView tv_pp = helper.getView(R.id.tv_pp);
            TextView view1 = helper.getView(R.id.tv_label_two);
            TextView view2 = helper.getView(R.id.tv_label_three);
            TextView tv_state1 = helper.getView(R.id.tv_state1);
            TextView tv_state2 = helper.getView(R.id.tv_state2);
            TextView tv_state = helper.getView(R.id.tv_state);
            tv_state1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.buttomClick(2,item.id);
                    }
                }
            });
            tv_state2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        listener.buttomClick(1,item.id);
                    }
                }
            });
            if(item.audit_status == 0){
                tv_state.setText("待审核");
                tv_state1.setVisibility(View.VISIBLE);
                tv_state2.setVisibility(View.VISIBLE);
            }else  if(item.audit_status ==1){
                tv_state.setText("审核通过");
                tv_state1.setVisibility(View.VISIBLE);
                tv_state2.setVisibility(View.GONE);
            }else  if(item.audit_status == 2){
                tv_state1.setVisibility(View.GONE);
                tv_state2.setVisibility(View.VISIBLE);
            }
            if(TextUtil.isNotEmpty(item.brand_img)){
                tv_pp.setVisibility(View.VISIBLE);
            }else {
                tv_pp.setVisibility(View.GONE);
            }
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
            if(item.getExt()!=null){
                if(TextUtil.isNotEmpty(item.getExt().is_brand)){
                    if(item.getExt().is_brand.equals("1")){
                        tv_pp.setVisibility(View.VISIBLE);
                    }else {
                        tv_pp.setVisibility(View.GONE);
                    }
                }else {
                    tv_pp.setVisibility(View.GONE);
                }
            }else {
//                tv_pp.setVisibility(View.GONE);
            }
            Glide.with(mContext).load(item.getLogo()).asBitmap().placeholder(R.drawable.moren).into((ImageView) helper.getView(R.id.iv_entity_store));


            helper.getView(R.id.layout_entity_store_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public  interface ButtonClickListener {
        void buttomClick(int type,int id);
    }
    private ButtonClickListener listener;

    public void setButtonClickListener(ButtonClickListener mListener){
        this.listener = mListener ;
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

}