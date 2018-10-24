package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.DiscoverSearchPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IDiscoverSearchView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class DiscoverSearchActivity extends BaseActivity implements IDiscoverSearchView, TextView.OnEditorActionListener {

    @BindView(R.id.edt_search)
    EditText edt_search;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.rl_recently)
    RelativeLayout rl_recently;

    private DiscoverSearchPresenter mPresenter;
    private String currentKeyword;
    private String historyContent;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DiscoverSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_discover_search;
    }

    @Override
    protected void initListener() {
        super.initListener();
        edt_search.setOnEditorActionListener(this);
        tv_search.setOnClickListener(v -> {
            currentKeyword = edt_search.getText().toString();
            if (isEmpty(currentKeyword)) {
                Common.staticToast("请输入您要搜索的商品");
            } else {
                if (isEmpty(historyContent)) {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", currentKeyword);
                } else {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", historyContent + "_" + currentKeyword);
                }
                DiscoverSearchResultActivity.startActivity(this, currentKeyword);
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mPresenter = new DiscoverSearchPresenter(this, this);
        mPresenter.getHotSearch();

        historyContent = SharedPrefUtil.getCacheSharedPrf("discover_search_history", "");
    }

    public void setHistoryData(String content) {
        if (isEmpty(historyContent)) {
            rl_recently.setVisibility(View.GONE);
        } else {
            String[] tags = content.split("_");
            rl_recently.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            currentKeyword = edt_search.getText().toString();
            if (isEmpty(currentKeyword)) {
                Common.staticToast("请输入您要搜索的商品");
            } else {
                if (isEmpty(historyContent)) {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", currentKeyword);
                } else {
                    SharedPrefUtil.saveCacheSharedPrf("discover_search_history", historyContent + "_" + currentKeyword);
                }
                DiscoverSearchResultActivity.startActivity(this, currentKeyword);
            }
        }
        return false;
    }

}
