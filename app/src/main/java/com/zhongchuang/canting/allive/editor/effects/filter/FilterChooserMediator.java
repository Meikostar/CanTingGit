/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */

package com.zhongchuang.canting.allive.editor.effects.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongchuang.canting.R;
import com.zhongchuang.canting.allive.editor.effects.control.BaseChooser;
import com.zhongchuang.canting.allive.editor.msg.Dispatcher;
import com.zhongchuang.canting.allive.editor.msg.body.DeleteLastAnimationFilter;
import com.zhongchuang.canting.allive.editor.msg.body.FilterTabClick;
import com.zhongchuang.canting.allive.quview.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;


public class FilterChooserMediator extends BaseChooser
        implements View.OnClickListener, PagerSlidingTabStrip.TabClickListener {
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mPagerTab;
    private ImageView mCancelBtn;

    public static FilterChooserMediator newInstance() {
        FilterChooserMediator dialog = new FilterChooserMediator();
        Bundle args = new Bundle();
//        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.aliyun_svideo_layout_filter_container, container);
        mViewPager = (ViewPager) rootView.findViewById(R.id.aliyun_svideo_filter_content_container);
        mPagerTab = (PagerSlidingTabStrip) rootView.findViewById(R.id.aliyun_svideo_filter_content_container_indicator);
        mCancelBtn = (ImageView) rootView.findViewById(R.id.aliyun_svideo_btn_cancel);
        mCancelBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ColorFilterChooserItem());
        fragments.add(new AnimationFilterChooserItem());
        mViewPager.setAdapter(new FilterViewPagerAdapter(getChildFragmentManager(), fragments));
        mPagerTab.setTextColorResource(R.color.aliyun_svideo_tab_text_color_selector);
        mPagerTab.setTabViewId(R.layout.aliyun_svideo_layout_tab_top);
        mPagerTab.setViewPager(mViewPager);
        mPagerTab.setTabClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Dispatcher.getInstance().postMsg(new DeleteLastAnimationFilter());
    }

    @Override
    public void onTabClickListener(int position) {
        switch (position) {
            case 0:
                Dispatcher.getInstance().postMsg(new FilterTabClick(FilterTabClick.POSITION_COLOR_FILTER));
                break;
            case 1:
                Dispatcher.getInstance().postMsg(new FilterTabClick(FilterTabClick.POSITION_ANIMATION_FILTER));
                break;
        }
    }

    @Override
    public void onTabDoubleClickListener(int position) {

    }

    private class FilterViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragments;

        public FilterViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0://滤镜
                    return getString(R.string.aliyun_svideo_filter);
                case 1://特效
                    return getString(R.string.aliyun_svideo_effect);
            }
            return "";
        }
    }
}
