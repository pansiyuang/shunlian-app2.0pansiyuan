package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.ActivityDetailAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.eventbus_bean.RefreshBlogEvent;
import com.shunlian.app.presenter.ActivityDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.find_send.FindSendPictureTextAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IActivityDetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class ActivityDetailActivity extends BaseActivity implements IActivityDetailView, ActivityDetailAdapter.OnAdapterCallBack, ShareGoodDialogUtil.OnShareBlogCallBack {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_join)
    MyImageView miv_join;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    public int offset;
    private LinearLayoutManager manager;
    private int totalDy;
    private int layoutHeight;
    private ActivityDetailPresenter mPresent;
    private String currentId;
    private List<BigImgEntity.Blog> blogList;
    private ActivityDetailAdapter mAdapter;
    private ObjectMapper objectMapper;
    private HotBlogsEntity.Detail currentDetail;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private PromptDialog promptDialog, plusDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.act_activity_detail;
    }

    public static void startAct(Context context, String actId) {
        Intent intent = new Intent(context, ActivityDetailActivity.class);
        intent.putExtra("activity_id", actId);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        NestedSlHeader header = new NestedSlHeader(this);
        lay_refresh.setRefreshHeaderView(header);

        defToolbar();
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        shareGoodDialogUtil.setOnShareBlogCallBack(this);
        EventBus.getDefault().register(this);
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        offset = toolbarParams.height;

        currentId = getIntent().getStringExtra("activity_id");

        mPresent = new ActivityDetailPresenter(this, this);
        mPresent.getActivityDetail(true, currentId);

        manager = new LinearLayoutManager(this);
        blogList = new ArrayList<>();
        recycler_list.setLayoutManager(manager);
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler_list.setNestedScrollingEnabled(false);

        objectMapper = new ObjectMapper();
    }


    @Override
    protected void initListener() {
        super.initListener();
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    View firstView = manager.findViewByPosition(firstPosition);
                    if (firstView instanceof RelativeLayout) {
                        totalDy += dy;
                        setBgColor(totalDy);
                    } else {
                        setToolbar();
                        totalDy = layoutHeight;
                    }
                }
            }
        });
        miv_join.setOnClickListener(v -> {
            if (!Common.isAlreadyLogin()) {
                Common.goGoGo(this, "login");
                return;
            }
            if (!Common.isPlus()) {
                initHintDialog();
                return;
            }
            try {
                String baseInfoStr = SharedPrefUtil.getSharedUserString("base_info", "");
                HotBlogsEntity.BaseInfo baseInfo = objectMapper.readValue(baseInfoStr, HotBlogsEntity.BaseInfo.class);
                FindSendPictureTextAct.SendConfig sendConfig = new FindSendPictureTextAct.SendConfig();
                if (baseInfo.white_list == 0) {
                    sendConfig.isWhiteList = false;
                } else {
                    sendConfig.isWhiteList = true;
                }
                sendConfig.activityID = currentId;
                if (!isEmpty(currentDetail.title)) {
                    sendConfig.activityTitle = "#" + currentDetail.title + "#";
                }
                sendConfig.memberId = baseInfo.member_id;
                FindSendPictureTextAct.startAct(ActivityDetailActivity.this, sendConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        lay_refresh.setOnRefreshListener(() -> {
            mPresent.initPage();
            mPresent.getActivityDetail(true, currentId);
        });
    }

    public void defToolbar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.titleBar(toolbar, false)
                .statusBarDarkFont(true, 0f)
                .addTag(GoodsDetailAct.class.getName()).init();
    }

    public void setBgColor(int totalDy) {
        ImmersionBar immersionBar = ImmersionBar.with(this).addViewSupportTransformColor(toolbar, R.color.transparent);
        if (totalDy <= layoutHeight) {
            if (totalDy <= 0) {
                totalDy = 0;
            }
            float alpha = (float) totalDy / layoutHeight;
            immersionBar.statusBarAlpha(alpha).addTag(GoodsDetailAct.class.getName()).init();
        } else {
            setToolbar();
        }
    }

    public void setToolbar() {
        immersionBar.statusBarAlpha(1.0f).addTag(GoodsDetailAct.class.getName()).init();
        miv_close.setAlpha(1.0f);
    }

    @Override
    public void getActivityDetail(List<BigImgEntity.Blog> list, HotBlogsEntity.Detail detail, int page, int totalPage) {
        currentDetail = detail;
        if (page == 1) {
            blogList.clear();
        }
        if (!isEmpty(list)) {
            blogList.addAll(list);
        }
        if (mAdapter == null) {
            mAdapter = new ActivityDetailAdapter(this, blogList, detail, shareGoodDialogUtil);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setAdapterCallBack(this);
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void downCountSuccess(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.down_num++;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void praiseBlog(String blogId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.is_praise = 1;
                blog.praise_num++;
            }
        }
        mAdapter.notifyDataSetChanged();
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
                promptDialog = new PromptDialog(this);
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setTvSureColor(R.color.pink_color);
                promptDialog.setTvCancleIsBold(false);
                promptDialog.setTvSureIsBold(false);
            }
            promptDialog.setSureAndCancleListener(String.format(getStringResouce(R.string.ready_to_unFocus), nickName),
                    getStringResouce(R.string.unfollow), view -> {
                        mPresent.focusUser(isFocus, memberId);
                        promptDialog.dismiss();
                    }, getStringResouce(R.string.give_up), view -> promptDialog.dismiss()
            ).show();
        } else {
            mPresent.focusUser(isFocus, memberId);
        }
    }

    @Override
    public void toPraiseBlog(String blogId) {
        mPresent.praiseBlos(blogId);
    }

    @Override
    public void toDown(String blogId) {
        mPresent.downCount(blogId);
    }

    @Override
    public void OnTopSize(int height) {
        layoutHeight = height - offset - ImmersionBar.getStatusBarHeight(this);
    }

    @Override
    public void shareGoodsSuccess(String blogId, String goodsId) {
        for (BigImgEntity.Blog blog : blogList) {
            if (blogId.equals(blog.id)) {
                blog.total_share_num++;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {
        mPresent.goodsShare("blog_goods", blogId, goodsId);
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
                mAdapter.notifyDataSetChanged();
                break;
            case RefreshBlogEvent.PRAISE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.is_praise = event.mData.is_praise;
                        blog.praise_num++;
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case RefreshBlogEvent.SHARE_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.total_share_num++;
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
            case RefreshBlogEvent.DOWNLOAD_TYPE:
                for (BigImgEntity.Blog blog : blogList) {
                    if (event.mData.blogId.equals(blog.id)) {
                        blog.down_num++;
                    }
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void initHintDialog() {
        if (plusDialog == null) {
            plusDialog = new PromptDialog(this);
            plusDialog.setTvCancleIsBold(false);
            plusDialog.setTvSureIsBold(false);
            plusDialog.setTvSureColor(R.color.pink_color);
            plusDialog.setTvSureBGColor(Color.WHITE);
        }
        plusDialog.setSureAndCancleListener("亲，您还不是PLUS会员哦,现在开通享7大专属权益", "立即开通", view -> {
            plusDialog.dismiss();
            H5X5Act.startAct(ActivityDetailActivity.this, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
        }, getStringResouce(R.string.errcode_cancel), view -> plusDialog.dismiss()).show();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
