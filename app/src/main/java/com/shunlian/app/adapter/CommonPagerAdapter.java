package com.shunlian.app.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shunlian.app.ui.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class CommonPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> fragments;
    private String[] titles;

    public CommonPagerAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] strings) {
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

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
