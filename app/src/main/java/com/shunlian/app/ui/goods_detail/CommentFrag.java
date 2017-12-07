package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/21.
 * 评价
 */

public class CommentFrag extends BaseFragment {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private List<CommentListEntity.Data> commentlists = new ArrayList<>();
    private LinearLayoutManager manager;
    private CommentAdapter commentAdapter;
    private String type = "ALL";
    public static final int pageSize = 20;//评价每页数量
    private int allPage;//总页数
    private GoodsDetailPresenter goodsDetailPresenter;
    private String mCommentId;
    private String goodsId;
    private boolean isClickHead;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View frag_comment = LayoutInflater.from(baseActivity).inflate(R.layout.frag_comment, container, false);
        return frag_comment;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        if (goodsDetailPresenter != null){
                            goodsDetailPresenter.onRefresh();
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
        goodsId = getArguments().getString("goodsId");
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recy_view.getLayoutParams();
        layoutParams.topMargin = ((GoodsDetailAct)baseActivity).offset;
        recy_view.setLayoutParams(layoutParams);
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        commentAdapter = new CommentAdapter(baseActivity,true,commentlists);
        recy_view.setAdapter(commentAdapter);
        commentAdapter.setCommentTypeListener(new CommentAdapter.ICommentTypeListener() {
            @Override
            public void onCommentType(String type) {
                isClickHead = true;
                CommentFrag.this.type = type;
                goodsDetailPresenter.setType(type);
                requestCommentPageData(type,"1");
            }
        });

        commentAdapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
            @Override
            public void onReload() {
                if (goodsDetailPresenter != null){
                    goodsDetailPresenter.onRefresh();
                }
            }
        });
    }

    /**
     * 评价列表
     * @param entity
     */
    public void setCommentList(CommentListEntity entity){
        List<CommentListEntity.Label> label = entity.label;
        CommentListEntity.ListData list = entity.list;
        int page = Integer.parseInt(list.page) + 1;
        this.allPage = Integer.parseInt(list.allPage);
        commentlists.clear();
        commentlists.addAll(list.data);
        boolean isClear = false;
        if (!isClickHead){
            isClear = true;
        }else {
            isClear = false;
        }
        commentAdapter.setLabel(label,isClear);
        commentAdapter.setPageLoading(page,allPage);
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 更多数据
     * @param entity
     */
    public void setCommentMoreList(CommentListEntity entity){
        CommentListEntity.ListData list = entity.list;
        int page = Integer.parseInt(list.page) + 1;
        this.allPage = Integer.parseInt(list.allPage);
        int size = commentlists.size();
        commentlists.addAll(list.data);
        commentAdapter.setPageLoading(page,allPage);
        if (size > 10){
            commentAdapter.notifyItemRangeChanged(size,pageSize);
        }else {
            commentAdapter.notifyDataSetChanged();
        }

    }

    public void loadFailure(){
        if (commentAdapter != null){
            commentAdapter.loadFailure();
        }
    }

    public void setPresenter(GoodsDetailPresenter goodsDetailPresenter, String id) {
        this.goodsDetailPresenter = goodsDetailPresenter;
        mCommentId = id;
        isClickHead = false;
        if (recy_view != null)
            recy_view.scrollToPosition(0);
    }

    /**
     * 评价分页数据
     * @param type
     * @param page
     */
    public void requestCommentPageData(String type,String page){
        goodsDetailPresenter.commentList(GoodsDetailPresenter.COMMENT_EMPTY_CODE,
                GoodsDetailPresenter.COMMENT_FAILURE_CODE,false,
                goodsId,type,page,String.valueOf(pageSize),null);
    }
}
