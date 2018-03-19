package com.shunlian.app.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FindCommentDetailAdapter;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.presenter.FindCommentDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFindCommentDetailView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/15.
 */

public class CommentDetailAct extends BaseActivity implements IFindCommentDetailView{

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
    private FindCommentDetailPresenter presenter;
    private LinearLayoutManager manager;
    private FindCommentDetailAdapter adapter;
    private FindCommentListEntity.ItemComment itemComment;
    private int currentTouchItem = -1;
    private List<FindCommentListEntity.ItemComment> mLikesBeans;

    public static void startAct(Context context,String comment_id){
        Intent intent = new Intent(context, CommentDetailAct.class);
        intent.putExtra("comment_id",comment_id);
        context.startActivity(intent);
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

        mtv_title.setText("评论详情");
        GradientDrawable gradientDrawable = (GradientDrawable) met_text.getBackground();
        gradientDrawable.setColor(Color.parseColor("#F2F6F9"));

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        String comment_id = getIntent().getStringExtra("comment_id");
        presenter = new FindCommentDetailPresenter(this,this,comment_id);

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @OnClick(R.id.met_text)
    public void onClick(){
        setEdittextFocusable(true,met_text);
    }

    @Override
    public void commentDetailList(final List<FindCommentListEntity.ItemComment> likesBeans, int page, int allpage) {
        this.mLikesBeans = likesBeans;
        if (adapter == null) {
            met_text.setHint("@"+likesBeans.get(0).nickname);
            adapter = new FindCommentDetailAdapter(this, likesBeans);
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
                    currentTouchItem = position;
                    LogUtil.zhLogW("====currentTouchItem======"+currentTouchItem);
                    itemComment = likesBeans.get(position);
                    if ("1".equals(itemComment.delete_enable)){//删除评论
                        delComment();
                    }else{
                        setEdittextFocusable(true,met_text);
                        met_text.setHint("@".concat(itemComment.nickname));
                        if (!isSoftShowing()) {
                            Common.showKeyboard(met_text);
                        }else {
                            Common.hideKeyboard(met_text);
                        }
                    }

                }
            });

            adapter.setPointFabulousListener(new FindCommentDetailAdapter.OnPointFabulousListener() {
                @Override
                public void onPointFabulous(int position) {
                    if (presenter != null){
                        if (presenter != null){
                            currentTouchItem = position;
                            FindCommentListEntity.ItemComment itemComment = mLikesBeans.get(position);
                            LogUtil.zhLogW(itemComment.item_id+"<====>"+itemComment.had_like);
                            presenter.pointFabulous(itemComment.item_id,itemComment.had_like);
                        }
                    }
                }
            });
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(page,allpage);
    }

    private void delComment() {
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
        mLikesBeans.add(0,itemComment);
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除成功
     */
    @Override
    public void delSuccess() {
        mLikesBeans.remove(currentTouchItem);
        adapter.notifyDataSetChanged();
    }

    /**
     * 评论总数
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
     * 点赞
     *
     * @param new_likes
     */
    @Override
    public void setPointFabulous(String new_likes) {
        FindCommentListEntity.ItemComment itemComment = mLikesBeans.get(currentTouchItem);
        itemComment.likes = new_likes;
        itemComment.had_like = "0".equals(itemComment.had_like) ? "1" : "0";
        adapter.notifyDataSetChanged();
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
        }else {
            pid = mLikesBeans.get(0).item_id;
        }
        presenter.sendComment(s,pid);
        met_text.setText("");
        met_text.setHint("添加评论");
        setEdittextFocusable(false,met_text);
        Common.hideKeyboard(met_text);
    }
}
