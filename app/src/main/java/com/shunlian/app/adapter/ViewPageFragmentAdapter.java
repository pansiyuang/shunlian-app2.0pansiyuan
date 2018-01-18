package com.shunlian.app.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.shunlian.app.R;
import com.shunlian.app.bean.ViewPageInfo;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.PagerSlidingTabStrip;
import java.util.ArrayList;
@SuppressLint("Recycle")
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    protected PagerSlidingTabStrip mPagerStrip;
    private final ViewPager mViewPager;
    private final ArrayList<ViewPageInfo> mTabs = new ArrayList<ViewPageInfo>();
    public ViewPageFragmentAdapter(FragmentManager fm, PagerSlidingTabStrip pageStrip, ViewPager pager) {
        super(fm);
        mContext = pager.getContext();
        mPagerStrip = pageStrip;
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mPagerStrip.setViewPager(mViewPager);
    }
    public void addTab(String title, String tag, BaseFragment fragment) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, fragment);
        addFragment(viewPageInfo);
    }
    public void addAllTab(ArrayList<ViewPageInfo> mTabs) {
        for (ViewPageInfo viewPageInfo : mTabs) {
            addFragment(viewPageInfo);
        }
    }
    private void addFragment(ViewPageInfo info) {
        if (info == null) {
            return;
        }
        // 加入tab title
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_tab_viewpager_fragment, null, false);
        TextView title = (TextView) v.findViewById(R.id.tab_title);
        title.setText(info.getTitle());
        mPagerStrip.addTab(v);
        mTabs.add(info);
        notifyDataSetChanged();
    }
    /**
     * 移除第一次
     */
    public void remove() {
        remove(0);
    }
    /**
     * 移除一个tab
     *  备注：如果index小于0，则从第一个开始删 如果大于tab的数量值则从最后一个开始删除
     */
    public void remove(int index) {
        if (mTabs.isEmpty()) {
            return;
        }
        if (index < 0) {
            index = 0;
        }
        if (index >= mTabs.size()) {
            index = mTabs.size() - 1;
        }
        mTabs.remove(index);
        mPagerStrip.removeTab(index, 1);
        notifyDataSetChanged();
    }
    /**
     * 移除所有的tab
     */
    public void removeAll() {
        if (mTabs.isEmpty()) {
            return;
        }
        mPagerStrip.removeAllTab();
        mTabs.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mTabs.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return info.getFragment();
}
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getTitle();
    }
    //解决Fragement no longer exists for key f0: index 0
    @Override
    public Parcelable saveState() {
        return null;
    }
}