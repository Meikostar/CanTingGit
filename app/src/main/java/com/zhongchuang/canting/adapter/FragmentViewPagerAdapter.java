package com.zhongchuang.canting.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/***
 * 功能描述:ViewPager的适配器（Fragment类）
 * 作者:chenwei
 * 时间:2016/12/16
 * 版本:
 ***/
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList==null?null:mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList==null?0:mFragmentList.size();
    }
}
