package com.shunlian.app.ui.discover_new;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.HotBlogAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.BaseInfoEvent;
import com.shunlian.app.eventbus_bean.BlogCommentEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DiscoveryLocationEvent;
import com.shunlian.app.eventbus_bean.MessageReadSuccessEvent;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.eventbus_bean.RefreshBlogListEvent;
import com.shunlian.app.listener.SoftKeyBoardListener;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShapeUtils;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.widget.BlogCommentSendPopwindow;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    @BindView(R.id.rl_rootView)
    RelativeLayout rl_rootView;

    private HotBlogPresenter hotBlogPresenter;
    private HotBlogAdapter hotBlogAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;
    private ObjectMapper objectMapper;
    private PromptDialog promptDialog;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private ShareInfoParam mShareInfoParam;
    private LottieAnimationView mAnimationView;
    private String currentCommentBlogId;
    private BlogCommentSendPopwindow mPopWindow;

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
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        EventBus.getDefault().register(this);
        shareGoodDialogUtil = new ShareGoodDialogUtil(baseActivity);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
        NestedSlHeader header = new NestedSlHeader(baseActivity);
        lay_refresh.setRefreshHeaderView(header);
        recycler_list.setNestedScrollingEnabled(false);

        objectMapper = new ObjectMapper();
        blogList = new ArrayList<>();
        hotBlogPresenter = new HotBlogPresenter(baseActivity, this);
        hotBlogPresenter.getHotBlogList(true);
        hotBlogPresenter.wordList();

        manager = new LinearLayoutManager(baseActivity);
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

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            blogList.clear();
            MainActivity.refreshBlogList = false;
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
            hotBlogAdapter = new HotBlogAdapter(baseActivity, blogList, hotBlogsEntity.ad_list);
            recycler_list.setAdapter(hotBlogAdapter);
            hotBlogAdapter.setAdapterCallBack(this);
        }
        if (currentPage == 1 && hotBlogsEntity.base_info != null & hotBlogsEntity.base_info.avatar != null) {
            hotBlogAdapter.setMyIcon(hotBlogsEntity.base_info.avatar);
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

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
            EventBus.getDefault().post(new MessageReadSuccessEvent(true));
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
                promptDialog = new PromptDialog(baseActivity);
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setTvSureColor(R.color.pink_color);
                promptDialog.setTvCancleIsBold(false);
                promptDialog.setTvSureIsBold(false);
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
    public void toPraiseBlog(String blogId, LottieAnimationView animationView) {
        hotBlogPresenter.praiseBlos(blogId);
        mAnimationView = animationView;
    }

    @Override
    public void toDown(String id) {
        hotBlogPresenter.downCount(id);
    }

    @Override
    public void getShareInfo(String blogId, String goodid) {
        mShareInfoParam = new ShareInfoParam();
        mShareInfoParam.goods_id = goodid;
        mShareInfoParam.blogId = blogId;
        if (hotBlogPresenter != null) {
            hotBlogPresenter.getShareInfo(hotBlogPresenter.goods, goodid);
        }
    }

    @Override
    public void showCommentView(String blogId) {
        currentCommentBlogId = blogId;
        showPopupComment();
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
    public void getWordList(List<String> wordList) {
        Common.setWordList(wordList);
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
    public void refreshData(RefreshBlogListEvent event) {
        if (hotBlogPresenter != null && event.isRefresh) {
            hotBlogPresenter.initPage();
            hotBlogPresenter.getHotBlogList(true);
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
            mPopWindow = new BlogCommentSendPopwindow(getActivity());
            mPopWindow.setOnPopClickListener((String content) -> {
                if (isEmpty(currentCommentBlogId)) {
                    return;
                }
                hotBlogPresenter.sendComment(content, "", currentCommentBlogId, "0");
            });
        }
        mPopWindow.show();
    }
}
