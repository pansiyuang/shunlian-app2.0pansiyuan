package com.shunlian.app.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SearchArticleAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.presenter.SearchArticlePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISearchArticleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.Common.hideKeyboard;

/**
 * Created by Administrator on 2018/3/17.
 */

public class SearchArticleActivity extends BaseActivity implements ISearchArticleView, TextView.OnEditorActionListener {

    @BindView(R.id.edt_goods_search)
    EditText edt_goods_search;

    @BindView(R.id.tv_search_cancel)
    TextView tv_search_cancel;

    @BindView(R.id.recycler_article)
    RecyclerView recycler_article;

    private SearchArticlePresenter mPresenter;
    private SearchArticleAdapter mAdapter;
    private List<ArticleEntity.Article> articleList;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchArticleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_search_article;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        articleList = new ArrayList<>();
        mPresenter = new SearchArticlePresenter(this, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mAdapter = new SearchArticleAdapter(this, articleList);
        recycler_article.setLayoutManager(manager);
        recycler_article.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        edt_goods_search.setOnEditorActionListener(this);
        tv_search_cancel.setOnClickListener(this);
    }

    @Override
    public void getSearchArticleList(List<ArticleEntity.Article> list, int page, int totalPage) {
        if (page == 1) {
            articleList.clear();
        }
        if (!isEmpty(list)) {
            articleList.addAll(list);
            mAdapter.notifyDataSetChanged();
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
            String str = edt_goods_search.getText().toString();
            if (!isEmpty(str)) {
                mPresenter.getSearchArticleList(true, str);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancel:
                finish();
                break;

        }
        super.onClick(view);
    }
}
