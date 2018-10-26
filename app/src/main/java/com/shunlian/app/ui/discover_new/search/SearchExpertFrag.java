package com.shunlian.app.ui.discover_new.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.adapter.SearchExpertAdapter;
import com.shunlian.app.bean.ExpertEntity;
import com.shunlian.app.presenter.SearchExpertPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.ISearchExpertView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchExpertFrag extends BaseLazyFragment implements ISearchExpertView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private SearchExpertPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<ExpertEntity.Expert> expertList;
    private SearchExpertAdapter mAdapter;
    private String currentKeyword;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_refresh_list, null, false);
        return view;
    }

    @Override
    protected void initData() {
    }

    public static SearchExpertFrag getInstance(String str) {
        SearchExpertFrag frag = new SearchExpertFrag();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", str);
        frag.setArguments(bundle);
        return frag;
    }

    public void setKeyWord(String str) {
        currentKeyword = str;
        if (mPresenter != null) {
            mPresenter.initPage();
            mPresenter.getSearchExpertList(true, currentKeyword);
        }
    }


    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    mPresenter.getSearchExpertList(true, currentKeyword);
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        super.initListener();
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        expertList = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        if (isEmpty(currentKeyword)) {
            currentKeyword = getArguments().getString("keyword");
        }
        mPresenter = new SearchExpertPresenter(getActivity(), this);
        mPresenter.getSearchExpertList(true, currentKeyword);

        nei_empty.setImageResource(R.mipmap.img_empty_common).setText("没有搜索到相关的达人").setButtonText(null);
    }

    @Override
    public void getExpertList(List<ExpertEntity.Expert> list, int page, int totalPage) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            expertList.clear();

            if (isEmpty(list)) {
                nei_empty.setVisibility(View.VISIBLE);
                recycler_list.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(list)) {
            expertList.addAll(list);
        }
        if (mAdapter == null) {
            mAdapter = new SearchExpertAdapter(getActivity(), expertList, this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (ExpertEntity.Expert expert : expertList) {
            if (memberId.equals(expert.member_id)) {
                if (expert.focus_status == 0) {
                    expert.focus_status = 1;
                } else {
                    expert.focus_status = 0;
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void toFocus(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }
}
