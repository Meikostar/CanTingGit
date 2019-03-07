package com.zhongchuang.canting.widget.loadingView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by linquandong on 15/12/17.
 */
public abstract class BaseLoadingPager extends FrameLayout {
    //加载默认的状态
    public static final int STATE_UNLOADED = 1;
    //加载的状态
    public static final int STATE_LOADING = 2;
    //加载失败的状态
    public static final int STATE_ERROR = 3;
    //加载空的状态
    public static final int STATE_EMPTY = 4;
    //加载成功的状态
    public static final int STATE_SUCCEED = 5;

    protected View mLoadingView;//转圈的view
    protected View mErrorView;//错误的view
    protected View mEmptyView;//空的view
    protected View mContentView;//成功的view

    public OnNetworkRetryListener mNetworkListener;

    private int viewstate = -1;

    private LayoutInflater mInflater;
    private Context mContext;

    public BaseLoadingPager(Context context) {
        this(context, null);
    }

    public BaseLoadingPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoadingPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createView(context);
    }

    protected  void createView(Context context)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
//        contentgroup.addView(creatSucceedView(inflater), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(creatEmptyView(mInflater), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(creatErrorView(mInflater), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(creatLoadingView(mInflater), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //默认显示加载
        showPager(STATE_LOADING);
    }

    protected abstract View creatLoadingView(LayoutInflater mInflater);

    protected abstract View creatErrorView(LayoutInflater mInflater);

    protected abstract View creatEmptyView(LayoutInflater mInflater);


    public void showPager(int viewstate) {
        if(this.viewstate == viewstate){
            return;
        }
        this.viewstate = viewstate;
        mContentView = getChildAt(getChildCount()-1);
        if(mContentView == null){
            return;
//            throw new Exception("mContentView is null ,pealse call attachContentView(contentView)")
        }
        initGone();
        switch (viewstate) {
            case STATE_UNLOADED:
                break;
            case STATE_EMPTY:
                mEmptyView.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                mErrorView.setVisibility(View.VISIBLE);
                break;
            case STATE_LOADING:
                mLoadingView.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCEED:
                mContentView.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void initGone() {
        mLoadingView.setVisibility(View.GONE);//转圈的view
        mErrorView.setVisibility(View.GONE);//错误的view
        mEmptyView.setVisibility(View.GONE);//空的view
        mContentView.setVisibility(View.GONE);//成功的view
    }

    public void setNetworkRetryListenner(OnNetworkRetryListener listener) {
        mNetworkListener = listener;
    }

}
