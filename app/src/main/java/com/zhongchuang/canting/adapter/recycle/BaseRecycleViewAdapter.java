package com.zhongchuang.canting.adapter.recycle;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/***
 * 功能描述:RecycleViewAdapter基类
 * 作者:chenwei
 * 时间:2016/12/21
 * 版本:v1.0
 ***/
public abstract class BaseRecycleViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
     public boolean isTrue=true;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getDatas() {
        if (datas==null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        isTrue=true;

    }

}
