package com.shunlian.app.ui.discover.other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.FindCommentListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IFindCommentListView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/14.
 */

public class CommentListAct extends BaseActivity implements IFindCommentListView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.met_text)
    MyEditText met_text;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_msg_count)
    MyTextView mtv_msg_count;

    @BindView(R.id.mtv_send)
    MyTextView mtv_send;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private LinearLayoutManager manager;
    private FindCommentListPresenter presenter;

    public static void startAct(Activity activity,String article_id){
        Intent intent = new Intent(activity, CommentListAct.class);
        intent.putExtra("article_id",article_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_commentlist;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null && presenter != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        presenter.onRefresh();
                    }
                }
            }
        });

        met_text.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!isEmpty(s)){
                    visible(mtv_send);
                    gone(miv_icon,mtv_msg_count);
                }else {
                    gone(mtv_send);
                    visible(miv_icon,mtv_msg_count);
                }
            }
        });
    }

    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();

        mtv_toolbar_title.setText(getStringResouce(R.string.comments));

        GradientDrawable gradientDrawable = (GradientDrawable) met_text.getBackground();
        gradientDrawable.setColor(Color.parseColor("#F2F6F9"));

        String article_id = getIntent().getStringExtra("article_id");
        presenter = new FindCommentListPresenter(this,this, article_id);


        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.white)));

    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.findCommentList();
    }

    @OnClick(R.id.met_text)
    public void onClick(){
        setEdittextFocusable(true,met_text);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

        if (request_code == 100){
            visible(nei_empty);
            gone(recy_view);
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("暂无评论").setButtonText(null);
        }else {
            gone(nei_empty);
            visible(recy_view);
        }
    }

    /**
     * 设置评论总数
     *
     * @param count
     */
    @Override
    public void setCommentAllCount(String count) {
        GradientDrawable background = (GradientDrawable) mtv_msg_count.getBackground();
        int w = TransformUtil.dip2px(this, 0.5f);
        background.setStroke(w,getColorResouce(R.color.white));
        mtv_msg_count.setText(count);
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
     * 删除提示
     */
    @Override
    public void delPrompt() {
        final PromptDialog dialog = new PromptDialog(this);
        dialog.setSureAndCancleListener(getStringResouce(R.string.are_you_sure_del_comment),
                getStringResouce(R.string.confirm), v -> {
                    if (presenter != null){
                        presenter.delComment();
                    }
                    dialog.dismiss();
                }, getStringResouce(R.string.errcode_cancel), v -> dialog.dismiss()).show();
    }

    /**
     * 软键盘处理
     */
    @Override
    public void showorhideKeyboard(String hint) {
        setEdittextFocusable(true, met_text);
        met_text.setHint(hint);
        if (!isSoftShowing()) {
            Common.showKeyboard(met_text);
        } else {
            Common.hideKeyboard(met_text);
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @OnClick(R.id.mtv_send)
    public void send(){
        String s = met_text.getText().toString();
        if (isEmpty(s)){
            Common.staticToast("评论内容不能为空");
            return;
        }
        presenter.sendComment(s);
        met_text.setText("");
        met_text.setHint(getStringResouce(R.string.add_comments));
        setEdittextFocusable(false,met_text);
        Common.hideKeyboard(met_text);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }
    }
}
