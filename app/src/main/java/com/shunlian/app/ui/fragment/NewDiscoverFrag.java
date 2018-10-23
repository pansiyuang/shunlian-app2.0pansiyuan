package com.shunlian.app.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover_new.ActivityFrag;
import com.shunlian.app.ui.discover_new.HotBlogFrag;
import com.shunlian.app.ui.discover_new.AttentionFrag;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.mylibrary.ImmersionBar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class NewDiscoverFrag extends BaseFragment {

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    private String[] titles = {"关注", "精选", "活动"};
    private List<BaseFragment> goodsFrags;
    private AttentionFrag attentionFrag;
    private HotBlogFrag hotBlogFrag;
    private ActivityFrag activityFrag;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_new_discover, null, false);
        return view;
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        initFrags();
        tab_layout.setupWithViewPager(view_pager);
        reflex(tab_layout);
    }

    public void initFrags() {
        goodsFrags = new ArrayList<>();
        attentionFrag = new AttentionFrag();
        hotBlogFrag = new HotBlogFrag();
        activityFrag = new ActivityFrag();

        goodsFrags.add(attentionFrag);
        goodsFrags.add(hotBlogFrag);
        goodsFrags.add(activityFrag);

        view_pager.setAdapter(new CommonLazyPagerAdapter(getChildFragmentManager(), goodsFrags, titles));
        view_pager.setOffscreenPageLimit(goodsFrags.size());
    }

    public void reflex(final TabLayout tabLayout) {
        tabLayout.post(() -> {
            try {
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int dp10 = TransformUtil.dip2px(tabLayout.getContext(), 12.5f);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);
                    TextView mTextView = (TextView) mTextViewField.get(tabView);
                    tabView.setPadding(0, 0, 0, 0);
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width;
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);
                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
