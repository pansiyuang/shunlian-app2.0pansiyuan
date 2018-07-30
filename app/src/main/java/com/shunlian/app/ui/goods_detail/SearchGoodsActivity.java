package com.shunlian.app.ui.goods_detail;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.HotSearchEntity;
import com.shunlian.app.presenter.SearchGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.collection.SearchResultAct;
import com.shunlian.app.ui.myself_store.GoodsSearchAct;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.ISearchGoodsView;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/19.
 */

public class SearchGoodsActivity extends BaseActivity implements ISearchGoodsView, View.OnClickListener, TextWatcher {
    public static final int SEARCH_REQUEST_CODE = 1002;

    @BindView(R.id.edt_goods_search)
    EditText edt_goods_search;

    @BindView(R.id.tv_search_cancel)
    TextView tv_search_cancel;

    @BindView(R.id.taglayout_hot)
    TagFlowLayout taglayout_hot;

    @BindView(R.id.taglayout_history)
    TagFlowLayout taglayout_history;

    @BindView(R.id.tv_clear)
    TextView tv_clear;

    @BindView(R.id.tv_hot)
    TextView tv_hot;

    @BindView(R.id.tv_history)
    TextView tv_history;

    @BindView(R.id.recycler_search)
    RecyclerView recycler_search;

    @BindView(R.id.ll_tag)
    LinearLayout ll_tag;

    @BindView(R.id.ll_clear)
    LinearLayout ll_clear;

    private SearchGoodsPresenter presenter;
    private TagAdapter<String> hotAdapter;
    private TagAdapter<String> historyAdapter;
    private SimpleRecyclerAdapter simpleRecyclerAdapter;
    private List<String> mTips;
    private List<String> hotTags = new ArrayList<>();
    private List<String> histotyTags = new ArrayList<>();
    private String currentKeyWord;
    private String currentFlag;
    private boolean isShowHotSearch;
    private PromptDialog promptDialog;
    private String save_goods_history;
    private String save_shop_history;

    public static void startActivityForResult(Activity context) {
        context.startActivityForResult(new Intent(context, SearchGoodsActivity.class), SEARCH_REQUEST_CODE);
    }

    public static void startActivityForResult(Activity context, String keyWord) {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra("keyword", keyWord);
        context.startActivityForResult(intent, SEARCH_REQUEST_CODE);
    }

    public static void startActivityForResult(Activity context, boolean isShowHotSearch, String flag) {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra("isShowHotSearch", isShowHotSearch);
        intent.putExtra("flag", flag);
        context.startActivityForResult(intent, SEARCH_REQUEST_CODE);
    }

    public static void startAct(Activity context, String keyWord, String flag) {
        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra("keyword", keyWord);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_goods;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        Intent intent = getIntent();
        currentKeyWord = intent.getStringExtra("keyword");
        currentFlag = intent.getStringExtra("flag");
        isShowHotSearch = intent.getBooleanExtra("isShowHotSearch", true);
        presenter = new SearchGoodsPresenter(this, this);

        if (isShowHotSearch) {
            presenter.getSearchTag();
        }

        if ("sortFrag".equals(currentFlag)) {
            edt_goods_search.setHint(currentKeyWord);
        } else {
            if (!isEmpty(currentKeyWord)) {
                edt_goods_search.setText(currentKeyWord);
                edt_goods_search.setFocusable(true);
                edt_goods_search.setSelection(currentKeyWord.length());
            }
        }
        if (!isShowHotSearch) {
            save_goods_history = SharedPrefUtil.COLLECTION_GOODS_HISTORY
                    .concat(SharedPrefUtil.getSharedUserString("member_id", ""));
            save_shop_history = SharedPrefUtil.COLLECTION_STORE_HISTORY
                    .concat(SharedPrefUtil.getSharedUserString("member_id", ""));
            setHistoryAdapter();
        }

        initKeywordSuggest();
    }

    private void setHistoryAdapter() {
        histotyTags.clear();
        edt_goods_search.setHint(getStringResouce(R.string.search_collection));
        String cacheSharedPrf = null;
        LogUtil.zhLogW("==currentFlag==========" + currentFlag);
        if ("goods".equals(currentFlag)) {
            cacheSharedPrf = SharedPrefUtil.getCacheSharedPrf(save_goods_history, "");
        } else if ("shop".equals(currentFlag)) {
            cacheSharedPrf = SharedPrefUtil.getCacheSharedPrf(save_shop_history, "");
        }
        if (!isEmpty(cacheSharedPrf)) {
            taglayout_history.setVisibility(View.VISIBLE);
            tv_history.setVisibility(View.VISIBLE);
            ll_clear.setVisibility(View.VISIBLE);
            String[] split = cacheSharedPrf.split(",");
            for (String s : split) {
                if (isEmpty(s)) {
                    continue;
                }
                histotyTags.add(s);
            }
        } else {
            taglayout_history.setVisibility(View.GONE);
            tv_history.setVisibility(View.GONE);
            ll_clear.setVisibility(View.GONE);
        }
        Collections.reverse(histotyTags);
        historyAdapter = new TagAdapter<String>(histotyTags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                View view = LayoutInflater.from(SearchGoodsActivity.this).inflate(R.layout.item_tag_layout, taglayout_history, false);
                TextView tv = (TextView) view.findViewById(R.id.tv_history_tag);
                tv.setText(s);
                return view;
            }
        };
        taglayout_history.setOnTagClickListener((view, position, parent) -> {
            String s = histotyTags.get(position);
            SearchResultAct.startAct(SearchGoodsActivity.this, s, currentFlag);
            finish();
            return true;
        });
        taglayout_history.setAdapter(historyAdapter);
    }

    /**
     * 初始化关键字提示
     */
    private void initKeywordSuggest() {
        mTips = new ArrayList<>();
        simpleRecyclerAdapter = new SimpleRecyclerAdapter(this, R.layout.item_tips, mTips) {
            @Override
            public void convert(SimpleViewHolder holder, Object o, int position) {
                TextView textView = holder.getView(R.id.tv_tip);
                textView.setText(mTips.get(position));
                holder.addOnClickListener(R.id.ll_parent);
            }
        };
        recycler_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_search.setAdapter(simpleRecyclerAdapter);
        simpleRecyclerAdapter.setOnItemClickListener((view, position) -> switchToJump(mTips.get(position)));
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_search_cancel.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        edt_goods_search.addTextChangedListener(this);
        edt_goods_search.setOnEditorActionListener((v, actionId, event) -> {
            String text = edt_goods_search.getText().toString();
            if ("sortFrag".equals(currentFlag) && isEmpty(text)) {
                text = edt_goods_search.getHint().toString();
            }
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!isShowHotSearch) {
                    if (!isEmpty(text)) {
                        if ("goods".equals(currentFlag)) {
                            String cacheSharedPrf = SharedPrefUtil.getCacheSharedPrf(save_goods_history, "");
                            String concat = cacheSharedPrf.concat(text.toString().concat(","));
                            SharedPrefUtil.saveCacheSharedPrf(save_goods_history, concat);
                        } else {
                            String cacheSharedPrf = SharedPrefUtil.getCacheSharedPrf(save_shop_history, "");
                            String concat = cacheSharedPrf.concat(text.toString().concat(","));
                            SharedPrefUtil.saveCacheSharedPrf(save_shop_history, concat);
                        }
                        SearchResultAct.startAct(SearchGoodsActivity.this, text.toString(), currentFlag);
                        finish();
                    }
                    return true;
                } else {
                    switchToJump(text.toString());
                    return true;
                }
            }
            return false;
        });

        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.ready_to_delete_history),
                getStringResouce(R.string.SelectRecommendAct_sure),
                v -> {
                    if (isShowHotSearch) {
                        presenter.clearSearchHistory();
                    } else {
                        if ("goods".equals(currentFlag)) {
                            SharedPrefUtil.saveCacheSharedPrf(save_goods_history, "");
                        } else if ("shop".equals(currentFlag)) {
                            SharedPrefUtil.saveCacheSharedPrf(save_shop_history, "");
                        }
                        clearSuccess("");
                    }
                    promptDialog.dismiss();
                }, getStringResouce(R.string.errcode_cancel),
                v -> promptDialog.dismiss());
    }

    @Override
    public void getSearchGoods(final HotSearchEntity entity) {
        hotTags.clear();
        histotyTags.clear();

        if (!isEmpty(entity.hot_keywords)) {
            tv_hot.setVisibility(View.VISIBLE);
            taglayout_hot.setVisibility(View.VISIBLE);
            hotTags.addAll(entity.hot_keywords);
        } else {
            tv_hot.setVisibility(View.GONE);
            taglayout_hot.setVisibility(View.GONE);
        }

        if (!isEmpty(entity.history_list)) {
            tv_history.setVisibility(View.VISIBLE);
            taglayout_history.setVisibility(View.VISIBLE);
            ll_clear.setVisibility(View.VISIBLE);
            histotyTags.addAll(entity.history_list);
        } else {
            tv_history.setVisibility(View.GONE);
            taglayout_history.setVisibility(View.GONE);
            ll_clear.setVisibility(View.GONE);
        }

        hotAdapter = new TagAdapter(hotTags) {
            @Override
            public View getView(FlowLayout parent, final int position, Object o) {
                final String tagStr = hotTags.get(position);
                View view = LayoutInflater.from(SearchGoodsActivity.this).inflate(R.layout.item_tag_layout, taglayout_hot, false);
                TextView tv = (TextView) view.findViewById(R.id.tv_history_tag);
                tv.setText(tagStr);

                view.setOnClickListener(v -> switchToJump(entity.hot_keywords.get(position)));
                return view;
            }
        };
        taglayout_hot.setAdapter(hotAdapter);

        historyAdapter = new TagAdapter(histotyTags) {
            @Override
            public View getView(FlowLayout parent, final int position, Object o) {
                final String tagStr = histotyTags.get(position);
                View view = LayoutInflater.from(SearchGoodsActivity.this).inflate(R.layout.item_tag_layout, taglayout_history, false);
                TextView tv = (TextView) view.findViewById(R.id.tv_history_tag);
                tv.setText(tagStr);
                view.setOnClickListener(v -> switchToJump(entity.history_list.get(position)));
                return view;
            }
        };
        taglayout_history.setAdapter(historyAdapter);
    }

    @Override
    public void getSearchTips(List<String> tips) {
        LogUtil.httpLogW("tips:" + tips.size());
        if (!isEmpty(tips)) {
            mTips.clear();
            mTips.addAll(tips);
            simpleRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void clearSuccess(String str) {
        histotyTags.clear();
        historyAdapter.notifyDataChanged();
        tv_history.setVisibility(View.GONE);
        ll_clear.setVisibility(View.GONE);
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
            case R.id.ll_clear:
                promptDialog.show();
                break;
        }
    }

    public void changeSearchMode(boolean isSearch) {
        if (isSearch) {
            ll_tag.setVisibility(View.GONE);
            recycler_search.setVisibility(View.VISIBLE);
        } else {
            ll_tag.setVisibility(View.VISIBLE);
            recycler_search.setVisibility(View.GONE);
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
        if (!isEmpty(s)) {
            if (isShowHotSearch) {
                changeSearchMode(true);
                presenter.getSearchTips(s.toString());
            }
        } else {
            changeSearchMode(false);
        }
    }

    public void switchToJump(String tag) {
        if (isEmpty(tag)) {
            return;
        }
        GoodsSearchParam param = new GoodsSearchParam();
        if ("sortFrag".equals(currentFlag)) {
            param.keyword = tag;
            CategoryAct.startAct(SearchGoodsActivity.this, param);
        } else if ("store_goods".equals(currentFlag)) {
            param.keyword = tag;
            GoodsSearchAct.startAct(SearchGoodsActivity.this, param);
        } else {
            Intent intent = new Intent(SearchGoodsActivity.this, CategoryAct.class);
            intent.putExtra("keyword", tag);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
