package com.shunlian.app.ui.discover_new;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.eventbus_bean.BaseInfoEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/15.
 */

public class HotBlogFrag extends BaseLazyFragment implements IHotBlogView, HotBlogAdapter.OnAdapterCallBack, ShareGoodDialogUtil.OnShareBlogCallBack {
    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private HotBlogPresenter hotBlogPresenter;
    private HotBlogAdapter hotBlogAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;
    private ObjectMapper objectMapper;
    private PromptDialog promptDialog;
    private ShareGoodDialogUtil shareGoodDialogUtil;

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.hotblog_frag, null, false);
        return view;
    }

    @Override
    protected void initData() {
        //分享
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        EventBus.getDefault().register(this);
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        objectMapper = new ObjectMapper();
        blogList = new ArrayList<>();
        hotBlogPresenter = new HotBlogPresenter(getActivity(), this);
        hotBlogPresenter.getHotBlogList(true);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("暂时没有用户发布精选文章")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            hotBlogPresenter.initPage();
            hotBlogPresenter.getHotBlogList(true);
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (hotBlogPresenter != null) {
                            hotBlogPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        super.initListener();
    }

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();

            if (isEmpty(hotBlogsEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                recycler_list.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            }
            saveBaseInfo(hotBlogsEntity.base_info);
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, hotBlogsEntity.ad_list, shareGoodDialogUtil);
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setAdapterCallBack(this);
        }
        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (memberId.equals(blog.member_id)) {
                if (blog.is_focus == 0) {
                    blog.is_focus = 1;
                } else {
                    blog.is_focus = 0;
                }
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
    }

    @Override
    public void praiseBlog(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.is_praise = 1;
                blog.praise_num++;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
    }

    @Override
    public void downCountSuccess(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.down_num++;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
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
    public void toFocusUser(int isFocus, String memberId, String nickName) {
        if (isFocus == 1) {
            if (promptDialog == null) {
                promptDialog = new PromptDialog(getActivity());
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setTvSureColor(R.color.pink_color);
            }
            promptDialog.setSureAndCancleListener(String.format(getStringResouce(R.string.ready_to_unFocus), nickName),
                    getStringResouce(R.string.unfollow), view -> {
                        hotBlogPresenter.focusUser(isFocus, memberId);
                        promptDialog.dismiss();
                    }, getStringResouce(R.string.give_up), view -> promptDialog.dismiss()
            ).show();
        } else {
            hotBlogPresenter.focusUser(isFocus, memberId);
        }
    }

    @Override
    public void toFocusMember(int isFocus, String memberId, String nickName) {
    }

    @Override
    public void toPraiseBlog(String blogId) {
        hotBlogPresenter.praiseBlos(blogId);
    }

    @Override
    public void toDown(String id) {
        hotBlogPresenter.downCount(id);
    }

    public void saveBaseInfo(HotBlogsEntity.BaseInfo baseInfo) {
        try {
            if (baseInfo != null) {
                String infoStr = objectMapper.writeValueAsString(baseInfo);
                SharedPrefUtil.saveSharedUserString("base_info", infoStr);
                EventBus.getDefault().post(new BaseInfoEvent(baseInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {
        hotBlogPresenter.goodsShare("blog_goods", blogId, goodsId);
    }

    @Override
    public void shareGoodsSuccess(String blogId, String goodsId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.total_share_num++;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(RefreshBlogEvent event) {
        switch (event.mType) {
            case RefreshBlogEvent.ATTENITON_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.memberId.equals(blog.member_id)) {
                        LogUtil.httpLogW("is_focus:" + event.mData.is_focus);
                        blog.is_focus = event.mData.is_focus;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
                break;
            case RefreshBlogEvent.PRAISE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.is_praise = event.mData.is_praise;
                        blog.praise_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
                break;
            case RefreshBlogEvent.SHARE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.total_share_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
                break;
            case RefreshBlogEvent.DOWNLOAD_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.down_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(DefMessageEvent event) {
        if (hotBlogPresenter != null && event.loginSuccess) {
            hotBlogPresenter.initPage();
            hotBlogPresenter.getHotBlogList(true);
        }
    }
}
