package com.shunlian.app.ui.discover_new.search;

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
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class DiscoverSearchResultActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public String[] titles = {"文章", "活动", "达人"};
    public List<BaseFragment> baseFragmentList;
    public String currentKeyword;
    private SearchBlogFrag blogFrag;
    private SearchActivityFrag activityFrag;
    private SearchExpertFrag expertFrag;
    private String historyContent;
    private List<String> tags;

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
        edt_search.setText(currentKeyword);
        edt_search.setSelection(currentKeyword.length());

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        initFrags();
        tab_layout.setupWithViewPager(viewPager);
        reflex(tab_layout);

        tags = new ArrayList<>();
        historyContent = SharedPrefUtil.getCacheSharedPrf("discover_search_history", "");
        setHistoryContent(historyContent);
    }

    public void setHistoryContent(String history) {
        if (!isEmpty(history)) {
            String[] tagStrs = history.split("_");
            for (int i = 0; i < tagStrs.length; i++) {
                tags.add(tagStrs[i]);
            }
        }
    }

    public void toSearch(String keyword) {
        if (isEmpty(keyword)) {
            Common.staticToast("请输入您要搜索的内容");
        } else {
            if (!tags.contains(keyword)) {
                if (isEmpty(historyContent)) {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", keyword);
                } else {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", historyContent + "_" + keyword);
                }
            }
            blogFrag.setKeyWord(currentKeyword);
            activityFrag.setKeyWord(currentKeyword);
            expertFrag.setKeyWord(currentKeyword);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        edt_search.setOnEditorActionListener(this);
        tv_search.setOnClickListener(v -> {
            currentKeyword = edt_search.getText().toString();
            toSearch(currentKeyword);
        });
    }

    public void initFrags() {
        baseFragmentList = new ArrayList<>();

        blogFrag = SearchBlogFrag.getInstance(currentKeyword);
        activityFrag = SearchActivityFrag.getInstance(currentKeyword);
        expertFrag = SearchExpertFrag.getInstance(currentKeyword);
        baseFragmentList.add(blogFrag);
        baseFragmentList.add(activityFrag);
        baseFragmentList.add(expertFrag);

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

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            currentKeyword = edt_search.getText().toString();
            toSearch(currentKeyword);
        }
        return false;
    }
}
