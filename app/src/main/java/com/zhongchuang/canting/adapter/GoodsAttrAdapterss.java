package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.BaseDto;
import com.zhongchuang.canting.been.GoodsAttrDto;

import java.util.List;

/**
 * 获取商品颜色，规格...
 * Created by rzb on 2019/6/18.
 */
public class GoodsAttrAdapterss extends BaseQuickAdapter<GoodsAttrDto, BaseViewHolder> {
    private Context           mContext;
    private GoodsSpecListener goodsSpecListener;

    public GoodsAttrAdapterss(List<GoodsAttrDto> data, Context mContext) {
        super(R.layout.item_shop_product_typess, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsAttrDto item) {
        if (item != null) {
            helper.setText(R.id.tv_item_select_commodity_key, item.getKey());
            LabelsView labelsView = helper.getView(R.id.tv_item_select_commodity_datas);
            labelsView.setSelectType(LabelsView.SelectType.SINGLE);

            labelsView.setLabels(item.data, new LabelsView.LabelTextProvider<BaseDto>() {
                @Override
                public CharSequence getLabelText(TextView label, int position, BaseDto data) {

                    return data.name;
                }
            });

            labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
                @Override
                public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
                    int a=0;
                    for(BaseDto dto:item.data){
                        if(a==position){
                            dto.isChoose=true;
                        }else {
                            dto.isChoose=false;
                        }
                        a++;
                    }
                    BaseDto bto= (BaseDto) data;

                    goodsSpecListener.callbackGoodsSpec(bto.id,item.getKey(),isSelect);

                }
            });
        }
    }

    public interface GoodsSpecListener {
        void callbackGoodsSpec(String vale, String key,boolean isselect);
    }

    public void setGoodsSpecListener(GoodsSpecListener listener){
        this.goodsSpecListener = listener;
    }
}
