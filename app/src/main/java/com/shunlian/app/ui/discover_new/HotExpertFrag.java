package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HotExpertAdapter;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.HotExpertPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IHotExpertView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/19.
 */

public class HotExpertFrag extends BaseLazyFragment implements IHotExpertView, HotExpertAdapter.OnAdapterCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recyclerView;

    private HotExpertPresenter mPresenter;
    private HotExpertAdapter mAdapter;
    private LinearLayoutManager manager;
    private List<HotBlogsEntity.Blog> blogList;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_hot_expert, null, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        blogList = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        mPresenter = new HotExpertPresenter(getActivity(), this);
        mPresenter.getHotExpertList();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getHotExpertList(List<HotBlogsEntity.Blog> hotBlogList) {
        if (isEmpty(hotBlogList)) {
            return;
        }
        blogList.addAll(hotBlogList);
        if (mAdapter == null) {
            mAdapter = new HotExpertAdapter(getActivity(), blogList);
            mAdapter.setAdapterCallBack(this);
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (HotBlogsEntity.Blog blog : blogList) {
            if (memberId.equals(blog.member_id)) {
                if (blog.is_focus == 0) {
                    blog.is_focus = 1;
                    blog.fans_num++;
                } else {
                    blog.fans_num--;
                    blog.is_focus = 0;
                }
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void praiseBlog(String blogId) {
        for (HotBlogsEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.is_praise = 1;
                blog.praise_num++;
            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void toFocusUser(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }

    @Override
    public void toPraiseBlog(String blogId) {
        mPresenter.praiseBlos(blogId);
    }
}
