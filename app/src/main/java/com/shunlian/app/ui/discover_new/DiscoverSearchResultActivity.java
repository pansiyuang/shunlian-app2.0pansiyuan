package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.confirm_order.SearchOrderResultActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class DiscoverSearchResultActivity extends BaseActivity {

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public String[] titles = {"文章", "活动", "达人"};
    public List<BaseFragment> baseFragmentList;
    public String currentKeyword;

    public static void startActivity(Context context, String keyword) {
        Intent intent = new Intent(context, DiscoverSearchResultActivity.class);
        intent.putExtra("keyword", keyword);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_discover_search_result;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentKeyword = getIntent().getStringExtra("keyword");

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        initFrags();
        tab_layout.setupWithViewPager(viewPager);
        reflex(tab_layout);
    }

    public void initFrags() {
        baseFragmentList = new ArrayList<>();

        viewPager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), baseFragmentList, titles));
        viewPager.setOffscreenPageLimit(baseFragmentList.size());
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
