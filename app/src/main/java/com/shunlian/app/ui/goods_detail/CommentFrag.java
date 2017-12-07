package com.shunlian.app.ui.goods_detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.bean.CommentListEntity;
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
    private int page = 1;
    private boolean isLoading = false;

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
            GoodsDetailAct detailAct = (GoodsDetailAct) baseActivity;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (!isLoading && lastPosition + 1 == manager.getItemCount()){
                        isLoading = true;
                        detailAct.requestCommentPageData(type,String.valueOf(page));
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
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) recy_view.getLayoutParams();
        layoutParams.topMargin = ((GoodsDetailAct)baseActivity).offset;
        recy_view.setLayoutParams(layoutParams);
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        commentAdapter = new CommentAdapter(baseActivity,false,commentlists);
        recy_view.setAdapter(commentAdapter);
        commentAdapter.setCommentTypeListener(new CommentAdapter.ICommentTypeListener() {
            @Override
            public void onCommentType(String type) {
                CommentFrag.this.type = type;
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
        this.page = Integer.parseInt(list.page) + 1;
        commentlists.clear();
        commentlists.addAll(list.data);
        commentAdapter.setLabel(label);
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * 更多数据
     * @param entity
     */
    public void setCommentMoreList(CommentListEntity entity){
        isLoading = false;
        CommentListEntity.ListData list = entity.list;
        this.page = Integer.parseInt(list.page) + 1;
        int size = commentlists.size();
        commentlists.addAll(list.data);
        commentAdapter.notifyItemRangeInserted(size - 1,commentlists.size() - size);
    }

//    public void
}
