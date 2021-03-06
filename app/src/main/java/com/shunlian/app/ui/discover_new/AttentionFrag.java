package com.shunlian.app.ui.discover_new;

import android.animation.Animator;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.AttentionAdapter;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.adapter.TieziAvarAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.BlogCommentEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.listener.SoftKeyBoardListener;
import com.shunlian.app.presenter.AttentionPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IAttentionView;
import com.shunlian.app.widget.BlogCommentSendDialog;
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

public class AttentionFrag extends BaseLazyFragment implements IAttentionView, HotBlogAdapter.OnAdapterCallBack, AttentionAdapter.OnFocusListener, ShareGoodDialogUtil.OnShareBlogCallBack {

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
    private PromptDialog promptDialog;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private LottieAnimationView mAnimationView;
    private String currentCommentBlogId;
    private BlogCommentSendDialog mPopWindow;
    private ShareInfoParam mShareInfoParam;

    @Override
    public void onDestroyView() {
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

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        EventBus.getDefault().register(this);
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
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
                    tieziAvarAdapter.setOnItemClickListener((view, position) -> HotExpertRankActivity.startActivity(getActivity()));
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

        if (blogList.size() == 0 && currentPage == 1) { //没有关注的用户并且当前是第一次获取数据
            if (attentionAdapter == null) {
                attentionAdapter = new AttentionAdapter(getActivity(), recomandFocusList);
                attentionAdapter.setOnFocusListener(this);
            }
            recycler_list.setAdapter(attentionAdapter);
            attentionAdapter.setHasFocus(hotBlogsEntity.is_have_focus);
        } else {
            if (hotBlogAdapter == null) {
                hotBlogAdapter = new HotBlogAdapter(getActivity(), blogList, getActivity(), recomandFocusList);
                hotBlogAdapter.setAdapterCallBack(this);
                hotBlogAdapter.setShowAttention(false);
            }
            if (currentPage == 1) {
                recycler_list.setAdapter(hotBlogAdapter);
            }
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
                hotBlogAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
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
                        if (hotBlogAdapter != null) {
                            hotBlogAdapter.MemberAdapterNotifyDataSetChanged();
                        }
                        if (attentionAdapter != null) {
                            attentionAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
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
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        if(mShareInfoParam==null){
            mShareInfoParam = new ShareInfoParam();
        }
        if (mShareInfoParam != null) {
            mShareInfoParam.isShowTiltle = false;
            mShareInfoParam.userName = baseEntity.data.userName;
            mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
            mShareInfoParam.shareLink = baseEntity.data.shareLink;
            mShareInfoParam.desc = baseEntity.data.desc;
            mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
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
        focusType = 0;
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
        focusType = 1;
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
        if(mPresenter!=null) {
            mPresenter.getShareInfo(mPresenter.goods, goodid);
        }
    }

    @Override
    public void showCommentView(String blogId) {
        currentCommentBlogId = blogId;
        showPopupComment();
    }

    @Override
    public void onFocus(int isFocus, String memberId) {
        focusType = 2;
        mPresenter.focusUser(isFocus, memberId);
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

    @Override
    public void shareSuccess(String blogId, String goodsId) {
        mPresenter.goodsShare("blog_goods", blogId, goodsId);
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
    public void refreshData(DefMessageEvent event) {
        if (mPresenter != null && event.loginSuccess) {
            mPresenter.initPage();
            mPresenter.getFocusblogs(true);
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
