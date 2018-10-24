package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/19.
 */

public class HotExpertRankActivity extends BaseActivity {

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] titles = {"精选达人", "每周达人榜"};
    private List<BaseFragment> goodsFrags;
    private HotExpertFrag hotExpertFrag;
    private WeekExpertRankFrag weekExpertRankFrag;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HotExpertRankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_hot_expert_rank;
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
        tab_layout.setupWithViewPager(viewpager);

        setTabLayoutLine(tab_layout, TransformUtil.dip2px(this, 30));
    }

    @Override
    protected void initListener() {
        miv_close.setOnClickListener(v -> finish());
        super.initListener();
    }

    public void initFrags() {
        goodsFrags = new ArrayList<>();
        hotExpertFrag = new HotExpertFrag();
        weekExpertRankFrag = new WeekExpertRankFrag();

        goodsFrags.add(hotExpertFrag);
        goodsFrags.add(weekExpertRankFrag);
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        viewpager.setOffscreenPageLimit(goodsFrags.size());
    }

    public void setTabLayoutLine(final TabLayout tabLayout, int lineWidth) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            //拿到tabLayout的mTabStrip属性
            LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
            int dp10 = TransformUtil.dip2px(tabLayout.getContext(), 10);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View tabView = mTabStrip.getChildAt(i);
                tabView.setPadding(0, 0, 0, 0);
                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = lineWidth;
                params.leftMargin = dp10;
                params.rightMargin = dp10;
                tabView.setLayoutParams(params);
                tabView.invalidate();
            }
        });
    }
}
