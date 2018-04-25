package com.shunlian.app.ui.fragment.first_page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.shunlian.app.bean.GetMenuEntity;

import java.util.ArrayList;
import java.util.List;


public class MyFrPagerAdapter extends FragmentPagerAdapter {

    private List<GetMenuEntity.MData> mDatas;

    private List<Fragment> fragments;

    public MyFrPagerAdapter(FragmentManager fm, List<GetMenuEntity.MData> mDatas, List<Fragment> fragments) {
        super(fm);
        this.mDatas = mDatas;
        this.fragments=fragments;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position).channel_name;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
