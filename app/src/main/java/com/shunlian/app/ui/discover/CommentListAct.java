package com.shunlian.app.ui.discover;

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
import com.shunlian.app.adapter.FindCommentListAdapter;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.presenter.FindCommentListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IFindCommentListView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/14.
 */

public class CommentListAct extends BaseActivity implements IFindCommentListView{

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

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;
    private LinearLayoutManager manager;
    private FindCommentListPresenter presenter;
    private FindCommentListAdapter adapter;
    private FindCommentListEntity.ItemComment itemComment;
    private int currentTouchItem = -1;
    private List<FindCommentListEntity.ItemComment> mItemComments;
    private int mHotCommentCount;

    public static void startAct(Activity activity,String article_id){
        Intent intent = new Intent(activity, CommentListAct.class);
        intent.putExtra("article_id",article_id);
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

        mtv_title.setText("评论");

        GradientDrawable gradientDrawable = (GradientDrawable) met_text.getBackground();
        gradientDrawable.setColor(Color.parseColor("#F2F6F9"));

        String article_id = getIntent().getStringExtra("article_id");
        presenter = new FindCommentListPresenter(this,this,article_id);


        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 15);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.white)));

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

    }

    @Override
    public void setCommentList(final List<FindCommentListEntity.ItemComment> itemComments,
                               int hotCommentCount, int page, int allPage) {
        mItemComments = itemComments;
        mHotCommentCount = hotCommentCount;
        if (adapter == null) {
            adapter = new FindCommentListAdapter(this, itemComments, hotCommentCount);
            recy_view.setAdapter(adapter);
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (presenter != null){
                        presenter.onRefresh();
                    }
                }
            });

            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    position = adapter.getPosition(position);
                    currentTouchItem = position;
                    itemComment = itemComments.get(position);
                    if ("1".equals(itemComment.delete_enable)){//删除
                        deleteComment();
                    }else {
                        setEdittextFocusable(true, met_text);
                        met_text.setHint("@".concat(itemComment.nickname));
                        if (!isSoftShowing()) {
                            Common.showKeyboard(met_text);
                        } else {
                            Common.hideKeyboard(met_text);
                        }
                    }
                }
            });


            adapter.setPointFabulousListener(new FindCommentListAdapter.OnPointFabulousListener() {
                @Override
                public void onPointFabulous(int position) {
                    if (presenter != null){
                        position = adapter.getPosition(position);
                        currentTouchItem = position;
                        FindCommentListEntity.ItemComment itemComment = itemComments.get(position);
                        presenter.pointFabulous(itemComment.item_id,itemComment.had_like);
                    }
                }
            });
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(page,allPage);
    }

    private void deleteComment() {
        final PromptDialog dialog = new PromptDialog(this);
        dialog.setSureAndCancleListener("确认删除评论吗？", "确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null){
                    presenter.delComment(itemComment.item_id);
                }
                dialog.dismiss();
            }
        }, "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        }).show();

    }

    /**
     * 刷新item
     * @param itemComment
     */
    @Override
    public void refreshItem(FindCommentListEntity.ItemComment itemComment) {
        Common.staticToasts(this,"发布成功",R.mipmap.icon_common_duihao);
        if (currentTouchItem != -1) {
            mItemComments.remove(currentTouchItem);
            mItemComments.add(currentTouchItem, itemComment);
        }else {
            mItemComments.add(mHotCommentCount,itemComment);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置点赞数量
     *
     * @param num
     */
    @Override
    public void setPointFabulous(String num) {
        FindCommentListEntity.ItemComment itemComment = mItemComments.get(currentTouchItem);
        itemComment.likes = num;
        itemComment.had_like = "0".equals(itemComment.had_like) ? "1" : "0";
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        mItemComments.remove(currentTouchItem);
        adapter.notifyDataSetChanged();
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
        String pid = "";
        if (itemComment != null){
            pid = itemComment.item_id;
        }
        presenter.sendComment(s,pid);
        met_text.setText("");
        met_text.setHint("添加评论");
        setEdittextFocusable(false,met_text);
        Common.hideKeyboard(met_text);
    }
}
