package com.zhongchuang.canting.adapter;


import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.StoreCategoryDto;

import java.util.List;

/**
 * Created by lihaoqi on 2019/01/25.
 */

public class ClassifyOneAdapters extends BaseQuickAdapter<StoreCategoryDto, BaseViewHolder> {



    public int selctedPos = 0;

    public ClassifyOneAdapters() {
        super(R.layout.item_classify_one);
    }

    public ClassifyOneAdapters(@Nullable List<StoreCategoryDto> data) {
        super(R.layout.item_classify_one, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreCategoryDto item) {
        int pos = helper.getAdapterPosition();
        if(selctedPos==pos){
            helper.setTextColor(R.id.tv_name, Color.parseColor("#212121"));
            helper.setBackgroundColor(R.id.tv_name,Color.parseColor("#ffffff"));
        }else{
            helper.setTextColor(R.id.tv_name,Color.parseColor("#212121"));
            helper.setBackgroundColor(R.id.tv_name,Color.parseColor("#f1f1f1"));
        }
        helper.setText(R.id.tv_name,item.getCategoryName());
    }


    @Override
    public void setNewData(@Nullable List<StoreCategoryDto> data) {
        super.setNewData(data);
        selctedPos = 0;
    }


}

