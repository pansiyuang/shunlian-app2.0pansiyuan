package com.shunlian.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shunlian.app.ui.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MyOrderAdapter extends FragmentPagerAdapter {


    private List<BaseFragment> fragments;

    public MyOrderAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments.size();
    }
}
