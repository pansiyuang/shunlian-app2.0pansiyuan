package com.shunlian.app.ui.discover_new.search;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
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
import com.shunlian.app.listener.SoftKeyBoardListener;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.widget.BlogCommentSendDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24.
 */

public class SearchBlogFrag extends BaseLazyFragment implements IHotBlogView, HotBlogAdapter.OnAdapterCallBack, ShareGoodDialogUtil.OnShareBlogCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private HotBlogPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<BigImgEntity.Blog> blogList;
    private HotBlogAdapter hotBlogAdapter;
    private String currentKeyword;
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
        View view = inflater.inflate(R.layout.layout_refresh_list, null, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    public static SearchBlogFrag getInstance(String str) {
        SearchBlogFrag frag = new SearchBlogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", str);
        frag.setArguments(bundle);
        return frag;
    }

    public void setKeyWord(String str) {
        currentKeyword = str;
        if (mPresenter != null) {
            mPresenter.initPage();
            mPresenter.getHotBlogList(true, "", currentKeyword);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getHotBlogList(true, "", currentKeyword);
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
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        //分享
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        if (isEmpty(currentKeyword)) {
            currentKeyword = getArguments().getString("keyword");
        }

        blogList = new ArrayList<>();
        mPresenter = new HotBlogPresenter(getActivity(), this);
        mPresenter.getHotBlogList(true, "", currentKeyword);

        nei_empty.setImageResource(R.mipmap.img_empty_common).setText("没有搜索到相关的文章").setButtonText(null);
    }

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();

            if (isEmpty(hotBlogsEntity.list)) {
                nei_empty.setVisibility(View.VISIBLE);
                lay_refresh.setVisibility(View.GONE);
            } else {
                nei_empty.setVisibility(View.GONE);
                lay_refresh.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            blogList.addAll(hotBlogsEntity.list);
        }
        if (hotBlogAdapter == null) {
            hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity());
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
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
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
            mShareInfoParam.desc = baseEntity.data.desc;
            mShareInfoParam.price = baseEntity.data.price;
            mShareInfoParam.title = baseEntity.data.title;
            mShareInfoParam.img = baseEntity.data.img;
            mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
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

    @Override
    public void getWordList(List<String> wordList) {

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
}
