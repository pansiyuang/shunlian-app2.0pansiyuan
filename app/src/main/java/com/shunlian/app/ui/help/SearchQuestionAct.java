package com.shunlian.app.ui.help;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SearchQAdapter;
import com.shunlian.app.bean.HelpSearchEntity;
import com.shunlian.app.presenter.PSearchQuestion;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.view.ISearchQView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SearchQuestionAct extends BaseActivity implements ISearchQView, TextWatcher {
    @BindView(R.id.edt_goods_search)
    EditText edt_goods_search;

    @BindView(R.id.mllayout_wu)
    MyLinearLayout mllayout_wu;

    @BindView(R.id.tv_search_cancel)
    TextView tv_search_cancel;

    @BindView(R.id.recycler_search)
    RecyclerView recycler_search;


    private PSearchQuestion presenter;
    private SearchQAdapter searchQAdapter;
    private String keyWord = "";
    private List<HelpSearchEntity.Content> contentList;

    public static void startAct(Activity context) {
        Intent intent = new Intent(context, SearchQuestionAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_question;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        presenter = new PSearchQuestion(this, this);
    }


    @Override
    protected void initListener() {
        super.initListener();
        tv_search_cancel.setOnClickListener(this);
        edt_goods_search.addTextChangedListener(this);
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                finish();
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        keyWord = s.toString();
        if (!isEmpty(keyWord)) {
            presenter.getSearchTips(keyWord);
        }
    }


    @Override
    public void setApiData(final List<HelpSearchEntity.Content> contents) {
        if (contents == null || contents.size() <= 0) {
            mllayout_wu.setVisibility(View.VISIBLE);
        } else {
            mllayout_wu.setVisibility(View.GONE);
        }
        if (searchQAdapter == null) {
            contentList = new ArrayList<>();
            contentList.addAll(contents);
            searchQAdapter = new SearchQAdapter(this, keyWord, false, contentList);
            recycler_search.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recycler_search.setAdapter(searchQAdapter);
            recycler_search.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0, getColorResouce(R.color.background_gray2)));
            searchQAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    HelpSolutionAct.startAct(getBaseContext(), contents.get(position).id);
                }
            });
        } else {
            contentList.clear();
            contentList.addAll(contents);
            searchQAdapter.notifyDataSetChanged();
        }
    }
}
