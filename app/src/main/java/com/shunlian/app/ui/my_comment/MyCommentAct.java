package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MyCommentAdapter;
import com.shunlian.app.adapter.WaitAppendCommentAdapter;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.presenter.MyCommentListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IMyCommentListView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentAct extends BaseActivity implements IMyCommentListView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_comment_all)
    MyTextView mtv_comment_all;

    @BindView(R.id.view_comment_all)
    View view_comment_all;

    @BindView(R.id.mtv_comment_append)
    MyTextView mtv_comment_append;

    @BindView(R.id.view_comment_append)
    View view_comment_append;

    @BindView(R.id.civ_head)
    CircleImageView civ_head;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;
    private int pink_color;
    private int new_text;
    private MyCommentListPresenter presenter;
    private int currentPageStatus = MyCommentListPresenter.ALL;
    private List<CommentListEntity.Data> lists = new ArrayList<>();
    private MyCommentAdapter allAdapter;
    private LinearLayoutManager manager;
    private WaitAppendCommentAdapter waitAdapter;
    private String nickname;
    private String avatar;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyCommentAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_my_comment;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (presenter != null) {
                            presenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pink_color = getResources().getColor(R.color.pink_color);
        new_text = getResources().getColor(R.color.new_text);

        presenter = new MyCommentListPresenter(this, this);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        int space = TransformUtil.dip2px(this, 7.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(space, 0,
                0, getResources().getColor(R.color.white_ash)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (currentPageStatus) {
            case MyCommentListPresenter.ALL:
                allComment();
                break;
            case MyCommentListPresenter.APPEND:
                appendComment();
                break;
        }
    }

    @OnClick(R.id.mllayout_all)
    public void allComment() {
        currentPageStatus = MyCommentListPresenter.ALL;
        classState(1);
        presenter.myCommentListAll();
    }


    @OnClick(R.id.mllayout_append)
    public void appendComment() {
        currentPageStatus = MyCommentListPresenter.APPEND;
        classState(2);
        presenter.myCommentListAppend();
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.comment();
    }

    private void classState(int state) {

        mtv_comment_all.setTextColor(state == 1 ? pink_color : new_text);
        view_comment_all.setVisibility(state == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_comment_append.setTextColor(state == 2 ? pink_color : new_text);
        view_comment_append.setVisibility(state == 2 ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == MyCommentListPresenter.ALL_COMMENT_CODE) {
            if (allAdapter != null) {
                allAdapter.loadFailure();
            }
        } else {
            if (waitAdapter != null) {
                waitAdapter.loadFailure();
            }
        }
    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 评价列表
     *
     * @param lists
     */
    @Override
    public void commentList(List<CommentListEntity.Data> lists, int currentPage, int allPage) {
        if (currentPage == 1) {
            this.lists.clear();
            if (currentPageStatus == MyCommentListPresenter.ALL) {
                waitAdapter = null;
            } else {
                allAdapter = null;
            }
        }
        this.lists.addAll(lists);
        if (currentPageStatus == MyCommentListPresenter.ALL) {
            allComment(currentPage, allPage);
        } else {
            appendComment(currentPage, allPage);
        }

        if (isEmpty(this.lists)) {
            nei_empty.setVisibility(View.VISIBLE);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("您还没有需要追评的商品").setButtonText("");
        } else {
            nei_empty.setVisibility(View.GONE);
        }
    }

    /**
     * 设置昵称和头像
     *
     * @param nickname
     * @param avatar
     */
    @Override
    public void setNicknameAndAvatar(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        GlideUtils.getInstance().loadImage(this, civ_head, avatar);
        mtv_nickname.setText(nickname);
    }

    private void appendComment(int currentPage, int allPage) {
        if (waitAdapter == null) {
            waitAdapter = new WaitAppendCommentAdapter(this, true, this.lists);
            waitAdapter.setPageLoading(currentPage, allPage);
            recy_view.setAdapter(waitAdapter);
            waitAdapter.setOnReloadListener(() -> {
                if (presenter != null) {
                    presenter.onRefresh();
                }
            });

            waitAdapter.setOnItemClickListener((view, position) -> {
                CommentListEntity.Data data = lists.get(position);
                CommentDetailAct.startAct(MyCommentAct.this, data, nickname, avatar);
            });
        } else {
            waitAdapter.setPageLoading(currentPage, allPage);
            waitAdapter.notifyDataSetChanged();
        }
    }

    private void allComment(int currentPage, int allPage) {
        if (allAdapter == null) {
            allAdapter = new MyCommentAdapter(this, true, this.lists);
            allAdapter.setPageLoading(currentPage, allPage);
            recy_view.setAdapter(allAdapter);
            allAdapter.setOnReloadListener(() -> {
                if (presenter != null) {
                    presenter.onRefresh();
                }
            });

            allAdapter.setOnItemClickListener((view, position) -> {
                CommentListEntity.Data data = lists.get(position);
                CommentDetailAct.startAct(MyCommentAct.this, data, nickname, avatar);
            });
        } else {
            allAdapter.setPageLoading(currentPage, allPage);
            allAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
