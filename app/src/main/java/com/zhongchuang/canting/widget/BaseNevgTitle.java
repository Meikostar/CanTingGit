package com.zhongchuang.canting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


import com.zhongchuang.canting.R;
import com.zhongchuang.canting.been.GAME;

import java.util.List;


/**
 * Created by yunshang on 2016/12/20.
 */

public class BaseNevgTitle extends LinearLayout{
   private GridView gridView;
   private HorizontalScrollView scroll_view;

    private Context context;
    private View view;
    public boolean isChoose;
    public ClickListener listener;
    private OrderGridAdapter adapter;
    private OnChangeListener mListener;
    private List<GAME> list;
    private int length;
    public interface ClickListener {
        void clicks();
    }

    public void clicks(ClickListener listener) {
        this.listener = listener;
    }



    public BaseNevgTitle(Context context) {
        this(context, null);
//        init();
    }

    public BaseNevgTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

//        init();
    }


    public BaseNevgTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        init();

    }
    private void setGridData() {
        //获得屏幕分辨路
        adapter.setLists(list);

    }
    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.title_change_view, this);
        gridView = view.findViewById(R.id.grid);
        scroll_view = view.findViewById(R.id.scroll_view);
        adapter=new OrderGridAdapter(context);
         length=(int)((context.getResources().getDisplayMetrics().density)*(98)+0.5f);

        int gridviewWidth = list.size() * (length );
        int itemWidth =length ;
        LayoutParams params = new LayoutParams(
                gridviewWidth, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 5, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(0); // 设置列表项水平间距
        gridView.setGravity(Gravity.CENTER);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onChagne(position);

                for(int i=0;i<list.size();i++){
                    list.get(i).isChoose = i == position;
                }
                adapter.setLists(list);

            }

        });
    }
    public void setDatas(List<GAME> list){

        this.list=list;
        init();

        adapter.setLists(list);
    }
   public void setSelect(int position){
       //获得屏幕分辨路
       //获取屏幕宽度
       int screenWidth=getResources().getDisplayMetrics().widthPixels;
       for(int i=0;i<list.size();i++){
           list.get(i).isChoose = i == position;
       }

           scroll_view.smoothScrollTo((position)*(length)-screenWidth/2,0);

       adapter.setLists(list);
   }

    public interface OnChangeListener {
        void onChagne(int currentIndex);
    }
    public void setOnChangeListener(OnChangeListener listener) {
        this.mListener = listener;
    }
}
