package com.shunlian.app.ui.discover_new.search;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.TagEntity;
import com.shunlian.app.presenter.DiscoverSearchPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IDiscoverSearchView;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @BindView(R.id.tagflow_recently)
    TagFlowLayout tagflow_recently;

    @BindView(R.id.tagflow_hot)
    TagFlowLayout tagflow_hot;

    @BindView(R.id.tv_del)
    TextView tv_del;

    private DiscoverSearchPresenter mPresenter;
    private String currentKeyword;
    private String historyContent;
    private TagAdapter<String> historyAdapter;
    private TagAdapter<TagEntity.Tag> hotAdapter;
    private List<String> tags = new ArrayList<>();
    private List<TagEntity.Tag> mTagList = new ArrayList<>();
    ;
    private PromptDialog promptDialog;

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
            jump2Result(currentKeyword);
        });

        tagflow_recently.setOnTagClickListener((view, position, parent) -> {
            String s = tags.get(position);
            jump2Result(s);
            return true;
        });
        tagflow_hot.setOnTagClickListener((view, position, parent) -> {
            String s = mTagList.get(position).key_word;
            jump2Result(s);
            return true;
        });
        tv_del.setOnClickListener(v -> {
            if (promptDialog == null) {
                promptDialog = new PromptDialog(this);
            }
            promptDialog.setSureAndCancleListener("确定要删除所有搜索记录吗？", getStringResouce(R.string.SelectRecommendAct_sure), view -> {
                promptDialog.dismiss();
                SharedPrefUtil.saveCacheSharedPrf("discover_search_history", "");
                rl_recently.setVisibility(View.GONE);
            }, getStringResouce(R.string.errcode_cancel), view -> promptDialog.dismiss()).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        historyContent = SharedPrefUtil.getCacheSharedPrf("discover_search_history", "");
        setHistoryData(historyContent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mPresenter = new DiscoverSearchPresenter(this, this);
        mPresenter.getHotSearch();
    }

    public void setHistoryData(String content) {
        tags.clear();
        if (isEmpty(content)) {
            rl_recently.setVisibility(View.GONE);
        } else {
            rl_recently.setVisibility(View.VISIBLE);
            String[] tagStrs = content.split("_");
            for (int i = 0; i < tagStrs.length; i++) {
                tags.add(tagStrs[i]);
            }
            Collections.reverse(tags);
            if (historyAdapter == null) {
                historyAdapter = new TagAdapter<String>(tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View view = LayoutInflater.from(DiscoverSearchActivity.this).inflate(R.layout.item_discover_tag, tagflow_recently, false);
                        TextView tv = view.findViewById(R.id.tv_tag);
                        tv.setText(s);
                        return view;
                    }
                };
            }
            tagflow_recently.setAdapter(historyAdapter);
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
                Common.staticToast("请输入您要搜索的内容");
            } else {
                jump2Result(currentKeyword);
            }
        }
        return false;
    }

    @Override
    public void getTagList(List<TagEntity.Tag> tagList) {
        if (!isEmpty(tagList)) {
            mTagList.addAll(tagList);

            if (hotAdapter == null) {
                hotAdapter = new TagAdapter<TagEntity.Tag>(tagList) {
                    @Override
                    public View getView(FlowLayout parent, int position, TagEntity.Tag tag) {
                        View view = LayoutInflater.from(DiscoverSearchActivity.this).inflate(R.layout.item_discover_tag, tagflow_hot, false);
                        TextView tv = view.findViewById(R.id.tv_tag);
                        tv.setText(tag.key_word);
                        return view;
                    }
                };
                tagflow_hot.setAdapter(hotAdapter);
            }
        }
    }

    public void jump2Result(String keyword) {
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
            DiscoverSearchResultActivity.startActivity(this, keyword);
        }
    }

}
