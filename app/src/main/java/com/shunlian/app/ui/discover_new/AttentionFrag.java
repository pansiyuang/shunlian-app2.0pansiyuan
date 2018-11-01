package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AttentionAdapter;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.adapter.TieziAvarAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.AttentionPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IAttentionView;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class AttentionFrag extends BaseLazyFragment implements IAttentionView, HotBlogAdapter.OnAdapterCallBack, AttentionAdapter.OnFocusListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.recycler_icons)
    RecyclerView recycler_icons;

    @BindView(R.id.rl_expert_rank)
    RelativeLayout rl_expert_rank;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    private AttentionPresenter mPresenter;
    private HotBlogAdapter hotBlogAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;
    private List<String> memberIcon;
    private AttentionAdapter attentionAdapter;
    private TieziAvarAdapter tieziAvarAdapter;
    private List<HotBlogsEntity.RecomandFocus> recomandFocusList;
    private int focusType; //0 关注blog列表用户,1关注推荐关注用户,2,关注空页面推荐关注用户

    QuickActions quick_actions;

    @Override
    public void onDestroyView() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroyView();
    }
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_attention, null, false);
        return view;
    }

    @Override
    protected void initData() {
        //分享
        quick_actions = new QuickActions(baseActivity);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        blogList = new ArrayList<>();
        memberIcon = new ArrayList<>();
        recomandFocusList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        mPresenter = new AttentionPresenter(getActivity(), this);
        mPresenter.getFocusblogs(true);
    }


    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            mPresenter.initPage();
            mPresenter.getFocusblogs(true);
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
        rl_expert_rank.setOnClickListener(v -> HotExpertRankActivity.startActivity(getActivity()));
        super.initListener();
    }

    @Override
    public void getFocusblogs(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();
            memberIcon.clear();
            if (!isEmpty(hotBlogsEntity.expert_list)) {
                memberIcon.addAll(hotBlogsEntity.expert_list);
                if (tieziAvarAdapter == null) {
                    tieziAvarAdapter = new TieziAvarAdapter(getActivity(), false, memberIcon);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recycler_icons.addItemDecoration(new HorizonItemDecoration(TransformUtil.dip2px(getActivity(), -9)));
                    recycler_icons.setLayoutManager(linearLayoutManager);
                    recycler_icons.setAdapter(tieziAvarAdapter);
                }
                recycler_icons.setVisibility(View.VISIBLE);
            } else {
                recycler_icons.setVisibility(View.GONE);
            }

            recomandFocusList.clear();
            recomandFocusList.addAll(hotBlogsEntity.recomand_focus_list);
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }

        if (isEmpty(blogList) && currentPage == 1) { //没有关注的用户并且当前是第一次获取数据
            if (attentionAdapter == null) {
                attentionAdapter = new AttentionAdapter(getActivity(), recomandFocusList);
                attentionAdapter.setOnFocusListener(this);
            }
            recycler_list.setAdapter(attentionAdapter);
        } else {
            if (hotBlogAdapter == null) {
                hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity(), recomandFocusList,quick_actions);
                hotBlogAdapter.setAdapterCallBack(this);
            }
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setPageLoading(currentPage, totalPage);
            hotBlogAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        switch (focusType) {
            case 0:
                for (BigImgEntity.Blog blog : blogList) {
                    if (memberId.equals(blog.member_id)) {
                        if (blog.is_focus == 0) {
                            blog.is_focus = 1;
                        } else {
                            blog.is_focus = 0;
                        }
                    }
                }
                hotBlogAdapter.notifyDataSetChanged();
                break;
            case 1:
            case 2:
                for (HotBlogsEntity.RecomandFocus recomandFocus : recomandFocusList) {
                    if (memberId.equals(recomandFocus.member_id)) {
                        if (recomandFocus.focus_status == 0) {
                            recomandFocus.focus_status = 1;
                        } else {
                            recomandFocus.focus_status = 0;
                        }
                    }
                    hotBlogAdapter.MemberAdapterNotifyDataSetChanged();
                    if (attentionAdapter == null) {
                        return;
                    }
                    attentionAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void praiseBlog(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.is_praise = 1;
                blog.praise_num++;
            }
        }
        hotBlogAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void toFocusUser(int isFocus, String memberId) {
        focusType = 0;
        mPresenter.focusUser(isFocus, memberId);
    }

    @Override
    public void toFocusMember(int isFocus, String memberId) {
        focusType = 1;
        mPresenter.focusUser(isFocus, memberId);
    }

    @Override
    public void toPraiseBlog(String blogId) {
        mPresenter.praiseBlos(blogId);
    }

    @Override
    public void onFocus(int isFocus, String memberId) {
        focusType = 2;
        mPresenter.focusUser(isFocus, memberId);
    }
}
