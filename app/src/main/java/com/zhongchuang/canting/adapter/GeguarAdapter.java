package com.zhongchuang.canting.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.Type;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.Custom_TagBtn;
import com.zhongchuang.canting.widget.FlexboxLayout;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class GeguarAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<List<Type>> list;


    public GeguarAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<List<Type>> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<List<Type>> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = inflater.inflate(R.layout.gui_item_view, null);
            holder.flb = view.findViewById(R.id.fbl_cont);
            holder.type = view.findViewById(R.id.type);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        initData(holder.flb,list.get(i));
        if(TextUtil.isNotEmpty(list.get(i).get(0).title)){
            holder.type.setText(list.get(i).get(0).title);
        }

        return view;
    }

    /**
     * 初始化标签适配器
     */
    private void initData(FlexboxLayout fblGarnish, final List<Type> datas) {
        fblGarnish.removeAllViews();
        if (datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                final Custom_TagBtn tagBtn = createBaseFlexItemTextView(datas.get(i),i);
               final int poition=i;
                tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                    @Override
                    public void clickDelete(int type) {
                        for (int j = 0; j < datas.size(); j++) {
                            if (j == poition) {
                                if (datas.get(j).select == 0) {
                                    return;
                                } else if (datas.get(j).select == 2) {
                                    datas.get(j).select = 1;
                                } else if (datas.get(j).select == 1) {
                                    datas.get(j).select = 2;
                                }
                            } else {
                                if (datas.get(j).select == 2) {
                                    datas.get(j).select = 1;
                                }

                            }

                        }
                        listener.itemclick(0,"");
                        notifyDataSetChanged();
                    }
                });
                fblGarnish.addView(tagBtn, i);
            }
        }
    }

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(Type content,int poition) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(context, 10);
        lp.leftMargin = DensityUtil.dip2px(context, 15);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(context).inflate(R.layout.dish_item, null);
        if(content.select==2){
            view.setBg(R.drawable.blue_rectangle);
            view.setColors(R.color.white);
        }else if(content.select==1){
            view.setBg(R.drawable.e6_rectangle);
            view.setColors(R.color.slow_black);
        }else {
            view.setBg(R.drawable.e6_rectangle);
            view.setColors(R.color.color8);
        }


        String name = TextUtil.isEmpty(content.cont)?"null":content.cont;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name) + 12;
        view.setSize(with, 30, 13, 1);
        view.setLayoutParams(lp);
        view.setCustomText( content.cont);
        view.setPoition(poition);

        return view;
    }

    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void itemclick(int poistion, String id);
    }

    class ViewHolder {

        FlexboxLayout flb;
        TextView type;


    }

}
