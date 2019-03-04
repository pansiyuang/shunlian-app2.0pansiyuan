package com.shunlian.app.ui.discover_new.comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.eventbus_bean.RejectedNotifyEvent;
import com.shunlian.app.listener.SoftKeyBoardListener;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.FindCommentDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IFindCommentDetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/15.
 */

public class CommentDetailAct extends BaseActivity implements IFindCommentDetailView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_msg_count)
    MyTextView mtv_msg_count;

    @BindView(R.id.mtv_toolbar_msgCount)
    MyTextView mtv_toolbar_msgCount;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    private FindCommentDetailPresenter presenter;
    private LinearLayoutManager manager;
    private MessageCountManager messageCountManager;
    private FindCommentListEntity.ItemComment currentComment;

    public static void startAct(Context context, String comment_id) {
        Intent intent = new Intent(context, CommentDetailAct.class);
        intent.putExtra("comment_id", comment_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_commentlist;
    }

    @Override
    protected void onResume() {
        if (messageCountManager.isLoad()) {
            String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
            if (quick_actions != null)
                quick_actions.setMessageCount(s);
        } else {
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null && presenter != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        presenter.onRefresh();
                    }
                }
            }
        });

        edt_content.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s.length() > 300) {
                    edt_content.setText(s.subSequence(0, 300));
                    edt_content.setSelection(300);
                    Common.staticToast("字数不能超过300");
                }
            }
        });

        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.initData();
            }

            @Override
            public void onLoadMore() {
                refreshview.stopLoadMore(true);
            }
        });

        recy_view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                edt_content.setFocusable(false);
                Common.hideKeyboard(edt_content);
            }
            return false;
        });

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                if (isEmpty(edt_content.getText().toString())) {
                    if (currentComment == null) {
                        edt_content.setHint(Common.getRandomWord());
                    } else {
                        edt_content.setHint("@" + currentComment.nickname);
                    }
                } else {
                    edt_content.setSelection(edt_content.getText().toString().length());
                }
            }

            @Override
            public void keyBoardHide(int height) {
                if (isEmpty(edt_content.getText().toString())) {
                    presenter.clearComment();
                    currentComment = null;
                    edt_content.setHint("留下你的精彩评论吧~");
                }
            }
        });
    }

    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white).statusBarView(R.id.view_status_bar)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();
        EventBus.getDefault().register(this);
        mrlayout_toolbar_more.setVisibility(View.GONE);

        mtv_toolbar_title.setText(getStringResouce(R.string.comment_details));
        GradientDrawable gradientDrawable = (GradientDrawable) edt_content.getBackground();
        gradientDrawable.setColor(getColorResouce(R.color.value_F2F6F9));

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new VerticalItemDecoration(space, 0, 0, getColorResouce(R.color.white)));

        String comment_id = getIntent().getStringExtra("comment_id");
        presenter = new FindCommentDetailPresenter(this, this, comment_id);

        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.findCommentList();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @OnClick(R.id.edt_content)
    public void onClick() {
        Common.showKeyboard(edt_content);
    }

    /**
     * 软键盘处理
     */
    @Override
    public void showorhideKeyboard(FindCommentListEntity.ItemComment comment) {
        currentComment = comment;
        setEdittextFocusable(true, edt_content);
        if (currentComment == null) {
            edt_content.setHint(Common.getRandomWord());
        } else {
            edt_content.setHint("@" + comment.nickname);
        }
        Common.showKeyboard(edt_content);
    }

    @Override
    public void hideKeyboard() {
        edt_content.setFocusable(false);
        Common.hideKeyboard(edt_content);
    }


    /**
     * 删除提示
     */
    @Override
    public void delPrompt() {
        final PromptDialog dialog = new PromptDialog(this);
        dialog.setSureAndCancleListener(getStringResouce(R.string.are_you_sure_del_comment),
                getStringResouce(R.string.confirm), v -> {
                    if (presenter != null) {
                        presenter.delComment();
                    }
                    dialog.dismiss();
                }, getStringResouce(R.string.errcode_cancel), v -> dialog.dismiss()).show();
    }

    /**
     * 评论总数
     *
     * @param count
     */
    @Override
    public void setCommentAllCount(String count) {
        mtv_toolbar_title.setText(String.format("共%s条回复", count));
        refreshview.stopRefresh(true);
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }

    /**
     * 设置hint
     *
     * @param hint
     */
    @Override
    public void setHint(String hint) {
        edt_content.setHint(hint);
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @OnClick(R.id.tv_send)
    public void send() {
        String s = edt_content.getText().toString();
        if (isEmpty(s)) {
            Common.staticToast("评论内容不能为空");
            return;
        }
        presenter.sendComment(s, currentComment);
        edt_content.setText("");
        edt_content.setHint("留下你的精彩评论吧~");
        setEdittextFocusable(false, edt_content);
        Common.hideKeyboard(edt_content);
    }


    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RejectedNotifyEvent event) {
        if (event.rejectedSuccess) {
            presenter.rejectedComment(event.commentId, event.parentCommentId);
        }
    }
}
