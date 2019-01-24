package com.shunlian.app.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.eventbus_bean.BaseInfoEvent;
import com.shunlian.app.eventbus_bean.DiscoveryLocationEvent;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover_new.ActivityFrag;
import com.shunlian.app.ui.discover_new.AttentionFrag;
import com.shunlian.app.ui.discover_new.HotBlogFrag;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.search.DiscoverSearchActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.LazyViewPager;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class NewDiscoverFrag extends BaseFragment {

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.view_pager)
    LazyViewPager view_pager;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    @BindView(R.id.ll_attention)
    LinearLayout ll_attention;

    @BindView(R.id.ll_blog)
    LinearLayout ll_blog;

    @BindView(R.id.ll_activity)
    LinearLayout ll_activity;

    @BindView(R.id.tv_attention)
    TextView tv_attention;

    @BindView(R.id.tv_blog)
    TextView tv_blog;

    @BindView(R.id.tv_activity)
    TextView tv_activity;

    @BindView(R.id.view_attention)
    View view_attention;

    @BindView(R.id.view_blog)
    View view_blog;

    @BindView(R.id.view_activity)
    View view_activity;

    private List<BaseFragment> goodsFrags;
    private AttentionFrag attentionFrag;
    private HotBlogFrag hotBlogFrag;
    private ActivityFrag activityFrag;
    private String avatar;
    private String[] titles = {"关注", "精选", "活动"};
    private HotBlogsEntity.BaseInfo currentBaseInfo;
    private CommonLazyPagerAdapter mAdapter;
    private boolean isInit;
    private int defaultPage = 1;

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
                    .keyboardEnable(true)
                    .init();
        }
    }

    public void setCurrentPage(String flag) {
        if (isEmpty(flag)) {
            return;
        }
        switch (flag) {
            case "attentionList":
                if (isInit) {
                    if (view_pager != null) {
                        view_pager.setCurrentItem(0);
                    }
                } else {
                    defaultPage = 0;
                }
                break;
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();

        initFrags();

        miv_icon.post(() -> {
            int[] location = new int[2];
            miv_icon.getLocationInWindow(location);
            int imgWidth = miv_icon.getWidth();
            EventBus.getDefault().post(new DiscoveryLocationEvent(location, imgWidth));
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_icon.setOnClickListener(v -> {
            String member_id = SharedPrefUtil.getSharedUserString("member_id", "null");
            if (!isEmpty(member_id) && Common.isAlreadyLogin()) {
                MyPageActivity.startAct(getActivity(), member_id);
            } else {
                Common.goGoGo(getActivity(), "login");
            }
        });
        miv_search.setOnClickListener(v -> {
            DiscoverSearchActivity.startActivity(getActivity());
        });

        ll_attention.setOnClickListener(v -> {
            if (!Common.isAlreadyLogin()) {
                Common.goGoGo(getActivity(), "login");
            } else {
                showTab(0);
                view_pager.setCurrentItem(0);
            }
        });

        ll_blog.setOnClickListener(v -> {
            showTab(1);
            view_pager.setCurrentItem(1);
        });

        ll_activity.setOnClickListener(v -> {
            showTab(2);
            view_pager.setCurrentItem(2);
        });
        view_pager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

        mAdapter = new CommonLazyPagerAdapter(getChildFragmentManager(), goodsFrags, titles);
        view_pager.setAdapter(mAdapter);
        view_pager.setOffscreenPageLimit(3);
        view_pager.setCurrentItem(defaultPage);
        showTab(defaultPage);
        isInit = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(BaseInfoEvent event) {
        currentBaseInfo = event.baseInfo;
        avatar = currentBaseInfo.avatar;
        GlideUtils.getInstance().loadCircleAvar(baseContext, miv_icon, avatar);
    }

    public void showTab(int position) {
        tv_attention.setTextColor(getColorResouce(R.color.value_484848));
        tv_blog.setTextColor(getColorResouce(R.color.value_484848));
        tv_activity.setTextColor(getColorResouce(R.color.value_484848));

        tv_attention.getPaint().setFakeBoldText(false);
        tv_blog.getPaint().setFakeBoldText(false);
        tv_activity.getPaint().setFakeBoldText(false);

        view_attention.setVisibility(View.INVISIBLE);
        view_blog.setVisibility(View.INVISIBLE);
        view_activity.setVisibility(View.INVISIBLE);
        switch (position) {
            case 0:
                tv_attention.setTextColor(getColorResouce(R.color.pink_color));
                tv_attention.getPaint().setFakeBoldText(true);
                view_attention.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_blog.setTextColor(getColorResouce(R.color.pink_color));
                tv_blog.getPaint().setFakeBoldText(true);
                view_blog.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_activity.setTextColor(getColorResouce(R.color.pink_color));
                tv_activity.getPaint().setFakeBoldText(true);
                view_activity.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
