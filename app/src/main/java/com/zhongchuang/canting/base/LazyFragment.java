package com.zhongchuang.canting.base;

/**
 * Created by fl on 2017/5/11.
 */

public abstract class LazyFragment extends BaseFragment {

    public boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            //显示
            isVisible = true;
            visibleWindow();
        }else {
            //不可见状态
            inVisibleWindow();
        }
    }

    protected void visibleWindow(){
        lazyView();
    }

    protected void inVisibleWindow(){}

    public abstract void lazyView();
}
