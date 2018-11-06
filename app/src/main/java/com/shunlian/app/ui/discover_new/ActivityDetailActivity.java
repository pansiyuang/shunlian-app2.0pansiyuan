package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
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
import com.shunlian.app.presenter.ActivityDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.find_send.FindSendPictureTextAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IActivityDetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class ActivityDetailActivity extends BaseActivity implements IActivityDetailView, ActivityDetailAdapter.OnAdapterCallBack {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_join)
    MyImageView miv_join;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

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
        defToolbar();

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
                LoginAct.startAct(ActivityDetailActivity.this);
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
                sendConfig.activityTitle = currentDetail.title;
                sendConfig.memberId = baseInfo.member_id;
                FindSendPictureTextAct.startAct(ActivityDetailActivity.this, sendConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        ImmersionBar immersionBar = ImmersionBar.with(this).addViewSupportTransformColor(toolbar, R.color.white);
        if (totalDy <= layoutHeight) {
            if (totalDy <= 0) {
                totalDy = 0;
            }
            float alpha = (float) totalDy / layoutHeight;
            immersionBar.statusBarAlpha(alpha).addTag(GoodsDetailAct.class.getName()).init();
            float v = 1.0f - alpha * 2;
            if (v <= 0) {
                v = alpha * 2 - 1;
                setImg(2, 1);
            } else {
                setImg(1, 2);
            }
            miv_close.setAlpha(v);
        } else {
            setToolbar();
        }
    }

    public void setToolbar() {
        setImg(2, 1);
        immersionBar.statusBarAlpha(1.0f).addTag(GoodsDetailAct.class.getName()).init();
        miv_close.setAlpha(1.0f);
    }

    private void setImg(int status, int oldStatus) {
        if (status != oldStatus) {
            if (status == 1) {
                miv_close.setImageResource(R.mipmap.icon_more_fanhui);
            } else {
                miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
            }
        }
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
            mAdapter = new ActivityDetailAdapter(this, blogList, detail, quick_actions);
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
    public void toFocusUser(int isFocus, String memberId) {
        mPresent.focusUser(isFocus, memberId);
    }

    @Override
    public void toPraiseBlog(String blogId) {
        mPresent.praiseBlos(blogId);
    }

    @Override
    public void OnTopSize(int height) {
        layoutHeight = height - offset - ImmersionBar.getStatusBarHeight(this);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
