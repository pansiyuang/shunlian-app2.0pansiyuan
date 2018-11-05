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
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.eventbus_bean.BaseInfoEvent;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover_new.ActivityFrag;
import com.shunlian.app.ui.discover_new.AttentionFrag;
import com.shunlian.app.ui.discover_new.HotBlogFrag;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.search.DiscoverSearchActivity;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    private String[] titles = {"关注", "精选", "活动"};
    private List<BaseFragment> goodsFrags;
    private AttentionFrag attentionFrag;
    private HotBlogFrag hotBlogFrag;
    private ActivityFrag activityFrag;
    private String avatar;
    private HotBlogsEntity.BaseInfo currentBaseInfo;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_new_discover, null, false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
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

        view_pager.setCurrentItem(1);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_icon.setOnClickListener(v -> {
            if (currentBaseInfo != null && !isEmpty(currentBaseInfo.member_id)) {
                MyPageActivity.startAct(getActivity(), currentBaseInfo.member_id);
            }
        });
        miv_search.setOnClickListener(v -> {
            DiscoverSearchActivity.startActivity(getActivity());
        });

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 && !Common.isAlreadyLogin()) {
                    LoginAct.startAct(getActivity());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(BaseInfoEvent event) {
        currentBaseInfo = event.baseInfo;
        avatar = currentBaseInfo.avatar;
        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_icon, avatar);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
