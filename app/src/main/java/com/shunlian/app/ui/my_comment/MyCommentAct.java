package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.eventbus_bean.CommentEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.MyCommentListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IMyCommentListView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentAct extends BaseActivity implements IMyCommentListView,
        MessageCountManager.OnGetMessageListener {

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
    MyImageView civ_head;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private int pink_color;
    private int new_text;
    private MessageCountManager messageCountManager;
    private MyCommentListPresenter presenter;
    private LinearLayoutManager manager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MyCommentAct.class);
        context.startActivity(intent);
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
                    if (lastPosition + 1 == manager.getItemCount()
                            && manager.getItemCount() > 0 && presenter != null) {
                        presenter.onRefresh();
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

        EventBus.getDefault().register(this);

        pink_color = getResources().getColor(R.color.pink_color);
        new_text = getResources().getColor(R.color.new_text);
        presenter = new MyCommentListPresenter(this, this);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        int space = TransformUtil.dip2px(this, 7.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(space, 0,
                0, getResources().getColor(R.color.white_ash)));
        recy_view.setFocusable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter == null)presenter = new MyCommentListPresenter(this, this);
        switch (presenter.currentPageStatus) {
            case MyCommentListPresenter.ALL:
                allComment();
                break;
            case MyCommentListPresenter.APPEND:
                appendComment();
                break;
        }

        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
    }

    @OnClick(R.id.mllayout_all)
    public void allComment() {
        if (presenter == null)presenter = new MyCommentListPresenter(this, this);
        presenter.currentPageStatus = MyCommentListPresenter.ALL;
        classState(1);
        presenter.myCommentListAll();
    }


    @OnClick(R.id.mllayout_append)
    public void appendComment() {
        if (presenter == null)presenter = new MyCommentListPresenter(this, this);
        presenter.currentPageStatus = MyCommentListPresenter.APPEND;
        classState(2);
        presenter.myCommentListAppend();
    }

    @OnClick(R.id.rl_more)
    public void more() {
        visible(quick_actions);
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
        visible(nei_empty);
        nei_empty.setNetExecption().setOnClickListener(v -> {
            if (presenter == null)presenter = new MyCommentListPresenter(this, this);
            switch (presenter.currentPageStatus) {
                case MyCommentListPresenter.ALL:
                    allComment();
                    break;
                case MyCommentListPresenter.APPEND:
                    appendComment();
                    break;
            }
        });
    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {
        if (request_code == 0) {
            visible(nei_empty);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("您还没有需要追评的商品").setButtonText("");
        } else {
            gone(nei_empty);
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
        GlideUtils.getInstance().loadCircleHeadImage(this, civ_head, avatar);
        mtv_nickname.setText(nickname);
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        gone(nei_empty);
        recy_view.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(CommentEvent event) {
        if (event.getStatus() == CommentEvent.SUCCESS_CHANGE_STATUS ||
                event.getStatus() == CommentEvent.SUCCESS_APPEND_STATUS) {
            presenter.myCommentListAll();
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
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}
