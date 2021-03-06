package com.shunlian.app.ui.discover_new;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.BlogCommentEvent;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.listener.SoftKeyBoardListener;
import com.shunlian.app.presenter.CommonBlogPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.view.ICommonBlogView;
import com.shunlian.app.widget.BlogCommentSendDialog;
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
 * Created by Administrator on 2018/10/22.
 */

public class CommonBlogFrag extends BaseLazyFragment implements ICommonBlogView, HotBlogAdapter.OnAdapterCallBack, HotBlogAdapter.OnDelBlogListener, HotBlogAdapter.OnFavoListener, ShareGoodDialogUtil.OnShareBlogCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private CommonBlogPresenter mPresenter;
    private String currentFrom, currentType, currentMemberId;
    private HotBlogAdapter hotBlogAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;
    private boolean isMine;
    private PromptDialog promptDialog;
    private LottieAnimationView mAnimationView;
    private String currentCommentBlogId;
    private BlogCommentSendDialog mPopWindow;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private ShareInfoParam mShareInfoParam;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_common_blog, null, false);
        return view;
    }

    public static BaseFragment getInstance(String comFrom, String memberId, boolean isSelf) {
        CommonBlogFrag commonBlogFrag = new CommonBlogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("from", comFrom);
        bundle.putBoolean("is_self", isSelf);
        bundle.putString("member_id", memberId);
        commonBlogFrag.setArguments(bundle);
        return commonBlogFrag;
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
        //分享
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
        NestedSlHeader header = new NestedSlHeader(getActivity());
        lay_refresh.setRefreshHeaderView(header);
        lay_refresh.setRefreshEnabled(false);

        currentMemberId = getArguments().getString("member_id");
        currentFrom = getArguments().getString("from");
        isMine = getArguments().getBoolean("is_self", false);

        if ("我的".equals(currentFrom)) {
            currentType = "2";
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText("空空如也，还没有分享过内容")
                    .setButtonText(null);
        } else if ("收藏".equals(currentFrom)) {
            currentType = "3";
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText("空空如也，还没有收藏过内容")
                    .setButtonText(null);
        }

        mPresenter = new CommonBlogPresenter(getActivity(), this);
        mPresenter.getBlogList(true, currentMemberId, currentType);

        blogList = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getBlogList(true, currentMemberId, currentType);
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
                    int position = manager.findFirstVisibleItemPosition();
                    View firstVisiableChildView = manager.findViewByPosition(position);
                    int itemHeight = firstVisiableChildView.getHeight();
                    int totalDistance = (position) * itemHeight - firstVisiableChildView.getTop();
                    ((MyPageActivity) getActivity()).setScrollDistance(totalDistance > 0 ? false : true, currentType);
                }
            }
        });
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            }
        });
        super.initListener();
    }

    public void initPage() {
        mPresenter.initPage();
        mPresenter.getBlogList(true, currentMemberId, currentType);
    }

    @Override
    public void getFocusblogs(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();

            if (isEmpty(hotBlogsEntity.list)) {
                recycler_list.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
            } else {
                recycler_list.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
            }
            ((MyPageActivity) getActivity()).initInfo(hotBlogsEntity.member_info, hotBlogsEntity.discovery_info, hotBlogsEntity.unread);
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setAdapterCallBack(this);
            hotBlogAdapter.setOnDelListener(this);
            hotBlogAdapter.setOnFavoListener(this);
        }

        if ("2".equals(currentType) && !isMine) {
            hotBlogAdapter.setShowAttention(false);
        }

        if ("2".equals(currentType) && isMine) {
            hotBlogAdapter.setShowMore(true);
        }

        hotBlogAdapter.setPageLoading(currentPage, totalPage);
        hotBlogAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        int focus;
        if (isFocus == 0) {
            focus = 1;
        } else {
            focus = 0;
        }
        ((MyPageActivity) getActivity()).setAttentStatus(focus, memberId);
        for (BigImgEntity.Blog blog : blogList) {
            if (memberId.equals(blog.member_id)) {
                blog.is_focus = focus;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    @Override
    public void praiseBlog(String blogId) {
        if (mAnimationView != null) {
            mAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    for (BigImgEntity.Blog blog : blogList) {
                        if (blogId.equals(blog.id)) {
                            blog.is_praise = 1;
                            blog.praise_num++;
                        }
                    }
                    hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimationView.playAnimation();
        }
    }

    @Override
    public void downCountSuccess(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.down_num++;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    @Override
    public void showFailureView(int request_code) {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        if (mShareInfoParam == null) {
            mShareInfoParam = new ShareInfoParam();
        }
        if (mShareInfoParam != null) {
            mShareInfoParam.isShowTiltle = false;
            mShareInfoParam.userName = baseEntity.data.userName;
            mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
            mShareInfoParam.shareLink = baseEntity.data.shareLink;
            mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
            mShareInfoParam.desc = baseEntity.data.desc;
            mShareInfoParam.price = baseEntity.data.price;
            mShareInfoParam.img = baseEntity.data.img;
            mShareInfoParam.title = baseEntity.data.title;
            mShareInfoParam.little_word = baseEntity.data.little_word;
            mShareInfoParam.time_text = baseEntity.data.time_text;
            mShareInfoParam.is_start = baseEntity.data.is_start;
            mShareInfoParam.market_price = baseEntity.data.market_price;
            mShareInfoParam.voucher = baseEntity.data.voucher;
            if (shareGoodDialogUtil != null) {
                shareGoodDialogUtil.shareGoodDialog(mShareInfoParam, true, true);
                shareGoodDialogUtil.setShareGoods();
            }
        }
    }

    @Override
    public void toFocusUser(int isFocus, String memberId, String nickName) {
        if (isFocus == 1) {
            if (promptDialog == null) {
                promptDialog = new PromptDialog(getActivity());
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setTvSureColor(R.color.pink_color);
                promptDialog.setTvCancleIsBold(false);
                promptDialog.setTvSureIsBold(false);
            }
            promptDialog.setSureAndCancleListener(String.format(getStringResouce(R.string.ready_to_unFocus), nickName),
                    getStringResouce(R.string.unfollow), view -> {
                        mPresenter.focusUser(isFocus, memberId);
                        promptDialog.dismiss();
                    }, getStringResouce(R.string.give_up), view -> promptDialog.dismiss()
            ).show();
        } else {
            mPresenter.focusUser(isFocus, memberId);
        }
    }

    @Override
    public void toFocusMember(int isFocus, String memberId, String nickName) {
    }

    @Override
    public void toPraiseBlog(String blogId, LottieAnimationView animationView) {
        mPresenter.praiseBlos(blogId);
        mAnimationView = animationView;
    }

    @Override
    public void toDown(String blogId) {
        mPresenter.downCount(blogId);
    }

    @Override
    public void getShareInfo(String blogId, String goodid) {
        mShareInfoParam = new ShareInfoParam();
        mShareInfoParam.goods_id = goodid;
        mShareInfoParam.blogId = blogId;
        if (mPresenter != null) {
            mPresenter.getShareInfo(mPresenter.goods, goodid);
        }
    }

    @Override
    public void showCommentView(String blogId) {
        currentCommentBlogId = blogId;
        showPopupComment();
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
    public void onDel(String blogId) {
        for (int i = 0; i < blogList.size(); i++) {
            BigImgEntity.Blog blog = blogList.get(i);
            if (blogId.equals(blog.id)) {
                blogList.remove(i);
            }
        }
        if (isEmpty(blogList)) {
            recycler_list.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnFavo(int isFavo, String blogId) {
        if (currentType.equals("3") && isMine) {
            for (int i = 0; i < blogList.size(); i++) {
                BigImgEntity.Blog blog = blogList.get(i);
                if (blogId.equals(blog.id) && isFavo == 0) {
                    blogList.remove(i);
                }
            }
            hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
            if (isEmpty(blogList)) {
                recycler_list.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {
        mPresenter.goodsShare("blog_goods", blogId, goodsId);
    }

    @Override
    public void shareGoodsSuccess(String blogId, String goodsId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.total_share_num++;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    @Override
    public void replySuccess(BigImgEntity.CommentItem commentItem) {
        for (BigImgEntity.Blog blog : blogList) {
            if (currentCommentBlogId.equals(blog.id)) {
                blog.comment_list.list.add(0, commentItem);
                blog.comment_list.total++;
                break;
            }
        }
        Common.staticToasts(getActivity(), "评论成功", R.mipmap.icon_common_duihao);
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
        currentCommentBlogId = null;
        mPopWindow.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(RefreshBlogEvent event) {
        switch (event.mType) {
            case RefreshBlogEvent.ATTENITON_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.memberId.equals(blog.member_id)) {
                        blog.is_focus = event.mData.is_focus;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
                break;
            case RefreshBlogEvent.PRAISE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.is_praise = event.mData.is_praise;
                        blog.praise_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
                break;
            case RefreshBlogEvent.SHARE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.total_share_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
                break;
            case RefreshBlogEvent.DOWNLOAD_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.down_num++;
                    }
                }
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(BlogCommentEvent event) {
        switch (event.sendType) {
            case BlogCommentEvent.ADD_TYPE:
                insertComment(event.mComment, true);
                break;
            case BlogCommentEvent.DEL_TYPE:
                insertComment(event.mComment, false);
                break;
        }
    }

    public void insertComment(FindCommentListEntity.ItemComment insertItem, boolean isAdd) {
        for (BigImgEntity.Blog blog : blogList) {
            if (insertItem.discovery_id.equals(blog.id)) {
                blog.comment_list.list.clear();
                if (isAdd) {
                    for (FindCommentListEntity.ItemComment item : insertItem.comment_list) {
                        blog.comment_list.list.add(new BigImgEntity.CommentItem(item));
                    }
                    blog.comment_list.total = insertItem.comment_count;
                } else {
                    for (FindCommentListEntity.ItemComment item : insertItem.reply_result) {
                        blog.comment_list.list.add(new BigImgEntity.CommentItem(item));
                    }
                    blog.comment_list.total = insertItem.reply_count;
                }
                break;
            }
        }
        hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    private void showPopupComment() {
        if (mPopWindow == null) {
            mPopWindow = new BlogCommentSendDialog(getActivity());
            mPopWindow.setOnPopClickListener((String content) -> {
                if (isEmpty(currentCommentBlogId)) {
                    return;
                }
                mPresenter.sendComment(content, "", currentCommentBlogId, "0");
            });
        }
        mPopWindow.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
