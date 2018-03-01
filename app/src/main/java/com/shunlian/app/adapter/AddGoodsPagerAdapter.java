package com.shunlian.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shunlian.app.ui.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AddGoodsAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;
    private String[] titles;

    public AddGoodsAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] strings) {
        super(fm);
        this.fragments = fragments;
        this.titles = strings;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
